package com.example.pecheconnect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapActivity extends AppCompatActivity {

    private MapView map;
    private BottomNavigationView bottomNav;

    // N'oublie pas de mettre l'IP de ton PC si tu testes sur un vrai téléphone !
    private final String URL_API_POSITIONS = "http://10.0.2.2:3000/casiers/positions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Charger la config OSM
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_map);

        // 2. Initialisation des vues
        bottomNav = findViewById(R.id.bottomNavigation);
        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);

        // 3. Centrer la carte par défaut (sur Boulogne-sur-Mer par exemple)
        map.getController().setZoom(15.0);
        map.getController().setCenter(new GeoPoint(50.7305, 1.5700));

        // 4. Lancer la récupération des casiers
        recupererPositionsCasiers();

        // 5. Configuration de la barre de navigation
        bottomNav.setSelectedItemId(R.id.nav_map);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                startActivity(new Intent(MapActivity.this, BordActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_alerts) {
                startActivity(new Intent(MapActivity.this, AlertActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return id == R.id.nav_map;
        });
    }

    private void recupererPositionsCasiers() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_API_POSITIONS, null,
                response -> {
                    try {
                        // On efface les anciens marqueurs si la carte se met à jour
                        map.getOverlays().clear();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject casier = response.getJSONObject(i);

                            int idCasier = casier.getInt("id_casier");
                            double latitude = casier.getDouble("latitude");
                            double longitude = casier.getDouble("longitude");
                            int nbCrustaces = casier.getInt("nb_crustaces_actuel");
                            int volSuspecte = casier.getInt("vol_suspecte"); // On récupère l'info de vol

                            // Logique des couleurs
                            int iconeMarqueur = R.drawable.marqueur_bleu; // Bleu par défaut (Actif)

                            if (volSuspecte == 1) {
                                // ALERTE PRIORITAIRE : Marqueur Rouge
                                iconeMarqueur = R.drawable.marqueur_rouge;
                            } else if (nbCrustaces >= 10) {
                                // CASIER PLEIN : Marqueur Vert (tu peux changer le seuil de 10)
                                iconeMarqueur = R.drawable.marqueur_vert;
                            }

                            GeoPoint position = new GeoPoint(latitude, longitude);
                            String titre = "Casier N°" + idCasier + " (" + nbCrustaces + " prises)";

                            ajouterMarqueur(position, titre, iconeMarqueur);
                        }

                        // Forcer la carte à se redessiner avec les nouveaux marqueurs
                        map.invalidate();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MapActivity.this, "Erreur de lecture des données", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(MapActivity.this, "Impossible de charger la carte", Toast.LENGTH_LONG).show();
                });

        queue.add(jsonArrayRequest);
    }

    // La fonction utilitaire pour générer des beaux marqueurs redimensionnés
    private void ajouterMarqueur(GeoPoint position, String titre, int resId) {
        Marker m = new Marker(map);
        m.setPosition(position);
        m.setTitle(titre);

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), resId, null);
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            // Taille fixée à 120x170 pour un beau rendu
            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 330, 80, true));
            m.setIcon(d);
        }

        m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(m);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
}