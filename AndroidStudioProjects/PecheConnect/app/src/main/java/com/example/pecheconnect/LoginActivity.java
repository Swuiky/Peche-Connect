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

public class LoginActivity extends AppCompatActivity {

    private HelperDatabase dbHelper; // Instance de la BDD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialisation de la BDD
        dbHelper = new HelperDatabase(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Liaison avec l'XML (Assure-toi que les ID correspondent à ton layout login)
        EditText edtEmail = findViewById(R.id.edt_Email_Login);
        EditText edtMDP = findViewById(R.id.edt_MDP_Login);
        Button btnValider = findViewById(R.id.btn_Valider_Connexion);

        // Action du bouton "Se connecter"
        btnValider.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String mdp = edtMDP.getText().toString();

            if (dbHelper.checkUser(email, mdp)) {
                Intent intent = new Intent(LoginActivity.this, BordActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Identifiants incorrects", Toast.LENGTH_LONG).show();
            }
        });

        TextView btnVersInscription = findViewById(R.id.textView4);
        btnVersInscription.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}