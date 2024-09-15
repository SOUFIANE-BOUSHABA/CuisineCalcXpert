package model;

public class Project {
    private int id;
    private String nomProjet;
    private double margeBeneficiaire;
    private double coutTotal;
    private EtatProjet etatProjet;
    private int clientId;

    public enum EtatProjet {
        EN_COURS, TERMINE, ANNULE
    }

    public Project() {}

    public Project(int id, String nomProjet, double margeBeneficiaire, double coutTotal, EtatProjet etatProjet, int clientId) {
        this.id = id;
        this.nomProjet = nomProjet;
        this.margeBeneficiaire = margeBeneficiaire;
        this.coutTotal = coutTotal;
        this.etatProjet = etatProjet;
        this.clientId = clientId;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomProjet() { return nomProjet; }
    public void setNomProjet(String nomProjet) { this.nomProjet = nomProjet; }
    public double getMargeBeneficiaire() { return margeBeneficiaire; }
    public void setMargeBeneficiaire(double margeBeneficiaire) { this.margeBeneficiaire = margeBeneficiaire; }
    public double getCoutTotal() { return coutTotal; }
    public void setCoutTotal(double coutTotal) { this.coutTotal = coutTotal; }
    public String getEtatProjet() { return etatProjet.name(); }
    public void setEtatProjet(EtatProjet etatProjet) { this.etatProjet = etatProjet; }
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", nomProjet='" + nomProjet + '\'' +
                ", margeBeneficiaire=" + margeBeneficiaire +
                ", coutTotal=" + coutTotal +
                ", etatProjet=" + etatProjet +
                ", clientId=" + clientId +
                '}';
    }
}
