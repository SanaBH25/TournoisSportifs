package com.example.tournoissportifs;

import android.content.Intent;
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

// ADMIN : équipes que je gère. Un clic ouvre l'écran Modifier / Supprimer.
public class MesEquipesActivity extends AppCompatActivity {

    private ListView lvMesEquipes;
    private final ArrayList<Equipe> equipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_equipes);

        lvMesEquipes = findViewById(R.id.lvMesEquipes);

        // Clic sur un ÉLÉMENT de liste : pas d'équivalent android:onClick en XML
        lvMesEquipes.setOnItemClickListener((parent, view, position, id) -> {
            if (position < equipes.size()) {
                ouvrirModification(equipes.get(position));
            }
        });
    }

    // onResume (et non onCreate) : la liste se recharge au retour de
    // l'écran Modifier/Supprimer, pour refléter les changements
    @Override
    protected void onResume() {
        super.onResume();
        chargerEquipes();
    }

    private void chargerEquipes() {
        ReponseApi reponse = ServiceApi.get("/api/teams", Session.getToken(this));

        if (reponse.getCode() == -1) {
            Toast.makeText(this, R.string.msg_serveur_injoignable, Toast.LENGTH_LONG).show();
            return;
        }
        if (reponse.getCode() == 401) {
            Toast.makeText(this, R.string.msg_session_expiree, Toast.LENGTH_LONG).show();
            Session.expirer(this);
            return;
        }
        if (!reponse.estSucces()) {
            Toast.makeText(this, reponse.getMessageErreur(
                    getString(R.string.msg_erreur_code, reponse.getCode())), Toast.LENGTH_SHORT).show();
            return;
        }

        equipes.clear();
        ArrayList<String> affichage = new ArrayList<>();
        try {
            // Réponse : {"teams": [ {id, nom, sport, saison, nb_inscrits}, ... ]}
            JSONArray teams = new JSONObject(reponse.getCorps()).getJSONArray("teams");

            for (int i = 0; i < teams.length(); i++) {
                JSONObject t = teams.getJSONObject(i);
                Equipe e = new Equipe(t.getString("id"), t.getString("nom"), t.getString("sport"));
                e.setSaison(t.isNull("saison") ? "" : t.optString("saison", ""));
                e.setNbInscrits(t.optInt("nb_inscrits", 0));
                equipes.add(e);
                affichage.add(getString(R.string.format_equipe_admin,
                        e.getNom(), e.getSport(), e.getNbInscrits()));
            }
        } catch (JSONException e) {
            Toast.makeText(this, R.string.msg_reponse_invalide, Toast.LENGTH_SHORT).show();
            return;
        }

        if (affichage.isEmpty()) {
            affichage.add(getString(R.string.msg_aucune_equipe_geree));
        }

        lvMesEquipes.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, affichage));
    }

    private void ouvrirModification(Equipe e) {
        Intent intent = new Intent(this, ModifierEquipeActivity.class);
        intent.putExtra(ModifierEquipeActivity.EXTRA_TEAM_ID, e.getId());
        intent.putExtra(ModifierEquipeActivity.EXTRA_TEAM_NOM, e.getNom());
        intent.putExtra(ModifierEquipeActivity.EXTRA_TEAM_SPORT, e.getSport());
        intent.putExtra(ModifierEquipeActivity.EXTRA_TEAM_SAISON, e.getSaison());
        startActivity(intent);
    }

    // android:onClick="onretour" (btnRetour)
    public void onretour(View view) {
        finish();
    }
}
