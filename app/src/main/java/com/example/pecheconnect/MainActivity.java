package com.example.pecheconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // Ajouté pour le bouton s'enregistrer
import android.widget.EditText; // Ajouté pour lire les champs
import android.widget.TextView;
import android.widget.Toast; // Ajouté pour afficher les messages

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // --- AJOUT POUR LA BDD ---
    private HelperDatabase dbHelper;
    private EditText edtEmail, edtMDP;
    private Button btn_Enregistrer;
    // -------------------------

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

        // --- INITIALISATION BDD ET VUES ---
        dbHelper = new HelperDatabase(this);
        edtEmail = findViewById(R.id.edt_Email);
        edtMDP = findViewById(R.id.edt_MDP);
        btn_Enregistrer = findViewById(R.id.btn_Enregistrer);

        // Action pour S'ENREGISTRER (C08 - Coder)
        btn_Enregistrer.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String mdp = edtMDP.getText().toString();

            if (!email.isEmpty() && !mdp.isEmpty()) {
                boolean isInserted = dbHelper.insertUser(email, mdp);
                if (isInserted) {
                    Intent intent = new Intent(MainActivity.this, BordActivity.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Champs vides !", Toast.LENGTH_SHORT).show();
            }
        });
        // ----------------------------------

        // Ton code existant pour aller à la connexion
        TextView btnConnexion = findViewById(R.id.textView4);
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}