package com.example.tournoissportifs;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dao.EquipeAdapter;
import dao.MembreEquipeAdapter;
import modele.Equipe;

public class CreerEquipeActivity extends AppCompatActivity {

    private long usagerId;
    private EditText etNomEquipe, etSport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_equipe);

        usagerId = getIntent().getLongExtra(ConnexionActivity.EXTRA_USAGER_ID, -1);

        etNomEquipe = findViewById(R.id.etNomEquipe);
        etSport = findViewById(R.id.etSport);
        Button btnCreerEquipe = findViewById(R.id.btnCreerEquipe);

        btnCreerEquipe.setOnClickListener(v -> creer());
    }

    private void creer() {
        String nom = etNomEquipe.getText().toString().trim();
        String sport = etSport.getText().toString().trim();

        if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(sport)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        EquipeAdapter equipeAdapter = new EquipeAdapter(this);
        MembreEquipeAdapter membreEquipeAdapter = new MembreEquipeAdapter(this);

        Equipe equipe = new Equipe(nom, sport);
        long idEquipe = equipeAdapter.inserer(equipe);

        membreEquipeAdapter.inserer(usagerId, idEquipe, "Gérant");

        Toast.makeText(this, "Équipe " + nom + " créée !", Toast.LENGTH_SHORT).show();
        finish();
    }
}
