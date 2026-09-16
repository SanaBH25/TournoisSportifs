package modele;

import java.util.Date;

public class Joueur {
    private String id;
    private String teamId;
    private String prenom;
    private String nom;
    private Date dateNaissance;

    public Joueur() {
    }

    public Joueur(Date dateNaissance, String id, String nom, String prenom, String teamId) {
        this.dateNaissance = dateNaissance;
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.teamId = teamId;
    }

    public Joueur(Date dateNaissance, String nom, String prenom, String teamId) {
        this.dateNaissance = dateNaissance;
        this.nom = nom;
        this.prenom = prenom;
        this.teamId = teamId;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "dateNaissance=" + dateNaissance +
                ", id='" + id + '\'' +
                ", teamId='" + teamId + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }
}
