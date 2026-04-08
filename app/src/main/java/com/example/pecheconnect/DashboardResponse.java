package com.example.pecheconnect;

import java.util.List;

public class DashboardResponse {
    public int totalCrustaces;
    public boolean estSuspect; // Pour l'icône danger
    public List<CasierData> casiers;
    public List<SessionData> historique;
}