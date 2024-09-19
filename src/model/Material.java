package model;

public class Material extends Component {
    private double coutUnitaire;
    private double quantite;
    private double coefficientQualite;
    private double coutTransport;

    public Material() {}

    public Material(int id, String nom, double coutUnitaire, double quantite, double tauxTVA, double coutTransport, double coefficientQualite, Project project) {
        super(id, nom, "Matériel", tauxTVA, project);
        this.coutUnitaire = coutUnitaire;
        this.quantite = quantite;
        this.coefficientQualite = coefficientQualite;
        this.coutTransport = coutTransport;
    }

    public double getCoutUnitaire() { return coutUnitaire; }
    public void setCoutUnitaire(double coutUnitaire) { this.coutUnitaire = coutUnitaire; }
    public double getQuantite() { return quantite; }
    public void setQuantite(double quantite) { this.quantite = quantite; }
    public double getCoefficientQualite() { return coefficientQualite; }
    public void setCoefficientQualite(double coefficientQualite) { this.coefficientQualite = coefficientQualite; }
    public double getCoutTransport() { return coutTransport; }
    public void setCoutTransport(double coutTransport) { this.coutTransport = coutTransport; }

    @Override
    public String toString() {
        return "Material{" +
                "coutUnitaire=" + coutUnitaire +
                ", quantite=" + quantite +
                ", coefficientQualite=" + coefficientQualite +
                ", coutTransport=" + coutTransport +
                "} " + super.toString();
    }
}
