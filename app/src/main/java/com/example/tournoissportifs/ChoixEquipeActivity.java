package com.example.tournoissportifs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChoixEquipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_equipe);

        long usagerId = getIntent().getLongExtra(ConnexionActivity.EXTRA_USAGER_ID, -1);
        String prenom = getIntent().getStringExtra(ConnexionActivity.EXTRA_USAGER_PRENOM);

        TextView tvBienvenue = findViewById(R.id.tvBienvenue);
        tvBienvenue.setText("Bienvenue " + prenom + " !");

        Button btnRejoindre = findViewById(R.id.btnRejoindre);
        Button btnCreer = findViewById(R.id.btnCreer);
        Button btnMesEquipes = findViewById(R.id.btnMesEquipes);

        btnRejoindre.setOnClickListener(v -> {
            Intent intent = new Intent(this, RejoindreEquipeActivity.class);
            intent.putExtra(ConnexionActivity.EXTRA_USAGER_ID, usagerId);
            startActivity(intent);
        });

        btnCreer.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreerEquipeActivity.class);
            intent.putExtra(ConnexionActivity.EXTRA_USAGER_ID, usagerId);
            startActivity(intent);
        });

        btnMesEquipes.setOnClickListener(v -> {
            Intent intent = new Intent(this, MesEquipesActivity.class);
            intent.putExtra(ConnexionActivity.EXTRA_USAGER_ID, usagerId);
            startActivity(intent);
        });
    }

    public void onquitte(View view) {
        finish();
    }
}
