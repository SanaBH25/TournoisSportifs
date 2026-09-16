package modele;

public class Equipe {
    private String id;
    private String nom;
    private String sport;

    public Equipe() {
    }

    public Equipe(String nom, String sport) {
        this.nom = nom;
        this.sport = sport;
    }

    public Equipe(String id, String nom, String sport) {
        this.id = id;
        this.nom = nom;
        this.sport = sport;
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

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    @Override
    public String toString() {
        return "Equipe{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", sport='" + sport + '\'' +
                '}';
    }
}
