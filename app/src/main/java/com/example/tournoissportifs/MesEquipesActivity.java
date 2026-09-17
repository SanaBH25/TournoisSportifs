package com.example.tournoissportifs;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import dao.MembreEquipeAdapter;
import modele.MembreEquipe;

public class MesEquipesActivity extends AppCompatActivity {

    private long usagerId;
    private MembreEquipeAdapter membreEquipeAdapter;
    private ListView lvMesEquipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_equipes);

        usagerId = getIntent().getLongExtra(ConnexionActivity.EXTRA_USAGER_ID, -1);
        membreEquipeAdapter = new MembreEquipeAdapter(this);
        lvMesEquipes = findViewById(R.id.lvMesEquipes);

        chargerEquipes();
    }

    private void chargerEquipes() {
        ArrayList<MembreEquipe> mesEquipes = membreEquipeAdapter.listerParUsager(usagerId);
        ArrayList<String> affichage = new ArrayList<>();

        for (MembreEquipe me : mesEquipes) {
            affichage.add(me.getNom() + " (" + me.getSport() + ") - Rôle: " + me.getRole());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, affichage);
        lvMesEquipes.setAdapter(adapter);
    }

    public void onretour(View view) {
        finish();
    }
}
