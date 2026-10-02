package com.example.tournoissportifs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import utils.ReponseApi;
import utils.ServiceApi;

public class InscriptionActivity extends AppCompatActivity {

    private EditText etPrenom, etNom, etEmail, etMotDePasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        etPrenom = findViewById(R.id.etPrenom);
        etNom = findViewById(R.id.etNom);
        etEmail = findViewById(R.id.etEmail);
        etMotDePasse = findViewById(R.id.etMotDePasse);

        Button btnInscrire = findViewById(R.id.btnInscrire);
        TextView tvVersConnexion = findViewById(R.id.tvVersConnexion);

        btnInscrire.setOnClickListener(v -> inscrire());

        tvVersConnexion.setOnClickListener(v -> {
            startActivity(new Intent(this, ConnexionActivity.class));
            finish();
        });
    }

    private void inscrire() {
        String prenom = etPrenom.getText().toString().trim();
        String nom = etNom.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String motDePasse = etMotDePasse.getText().toString();

        if (TextUtils.isEmpty(prenom) || TextUtils.isEmpty(nom)
                || TextUtils.isEmpty(email) || TextUtils.isEmpty(motDePasse)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Serveur injoignable", Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject json = new JSONObject(reponse.getCorps());

            // 3. Erreur 400 : courriel existant OU validation Marshmallow
            if (!reponse.estSucces()) {
                Toast.makeText(this, extraireErreur(json), Toast.LENGTH_LONG).show();
                return;
            }

            // 4. Succès (201)
            Toast.makeText(this, "Compte créé, vous pouvez vous connecter",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ConnexionActivity.class));
            finish();

        } catch (JSONException e) {
            Toast.makeText(this, "Réponse du serveur invalide", Toast.LENGTH_SHORT).show();
        }
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
            return "Données invalides";
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