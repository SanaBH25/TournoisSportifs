package com.example.tournoissportifs;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import modele.Enfant;
import utils.ReponseApi;
import utils.ServiceApi;
import utils.Session;

// ADMIN : joueurs inscrits à une équipe. Un clic propose de retirer le joueur.
public class JoueursEquipeActivity extends AppCompatActivity {

    public static final String EXTRA_TEAM_ID = "teamId";
    public static final String EXTRA_TEAM_NOM = "teamNom";

    private String teamId;
    private String teamNom;
    private ListView lvJoueurs;
    private final ArrayList<Enfant> joueurs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joueurs_equipe);

        teamId = getIntent().getStringExtra(EXTRA_TEAM_ID);
        teamNom = getIntent().getStringExtra(EXTRA_TEAM_NOM);

        TextView lblTitre = findViewById(R.id.lblTitre);
        lblTitre.setText(getString(R.string.titre_joueurs_equipe, teamNom));

        lvJoueurs = findViewById(R.id.lvJoueurs);

        // Clic sur un ÉLÉMENT de liste : pas d'équivalent android:onClick en XML
        lvJoueurs.setOnItemClickListener((parent, view, position, id) -> {
            if (position < joueurs.size()) {
                confirmerRetrait(joueurs.get(position));
            }
        });

        chargerJoueurs();
    }

    // GET /api/teams/<id>/joueurs
    private void chargerJoueurs() {
        ReponseApi reponse = ServiceApi.get("/api/teams/" + teamId + "/joueurs",
                Session.getToken(this));

        if (traiterErreurs(reponse)) {
            return;
        }

        joueurs.clear();
        ArrayList<String> affichage = new ArrayList<>();
        try {
            // Réponse : {"team": {...}, "joueurs": [ {id, prenom, nom, date_naissance, parent, parent_email}, ... ]}
            JSONArray liste = new JSONObject(reponse.getCorps()).getJSONArray("joueurs");
            for (int i = 0; i < liste.length(); i++) {
                JSONObject o = liste.getJSONObject(i);
                String date = o.isNull("date_naissance") ? null : o.optString("date_naissance");
                Enfant e = new Enfant(o.getString("id"), o.getString("prenom"),
                        o.getString("nom"), date);
                joueurs.add(e);
                affichage.add(getString(R.string.format_joueur,
                        e.getPrenom(), e.getNom(), o.optString("parent", "")));
            }
        } catch (JSONException e) {
            Toast.makeText(this, R.string.msg_reponse_invalide, Toast.LENGTH_SHORT).show();
            return;
        }

        if (affichage.isEmpty()) {
            affichage.add(getString(R.string.msg_aucun_joueur));
        }

        lvJoueurs.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, affichage));
    }

    private void confirmerRetrait(Enfant joueur) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.titre_retirer_joueur)
                .setMessage(getString(R.string.msg_confirmer_retrait,
                        joueur.getPrenom() + " " + joueur.getNom(), teamNom))
                .setPositiveButton(R.string.btn_retirer, (dialog, which) -> retirer(joueur))
                .setNegativeButton(R.string.btn_annuler, null)
                .show();
    }

    // DELETE /api/teams/<team_id>/joueurs/<enfant_id> (l'enfant reste chez son parent)
    private void retirer(Enfant joueur) {
        ReponseApi reponse = ServiceApi.delete(
                "/api/teams/" + teamId + "/joueurs/" + joueur.getId(), Session.getToken(this));

        if (traiterErreurs(reponse)) {
            return;
        }
        Toast.makeText(this, reponse.getMessageErreur(getString(R.string.msg_joueur_retire)),
                Toast.LENGTH_SHORT).show();
        chargerJoueurs();
    }

    // Affiche l'erreur s'il y en a une ; retourne true si l'appel a échoué
    private boolean traiterErreurs(ReponseApi reponse) {
        if (reponse.getCode() == -1) {
            Toast.makeText(this, R.string.msg_serveur_injoignable, Toast.LENGTH_LONG).show();
            return true;
        }
        if (reponse.getCode() == 401) {
            Toast.makeText(this, R.string.msg_session_expiree, Toast.LENGTH_LONG).show();
            Session.expirer(this);
            return true;
        }
        if (!reponse.estSucces()) {
            Toast.makeText(this, reponse.getMessageErreur(
                    getString(R.string.msg_erreur_code, reponse.getCode())), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    // android:onClick="onretour" (btnRetour)
    public void onretour(View view) {
        finish();
    }
}
