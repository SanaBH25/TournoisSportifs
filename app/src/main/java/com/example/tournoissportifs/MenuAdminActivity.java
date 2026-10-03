package com.example.tournoissportifs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import utils.Session;

// Menu de l'ADMIN d'équipes : créer, consulter, modifier, supprimer ses équipes
public class MenuAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        TextView lblBienvenue = findViewById(R.id.lblBienvenue);
        lblBienvenue.setText(getString(R.string.bienvenue_admin, Session.getPrenom(this)));
    }

    // android:onClick="ouvrirCreer" (btnCreer)
    public void ouvrirCreer(View view) {
        startActivity(new Intent(this, CreerEquipeActivity.class));
    }

    // android:onClick="ouvrirMesEquipes" (btnMesEquipes)
    public void ouvrirMesEquipes(View view) {
        startActivity(new Intent(this, MesEquipesActivity.class));
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
