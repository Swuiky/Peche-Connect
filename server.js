import express from "express";
import cors from "cors";
import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";
import "dotenv/config";
import { pool } from "./db.js";
import { requireAuth, requireAdmin } from "./auth.js";

const app = express();
app.use(cors());
app.use(express.json());

// -----------------------------------------------
// Statut de l'API
// -----------------------------------------------

app.get("/statut", (req, res) => {
  res.json({ ok: true });
});

// -----------------------------------------------
// Connexion
// -----------------------------------------------

// L'utilisateur envoie son email et mot de passe,
// le serveur répond avec un token JWT si les identifiants sont corrects
app.post("/auth/connexion", async (req, res) => {
  const { email, motDePasse } = req.body || {};

  if (!email || !motDePasse)
    return res.status(400).json({ erreur: "Email et mot de passe requis" });

  try {
    const [lignes] = await pool.query(
      "SELECT id_user, email, password_hash, `role`, actif FROM utilisateur WHERE email = ? LIMIT 1",
      [email]
    );
    const utilisateur = lignes[0];

    if (!utilisateur || !utilisateur.actif)
      return res.status(401).json({ erreur: "Identifiants invalides" });

    const motDePasseCorrect = await bcrypt.compare(motDePasse, utilisateur.password_hash);
    if (!motDePasseCorrect)
      return res.status(401).json({ erreur: "Identifiants invalides" });

    await pool.query(
      "UPDATE utilisateur SET derniere_connexion = NOW(3) WHERE id_user = ?",
      [utilisateur.id_user]
    );

    // Le token contient l'identité de l'utilisateur, signé avec la clé secrète
    const token = jwt.sign(
      { id: utilisateur.id_user, email: utilisateur.email, role: utilisateur.role },
      process.env.JWT_SECRET,
      { expiresIn: "8h" }
    );

    res.json({
      token,
      utilisateur: { id: utilisateur.id_user, email: utilisateur.email, role: utilisateur.role }
    });
  } catch (err) {
    res.status(500).json({ erreur: "Erreur serveur" });
  }
});

// Route à utiliser une seule fois pour créer le premier compte administrateur
app.post("/auth/bootstrap-admin", async (req, res) => {
  const { email, motDePasse } = req.body || {};

  if (!email || !motDePasse)
    return res.status(400).json({ erreur: "Email et mot de passe requis" });

  try {
    const [existants] = await pool.query(
      "SELECT id_user FROM utilisateur WHERE `role` = 'admin' LIMIT 1"
    );
    if (existants.length)
      return res.status(403).json({ erreur: "Un compte administrateur existe déjà" });

    const hash = await bcrypt.hash(motDePasse, 10);
    await pool.query(
      "INSERT INTO utilisateur(email, password_hash, `role`) VALUES(?, ?, 'admin')",
      [email, hash]
    );
    res.json({ ok: true });
  } catch (err) {
    res.status(500).json({ erreur: "Erreur serveur" });
  }
});

// -----------------------------------------------
// Casiers
// -----------------------------------------------

// Liste de tous les casiers enregistrés
app.get("/casiers", requireAuth, async (req, res) => {
  try {
    const [lignes] = await pool.query(
      "SELECT id_casier, uid, label, is_active FROM Casier ORDER BY id_casier DESC"
    );
    res.json(lignes);
  } catch (err) {
    res.status(500).json({ erreur: "Erreur serveur" });
  }
});

// Dernière mesure enregistrée pour un casier
app.get("/casiers/:id/derniere-mesure", requireAuth, async (req, res) => {
  try {
    const [lignes] = await pool.query(
      `SELECT id_mesure, date_mesure, latitude, longitude,
              nb_prises, systeme_actif, batterie_pct, vol_suspecte
       FROM Mesure
       WHERE id_casier = ?
       ORDER BY date_mesure DESC
       LIMIT 1`,
      [Number(req.params.id)]
    );
    res.json(lignes[0] || null);
  } catch (err) {
    res.status(500).json({ erreur: "Erreur serveur" });
  }
});

// Historique des mesures d'un casier sur une période
app.get("/casiers/:id/historique", requireAuth, async (req, res) => {
  const dateDebut = req.query.debut ? new Date(req.query.debut) : null;
  const dateFin   = req.query.fin   ? new Date(req.query.fin)   : null;

  if (!dateDebut || !dateFin || isNaN(dateDebut) || isNaN(dateFin))
    return res.status(400).json({ erreur: "Paramètres 'debut' et 'fin' requis (format ISO)" });

  try {
    const [lignes] = await pool.query(
      `SELECT id_mesure, date_mesure, latitude, longitude,
              nb_prises, systeme_actif, batterie_pct, vol_suspecte
       FROM Mesure
       WHERE id_casier = ? AND date_mesure BETWEEN ? AND ?
       ORDER BY date_mesure DESC
       LIMIT 2000`,
      [Number(req.params.id), dateDebut, dateFin]
    );
    res.json(lignes);
  } catch (err) {
    res.status(500).json({ erreur: "Erreur serveur" });
  }
});

// -----------------------------------------------
// Alertes
// -----------------------------------------------

// Liste des alertes d'un casier
app.get("/casiers/:id/alertes", requireAuth, async (req, res) => {
  try {
    const [lignes] = await pool.query(
      `SELECT id_alerte, type_alerte, message, cree_le, acquitte_le, acquitte_par
       FROM alertes
       WHERE id_casier = ?
       ORDER BY cree_le DESC
       LIMIT 500`,
      [Number(req.params.id)]
    );
    res.json(lignes);
  } catch (err) {
    res.status(500).json({ erreur: "Erreur serveur" });
  }
});

// Acquitter une alerte (marquer comme traitée)
app.patch("/alertes/:id/acquitter", requireAuth, async (req, res) => {
  try {
    await pool.query(
      "UPDATE alertes SET acquitte_le = NOW(3), acquitte_par = ? WHERE id_alerte = ? AND acquitte_le IS NULL",
      [req.user.id, Number(req.params.id)]
    );
    res.json({ ok: true });
  } catch (err) {
    res.status(500).json({ erreur: "Erreur serveur" });
  }
});

// -----------------------------------------------
// Réception des mesures IoT
// Protégée par une clé partagée dans le header x-iot-key
// -----------------------------------------------

app.post("/mesures", async (req, res) => {
  const cle = req.headers["x-iot-key"];
  if (!cle || cle !== process.env.CLE_IOT)
    return res.status(401).json({ erreur: "Clé IoT invalide" });

  const {
    uid, latitude, longitude, pression,
    batterie_pct, nb_prises, vol_suspecte, systeme_actif, id_session
  } = req.body || {};

  if (uid == null || latitude == null || longitude == null || pression == null ||
      batterie_pct == null || nb_prises == null || vol_suspecte == null || systeme_actif == null)
    return res.status(400).json({ erreur: "Champs requis manquants" });

  try {
    const [casiers] = await pool.query(
      "SELECT id_casier FROM Casier WHERE uid = ? LIMIT 1",
      [uid]
    );
    if (!casiers.length)
      return res.status(404).json({ erreur: "Casier introuvable" });

    const [result] = await pool.query(
      `INSERT INTO Mesure
         (latitude, longitude, pression, date_mesure, batterie_pct,
          vol_suspecte, nb_prises, systeme_actif, id_casier, id_session)
       VALUES (?, ?, ?, NOW(3), ?, ?, ?, ?, ?, ?)`,
      [
        latitude, longitude, pression,
        batterie_pct,
        vol_suspecte  ? 1 : 0,
        nb_prises,
        systeme_actif ? 1 : 0,
        casiers[0].id_casier,
        id_session ?? null
      ]
    );
    res.status(201).json({ ok: true, id_mesure: result.insertId });
  } catch (err) {
    res.status(500).json({ erreur: "Erreur serveur" });
  }
});

// -----------------------------------------------
// Sessions de pêche
// -----------------------------------------------

// Historique de toutes les sessions avec leurs statistiques
app.get("/sessions", requireAuth, async (req, res) => {
  try {
    const [lignes] = await pool.query(
      `SELECT s.id_session, s.debut, s.fin,
              COUNT(DISTINCT m.id_casier)   AS nb_casiers,
              COUNT(m.id_mesure)            AS nb_mesures,
              COALESCE(SUM(m.nb_prises), 0) AS total_prises
       FROM \`Session\` s
       LEFT JOIN Mesure m ON m.id_session = s.id_session
       GROUP BY s.id_session, s.debut, s.fin
       ORDER BY s.debut DESC`
    );
    res.json(lignes);
  } catch (err) {
    res.status(500).json({ erreur: "Erreur serveur" });
  }
});

// -----------------------------------------------
// Administration des utilisateurs
// -----------------------------------------------

// Liste de tous les utilisateurs (admin uniquement)
app.get("/admin/utilisateurs", requireAuth, requireAdmin, async (req, res) => {
  try {
    const [lignes] = await pool.query(
      "SELECT id_user, email, `role`, actif, derniere_connexion FROM utilisateur ORDER BY id_user DESC"
    );
    res.json(lignes);
  } catch (err) {
    res.status(500).json({ erreur: "Erreur serveur" });
  }
});

// Créer un nouvel utilisateur (admin uniquement)
app.post("/admin/utilisateurs", requireAuth, requireAdmin, async (req, res) => {
  const { email, motDePasse, role } = req.body || {};

  if (!email || !motDePasse)
    return res.status(400).json({ erreur: "Email et mot de passe requis" });

  try {
    const roleValide = (role === "admin") ? "admin" : "lecture";
    const hash = await bcrypt.hash(motDePasse, 10);
    await pool.query(
      "INSERT INTO utilisateur(email, password_hash, `role`) VALUES(?, ?, ?)",
      [email, hash, roleValide]
    );
    res.json({ ok: true });
  } catch (err) {
    res.status(500).json({ erreur: "Erreur serveur" });
  }
});

// -----------------------------------------------
// Démarrage du serveur
// -----------------------------------------------

app.listen(Number(process.env.PORT || 3000), () => {
  console.log(`Serveur démarré sur http://localhost:${process.env.PORT || 3000}`);
});
