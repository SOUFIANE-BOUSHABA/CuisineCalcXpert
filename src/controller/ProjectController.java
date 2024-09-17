package controller;

import model.Client;
import model.Material;
import model.Project;
import model.Workforce;
import service.ClientService;
import service.MaterialService;
import service.ProjectService;
import service.WorkforceService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjectController {
    private final ClientService clientService;
    private final ProjectService projectService;
    private  WorkforceService workforceService;
    private  MaterialService materialService;
    private final Scanner scanner;
    private double surface;

    public ProjectController(ClientService clientService, ProjectService projectService , WorkforceService workforceService , MaterialService materialService) {
        this.clientService = clientService;
        this.projectService = projectService;
        this.workforceService = workforceService;
        this.materialService = materialService;
        this.scanner = new Scanner(System.in);
    }

    public void createProject() {
        System.out.println("--- Création d'un Nouveau Projet ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        Client client = null;

        switch (choice) {
            case 1:
                System.out.println("--- Recherche de client existant ---");
                System.out.print("Entrez le nom du client : ");
                String clientName = scanner.nextLine();

                Optional<Client> clientOpt = clientService.findByNom(clientName);
                if (clientOpt.isPresent()) {
                    client = clientOpt.get();
                    System.out.println("Client trouvé !");
                    System.out.println("Nom : " + client.getNom());
                    System.out.println("Adresse : " + client.getAdresse());
                    System.out.println("Numéro de téléphone : " + client.getTelephone());

                    System.out.print("Souhaitez-vous continuer avec ce client ? (y/n) : ");
                    String choiceToProceed = scanner.nextLine();
                    if (!choiceToProceed.equalsIgnoreCase("y")) {
                        System.out.println("Opération annulée.");
                        return;
                    }
                } else {
                    System.out.println("Client non trouvé.");
                    return;
                }
                break;

            case 2:
                System.out.println("--- Ajout d'un nouveau client ---");
                System.out.print("Nom: ");
                String nom = scanner.nextLine();
                System.out.print("Adresse: ");
                String adresse = scanner.nextLine();
                System.out.print("Téléphone: ");
                String telephone = scanner.nextLine();
                System.out.print("Est-ce un client professionnel (true/false)? ");
                boolean estProfessionnel = scanner.nextBoolean();
                System.out.print("Remise: ");
                double remise = scanner.nextDouble();
                scanner.nextLine();

                client = new Client(0, nom, adresse, telephone, estProfessionnel, remise);
                clientService.create(client);
                System.out.println("Client ajouté avec succès !");
                break;

            default:
                System.out.println("Option invalide. Opération annulée.");
                return;
        }

        System.out.println("--- Création d'un Nouveau Projet ---");
        System.out.print("Entrez le nom du projet : ");
        String projectName = scanner.nextLine();
        System.out.print("Entrez la surface de la cuisine (en m²) : ");
        surface = scanner.nextDouble();
        scanner.nextLine();

        Project project = new Project();
        project.setNomProjet(projectName);
        project.setMargeBeneficiaire(0);
        project.setCoutTotal(0);
        project.setEtatProjet(Project.EtatProjet.EN_COURS);
        project.setClientId(client.getId());

        projectService.create(project);

        System.out.println("Projet créé avec succès !");



        addMaterialsAndLabor(project);
        calculateProjectCost(project);
    }

    private void addMaterialsAndLabor(Project project) {

        System.out.println("--- Ajout des matériaux ---");
        while (true) {
            System.out.print("Entrez le nom du matériau : ");
            String materialName = scanner.nextLine();
            System.out.print("Entrez la quantité de ce matériau (en m²) : ");
            double quantity = scanner.nextDouble();
            System.out.print("Entrez le coût unitaire de ce matériau (€/m²) : ");
            double unitCost = scanner.nextDouble();
            System.out.print("Entrez le coût de transport de ce matériau (€) : ");
            double transportCost = scanner.nextDouble();
            System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");
            double qualityCoefficient = scanner.nextDouble();
            scanner.nextLine();

            Material material = new Material(0, materialName, unitCost, quantity, 20   , transportCost, qualityCoefficient , project );

            projectService.addMaterialToProject(project.getId(), material);

            System.out.println("Matériau ajouté avec succès !");
            System.out.print("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            if (!scanner.nextLine().equalsIgnoreCase("y")) {
                break;
            }
        }


        System.out.println("--- Ajout de la main-d'œuvre ---");
        while (true) {
            System.out.print("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
            String laborType = scanner.nextLine();
            System.out.print("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
            double hourlyRate = scanner.nextDouble();
            System.out.print("Entrez le nombre d'heures travaillées : ");
            double hoursWorked = scanner.nextDouble();
            System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
            double productivityFactor = scanner.nextDouble();
            scanner.nextLine();

            Workforce workforce = new Workforce(0, laborType, hourlyRate, hoursWorked, 20, hourlyRate, hoursWorked, productivityFactor , project );

            projectService.addWorkforceToProject(project.getId(), workforce);

            System.out.println("Main-d'œuvre ajoutée avec succès !");
            System.out.print("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
            if (!scanner.nextLine().equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    private void calculateProjectCost(Project project) {
        System.out.println("--- Calcul du coût total ---");
        System.out.print("Souhaitez-vous appliquer une TVA au projet ? (y/n) : ");
        boolean applyVAT = scanner.nextLine().equalsIgnoreCase("y");

        final double vatRate;
        if (applyVAT) {
            System.out.print("Entrez le pourcentage de TVA (%) : ");
            vatRate = scanner.nextDouble();
            scanner.nextLine();
        } else {
            vatRate = 0;
        }

        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        boolean applyMargin = scanner.nextLine().equalsIgnoreCase("y");

        double marginRate = 0;
        if (applyMargin) {
            System.out.print("Entrez le pourcentage de marge bénéficiaire (%) : ");
            marginRate = scanner.nextDouble();
            scanner.nextLine();
            project.setMargeBeneficiaire(marginRate);
            projectService.update(project);
        }

        List<Material> materials = materialService.getMaterialsByProjectId(project.getId());
        List<Workforce> workforces = workforceService.getWorkforcesByProjectId(project.getId());

        materials.forEach(material -> {
            material.setTauxTVA(vatRate);
            materialService.updateTva(material);
        });

        workforces.forEach(workforce -> {
            workforce.setTauxTVA(vatRate);
            workforceService.updateTva(workforce);
        });

        double totalCost = projectService.calculateTotalCost(project.getId(), project.getClientId(), vatRate, marginRate);

        System.out.println("--- Résultat du Calcul ---");

        Project projectDetails = projectService.getProjectDetails(project.getId());
        System.out.println("Nom du projet : " + projectDetails.getNomProjet());
        System.out.println("Client : " + clientService.findById(projectDetails.getClientId()).get().getNom());
        System.out.println("Adresse du chantier : " + clientService.findById(projectDetails.getClientId()).get().getAdresse());
        System.out.println("Surface : " + surface + " m²");

        System.out.println("--- Détail des Coûts ---");

        System.out.println("1. Matériaux :\n");
        List<Material> materialls = materialService.getMaterialsByProjectId(project.getId());
        double totalMaterialCostBeforeVAT = 0;
        double totalMaterialCostWithVAT = 0;

        for (Material material : materialls) {
            double materialCost = materialService.calculateMaterialCost(material.getId());
            totalMaterialCostBeforeVAT += materialCost;
            double materialCostWithVAT = materialCost * (1 + material.getTauxTVA() / 100);
            totalMaterialCostWithVAT += materialCostWithVAT;
            System.out.printf("- %s : %.2f € (quantité : %.2f m², coût unitaire : %.2f €/m², qualité : %.1f, transport : %.2f €, TVA : %.2f%%)%n",
                    material.getNom(),
                    materialCostWithVAT,
                    material.getQuantite(),
                    material.getCoutUnitaire(),
                    material.getCoefficientQualite(),
                    material.getCoutTransport(),
                    material.getTauxTVA());
        }

        System.out.printf("**Coût total des matériaux avant TVA : %.2f €**%n", totalMaterialCostBeforeVAT);
        System.out.printf("**Coût total des matériaux avec TVA : %.2f €**%n", totalMaterialCostWithVAT);

        System.out.println("2. Main-d'œuvre :\n");

        List<Workforce> workforcces = workforceService.getWorkforcesByProjectId(project.getId());
        double totalWorkforceCostBeforeVAT = 0;
        double totalWorkforceCostWithVAT = 0;

        for (Workforce workforce : workforcces) {
            double workforceCost = workforceService.calculateWorkforceCost(workforce.getId());
            totalWorkforceCostBeforeVAT += workforceCost;
            double workforceCostWithVAT = workforceCost * (1 + workforce.getTauxTVA() / 100);
            totalWorkforceCostWithVAT += workforceCostWithVAT;
            System.out.printf("- %s : %.2f € (heures travaillées : %.2f, taux horaire : %.2f €/h, productivité : %.1f, TVA : %.2f%%)%n",
                    workforce.getNom(),
                    workforceCostWithVAT,
                    workforce.getHeuresTravail(),
                    workforce.getTauxHoraire(),
                    workforce.getProductiviteOuvrier(),
                    workforce.getTauxTVA());
        }

        System.out.printf("**Coût total de la main-d'œuvre avant TVA : %.2f €**%n", totalWorkforceCostBeforeVAT);
        System.out.printf("**Coût total de la main-d'œuvre avec TVA : %.2f €**%n", totalWorkforceCostWithVAT);

        System.out.println("3. Coût total avant marge : " + totalCost + " €");
        System.out.println("4. Marge bénéficiaire : " + marginRate + "% : " + (totalCost * (marginRate / 100)) + " €");

        System.out.println("Coût total final du projet : " + (totalCost + (totalCost * (marginRate / 100))) + " €");

        System.out.println("--- Enregistrement du Devis ---");
        System.out.print("Entrez la date d'émission du devis (format : jj/mm/aaaa) : ");
        String issueDate = scanner.nextLine();
        System.out.print("Entrez la date de validité du devis (format : jj/mm/aaaa) : ");
        String validityDate = scanner.nextLine();
        System.out.println("Souhaitez-vous enregistrer le devis ? (y/n) :");
        String choice = scanner.nextLine();

        if (!choice.equalsIgnoreCase("y")) {
            System.out.println("Opération annulée.");
            project.setEtatProjet(Project.EtatProjet.ANNULE);
            projectService.update(project);
            return;
        }
        projectService.saveQuote(project.getId(), issueDate, validityDate, totalCost);

        System.out.println("Devis enregistré avec succès !");
    }


    public void getProjectCostById() {
        System.out.println("--- Calcul du coût total d'un projet ---");
        System.out.print("Entrez l'ID du projet : ");
        int projectId = scanner.nextInt();
        scanner.nextLine();

        Optional<Project> projectOpt = projectService.findById(projectId);
        if (projectOpt.isPresent()){
            Project project = projectOpt.get();
            double totalCost = project.getCoutTotal();
            System.out.println("Le coût total du projet est : " + totalCost);
        } else {
            System.out.println("Projet avec l'ID " + projectId + " non trouvé.");
        }
    }


    public void displayAllProjects() {
        System.out.println("--- Liste des projets ---");
        projectService.findAll().forEach(project -> System.out.println(project));
    }
}