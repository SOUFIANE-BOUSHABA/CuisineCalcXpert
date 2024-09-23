package controller;

import model.Client;
import model.Material;
import model.Project;
import model.Workforce;
import service.ClientService;
import service.MaterialService;
import service.ProjectService;
import service.WorkforceService;

import java.util.*;

public class ProjectController {
    private final ClientService clientService;
    private final ProjectService projectService;
    private final WorkforceService workforceService;
    private final MaterialService materialService;
    private final Scanner scanner;
    private double surface;


    private static final String ANSI_BOLD_BLUE = "\033[1;34m";
    private static final String ANSI_BOLD_GREEN = "\033[1;32m";
    private static final String ANSI_BOLD_YELLOW = "\033[1;33m";
    private static final String ANSI_BOLD_RED = "\033[1;31m";
    private static final String ANSI_BOLD_WHITE = "\033[1;37m";
    private static final String ANSI_RESET = "\033[0m";
    public static final String CYAN = "\u001B[36m";

    public ProjectController(ClientService clientService, ProjectService projectService, WorkforceService workforceService, MaterialService materialService) {
        this.clientService = clientService;
        this.projectService = projectService;
        this.workforceService = workforceService;
        this.materialService = materialService;
        this.scanner = new Scanner(System.in);
    }

    public void createProject() {
        System.out.println(ANSI_BOLD_BLUE  + "╔═══════════════════════════════════════════════════════════════════════╗");
        System.out.println(ANSI_BOLD_BLUE +  "║              --- Création d'un Nouveau Projet ---                     ║");
        System.out.println(ANSI_BOLD_BLUE  + "╠═══════════════════════════════════════════════════════════════════════╣");
        System.out.println(ANSI_BOLD_WHITE + "║ " + "       chercher un client existant ou en ajouter un nouveau ?         " + ANSI_BOLD_WHITE + "║");
        System.out.println(ANSI_BOLD_WHITE + "║       " + ANSI_BOLD_GREEN +       " 1. Chercher un client existant                                 " + ANSI_BOLD_WHITE +            "║");
        System.out.println(ANSI_BOLD_WHITE + "║       " + ANSI_BOLD_GREEN +       " 2. Ajouter un nouveau client                                   " + ANSI_BOLD_WHITE +            "║");
        System.out.println(ANSI_BOLD_WHITE + "╚═══════════════════════════════════════════════════════════════════════╝");

        System.out.print(ANSI_BOLD_YELLOW + "Choisissez une option : ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        Client client = null;

        switch (choice) {
            case 1:
                System.out.println(ANSI_BOLD_BLUE + "--- Recherche de client existant ---");
                System.out.print("Entrez le nom du client : ");
                String clientName = scanner.nextLine();

                Optional<Client> clientOpt = clientService.findByNom(clientName);
                if (clientOpt.isPresent()) {
                    client = clientOpt.get();
                    System.out.println(ANSI_BOLD_GREEN + "Client trouvé !");

                    System.out.println(ANSI_BOLD_YELLOW + "Nom : " + ANSI_BOLD_WHITE + client.getNom());
                    System.out.println(ANSI_BOLD_YELLOW + "Adresse : " + ANSI_BOLD_WHITE + client.getAdresse());
                    System.out.println(ANSI_BOLD_YELLOW + "Numéro de téléphone : " + ANSI_BOLD_WHITE + client.getTelephone());

                    System.out.print("Souhaitez-vous continuer avec ce client ? " + ANSI_BOLD_YELLOW + "(y/n) : ");
                    String choiceToProceed = scanner.nextLine();
                    if (!choiceToProceed.equalsIgnoreCase("y")) {
                        System.out.println(ANSI_BOLD_RED + "Opération annulée.");
                        return;
                    }
                } else {
                    System.out.println(ANSI_BOLD_RED + "Client non trouvé.");
                    return;
                }
                break;

            case 2:
                System.out.println(ANSI_BOLD_BLUE + "--- Ajout d'un nouveau client ---");
                System.out.print("Nom: ");
                String nom = scanner.nextLine();
                System.out.print("Adresse: ");
                String adresse = scanner.nextLine();
                System.out.print("Téléphone: ");
                String telephone = scanner.nextLine();
                System.out.print("Est-ce un client professionnel " + ANSI_BOLD_YELLOW + "(true/false)? ");
                boolean estProfessionnel = scanner.nextBoolean();
                System.out.print("Remise: ");
                double remise = scanner.nextDouble();
                scanner.nextLine();

                client = new Client(0, nom, adresse, telephone, estProfessionnel, remise);
                clientService.create(client);
                System.out.println(ANSI_BOLD_GREEN + "Client ajouté avec succès !");
                break;

            default:
                System.out.println(ANSI_BOLD_RED + "Option invalide. Opération annulée.");
                return;
        }

        System.out.println(ANSI_BOLD_BLUE + "--- Création d'un Nouveau Projet ---");
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

        System.out.println(ANSI_BOLD_GREEN + "Projet créé avec succès !" + ANSI_RESET);

        addMaterialsAndLabor(project);
        calculateProjectCost(project);
    }

    private void addMaterialsAndLabor(Project project) {
        System.out.println(ANSI_BOLD_BLUE + "--- Ajout des matériaux ---" + ANSI_RESET);

        while (true) {
            System.out.print(CYAN + "Entrez le nom du matériau : " + ANSI_RESET);
            String materialName = scanner.nextLine();

            System.out.print(CYAN + "Entrez la quantité de ce matériau (en m²) : " + ANSI_RESET);
            double quantity = scanner.nextDouble();

            System.out.print(CYAN + "Entrez le coût unitaire de ce matériau (€/m²) : " + ANSI_RESET);
            double unitCost = scanner.nextDouble();

            System.out.print(CYAN + "Entrez le coût de transport de ce matériau (€) : " + ANSI_RESET);
            double transportCost = scanner.nextDouble();

            System.out.print(CYAN + "Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : " + ANSI_RESET);
            double qualityCoefficient = scanner.nextDouble();
            scanner.nextLine();

            Material material = new Material(0, materialName, unitCost, quantity, 20, transportCost, qualityCoefficient, project);
            projectService.addMaterialToProject(project.getId(), material);

            System.out.println(ANSI_BOLD_GREEN + "Matériau ajouté avec succès !" + ANSI_RESET);
            System.out.print(ANSI_BOLD_YELLOW + "Voulez-vous ajouter un autre matériau ? (y/n) : " + ANSI_RESET);
            if (!scanner.nextLine().equalsIgnoreCase("y")) {
                break;
            }
        }

        System.out.println(ANSI_BOLD_BLUE + "--- Ajout de la main-d'œuvre ---" + ANSI_RESET);
        while (true) {
            System.out.print(CYAN + "Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : " + ANSI_RESET);
            String laborType = scanner.nextLine();

            System.out.print(CYAN + "Entrez le taux horaire de cette main-d'œuvre (€/h) : " + ANSI_RESET);
            double hourlyRate = scanner.nextDouble();

            System.out.print(CYAN + "Entrez le nombre d'heures travaillées : " + ANSI_RESET);
            double hoursWorked = scanner.nextDouble();

            System.out.print(CYAN + "Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : " + ANSI_RESET);
            double productivityFactor = scanner.nextDouble();
            scanner.nextLine();

            Workforce workforce = new Workforce(0, laborType, 20, hourlyRate, hoursWorked, productivityFactor, project);
            projectService.addWorkforceToProject(project.getId(), workforce);

            System.out.println(ANSI_BOLD_GREEN + "Main-d'œuvre ajoutée avec succès !" + ANSI_RESET);
            System.out.print(ANSI_BOLD_YELLOW + "Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : " + ANSI_RESET);
            if (!scanner.nextLine().equalsIgnoreCase("y")) {
                break;
            }
        }
    }


    private void calculateProjectCost(Project project) {
        System.out.println(ANSI_BOLD_BLUE + "--- Calcul du coût total ---" + ANSI_RESET);
        System.out.print(ANSI_BOLD_YELLOW + "Souhaitez-vous appliquer une TVA au projet ? (y/n) : " + ANSI_RESET);
        boolean applyVAT = scanner.nextLine().equalsIgnoreCase("y");

        final double Tva;
        if (applyVAT) {
            System.out.print(ANSI_BOLD_YELLOW + "Entrez le pourcentage de TVA (%) : " + ANSI_RESET);
            Tva = scanner.nextDouble();
            scanner.nextLine();
        } else {
            Tva = 0;
        }

        System.out.print(ANSI_BOLD_YELLOW + "Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : " + ANSI_RESET);
        boolean applyMargin = scanner.nextLine().equalsIgnoreCase("y");

        double margeBinific = 0;
        if (applyMargin) {
            System.out.print(ANSI_BOLD_YELLOW + "Entrez le pourcentage de marge bénéficiaire (%) : " + ANSI_RESET);
            margeBinific = scanner.nextDouble();
            scanner.nextLine();
            project.setMargeBeneficiaire(margeBinific);
            projectService.update(project);
        }

        List<Material> materials = materialService.getMaterialsByProjectId(project.getId());
        List<Workforce> workforces = workforceService.getWorkforcesByProjectId(project.getId());

        materials.forEach(material -> {
            material.setTauxTVA(Tva);
            materialService.updateTva(material);
        });

        workforces.forEach(workforce -> {
            workforce.setTauxTVA(Tva);
            workforceService.updateTva(workforce);
        });

        double totalCost = projectService.calculateTotalCost(project.getId(), project.getClientId(), Tva, margeBinific);

        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);
        System.out.println(ANSI_BOLD_GREEN + "|--- Résultat du Calcul \uD83D\uDCC4 ---" + ANSI_RESET );
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);

        Project projectDetails = projectService.getProjectDetails(project.getId());
        System.out.printf("|   Nom du projet :         |  %s%s%s%n", CYAN, projectDetails.getNomProjet(), ANSI_RESET);
        System.out.printf("|   Client :                |  %s%s%s%n", CYAN, clientService.findById(projectDetails.getClientId()).get().getNom(), ANSI_RESET);
        System.out.printf("|   Adresse du chantier :   |  %s%s%s%n", CYAN, clientService.findById(projectDetails.getClientId()).get().getAdresse(), ANSI_RESET);
        System.out.printf("|   Surface :               |  %s%.2f m²%s%n", CYAN, surface, ANSI_RESET);
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);

        System.out.println(ANSI_BOLD_YELLOW + "|--- Détail des Coûts ---" + ANSI_RESET);
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);
        System.out.println(ANSI_BOLD_YELLOW + "|  1. Matériaux :" + ANSI_RESET);
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);

        List<Material> materialls = materialService.getMaterialsByProjectId(project.getId());
        double totalMaterialCostBeforeVAT = 0;
        double totalMaterialCostWithVAT = 0;

        System.out.printf(CYAN + "| %-25s | %-15s | %-13s | %-13s | %-13s | %-10s | %-15s |%n", "Matériaux", "Coût unitaire", "Quantité", "Coût total", "Transport", "TVA", "Coût final" + ANSI_RESET);
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);

        for (Material material : materialls) {
            double materialCost = materialService.calculateMaterialCost(material.getId());
            totalMaterialCostBeforeVAT += materialCost;
            double materialCostWithVAT = materialCost * (1 + material.getTauxTVA() / 100);
            totalMaterialCostWithVAT += materialCostWithVAT;

            System.out.printf("| %-25s | %-15.2f |  %-12.2f |  %-12.2f |  %-12.2f |  %-10.2f |  %-9.2f € |%n",
                    material.getNom(),
                    material.getCoutUnitaire(),
                    material.getQuantite(),
                    materialCost,
                    material.getCoutTransport(),
                    material.getTauxTVA(),
                    materialCostWithVAT);
        }
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);
        System.out.printf(ANSI_BOLD_YELLOW + "| **Coût total des matériaux avant TVA : %.2f €**%n" + ANSI_RESET, totalMaterialCostBeforeVAT);
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);
        System.out.printf(ANSI_BOLD_YELLOW + "| **Coût total des matériaux avec TVA : %.2f €**%n" + ANSI_RESET, totalMaterialCostWithVAT);
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);

        System.out.println(ANSI_BOLD_YELLOW + "|  2. Main-d'œuvre :" + ANSI_RESET);
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);

        List<Workforce> workforcces = workforceService.getWorkforcesByProjectId(project.getId());
        double totalWorkforceCostBeforeVAT = 0;
        double totalWorkforceCostWithVAT = 0;

        System.out.printf(CYAN + "| %-25s | %-15s | %-10s | %-15s | %-10s | %-12s |%n", "Main-d'œuvre", "Taux horaire", "Heures", "Coût total", "Productivité", "TVA"  + ANSI_RESET);
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);

        for (Workforce workforce : workforcces) {
            double workforceCost = workforceService.calculateWorkforceCost(workforce.getId());
            totalWorkforceCostBeforeVAT += workforceCost;
            double workforceCostWithVAT = workforceCost * (1 + workforce.getTauxTVA() / 100);
            totalWorkforceCostWithVAT += workforceCostWithVAT;

            System.out.printf("| %-25s | %-12.2f €/h | %-10.2f | %-12.2f € | %-12.1f | %-10.2f%% |%n",
                    workforce.getNom(),
                    workforce.getTauxHoraire(),
                    workforce.getHeuresTravail(),
                    workforceCostWithVAT,
                    workforce.getProductiviteOuvrier(),
                    workforce.getTauxTVA());
        }
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);
        System.out.printf(ANSI_BOLD_YELLOW + "| **Coût total de la main-d'œuvre avant TVA : %.2f €**%n" + ANSI_RESET, totalWorkforceCostBeforeVAT);
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);
        System.out.printf(ANSI_BOLD_YELLOW + "| **Coût total de la main-d'œuvre avec TVA : %.2f €**%n" + ANSI_RESET, totalWorkforceCostWithVAT);
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);

        System.out.printf(ANSI_BOLD_YELLOW + "|  3. Coût total avant marge : %.2f €%n" + ANSI_RESET, totalCost);
        System.out.printf(ANSI_BOLD_YELLOW + "|  4. Marge bénéficiaire : %.2f %% : %.2f €%n" + ANSI_RESET, margeBinific, (totalCost * (margeBinific / 100)));
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);

        System.out.printf(CYAN + "| Coût total final du projet : %.2f €" + ANSI_RESET + " |%n", (totalCost + (totalCost * (margeBinific / 100))));
        System.out.println(CYAN + "+-------------------------------------------------------------------------------------------------------------------------+" + ANSI_RESET);


        System.out.println(ANSI_BOLD_BLUE + "--- Enregistrement du Devis ---" + ANSI_RESET);
        System.out.print(ANSI_BOLD_YELLOW + "Entrez la date d'émission du devis (format : jj/mm/aaaa) : " + ANSI_RESET);
        String issueDate = scanner.nextLine();
        System.out.print(ANSI_BOLD_YELLOW + "Entrez la date de validité du devis (format : jj/mm/aaaa) : " + ANSI_RESET);
        String validityDate = scanner.nextLine();
        System.out.print(ANSI_BOLD_YELLOW + "Souhaitez-vous enregistrer le devis ? (y/n) :" + ANSI_RESET);
        String choice = scanner.nextLine();

        if (!choice.equalsIgnoreCase("y")) {
            System.out.println(ANSI_BOLD_RED + "Opération annulée." + ANSI_RESET);
            project.setEtatProjet(Project.EtatProjet.ANNULE);
            projectService.update(project);
            return;
        }
        projectService.createDevis(project.getId(), issueDate, validityDate, totalCost);

        System.out.println(ANSI_BOLD_GREEN + "Devis enregistré avec succès !" + ANSI_RESET);
    }



    public void getProjectCostById() {
        System.out.println("--- Calcul du coût total d'un projet ---");
        System.out.print("Entrez l'ID du projet : ");
        int projectId = scanner.nextInt();
        scanner.nextLine();

        Optional<Project> projectOpt = projectService.findById(projectId);
        if (projectOpt.isPresent()){
            Project project = projectOpt.get();
            calculateProjectCost(project);
        } else {
            System.out.println("Projet avec l'ID " + projectId + " non trouvé.");
        }
    }


    public void displayAllProjects() {
        System.out.println("--- Liste des projets ---");
        Optional<List<Map<String, Object>>> optionalProjects = projectService.getAllProjectsWithClients();

        if (optionalProjects.isPresent()) {
            List<Map<String, Object>> projects = optionalProjects.get();
            Map<String, List<String>> clientProjectsMap = new HashMap<>();

            for (Map<String, Object> project : projects) {
                String clientName = (String) project.get("client_name");
                String projectName = (String) project.get("project_name");
                clientProjectsMap.computeIfAbsent(clientName, k -> new ArrayList<>()).add(projectName);
            }

            clientProjectsMap.forEach((client, projectList) -> {
                System.out.println("Le client \"" + client + "\" a les projets :");
                for (int i = 0; i < projectList.size(); i++) {
                    System.out.println((i + 1) + " : " + projectList.get(i));
                }
            });
        } else {
            System.out.println("Aucun projet trouvé.");
        }
    }
}