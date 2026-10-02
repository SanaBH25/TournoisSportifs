package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.tournoissportifs.ConnexionActivity;

public class Session {

    private static final String FICHIER = "session";
    private static final String CLE_TOKEN = "token";
    private static final String CLE_PRENOM = "prenom";
    private static final String CLE_ROLE = "role";

    // Valeurs envoyées par Flask (enum RoleUtilisateur)
    public static final String ROLE_PARENT = "parent";
    public static final String ROLE_ADMIN_EQUIPE = "admin_equipe";
    public static final String ROLE_ADMIN_PLATEFORME = "admin_plateforme";

    private Session() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE);
    }

    public static void ouvrir(Context context, String token, String prenom, String role) {
        prefs(context).edit()
                .putString(CLE_TOKEN, token)
                .putString(CLE_PRENOM, prenom)
                .putString(CLE_ROLE, role)
                .apply();
    }

    public static String getToken(Context context) {
        return prefs(context).getString(CLE_TOKEN, null);
    }

    public static String getPrenom(Context context) {
        return prefs(context).getString(CLE_PRENOM, "");
    }

    public static String getRole(Context context) {
        return prefs(context).getString(CLE_ROLE, ROLE_PARENT);
    }

    // Sert UNIQUEMENT à choisir le menu à afficher.
    // La vraie protection est côté serveur (@role_requis -> 403).
    public static boolean estAdmin(Context context) {
        String role = getRole(context);
        return ROLE_ADMIN_EQUIPE.equals(role) || ROLE_ADMIN_PLATEFORME.equals(role);
    }

    public static void fermer(Context context) {
        prefs(context).edit().clear().apply();
    }

    // Vide la session et renvoie à l'écran de connexion.
    // CLEAR_TASK vide la pile : le bouton Retour ne ramène pas dans l'app.
    // Utilisée pour la déconnexion volontaire ET pour le token expiré (401).
    public static void deconnecter(Activity activite) {
        fermer(activite);
        Intent intent = new Intent(activite, ConnexionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activite.startActivity(intent);
        activite.finish();
    }

    // Conservée pour la lisibilité : même effet que deconnecter()
    public static void expirer(Activity activite) {
        deconnecter(activite);
    }
}
