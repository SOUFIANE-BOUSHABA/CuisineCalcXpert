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
        double surface = scanner.nextDouble();
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

        final double vatRate ;
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


        double totalCost = projectService.calculateTotalCost(project.getId(), vatRate, marginRate);

        System.out.println("--- Résultat du Calcul ---");

        Project projectDetails = projectService.getProjectDetails(project.getId());
        System.out.println("Nom du projet : " + projectDetails.getNomProjet());
        System.out.println("Client : " + clientService.findById(projectDetails.getClientId()).get().getNom());
        System.out.println("Adresse du chantier : " + clientService.findById(projectDetails.getClientId()).get().getAdresse());
        System.out.println("Surface : " + projectDetails.getCoutTotal() + " m²");

        System.out.println("Coût total final du projet : " + totalCost + " €");

        System.out.println("--- Enregistrement du Devis ---");
        System.out.print("Entrez la date d'émission du devis (format : jj/mm/aaaa) : ");
        String issueDate = scanner.nextLine();
        System.out.print("Entrez la date de validité du devis (format : jj/mm/aaaa) : ");
        String validityDate = scanner.nextLine();

        projectService.saveQuote(project.getId(), issueDate, validityDate, totalCost);

        System.out.println("Devis enregistré avec succès !");
    }


    public void displayAllProjects() {
        System.out.println("--- Liste des projets ---");
        projectService.findAll().forEach(project -> System.out.println(project));
    }
}