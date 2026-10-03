package com.example.tournoissportifs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import utils.Session;

// Menu du PARENT : voir les équipes, gérer ses enfants et leurs inscriptions
public class MenuParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_parent);

        TextView lblBienvenue = findViewById(R.id.lblBienvenue);
        lblBienvenue.setText(getString(R.string.bienvenue, Session.getPrenom(this)));
    }

    // android:onClick="ouvrirEquipes" (btnVoirEquipes)
    // ChoixEquipeActivity sans enfant = simple consultation
    public void ouvrirEquipes(View view) {
        startActivity(new Intent(this, ChoixEquipeActivity.class));
    }

    // android:onClick="ouvrirMesEnfants" (btnMesEnfants)
    public void ouvrirMesEnfants(View view) {
        startActivity(new Intent(this, MesEnfantsActivity.class));
    }

    // android:onClick="deconnecter" (btnDeconnexion)
    public void deconnecter(View view) {
        Toast.makeText(this, R.string.msg_deconnecte, Toast.LENGTH_SHORT).show();
        Session.deconnecter(this);
    }

    // android:onClick="onquitte" (btnQuitter)
    public void onquitte(View view) {
        finish();
    }
}
