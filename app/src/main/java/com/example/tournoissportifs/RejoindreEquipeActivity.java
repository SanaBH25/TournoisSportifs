package com.example.tournoissportifs;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import dao.EquipeAdapter;
import dao.MembreEquipeAdapter;
import modele.Equipe;

public class RejoindreEquipeActivity extends AppCompatActivity {

    private long usagerId;
    private ArrayList<Equipe> equipes;
    private EquipeAdapter equipeAdapter;
    private MembreEquipeAdapter membreEquipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejoindre_equipe);

        usagerId = getIntent().getLongExtra(ConnexionActivity.EXTRA_USAGER_ID, -1);

        equipeAdapter = new EquipeAdapter(this);
        membreEquipeAdapter = new MembreEquipeAdapter(this);

        ListView lvEquipes = findViewById(R.id.lvEquipes);
        equipes = equipeAdapter.lister();

        ArrayList<String> affichage = new ArrayList<>();
        for (Equipe e : equipes) {
            affichage.add(e.getNom() + " (" + e.getSport() + ")");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, affichage);
        lvEquipes.setAdapter(arrayAdapter);

        lvEquipes.setOnItemClickListener((parent, view, position, id) -> rejoindre(equipes.get(position)));
    }

    private void rejoindre(Equipe equipe) {
        long idEquipe = Long.parseLong(equipe.getId());

        if (membreEquipeAdapter.estMembre(usagerId, idEquipe)) {
            Toast.makeText(this, "Vous êtes déjà membre de cette équipe", Toast.LENGTH_SHORT).show();
            return;
        }

        membreEquipeAdapter.inserer(usagerId, idEquipe, "Joueur");
        Toast.makeText(this, "Équipe " + equipe.getNom() + " rejointe !", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onretour(View view) {
        finish();
    }
}
