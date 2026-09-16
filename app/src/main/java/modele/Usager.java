package modele;

import java.util.ArrayList;

public class Usager {
    private String id;
    private String prenom;
    private String nom;
    private ArrayList<Joueur> listEnfant;
    private String email;
    private String motDePasse;
    private String accessToken;

    public Usager() {
    }

    public Usager(String accessToken, String email, String id, ArrayList<Joueur> listEnfant, String motDePasse, String nom, String prenom) {
        this.accessToken = accessToken;
        this.email = email;
        this.id = id;
        this.listEnfant = listEnfant;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Usager(String accessToken, String email, ArrayList<Joueur> listEnfant, String motDePasse, String nom, String prenom) {
        this.accessToken = accessToken;
        this.email = email;
        this.listEnfant = listEnfant;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Joueur> getListEnfant() {
        return listEnfant;
    }

    public void setListEnfant(ArrayList<Joueur> listEnfant) {
        this.listEnfant = listEnfant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    @Override
    public String toString() {
        return "Usager{" +
                "accessToken='" + accessToken + '\'' +
                ", id='" + id + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", listEnfant=" + listEnfant +
                ", email='" + email + '\'' +
                '}';
    }
}
