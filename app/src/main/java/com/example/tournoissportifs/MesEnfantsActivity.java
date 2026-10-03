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

import modele.Enfant;
import utils.ReponseApi;
import utils.ServiceApi;
import utils.Session;

// PARENT : liste de mes enfants. Un clic ouvre les équipes de l'enfant.
public class MesEnfantsActivity extends AppCompatActivity {

    private ListView lvEnfants;
    private final ArrayList<Enfant> enfants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_enfants);

        lvEnfants = findViewById(R.id.lvEnfants);

        // Clic sur un ÉLÉMENT de liste : pas d'équivalent android:onClick en XML
        lvEnfants.setOnItemClickListener((parent, view, position, id) -> {
            if (position < enfants.size()) {
                Enfant e = enfants.get(position);
                Intent intent = new Intent(this, EquipesEnfantActivity.class);
                intent.putExtra(EquipesEnfantActivity.EXTRA_ENFANT_ID, e.getId());
                intent.putExtra(EquipesEnfantActivity.EXTRA_ENFANT_PRENOM, e.getPrenom());
                startActivity(intent);
            }
        });
    }

    // Recharge au retour de l'écran "Ajouter un enfant"
    @Override
    protected void onResume() {
        super.onResume();
        chargerEnfants();
    }

    private void chargerEnfants() {
        ReponseApi reponse = ServiceApi.get("/api/enfants", Session.getToken(this));

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

        enfants.clear();
        ArrayList<String> affichage = new ArrayList<>();
        try {
            // Réponse : {"enfants": [ {id, prenom, nom, date_naissance}, ... ]}
            JSONArray liste = new JSONObject(reponse.getCorps()).getJSONArray("enfants");

            for (int i = 0; i < liste.length(); i++) {
                JSONObject o = liste.getJSONObject(i);
                String date = o.isNull("date_naissance") ? null : o.optString("date_naissance");
                Enfant e = new Enfant(o.getString("id"), o.getString("prenom"),
                        o.getString("nom"), date);
                enfants.add(e);
                affichage.add(date == null
                        ? getString(R.string.format_enfant, e.getPrenom(), e.getNom())
                        : getString(R.string.format_enfant_date, e.getPrenom(), e.getNom(), date));
            }
        } catch (JSONException e) {
            Toast.makeText(this, R.string.msg_reponse_invalide, Toast.LENGTH_SHORT).show();
            return;
        }

        if (affichage.isEmpty()) {
            affichage.add(getString(R.string.msg_aucun_enfant));
        }

        lvEnfants.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, affichage));
    }

    // android:onClick="ouvrirAjouterEnfant" (btnAjouterEnfant)
    public void ouvrirAjouterEnfant(View view) {
        startActivity(new Intent(this, AjouterEnfantActivity.class));
    }

    // android:onClick="onretour" (btnRetour)
    public void onretour(View view) {
        finish();
    }
}
