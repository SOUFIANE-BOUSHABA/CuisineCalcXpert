import java.util.Scanner;
import service.ClientService;
import service.MaterialService;
import service.ProjectService;
import controller.ClientController;
import controller.ProjectController;
import service.WorkforceService;
import utils.DatabaseConnection;

public class Main {
    public static final String BOLD = "\u001B[1m";
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String RED = "\u001B[31m";
    private static final String ANSI_RESET = "\033[0m";

    public static void main(String[] args) {

        DatabaseConnection dbConnection1 = DatabaseConnection.getInstance();
        DatabaseConnection dbConnection2 = DatabaseConnection.getInstance();

        if (dbConnection1 == dbConnection2) {
            System.out.println("Singleton ");
        } else {
            System.out.println("not Singleton ");
        }




        Scanner scanner = new Scanner(System.in);

        ClientService clientService = new ClientService();
        ProjectService projectService = new ProjectService();
        MaterialService materialService = new MaterialService();
        WorkforceService workforceService = new WorkforceService();

        ClientController clientController = new ClientController(clientService);
        ProjectController projectController = new ProjectController(clientService, projectService, workforceService, materialService);

        while (true) {
            System.out.println(CYAN + BOLD + "╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println(CYAN + BOLD + "║" + "                      " + BLUE + "=== Bienvenue dans l'application de gestion des projets de rénovation de cuisines ===" + "                                 " + CYAN + BOLD +               "║");
            System.out.println(CYAN + BOLD + "╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.println(CYAN + BOLD + "║" + "                                                        " + GREEN + "=== Menu Principal ===" + "                                                              " + CYAN + BOLD +                "║");
            System.out.println(CYAN + BOLD + "╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.println(CYAN + BOLD + "║" + " " + YELLOW + BOLD + "1. Créer un nouveau projet" + "                                                                                                                 " + CYAN + BOLD +                 "║");
            System.out.println(CYAN + BOLD + "║" + " " + YELLOW + BOLD + "2. Afficher les projets existants" + "                                                                                                          " + CYAN + BOLD +               "║");
            System.out.println(CYAN + BOLD + "║" + " " + YELLOW + BOLD + "3. Calculer le coût d'un projet" + "                                                                                                            " + CYAN + BOLD +                  "║");
            System.out.println(CYAN + BOLD + "║" + " " + YELLOW + BOLD + "4. Quitter" + "                                                                                                                                 " + CYAN + BOLD +                "║");
            System.out.println(CYAN + BOLD + "╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
            System.out.print(BOLD + "Choisissez une option : ");


            int choice =0 ;
            do {
                if(!scanner.hasNextInt()){
                    System.out.println(RED + "Option invalide. Veuillez réessayer." +    ANSI_RESET);
                    scanner.nextLine();
                    continue;
                }
                else{
                    choice= scanner.nextInt();
                }
            }while (choice==0);


            switch (choice) {
                case 1:
                    projectController.createProject();
                    break;

                case 2:
                    projectController.displayAllProjects();
                    break;

                case 3:
                    projectController.getProjectCostById();
                    break;

                case 4:
                    System.out.println(GREEN + "Merci d'avoir utilisé l'application. À bientôt !");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println(RED + "Option invalide. Veuillez réessayer.");
                    break;
            }
        }
    }
}
