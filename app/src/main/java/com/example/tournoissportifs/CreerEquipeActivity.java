package com.example.tournoissportifs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import utils.ReponseApi;
import utils.ServiceApi;
import utils.Session;

public class CreerEquipeActivity extends AppCompatActivity {

    private EditText txtNomEquipe, txtSport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_equipe);

        txtNomEquipe = findViewById(R.id.txtNomEquipe);
        txtSport = findViewById(R.id.txtSport);
    }

    // android:onClick="creer" (btnCreerEquipe)
    public void creer(View view) {
        String nom = txtNomEquipe.getText().toString().trim();
        String sport = txtSport.getText().toString().trim();

        if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(sport)) {
            Toast.makeText(this, R.string.msg_champs_vides, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject corps = new JSONObject();
            corps.put("nom", nom);
            corps.put("sport", sport);

            // Route protégée : on envoie le token de la session
            ReponseApi reponse = ServiceApi.post("/api/teams", corps.toString(),
                    Session.getToken(this));

            if (reponse.getCode() == -1) {
                Toast.makeText(this, R.string.msg_serveur_injoignable, Toast.LENGTH_LONG).show();
                return;
            }

            // Token expiré ou invalide -> retour à la connexion
            if (reponse.getCode() == 401) {
                Toast.makeText(this, R.string.msg_session_expiree, Toast.LENGTH_LONG).show();
                Session.expirer(this);
                return;
            }

            JSONObject json = new JSONObject(reponse.getCorps());

            if (!reponse.estSucces()) {
                Toast.makeText(this,
                        json.optString("message", getString(R.string.msg_donnees_invalides)),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, getString(R.string.msg_equipe_creee, json.optString("nom")),
                    Toast.LENGTH_SHORT).show();
            finish();

        } catch (JSONException e) {
            Toast.makeText(this, R.string.msg_reponse_invalide, Toast.LENGTH_SHORT).show();
        }
    }

    // android:onClick="onretour" (btnRetour)
    public void onretour(View view) {
        finish();
    }
}
