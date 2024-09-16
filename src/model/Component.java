package model;

public abstract class Component {
    private int id;
    private String nom;
    private double coutUnitaire;
    private double quantite;
    private String typeComposant;
    private double tauxTVA;
    private Project project;

    public Component() {}

    public Component(int id, String nom, double coutUnitaire, double quantite, String typeComposant, double tauxTVA , Project project) {
        this.id = id;
        this.nom = nom;
        this.coutUnitaire = coutUnitaire;
        this.quantite = quantite;
        this.typeComposant = typeComposant;
        this.tauxTVA = tauxTVA;
        this.project = project;

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getCoutUnitaire() { return coutUnitaire; }
    public void setCoutUnitaire(double coutUnitaire) { this.coutUnitaire = coutUnitaire; }
    public double getQuantite() { return quantite; }
    public void setQuantite(double quantite) { this.quantite = quantite; }
    public String getTypeComposant() { return typeComposant; }
    public void setTypeComposant(String typeComposant) { this.typeComposant = typeComposant; }
    public double getTauxTVA() { return tauxTVA; }
    public void setTauxTVA(double tauxTVA) { this.tauxTVA = tauxTVA; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }


    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", coutUnitaire=" + coutUnitaire +
                ", quantite=" + quantite +
                ", typeComposant='" + typeComposant + '\'' +
                ", tauxTVA=" + tauxTVA +
                '}';
    }
}
