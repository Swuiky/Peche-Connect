package com.example.pecheconnect;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.nio.channels.AlreadyBoundException;

public class MapActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map); // Assure-toi que le nom du XML est bien activity_alerte

        // 1. Initialisation de la navigation
        bottomNav = findViewById(R.id.bottomNavigation);

        // 2. Sélectionner l'icône "Alertes" par défaut sur cet écran
        // C'est ce qui fera apparaître ton "boutton_select.png" sur l'étoile du milieu
        bottomNav.setSelectedItemId(R.id.nav_map);

        // 3. Gestion des clics pour changer d'activité
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                // Retour au Tableau de Bord
                Intent intent = new Intent(MapActivity.this, BordActivity.class);
                startActivity(intent);
                // Transition 0,0 pour éviter que l'écran ne "saute"
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (id == R.id.nav_map) {

                return true;

            } else if (id == R.id.nav_alerts) {
                startActivity(new Intent(MapActivity.this, AlertActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }

            return false;
        });
    }
}
