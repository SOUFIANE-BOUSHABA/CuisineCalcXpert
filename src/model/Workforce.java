package model;

public class Workforce extends Component {
    private double tauxHoraire;
    private double heuresTravail;
    private double productiviteOuvrier;

    public Workforce() {}

    public Workforce(int id, String nom, double coutUnitaire, double quantite, double tauxTVA, double tauxHoraire, double heuresTravail, double productiviteOuvrier , Project project) {
        super(id, nom, coutUnitaire, quantite, "Workforce", tauxTVA , project);
        this.tauxHoraire = tauxHoraire;
        this.heuresTravail = heuresTravail;
        this.productiviteOuvrier = productiviteOuvrier;
    }

    public double getTauxHoraire() { return tauxHoraire; }
    public void setTauxHoraire(double tauxHoraire) { this.tauxHoraire = tauxHoraire; }
    public double getHeuresTravail() { return heuresTravail; }
    public void setHeuresTravail(double heuresTravail) { this.heuresTravail = heuresTravail; }
    public double getProductiviteOuvrier() { return productiviteOuvrier; }
    public void setProductiviteOuvrier(double productiviteOuvrier) { this.productiviteOuvrier = productiviteOuvrier; }

    @Override
    public String toString() {
        return "Labor{" +
                "tauxHoraire=" + tauxHoraire +
                ", heuresTravail=" + heuresTravail +
                ", productiviteOuvrier=" + productiviteOuvrier +
                "} " + super.toString();
    }
}
