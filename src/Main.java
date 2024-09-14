import controller.ClientController;
import service.ClientService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ClientService clientService = new ClientService();
        ClientController clientController = new ClientController(clientService);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Menu de gestion des clients ---");
            System.out.println("1. Ajouter un nouveau client");
            System.out.println("2. Rechercher un client par ID");
            System.out.println("3. Mettre à jour un client");
            System.out.println("4. Supprimer un client");
            System.out.println("5. Afficher tous les clients");
            System.out.println("6. Quitter");
            System.out.print("Choisissez une option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    clientController.addClient();
                    break;
                case 2:
                    clientController.findClient();
                    break;
                case 3:
                    clientController.updateClient();
                    break;
                case 4:
                    clientController.deleteClient();
                    break;
                case 5:
                    clientController.displayAllClients();
                    break;
                case 6:
                    System.out.println("Merci d'avoir utilisé le gestionnaire de clients.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }
}
