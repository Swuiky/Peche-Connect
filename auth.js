import jwt from "jsonwebtoken";

// Vérifie qu'un token JWT valide est présent dans le header Authorization
export function requireAuth(req, res, next) {
  const header = req.headers.authorization || "";
  const token = header.startsWith("Bearer ") ? header.slice(7) : null;

  if (!token)
    return res.status(401).json({ erreur: "Token manquant" });

  try {
    req.user = jwt.verify(token, process.env.JWT_SECRET);
    return next();
  } catch {
    return res.status(401).json({ erreur: "Token invalide" });
  }
}

// Vérifie que l'utilisateur connecté est un administrateur
export function requireAdmin(req, res, next) {
  if (req.user?.role !== "admin")
    return res.status(403).json({ erreur: "Accès réservé aux administrateurs" });
  return next();
}
