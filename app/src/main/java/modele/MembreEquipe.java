package modele;

public class MembreEquipe {

    private String teamId;
    private String nom;
    private String sport;
    private String role;


    public MembreEquipe() {
    }

    public MembreEquipe(String nom, String role, String sport) {
        this.nom = nom;
        this.role = role;
        this.sport = sport;
    }

    public MembreEquipe(String nom, String role, String sport, String teamId) {
        this.nom = nom;
        this.role = role;
        this.sport = sport;
        this.teamId = teamId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "MembreEquipe{" +
                "nom='" + nom + '\'' +
                ", teamId='" + teamId + '\'' +
                ", sport='" + sport + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
