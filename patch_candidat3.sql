-- =========================================================
-- Pêche-Connect — Patch BDDPecheConnect
-- Candidat 3 — BTS CIEL Session 2026
-- ---------------------------------------------------------
-- Prérequis  : base BDDPecheConnect existante avec ses
--              tables Casier, Session, Mesure, utilisateur
-- Exécution  : une seule fois
-- Données    : aucune suppression, aucune perte
-- Convention : nommage entièrement en français,
--              cohérent avec le schéma existant
-- =========================================================

USE BDDPecheConnect;

-- ---------------------------------------------------------
-- 1. Table utilisateur
--
--    Corrections :
--      - password_hash : VARCHAR(64) trop court pour bcrypt
--        (bcrypt génère systématiquement 60 caractères ;
--         une valeur trop courte tronque le hash en silence)
--      - email : ajout contrainte UNIQUE (intégrité métier)
--
--    Ajouts :
--      - role            : niveau d'accès (admin / lecture)
--      - actif           : compte activé ou désactivé
--      - derniere_connexion : date de dernière connexion
-- ---------------------------------------------------------
ALTER TABLE utilisateur
  MODIFY  password_hash       VARCHAR(255)             NOT NULL,
  ADD UNIQUE KEY uq_email     (email),
  ADD COLUMN `role`           ENUM('admin','lecture')  NOT NULL DEFAULT 'lecture',
  ADD COLUMN actif            TINYINT(1)               NOT NULL DEFAULT 1,
  ADD COLUMN derniere_connexion DATETIME(3)            NULL;

-- ---------------------------------------------------------
-- 2. Table Casier
--
--    Correction :
--      - id_session passe de NOT NULL à NULL
--        Un casier peut exister sans être rattaché à une
--        session active (déploiement en dehors d'une sortie)
-- ---------------------------------------------------------
ALTER TABLE Casier
  MODIFY id_session INT NULL;

-- ---------------------------------------------------------
-- 3. Table Mesure
--
--    Ajouts :
--      - nb_prises     : nombre de prises remontées par l'IoT
--      - systeme_actif : état du système embarqué du casier
--
--    Index composites :
--      - (id_casier, date_mesure) : requêtes historique
--        par casier sur une période
--      - (id_session, date_mesure) : requêtes agrégats
--        par session pour la page historique
-- ---------------------------------------------------------
ALTER TABLE Mesure
  ADD COLUMN nb_prises      INT        NOT NULL DEFAULT 0,
  ADD COLUMN systeme_actif  TINYINT(1) NOT NULL DEFAULT 1;

CREATE INDEX idx_mesure_casier_date
  ON Mesure (id_casier, date_mesure);

CREATE INDEX idx_mesure_session_date
  ON Mesure (id_session, date_mesure);

-- ---------------------------------------------------------
-- 4. Nouvelle table alertes
--
--    Stocke les alertes générées par les casiers connectés.
--
--    Colonnes :
--      - id_alerte    : clé primaire auto-incrémentée
--      - id_casier    : casier concerné (FK obligatoire)
--      - type_alerte  : code court (ex : "vol", "batterie")
--      - message      : description lisible de l'alerte
--      - cree_le      : horodatage automatique de création
--      - acquitte_le  : horodatage d'accusé de réception
--      - acquitte_par : utilisateur ayant traité l'alerte
--                       (SET NULL si l'utilisateur est supprimé)
--
--    Contraintes :
--      - FK vers Casier (intégrité référentielle)
--      - FK vers utilisateur avec ON DELETE SET NULL
--        (l'alerte reste si l'utilisateur est supprimé)
--
--    Index :
--      - (id_casier, cree_le) : requêtes API par casier
-- ---------------------------------------------------------
CREATE TABLE alertes (
  id_alerte    INT          NOT NULL AUTO_INCREMENT,
  id_casier    INT          NOT NULL,
  type_alerte  VARCHAR(64)  NOT NULL,
  message      TEXT,
  cree_le      DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  acquitte_le  DATETIME(3)  NULL,
  acquitte_par INT          NULL,

  PRIMARY KEY (id_alerte),

  CONSTRAINT fk_alerte_casier
    FOREIGN KEY (id_casier)
    REFERENCES Casier(id_casier),

  CONSTRAINT fk_alerte_utilisateur
    FOREIGN KEY (acquitte_par)
    REFERENCES utilisateur(id_user)
    ON DELETE SET NULL
);

CREATE INDEX idx_alertes_casier_date
  ON alertes (id_casier, cree_le);

-- =========================================================
-- Fin du script
-- =========================================================
