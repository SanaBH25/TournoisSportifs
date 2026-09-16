package com.example.tournoissportifs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dao.UsagerAdapter;
import modele.Usager;
import utils.Utilitaire;

public class InscriptionActivity extends AppCompatActivity {

    private EditText etPrenom, etNom, etEmail, etMotDePasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        etPrenom = findViewById(R.id.etPrenom);
        etNom = findViewById(R.id.etNom);
        etEmail = findViewById(R.id.etEmail);
        etMotDePasse = findViewById(R.id.etMotDePasse);

        Button btnInscrire = findViewById(R.id.btnInscrire);
        TextView tvVersConnexion = findViewById(R.id.tvVersConnexion);

        btnInscrire.setOnClickListener(v -> inscrire());

        tvVersConnexion.setOnClickListener(v -> {
            startActivity(new Intent(this, ConnexionActivity.class));
            finish();
        });
    }

    private void inscrire() {
        String prenom = etPrenom.getText().toString().trim();
        String nom = etNom.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String motDePasse = etMotDePasse.getText().toString();

        if (TextUtils.isEmpty(prenom) || TextUtils.isEmpty(nom)
                || TextUtils.isEmpty(email) || TextUtils.isEmpty(motDePasse)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        UsagerAdapter usagerAdapter = new UsagerAdapter(this);

        if (usagerAdapter.getParEmail(email) != null) {
            Toast.makeText(this, "Un compte existe déjà avec cet email", Toast.LENGTH_SHORT).show();
            return;
        }

        String motDePasseHache = Utilitaire.hacherMotDePasse(motDePasse);
        Usager usager = new Usager(null, email, null, motDePasseHache, nom, prenom);
        usagerAdapter.inserer(usager);

        Toast.makeText(this, "Compte créé, vous pouvez vous connecter", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ConnexionActivity.class));
        finish();
    }
}
