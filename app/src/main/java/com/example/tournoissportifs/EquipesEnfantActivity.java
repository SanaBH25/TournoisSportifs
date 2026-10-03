package com.example.tournoissportifs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.ReponseApi;
import utils.ServiceApi;
import utils.Session;

// PARENT : équipes auxquelles un enfant est inscrit + bouton pour l'inscrire ailleurs
public class EquipesEnfantActivity extends AppCompatActivity {

    public static final String EXTRA_ENFANT_ID = "enfantId";
    public static final String EXTRA_ENFANT_PRENOM = "enfantPrenom";

    private String enfantId;
    private String enfantPrenom;
    private ListView lvEquipesEnfant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipes_enfant);

        enfantId = getIntent().getStringExtra(EXTRA_ENFANT_ID);
        enfantPrenom = getIntent().getStringExtra(EXTRA_ENFANT_PRENOM);

        TextView lblTitre = findViewById(R.id.lblTitre);
        lblTitre.setText(getString(R.string.titre_equipes_enfant, enfantPrenom));

        lvEquipesEnfant = findViewById(R.id.lvEquipesEnfant);
    }

    // Recharge au retour de l'écran d'inscription
    @Override
    protected void onResume() {
        super.onResume();
        chargerEquipes();
    }

    private void chargerEquipes() {
        ReponseApi reponse = ServiceApi.get("/api/enfants/" + enfantId + "/equipes",
                Session.getToken(this));

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

        ArrayList<String> affichage = new ArrayList<>();
        try {
            // Réponse : {"enfant": {...}, "teams": [ {id, nom, sport, saison}, ... ]}
            JSONArray teams = new JSONObject(reponse.getCorps()).getJSONArray("teams");
            for (int i = 0; i < teams.length(); i++) {
                JSONObject t = teams.getJSONObject(i);
                affichage.add(getString(R.string.format_equipe,
                        t.getString("nom"), t.getString("sport")));
            }
        } catch (JSONException e) {
            Toast.makeText(this, R.string.msg_reponse_invalide, Toast.LENGTH_SHORT).show();
            return;
        }

        if (affichage.isEmpty()) {
            affichage.add(getString(R.string.msg_enfant_aucune_equipe, enfantPrenom));
        }

        lvEquipesEnfant.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, affichage));
    }

    // android:onClick="ouvrirInscription" (btnInscrire)
    // Ouvre la liste des équipes en mode "choisir une équipe pour cet enfant"
    public void ouvrirInscription(View view) {
        Intent intent = new Intent(this, ChoixEquipeActivity.class);
        intent.putExtra(EXTRA_ENFANT_ID, enfantId);
        intent.putExtra(EXTRA_ENFANT_PRENOM, enfantPrenom);
        startActivity(intent);
    }

    // android:onClick="onretour" (btnRetour)
    public void onretour(View view) {
        finish();
    }
}
