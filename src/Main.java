import controller.ProjectController;
import service.ClientService;
import service.ProjectService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ClientService clientService = new ClientService();
        ProjectService projectService = new ProjectService();
        ProjectController projectController = new ProjectController(clientService, projectService);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Créer un nouveau projet");
            System.out.println("2. Afficher les projets existants");
            System.out.println("3. Calculer le coût d'un projet");
            System.out.println("4. Quitter");
            System.out.print("Choisissez une option : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    projectController.createProject();
                    break;

                case 2:
                    projectController.displayAllProjects();
                    break;

                case 3:
                    // Implement the cost calculation logic here
                    break;

                case 4:
                    System.out.println("Merci d'avoir utilisé le gestionnaire de projets.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }
}
