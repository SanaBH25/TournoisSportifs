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

public class ConnexionActivity extends AppCompatActivity {

    public static final String EXTRA_USAGER_ID = "usagerId";
    public static final String EXTRA_USAGER_PRENOM = "usagerPrenom";

    private EditText etEmail, etMotDePasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        etEmail = findViewById(R.id.etEmail);
        etMotDePasse = findViewById(R.id.etMotDePasse);

        Button btnConnexion = findViewById(R.id.btnConnexion);
        TextView tvVersInscription = findViewById(R.id.tvVersInscription);

        btnConnexion.setOnClickListener(v -> connecter());

        tvVersInscription.setOnClickListener(v -> {
            startActivity(new Intent(this, InscriptionActivity.class));
            finish();
        });
    }

    private void connecter() {
        String email = etEmail.getText().toString().trim();
        String motDePasse = etMotDePasse.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(motDePasse)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        UsagerAdapter usagerAdapter = new UsagerAdapter(this);
        Usager usager = usagerAdapter.getParEmail(email);

        if (usager == null || !Utilitaire.verifierMotDePasse(motDePasse, usager.getMotDePasse())) {
            Toast.makeText(this, "Email ou mot de passe invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ChoixEquipeActivity.class);
        intent.putExtra(EXTRA_USAGER_ID, Long.parseLong(usager.getId()));
        intent.putExtra(EXTRA_USAGER_PRENOM, usager.getPrenom());
        startActivity(intent);
        finish();
    }
}
