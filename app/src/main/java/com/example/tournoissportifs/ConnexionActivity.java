package com.example.tournoissportifs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import utils.ReponseApi;
import utils.ServiceApi;
import utils.Session;

public class ConnexionActivity extends AppCompatActivity {

    // Gardées pour ne pas casser les autres activités pendant la migration
    public static final String EXTRA_USAGER_ID = "usagerId";
    public static final String EXTRA_USAGER_PRENOM = "usagerPrenom";

    private EditText etEmail, etMotDePasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        etEmail = findViewById(R.id.etEmail);
        etMotDePasse = findViewById(R.id.etMotDePasse);

        Button btnConnexion = findViewById(R.id.btnConnexion);
        TextView tvVersInscription = findViewById(R.id.tvVersInscription);

        btnConnexion.setOnClickListener(v -> connecter());

        tvVersInscription.setOnClickListener(v -> {
            startActivity(new Intent(this, InscriptionActivity.class));
            finish();
        });
    }

    private void connecter() {
        String email = etEmail.getText().toString().trim();
        String motDePasse = etMotDePasse.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(motDePasse)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 1. Corps JSON : les clés doivent correspondre à celles attendues par Flask
            JSONObject corps = new JSONObject();
            corps.put("email", email);
            corps.put("password", motDePasse);

            // 2. Appel POST /login (pas de token : on n'est pas encore connecté)
            ReponseApi reponse = ServiceApi.post("/login", corps.toString(), null);

            // 3. Serveur injoignable
            if (reponse.getCode() == -1) {
                Toast.makeText(this, "Serveur injoignable", Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject json = new JSONObject(reponse.getCorps());

            // 4. Erreur (400 ou 401) : on affiche le message envoyé par Flask
            if (!reponse.estSucces()) {
                Toast.makeText(this, json.optString("message", "Erreur de connexion"),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 5. Succès : on garde le token et le prénom
            String token = json.getString("token");
            String prenom = json.optString("prenom", "");
            Session.ouvrir(this, token, prenom);

            Intent intent = new Intent(this, ChoixEquipeActivity.class);
            intent.putExtra(EXTRA_USAGER_PRENOM, prenom);
            startActivity(intent);
            finish();

        } catch (JSONException e) {
            Toast.makeText(this, "Réponse du serveur invalide", Toast.LENGTH_SHORT).show();
        }
    }
}