package com.example.pecheconnect;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText edtEmail = findViewById(R.id.edt_Email_Login);
        EditText edtMDP = findViewById(R.id.edt_MDP_Login);
        Button btnValider = findViewById(R.id.btn_Valider_Connexion);

        // Action du bouton "Se connecter"
        btnValider.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String mdp = edtMDP.getText().toString().trim();

            if (!email.isEmpty() && !mdp.isEmpty()) {

                ApiClient.checkUser(email, mdp, new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.body() != null && response.body().success) {
                            // Connexion réussie → on redirige
                            Intent intent = new Intent(LoginActivity.this, BordActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Identifiants incorrects", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        // Erreur réseau (Raspberry éteint, mauvaise IP, etc.)
                        Toast.makeText(LoginActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(this, "Champs vides !", Toast.LENGTH_SHORT).show();
            }
        });

        TextView btnVersInscription = findViewById(R.id.textView4);
        btnVersInscription.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}