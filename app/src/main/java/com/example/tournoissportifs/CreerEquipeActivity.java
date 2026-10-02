package com.example.tournoissportifs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import utils.ReponseApi;
import utils.ServiceApi;
import utils.Session;

public class CreerEquipeActivity extends AppCompatActivity {

    private EditText etNomEquipe, etSport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_equipe);

        etNomEquipe = findViewById(R.id.etNomEquipe);
        etSport = findViewById(R.id.etSport);
        Button btnCreerEquipe = findViewById(R.id.btnCreerEquipe);

        btnCreerEquipe.setOnClickListener(v -> creer());
    }

    private void creer() {
        String nom = etNomEquipe.getText().toString().trim();
        String sport = etSport.getText().toString().trim();

        if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(sport)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Serveur injoignable", Toast.LENGTH_LONG).show();
                return;
            }

            // Token expiré ou invalide -> retour à la connexion
            if (reponse.getCode() == 401) {
                Toast.makeText(this, "Session expirée, reconnectez-vous", Toast.LENGTH_LONG).show();
                Session.expirer(this);
                return;
            }

            JSONObject json = new JSONObject(reponse.getCorps());

            if (!reponse.estSucces()) {
                Toast.makeText(this, json.optString("message", "Données invalides"),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Équipe " + json.optString("nom") + " créée !",
                    Toast.LENGTH_SHORT).show();
            finish();

        } catch (JSONException e) {
            Toast.makeText(this, "Réponse du serveur invalide", Toast.LENGTH_SHORT).show();
        }
    }

    public void onretour(View view) {
        finish();
    }
}