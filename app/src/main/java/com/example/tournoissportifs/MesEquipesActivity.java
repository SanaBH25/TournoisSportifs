package com.example.tournoissportifs;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.ReponseApi;
import utils.ServiceApi;
import utils.Session;

public class MesEquipesActivity extends AppCompatActivity {

    private ListView lvMesEquipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_equipes);

        lvMesEquipes = findViewById(R.id.lvMesEquipes);
        chargerEquipes();
    }

    private void chargerEquipes() {
        ReponseApi reponse = ServiceApi.get("/api/teams", Session.getToken(this));

        if (reponse.getCode() == -1) {
            Toast.makeText(this, "Serveur injoignable", Toast.LENGTH_LONG).show();
            return;
        }
        if (reponse.getCode() == 401) {
            Toast.makeText(this, "Session expirée, reconnectez-vous", Toast.LENGTH_LONG).show();
            Session.expirer(this);
            return;
        }
        if (!reponse.estSucces()) {
            Toast.makeText(this, "Erreur " + reponse.getCode(), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> affichage = new ArrayList<>();
        try {
            // Réponse : {"teams": [ {id, nom, sport, saison, role}, ... ]}
            JSONObject json = new JSONObject(reponse.getCorps());
            JSONArray teams = json.getJSONArray("teams");

            for (int i = 0; i < teams.length(); i++) {
                JSONObject t = teams.getJSONObject(i);
                affichage.add(t.getString("nom") + " (" + t.getString("sport") + ")"
                        + " - Rôle : " + t.optString("role", "?"));
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Réponse du serveur invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        if (affichage.isEmpty()) {
            affichage.add("Vous n'êtes membre d'aucune équipe");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, affichage);
        lvMesEquipes.setAdapter(adapter);
    }

    public void onretour(View view) {
        finish();
    }
}