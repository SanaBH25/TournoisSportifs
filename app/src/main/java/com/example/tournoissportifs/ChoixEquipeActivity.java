package com.example.tournoissportifs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import utils.Session;

public class ChoixEquipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_equipe);

        // Le prénom vient maintenant de la session, plus besoin d'usagerId
        TextView tvBienvenue = findViewById(R.id.tvBienvenue);
        tvBienvenue.setText("Bienvenue " + Session.getPrenom(this) + " !");

        Button btnRejoindre = findViewById(R.id.btnRejoindre);
        Button btnCreer = findViewById(R.id.btnCreer);
        Button btnMesEquipes = findViewById(R.id.btnMesEquipes);

        btnRejoindre.setOnClickListener(v ->
                startActivity(new Intent(this, RejoindreEquipeActivity.class)));

        btnCreer.setOnClickListener(v ->
                startActivity(new Intent(this, CreerEquipeActivity.class)));

        btnMesEquipes.setOnClickListener(v ->
                startActivity(new Intent(this, MesEquipesActivity.class)));
    }

    public void onquitte(View view) {
        finish();
    }
}