package com.example.tournoissportifs;

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

import modele.Equipe;
import utils.ReponseApi;
import utils.ServiceApi;
import utils.Session;

// PARENT : liste de toutes les équipes. Deux modes :
//  - SANS enfant (depuis le menu)      : simple consultation
//  - AVEC enfant (depuis ses équipes)  : un clic inscrit l'enfant à l'équipe
public class ChoixEquipeActivity extends AppCompatActivity {

    private final ArrayList<Equipe> equipes = new ArrayList<>();
    private String enfantId;      // null = mode consultation
    private String enfantPrenom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_equipe);

        enfantId = getIntent().getStringExtra(EquipesEnfantActivity.EXTRA_ENFANT_ID);
        enfantPrenom = getIntent().getStringExtra(EquipesEnfantActivity.EXTRA_ENFANT_PRENOM);

        TextView lblTitre = findViewById(R.id.lblTitre);
        lblTitre.setText(enfantId == null
                ? getString(R.string.titre_toutes_equipes)
                : getString(R.string.titre_inscrire_enfant, enfantPrenom));

        ListView lvEquipes = findViewById(R.id.lvEquipes);

        if (!chargerEquipes()) {
            return;
        }

        ArrayList<String> affichage = new ArrayList<>();
        for (Equipe e : equipes) {
            int format = e.isDejaInscrit() ? R.string.format_equipe_inscrit : R.string.format_equipe;
            affichage.add(getString(format, e.getNom(), e.getSport()));
        }
        if (affichage.isEmpty()) {
            affichage.add(getString(R.string.msg_aucune_equipe_disponible));
        }

        lvEquipes.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, affichage));

        // En mode inscription seulement : un clic inscrit l'enfant
        // (clic sur un ÉLÉMENT de liste : pas d'équivalent android:onClick en XML)
        if (enfantId != null) {
            lvEquipes.setOnItemClickListener((parent, view, position, id) -> {
                if (position < equipes.size()) {
                    inscrire(equipes.get(position));
                }
            });
        }
    }

    // Remplit "equipes" ; retourne false en cas d'erreur
    private boolean chargerEquipes() {
        // Avec enfant_id, le serveur indique où l'enfant est déjà inscrit
        String chemin = enfantId == null ? "/api/teams/all"
                                         : "/api/teams/all?enfant_id=" + enfantId;
        ReponseApi reponse = ServiceApi.get(chemin, Session.getToken(this));

        if (reponse.getCode() == -1) {
            Toast.makeText(this, R.string.msg_serveur_injoignable, Toast.LENGTH_LONG).show();
            return false;
        }
        if (reponse.getCode() == 401) {
            Toast.makeText(this, R.string.msg_session_expiree, Toast.LENGTH_LONG).show();
            Session.expirer(this);
            return false;
        }
        if (!reponse.estSucces()) {
            Toast.makeText(this, reponse.getMessageErreur(
                    getString(R.string.msg_erreur_code, reponse.getCode())), Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            JSONArray teams = new JSONObject(reponse.getCorps()).getJSONArray("teams");
            for (int i = 0; i < teams.length(); i++) {
                JSONObject t = teams.getJSONObject(i);
                Equipe e = new Equipe(t.getString("id"), t.getString("nom"), t.getString("sport"));
                e.setDejaInscrit(t.optBoolean("deja_inscrit", false));
                equipes.add(e);
            }
            return true;
        } catch (JSONException e) {
            Toast.makeText(this, R.string.msg_reponse_invalide, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // POST /api/enfants/<enfant_id>/equipes/<team_id>
    private void inscrire(Equipe equipe) {
        if (equipe.isDejaInscrit()) {
            Toast.makeText(this, getString(R.string.msg_deja_inscrit, enfantPrenom),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ReponseApi reponse = ServiceApi.post(
                "/api/enfants/" + enfantId + "/equipes/" + equipe.getId(),
                null, Session.getToken(this));

        if (reponse.getCode() == -1) {
            Toast.makeText(this, R.string.msg_serveur_injoignable, Toast.LENGTH_LONG).show();
            return;
        }
        if (reponse.getCode() == 401) {
            Toast.makeText(this, R.string.msg_session_expiree, Toast.LENGTH_LONG).show();
            Session.expirer(this);
            return;
        }

        // Succès (201) ou erreur (400 déjà inscrit, 404...) : Flask envoie un "message"
        Toast.makeText(this, reponse.getMessageErreur(
                getString(R.string.msg_erreur_code, reponse.getCode())), Toast.LENGTH_SHORT).show();

        if (reponse.estSucces()) {
            finish();
        }
    }

    // android:onClick="onretour" (btnRetour)
    public void onretour(View view) {
        finish();
    }
}
