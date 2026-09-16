package com.example.tournoissportifs;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import utils.Utilitaire;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        String hash1 = Utilitaire.hacherMotDePasse("monMotDePasse123");
//        Log.d("TEST_BCRYPT", "Hash obtenu : " + hash1);
//        String hash2 = Utilitaire.hacherMotDePasse("monMotDePasse123");
//        Log.d("TEST_BCRYPT", "Hash obtenu : " + hash2);

//        String hash = Utilitaire.hacherMotDePasse("abc123");
//        Log.d("TEST_BCRYPT", "Hash : " + hash);
//        Log.d("TEST_BCRYPT", "Vérif bon mdp : " + Utilitaire.verifierMotDePasse("abc123", hash));
//        Log.d("TEST_BCRYPT", "Vérif mauvais mdp : " + Utilitaire.verifierMotDePasse("mauvais", hash));
    }


}