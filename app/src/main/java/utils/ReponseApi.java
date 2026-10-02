package utils;

public class ReponseApi {

    // Code HTTP renvoyé par le serveur (200, 201, 400, 401...)
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

    @Override
    public String toString() {
        return "ReponseApi{" +
                "code=" + code +
                ", corps='" + corps + '\'' +
                '}';
    }
}