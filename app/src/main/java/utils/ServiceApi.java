package utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ServiceApi {

    // Adresse du serveur Flask (voir la ligne "Running on" dans PyCharm)
    public static final String URL_BASE = "http://192.168.68.57:5000";

    private static final String TAG = "ServiceApi";
    private static final int DELAI_MS = 10000; // 10 secondes max

    private ServiceApi() {
    }

    // ============================================================
    // Méthodes publiques — utilisées par les activités
    // ============================================================
    public static ReponseApi get(String chemin, String token) {
        return executer("GET", chemin, null, token);
    }

    public static ReponseApi post(String chemin, String corpsJson, String token) {
        return executer("POST", chemin, corpsJson, token);
    }

    // ============================================================
    // Méthode centrale — fait le vrai travail réseau
    // ============================================================
    private static ReponseApi executer(final String methode, final String chemin,
                                       final String corpsJson, final String token) {

        // Objet créé AVANT le thread, rempli DANS le thread
        final ReponseApi reponse = new ReponseApi(-1, "Serveur injoignable");

        Thread thread = new Thread() {
            @Override
            public void run() {
                HttpURLConnection urlConn = null;
                try {
                    // 1. URL complète
                    URL location = new URL(URL_BASE + chemin);
                    urlConn = (HttpURLConnection) location.openConnection();
                    urlConn.setRequestMethod(methode);
                    urlConn.setConnectTimeout(DELAI_MS);
                    urlConn.setReadTimeout(DELAI_MS);
                    urlConn.setRequestProperty("Accept", "application/json");

                    // 2. Token JWT si on en a un
                    if (token != null) {
                        urlConn.setRequestProperty("Authorization", "Bearer " + token);
                    }

                    // 3. Corps JSON pour les POST
                    if (corpsJson != null) {
                        urlConn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                        urlConn.setDoOutput(true);
                        OutputStream out = urlConn.getOutputStream();
                        out.write(corpsJson.getBytes(StandardCharsets.UTF_8));
                        out.close();
                    }

                    // 4. Code HTTP
                    int code = urlConn.getResponseCode();

                    // 5. Succès -> getInputStream ; erreur (400, 401...) -> getErrorStream
                    InputStream flux = (code >= 200 && code < 300)
                            ? urlConn.getInputStream()
                            : urlConn.getErrorStream();

                    String texte = "";
                    if (flux != null) {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(flux, StandardCharsets.UTF_8));
                        texte = lireReponse(in);
                        in.close();
                    }

                    reponse.setCode(code);
                    reponse.setCorps(texte);

                } catch (IOException e) {
                    Log.v(TAG, "Erreur connexion : " + e.getMessage());
                    // reponse garde -1 / "Serveur injoignable"
                } finally {
                    if (urlConn != null) {
                        urlConn.disconnect();
                    }
                }
            }
        };

        thread.start();
        try {
            thread.join(); // attendre la fin de l'appel réseau
        } catch (InterruptedException e) {
            Log.v(TAG, "Thread interrompu");
        }

        return reponse;
    }

    // Même méthode que dans le cours 14
    private static String lireReponse(BufferedReader in) throws IOException {
        StringBuilder sb = new StringBuilder();
        String ligne;
        while ((ligne = in.readLine()) != null) {
            sb.append(ligne);
        }
        return sb.toString();
    }
}