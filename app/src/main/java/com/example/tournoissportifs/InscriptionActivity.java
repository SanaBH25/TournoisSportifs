package com.example.tournoissportifs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import utils.ReponseApi;
import utils.ServiceApi;

public class InscriptionActivity extends AppCompatActivity {

    private EditText txtPrenom, txtNom, txtEmail, txtMotDePasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        txtPrenom = findViewById(R.id.txtPrenom);
        txtNom = findViewById(R.id.txtNom);
        txtEmail = findViewById(R.id.txtEmail);
        txtMotDePasse = findViewById(R.id.txtMotDePasse);
    }

    // android:onClick="inscrire" (btnInscrire)
    public void inscrire(View view) {
        String prenom = txtPrenom.getText().toString().trim();
        String nom = txtNom.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String motDePasse = txtMotDePasse.getText().toString();

        if (TextUtils.isEmpty(prenom) || TextUtils.isEmpty(nom)
                || TextUtils.isEmpty(email) || TextUtils.isEmpty(motDePasse)) {
            Toast.makeText(this, R.string.msg_champs_vides, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 1. Corps JSON : mêmes clés que UserSchema côté Flask
            JSONObject corps = new JSONObject();
            corps.put("email", email);
            corps.put("password", motDePasse);
            corps.put("nom", nom);
            corps.put("prenom", prenom);

            // 2. POST /signup (pas de token)
            ReponseApi reponse = ServiceApi.post("/signup", corps.toString(), null);

            if (reponse.getCode() == -1) {
                Toast.makeText(this, R.string.msg_serveur_injoignable, Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject json = new JSONObject(reponse.getCorps());

            // 3. Erreur 400 : courriel existant OU validation Marshmallow
            if (!reponse.estSucces()) {
                Toast.makeText(this, extraireErreur(json), Toast.LENGTH_LONG).show();
                return;
            }

            // 4. Succès (201)
            Toast.makeText(this, R.string.msg_compte_cree, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ConnexionActivity.class));
            finish();

        } catch (JSONException e) {
            Toast.makeText(this, R.string.msg_reponse_invalide, Toast.LENGTH_SHORT).show();
        }
    }

    // android:onClick="allerConnexion" (lblVersConnexion)
    public void allerConnexion(View view) {
        startActivity(new Intent(this, ConnexionActivity.class));
        finish();
    }

    // Transforme la réponse d'erreur de Flask en texte lisible
    private String extraireErreur(JSONObject json) {
        // Cas simple : {"message": "Cet email existe déjà"}
        if (json.has("message")) {
            return json.optString("message");
        }

        // Cas validation : {"details": {"password": ["Shorter than..."], ...}}
        JSONObject details = json.optJSONObject("details");
        if (details == null) {
            return getString(R.string.msg_donnees_invalides);
        }

        StringBuilder sb = new StringBuilder();
        Iterator<String> champs = details.keys();
        while (champs.hasNext()) {
            String champ = champs.next();
            JSONArray messages = details.optJSONArray(champ);
            if (messages != null && messages.length() > 0) {
                sb.append(champ).append(" : ").append(messages.optString(0)).append("\n");
            }
        }
        return sb.toString().trim();
    }
}
