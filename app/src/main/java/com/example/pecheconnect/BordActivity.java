package com.example.pecheconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BordActivity extends AppCompatActivity {

    private TextView txtNbActuel, txtDirectLabel;
    private ImageView imgDanger;
    private RecyclerView rvHistorique;
    private HistoriqueAdapter adapter;
    private Button btnRemonter;
    private SwipeRefreshLayout swipeRefresh;

    private int currentCasierId = -1;
    private final int currentUserId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bord);

        txtNbActuel = findViewById(R.id.txt_nb_actuel);
        txtDirectLabel = findViewById(R.id.txt_direct_label);
        imgDanger = findViewById(R.id.img_danger_alert);
        rvHistorique = findViewById(R.id.rv_historique);
        btnRemonter = findViewById(R.id.btn_remonter);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        rvHistorique.setLayoutManager(new LinearLayoutManager(this));

        swipeRefresh.setOnRefreshListener(() -> loadData(currentUserId));
        loadData(currentUserId);

        btnRemonter.setOnClickListener(v -> {
            if (currentCasierId != -1) {
                declencherRemontee(currentCasierId, currentUserId);
            } else {
                Toast.makeText(this, "Chargement en cours...", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_dashboard);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_alerts) {
                startActivity(new Intent(BordActivity.this, AlertActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }if (id == R.id.nav_map) {
                startActivity(new Intent(BordActivity.this, MapActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return id == R.id.nav_dashboard;
        });
    }

    private void loadData(int userId) {
        swipeRefresh.setRefreshing(true);
        ApiClient.getInstance().getDashboardData(userId).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    DashboardResponse data = response.body();
                    if (data.casiers != null && !data.casiers.isEmpty()) {
                        currentCasierId = data.casiers.get(0).id_casier;
                        txtNbActuel.setText(String.valueOf(data.casiers.get(0).nb_crustaces_actuel));
                    }
                    if (data.estSuspect) {
                        imgDanger.setVisibility(View.VISIBLE);
                        txtNbActuel.setTextColor(0xFFFF0000);
                        txtDirectLabel.setText("ACTIVITÉ SUSPECTE !");
                    } else {
                        imgDanger.setVisibility(View.GONE);
                        txtNbActuel.setTextColor(0xFF80B7E9);
                        txtDirectLabel.setText("crustacés dans le casier");
                    }
                    if (data.historique != null) {
                        adapter = new HistoriqueAdapter(data.historique);
                        rvHistorique.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(BordActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void declencherRemontee(int idCasier, int idUser) {
        RemonteRequest request = new RemonteRequest(idCasier, idUser);
        ApiClient.getInstance().remonterCasier(request).enqueue(new Callback<RemonteResponse>() {
            @Override
            public void onResponse(Call<RemonteResponse> call, Response<RemonteResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    Toast.makeText(BordActivity.this, "Casier remonté !", Toast.LENGTH_SHORT).show();
                    loadData(idUser);
                }
            }
            @Override
            public void onFailure(Call<RemonteResponse> call, Throwable t) {
                Toast.makeText(BordActivity.this, "Serveur injoignable", Toast.LENGTH_SHORT).show();
            }
        });
    }
}