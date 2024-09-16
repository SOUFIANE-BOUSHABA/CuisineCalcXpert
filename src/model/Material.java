package model;

public class Material extends Component {
    private double coefficientQualite;
    private double coutTransport;

    public Material() {}

    public Material(int id, String nom, double coutUnitaire, double quantite, double tauxTVA, double coutTransport, double coefficientQualite , Project project) {
        super(id, nom, coutUnitaire, quantite, "Matériel", tauxTVA , project);
        this.coefficientQualite = coefficientQualite;
        this.coutTransport = coutTransport;
    }

    public double getCoefficientQualite() { return coefficientQualite; }
    public void setCoefficientQualite(double coefficientQualite) { this.coefficientQualite = coefficientQualite; }

    public double getCoutTransport() { return coutTransport; }
    public void setCoutTransport(double coutTransport) { this.coutTransport = coutTransport; }


    @Override
    public String toString() {
        return "Material{" +
                "coefficientQualite=" + coefficientQualite +
                ", coutTransport=" + coutTransport +
                "} " + super.toString();
    }
}
