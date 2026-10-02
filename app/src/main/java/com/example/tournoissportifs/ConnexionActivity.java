package com.example.tournoissportifs;

import android.content.Intent;
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

public class ConnexionActivity extends AppCompatActivity {

    private EditText txtEmail, txtMotDePasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        txtEmail = findViewById(R.id.txtEmail);
        txtMotDePasse = findViewById(R.id.txtMotDePasse);
    }

    // android:onClick="connecter" (btnConnexion)
    public void connecter(View view) {
        String email = txtEmail.getText().toString().trim();
        String motDePasse = txtMotDePasse.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(motDePasse)) {
            Toast.makeText(this, R.string.msg_champs_vides, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 1. Corps JSON : les clés doivent correspondre à celles attendues par Flask
            JSONObject corps = new JSONObject();
            corps.put("email", email);
            corps.put("password", motDePasse);

            // 2. POST /login (pas de token : on n'est pas encore connecté)
            ReponseApi reponse = ServiceApi.post("/login", corps.toString(), null);

            // 3. Serveur injoignable
            if (reponse.getCode() == -1) {
                Toast.makeText(this, R.string.msg_serveur_injoignable, Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject json = new JSONObject(reponse.getCorps());

            // 4. Erreur (400 ou 401) : on affiche le message envoyé par Flask
            if (!reponse.estSucces()) {
                Toast.makeText(this,
                        json.optString("message", getString(R.string.msg_erreur_connexion)),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 5. Succès : on garde le token, le prénom et le rôle dans la session
            Session.ouvrir(this, json.getString("token"), json.optString("prenom", ""),
                    json.optString("role", Session.ROLE_PARENT));

            // 6. Menu selon le rôle (la sécurité réelle est côté serveur : 403)
            Class<?> menu = Session.estAdmin(this) ? MenuAdminActivity.class
                                                   : MenuParentActivity.class;
            startActivity(new Intent(this, menu));
            finish();

        } catch (JSONException e) {
            Toast.makeText(this, R.string.msg_reponse_invalide, Toast.LENGTH_SHORT).show();
        }
    }

    // android:onClick="allerInscription" (lblVersInscription)
    public void allerInscription(View view) {
        startActivity(new Intent(this, InscriptionActivity.class));
        finish();
    }
}
