package com.example.pecheconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail, edtMDP;
    private Button btn_Enregistrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEmail = findViewById(R.id.edt_Email);
        edtMDP = findViewById(R.id.edt_MDP);
        btn_Enregistrer = findViewById(R.id.btn_Enregistrer);

        // Action pour S'ENREGISTRER
        btn_Enregistrer.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String mdp = edtMDP.getText().toString().trim();

            if (!email.isEmpty() && !mdp.isEmpty()) {

                ApiClient.insertUser(email, mdp, new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.body() != null && response.body().success) {
                            // Inscription réussie → on redirige
                            Intent intent = new Intent(MainActivity.this, BordActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Email déjà utilisé !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        // Erreur réseau (Raspberry éteint, mauvaise IP, etc.)
                        Toast.makeText(MainActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(this, "Champs vides !", Toast.LENGTH_SHORT).show();
            }
        });

        // Bouton vers LoginActivity
        TextView btnConnexion = findViewById(R.id.textView4);
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}