package com.example.pecheconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class AlertActivity extends AppCompatActivity {

    private ListView listViewAlertes;
    private Button btnEffacerAlertes;
    private SwipeRefreshLayout swipeRefresh;
    private AlerteAdapter adapter;
    private final int currentUserId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        listViewAlertes = findViewById(R.id.listViewAlertes);
        btnEffacerAlertes = findViewById(R.id.btn_effacer_alertes);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        // Initialise l'adaptateur avec une liste vide
        adapter = new AlerteAdapter(this, new ArrayList<>());
        listViewAlertes.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::loadAlertesData);
        loadAlertesData();

        btnEffacerAlertes.setOnClickListener(v -> declencherEffacementAlertes(currentUserId));

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_alerts);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                startActivity(new Intent(this, BordActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            if (id == R.id.nav_map) {
                startActivity(new Intent(this, MapActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return id == R.id.nav_alerts;
        });
    }

    private void loadAlertesData() {
        swipeRefresh.setRefreshing(true);

        // On appelle l'endpoint
        ApiClient.getInstance().getAlertes().enqueue(new Callback<List<AlerteItem>>() {
            @Override
            public void onResponse(Call<List<AlerteItem>> call, Response<List<AlerteItem>> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<AlerteItem> alertes = response.body();
                    adapter.clear();
                    adapter.addAll(alertes);
                    adapter.notifyDataSetChanged();

                    if (alertes.isEmpty()) {
                        Toast.makeText(AlertActivity.this,
                                "Aucune alerte active", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AlerteItem>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(AlertActivity.this,
                        "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void declencherEffacementAlertes(int userId) {
        RemonteRequest request = new RemonteRequest(-1, userId);
        ApiClient.effacerAlertes(request).enqueue(new Callback<RemonteResponse>() {
            @Override
            public void onResponse(Call<RemonteResponse> call, Response<RemonteResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    Toast.makeText(AlertActivity.this, "Alertes effacées !", Toast.LENGTH_SHORT).show();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<RemonteResponse> call, Throwable t) {
                Toast.makeText(AlertActivity.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }
}