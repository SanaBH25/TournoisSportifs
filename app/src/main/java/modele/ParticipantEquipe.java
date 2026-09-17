package modele;

public class ParticipantEquipe {

    private String nomUsager;
    private String role;
    private String teamId;

    public ParticipantEquipe() {
    }

    public ParticipantEquipe(String nomUsager, String role, String teamId) {
        this.nomUsager = nomUsager;
        this.role = role;
        this.teamId = teamId;
    }

    public String getNomUsager() {
        return nomUsager;
    }

    public void setNomUsager(String nomUsager) {
        this.nomUsager = nomUsager;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "ParticipantEquipe{" +
                "nomUsager='" + nomUsager + '\'' +
                ", role='" + role + '\'' +
                ", teamId='" + teamId + '\'' +
                '}';
    }
}
 