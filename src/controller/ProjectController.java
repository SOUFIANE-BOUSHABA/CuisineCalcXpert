package controller;

import model.Client;
import model.Project;
import service.ClientService;
import service.ProjectService;

import java.util.Optional;
import java.util.Scanner;

public class ProjectController {
    private final ClientService clientService;
    private final ProjectService projectService;
    private final Scanner scanner;

    public ProjectController(ClientService clientService, ProjectService projectService) {
        this.clientService = clientService;
        this.projectService = projectService;
        this.scanner = new Scanner(System.in);
    }

    public void createProject() {
        System.out.println("--- Création d'un Nouveau Projet ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

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
    }

    public void displayAllProjects() {
        System.out.println("--- Liste des projets ---");
        projectService.findAll().forEach(project -> System.out.println(project));
    }
}