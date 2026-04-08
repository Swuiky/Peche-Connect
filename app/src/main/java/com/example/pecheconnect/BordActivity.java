package com.example.pecheconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BordActivity extends AppCompatActivity {

    private TextView txtTotalGlobal, txtNbActuel, txtDirectLabel;
    private ImageView imgDanger;
    private RecyclerView rvHistorique;
    private HistoriqueAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bord);

        txtNbActuel = findViewById(R.id.txt_nb_actuel);
        txtDirectLabel = findViewById(R.id.txt_direct_label);
        imgDanger = findViewById(R.id.img_danger_alert);
        rvHistorique = findViewById(R.id.rv_historique);

        rvHistorique.setLayoutManager(new LinearLayoutManager(this));

        loadData(1); // Simule l'utilisateur 1

        // On récupère la barre de navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        // On indique que l'onglet "Tableau de bord" est celui sélectionné par défaut sur cette page
        bottomNav.setSelectedItemId(R.id.nav_dashboard);

        // On configure le clic sur les boutons
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_alerts) {
                // Aller vers l'écran des alertes
                Intent intent = new Intent(BordActivity.this, AlertActivity.class);
                startActivity(intent);

                // Optionnel : Supprime l'animation de glissement pour un effet plus "app"
                overridePendingTransition(0, 0);
                return true;

            } else if (id == R.id.nav_map) {
                // Si tu as une MapActivity, décommente la ligne suivante :
                // startActivity(new Intent(BordActivity.this, MapActivity.class));
                return true;

            } else if (id == R.id.nav_dashboard) {
                // On est déjà dessus, on ne fait rien
                return true;
            }

            return false;
        });
    }

    private void loadData(int userId) {
        ApiClient.getInstance().getDashboardData(userId).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DashboardResponse data = response.body();

                    // 2. Milieu : Direct + Alerte
                    if (data.casiers != null && !data.casiers.isEmpty()) {
                        txtNbActuel.setText(String.valueOf(data.casiers.get(0).nb_crustaces_actuel));
                    }

                    if (data.estSuspect) {
                        imgDanger.setVisibility(View.VISIBLE);
                        txtNbActuel.setTextColor(0xFFFF0000); // Rouge
                        txtDirectLabel.setText("ACTIVITÉ SUSPECTE !");
                    } else {
                        imgDanger.setVisibility(View.GONE);
                        txtNbActuel.setTextColor(0xFF80B7E9); // Bleu
                        txtDirectLabel.setText("crustacés dans le casier");
                    }

                    // 3. Bas : Historique
                    if (data.historique != null) {
                        adapter = new HistoriqueAdapter(data.historique);
                        rvHistorique.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                Toast.makeText(BordActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });

    }

}