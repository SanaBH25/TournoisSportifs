package modele;

public class Equipe {
    private String id;
    private String nom;
    private String sport;
    private String saison;
    private boolean dejaInscrit;  // parent : l'enfant choisi est déjà inscrit à cette équipe
    private int nbInscrits;       // admin : nombre d'enfants inscrits

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

    public String getSaison() {
        return saison;
    }

    public void setSaison(String saison) {
        this.saison = saison;
    }

    public boolean isDejaInscrit() {
        return dejaInscrit;
    }

    public void setDejaInscrit(boolean dejaInscrit) {
        this.dejaInscrit = dejaInscrit;
    }

    public int getNbInscrits() {
        return nbInscrits;
    }

    public void setNbInscrits(int nbInscrits) {
        this.nbInscrits = nbInscrits;
    }

    @Override
    public String toString() {
        return "Equipe{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", sport='" + sport + '\'' +
                ", saison='" + saison + '\'' +
                ", dejaInscrit=" + dejaInscrit +
                ", nbInscrits=" + nbInscrits +
                '}';
    }
}
