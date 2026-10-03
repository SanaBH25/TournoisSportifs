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

// PARENT : ajouter un enfant (POST /api/enfants)
public class AjouterEnfantActivity extends AppCompatActivity {

    private EditText txtPrenom, txtNom, txtDateNaissance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_enfant);

        txtPrenom = findViewById(R.id.txtPrenom);
        txtNom = findViewById(R.id.txtNom);
        txtDateNaissance = findViewById(R.id.txtDateNaissance);
    }

    // android:onClick="ajouter" (btnAjouter)
    public void ajouter(View view) {
        String prenom = txtPrenom.getText().toString().trim();
        String nom = txtNom.getText().toString().trim();
        String date = txtDateNaissance.getText().toString().trim();

        if (TextUtils.isEmpty(prenom) || TextUtils.isEmpty(nom)) {
            Toast.makeText(this, R.string.msg_champs_vides, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject corps = new JSONObject();
            corps.put("prenom", prenom);
            corps.put("nom", nom);
            // Date optionnelle : on ne l'envoie que si elle est saisie (format AAAA-MM-JJ)
            if (!date.isEmpty()) {
                corps.put("date_naissance", date);
            }

            ReponseApi reponse = ServiceApi.post("/api/enfants", corps.toString(),
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
                // ex. date_naissance : Not a valid date.
                Toast.makeText(this, reponse.getMessageErreur(
                        getString(R.string.msg_donnees_invalides)), Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(this, getString(R.string.msg_enfant_ajoute, prenom),
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
