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

import modele.Equipe;
import utils.ReponseApi;
import utils.ServiceApi;
import utils.Session;

public class RejoindreEquipeActivity extends AppCompatActivity {

    private final ArrayList<Equipe> equipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejoindre_equipe);

        ListView lvEquipes = findViewById(R.id.lvEquipes);

        if (!chargerToutesLesEquipes()) {
            return;
        }

        ArrayList<String> affichage = new ArrayList<>();
        for (Equipe e : equipes) {
            String ligne = e.getNom() + " (" + e.getSport() + ")";
            if (e.isEstMembre()) {
                ligne += " ✓ déjà membre";
            }
            affichage.add(ligne);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, affichage);
        lvEquipes.setAdapter(arrayAdapter);

        lvEquipes.setOnItemClickListener((parent, view, position, id) ->
                rejoindre(equipes.get(position)));
    }

    // Remplit la liste "equipes" ; retourne false en cas d'erreur
    private boolean chargerToutesLesEquipes() {
        ReponseApi reponse = ServiceApi.get("/api/teams/all", Session.getToken(this));

        if (reponse.getCode() == -1) {
            Toast.makeText(this, "Serveur injoignable", Toast.LENGTH_LONG).show();
            return false;
        }
        if (reponse.getCode() == 401) {
            Toast.makeText(this, "Session expirée, reconnectez-vous", Toast.LENGTH_LONG).show();
            Session.expirer(this);
            return false;
        }

        try {
            JSONObject json = new JSONObject(reponse.getCorps());
            JSONArray teams = json.getJSONArray("teams");

            for (int i = 0; i < teams.length(); i++) {
                JSONObject t = teams.getJSONObject(i);
                Equipe e = new Equipe(t.getString("id"), t.getString("nom"), t.getString("sport"));
                e.setEstMembre(t.optBoolean("est_membre", false));
                equipes.add(e);
            }
            return true;
        } catch (JSONException e) {
            Toast.makeText(this, "Réponse du serveur invalide", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void rejoindre(Equipe equipe) {
        // Vérification locale d'abord : évite un appel réseau inutile
        if (equipe.isEstMembre()) {
            Toast.makeText(this, "Vous êtes déjà membre de cette équipe", Toast.LENGTH_SHORT).show();
            return;
        }

        // POST sans corps : l'id de l'équipe est dans l'URL, l'usager dans le token
        ReponseApi reponse = ServiceApi.post("/api/teams/" + equipe.getId() + "/join",
                null, Session.getToken(this));

        if (reponse.getCode() == -1) {
            Toast.makeText(this, "Serveur injoignable", Toast.LENGTH_LONG).show();
            return;
        }
        if (reponse.getCode() == 401) {
            Toast.makeText(this, "Session expirée, reconnectez-vous", Toast.LENGTH_LONG).show();
            Session.expirer(this);
            return;
        }

        try {
            JSONObject json = new JSONObject(reponse.getCorps());
            Toast.makeText(this, json.optString("message", "Erreur " + reponse.getCode()),
                    Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this, "Réponse du serveur invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        if (reponse.estSucces()) {
            finish();
        }
    }

    public void onretour(View view) {
        finish();
    }
}