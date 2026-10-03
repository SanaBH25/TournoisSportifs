package com.example.tournoissportifs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import utils.ReponseApi;
import utils.ServiceApi;
import utils.Session;

// ADMIN : modifier ou supprimer une équipe qu'il gère
public class ModifierEquipeActivity extends AppCompatActivity {

    public static final String EXTRA_TEAM_ID = "teamId";
    public static final String EXTRA_TEAM_NOM = "teamNom";
    public static final String EXTRA_TEAM_SPORT = "teamSport";
    public static final String EXTRA_TEAM_SAISON = "teamSaison";

    private String teamId;
    private EditText txtNomEquipe, txtSport, txtSaison;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_equipe);

        txtNomEquipe = findViewById(R.id.txtNomEquipe);
        txtSport = findViewById(R.id.txtSport);
        txtSaison = findViewById(R.id.txtSaison);

        // On pré-remplit le formulaire avec les valeurs actuelles
        teamId = getIntent().getStringExtra(EXTRA_TEAM_ID);
        txtNomEquipe.setText(getIntent().getStringExtra(EXTRA_TEAM_NOM));
        txtSport.setText(getIntent().getStringExtra(EXTRA_TEAM_SPORT));
        txtSaison.setText(getIntent().getStringExtra(EXTRA_TEAM_SAISON));
    }

    // android:onClick="enregistrer" (btnEnregistrer) -> PUT /api/teams/<id>
    public void enregistrer(View view) {
        String nom = txtNomEquipe.getText().toString().trim();
        String sport = txtSport.getText().toString().trim();
        String saison = txtSaison.getText().toString().trim();

        if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(sport)) {
            Toast.makeText(this, R.string.msg_champs_vides, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject corps = new JSONObject();
            corps.put("nom", nom);
            corps.put("sport", sport);
            corps.put("saison", saison.isEmpty() ? JSONObject.NULL : saison);

            ReponseApi reponse = ServiceApi.put("/api/teams/" + teamId, corps.toString(),
                    Session.getToken(this));

            if (traiterErreurs(reponse)) {
                return;
            }
            Toast.makeText(this, R.string.msg_equipe_modifiee, Toast.LENGTH_SHORT).show();
            finish();

        } catch (JSONException e) {
            Toast.makeText(this, R.string.msg_reponse_invalide, Toast.LENGTH_SHORT).show();
        }
    }

    // android:onClick="ouvrirJoueurs" (btnVoirJoueurs)
    public void ouvrirJoueurs(View view) {
        Intent intent = new Intent(this, JoueursEquipeActivity.class);
        intent.putExtra(JoueursEquipeActivity.EXTRA_TEAM_ID, teamId);
        intent.putExtra(JoueursEquipeActivity.EXTRA_TEAM_NOM, txtNomEquipe.getText().toString());
        startActivity(intent);
    }

    // android:onClick="supprimer" (btnSupprimer) : on demande confirmation d'abord
    public void supprimer(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.titre_confirmer_suppression)
                .setMessage(getString(R.string.msg_confirmer_suppression,
                        txtNomEquipe.getText().toString()))
                .setPositiveButton(R.string.btn_supprimer, (dialog, which) -> supprimerEquipe())
                .setNegativeButton(R.string.btn_annuler, null)
                .show();
    }

    // DELETE /api/teams/<id> (supprime aussi les inscriptions des enfants)
    private void supprimerEquipe() {
        ReponseApi reponse = ServiceApi.delete("/api/teams/" + teamId, Session.getToken(this));

        if (traiterErreurs(reponse)) {
            return;
        }
        Toast.makeText(this, reponse.getMessageErreur(getString(R.string.msg_equipe_supprimee)),
                Toast.LENGTH_SHORT).show();
        finish();
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
            // 400 (données), 403 (pas votre équipe), 404 (introuvable)
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
