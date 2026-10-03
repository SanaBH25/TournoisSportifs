package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class ReponseApi {

    // Code HTTP renvoyé par le serveur (200, 201, 400, 401, 403, 404...)
    // -1 = le serveur n'a pas pu être joint (erreur réseau)
    private int code;

    // Contenu de la réponse (texte JSON envoyé par Flask)
    private String corps;

    public ReponseApi() {
    }

    public ReponseApi(int code, String corps) {
        this.code = code;
        this.corps = corps;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCorps() {
        return corps;
    }

    public void setCorps(String corps) {
        this.corps = corps;
    }

    // Vrai pour toute réponse 2xx :
    // 200 (OK) pour /login, 201 (Created) pour /signup et la création d'équipe
    public boolean estSucces() {
        return code >= 200 && code < 300;
    }

    // Extrait un message d'erreur lisible du JSON envoyé par Flask :
    //   {"message": "..."}                          -> le message
    //   {"details": {"champ": ["erreur", ...]}}     -> "champ : erreur" (une ligne par champ)
    // Retourne parDefaut si le corps n'est pas du JSON exploitable.
    public String getMessageErreur(String parDefaut) {
        try {
            JSONObject json = new JSONObject(corps);
            if (json.has("message")) {
                return json.optString("message", parDefaut);
            }
            JSONObject details = json.optJSONObject("details");
            if (details != null) {
                StringBuilder sb = new StringBuilder();
                Iterator<String> champs = details.keys();
                while (champs.hasNext()) {
                    String champ = champs.next();
                    JSONArray messages = details.optJSONArray(champ);
                    if (messages != null && messages.length() > 0) {
                        sb.append(champ).append(" : ").append(messages.optString(0)).append("\n");
                    }
                }
                if (sb.length() > 0) {
                    return sb.toString().trim();
                }
            }
        } catch (Exception e) {
            // corps vide ou pas du JSON : on garde le message par défaut
        }
        return parDefaut;
    }

    @Override
    public String toString() {
        return "ReponseApi{" +
                "code=" + code +
                ", corps='" + corps + '\'' +
                '}';
    }
}
