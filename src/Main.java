import java.util.Scanner;
import service.ClientService;
import service.MaterialService;
import service.ProjectService;
import controller.ClientController;
import controller.ProjectController;
import service.WorkforceService;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        ClientService clientService = new ClientService();
        ProjectService projectService = new ProjectService();
        MaterialService materialService = new MaterialService();
        WorkforceService workforceService = new WorkforceService();


        ClientController clientController = new ClientController(clientService);
        ProjectController projectController = new ProjectController(clientService, projectService , workforceService, materialService);

        while (true) {
            System.out.println("=== Bienvenue dans l'application de gestion des projets de rénovation de cuisines ===");
            System.out.println("=== Menu Principal ===");
            System.out.println("1. Créer un nouveau projet");
            System.out.println("2. Afficher les projets existants");
            System.out.println("3. Quitter");
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
                    System.out.println("Merci d'avoir utilisé l'application. À bientôt !");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
                    break;
            }
        }
    }
}
