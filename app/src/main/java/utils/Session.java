package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.app.Activity;
import android.content.Intent;

import com.example.tournoissportifs.ConnexionActivity;

public class Session {

    private static final String FICHIER = "session";
    private static final String CLE_TOKEN = "token";
    private static final String CLE_PRENOM = "prenom";

    private Session() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE);
    }

    public static void ouvrir(Context context, String token, String prenom) {
        prefs(context).edit()
                .putString(CLE_TOKEN, token)
                .putString(CLE_PRENOM, prenom)
                .apply();
    }

    public static String getToken(Context context) {
        return prefs(context).getString(CLE_TOKEN, null);
    }

    public static String getPrenom(Context context) {
        return prefs(context).getString(CLE_PRENOM, "");
    }

    public static void fermer(Context context) {
        prefs(context).edit().clear().apply();
    }

    // Appelée quand le serveur répond 401 : on vide la session
    // et on renvoie l'usager à l'écran de connexion
    public static void expirer(Activity activite) {
        fermer(activite);
        Intent intent = new Intent(activite, ConnexionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activite.startActivity(intent);
        activite.finish();
    }
}