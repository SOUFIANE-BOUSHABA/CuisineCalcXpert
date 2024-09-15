package controller;

import model.Client;
import service.ClientService;

import java.util.Optional;
import java.util.Scanner;

public class ClientController {
    private final ClientService clientService;
    private final Scanner scanner;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
        this.scanner = new Scanner(System.in);
    }


    public void addClient() {
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
        scanner.nextLine(); // Consommer la ligne restante

        Client client = new Client(0, nom, adresse, telephone, estProfessionnel, remise);
        clientService.create(client);
        System.out.println("Client ajouté avec succès!");
    }


    public void findClient() {
        System.out.print("Entrez l'ID du client: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Client> client = clientService.findById(id);
        if (client.isPresent()) {
            System.out.println("Client trouvé: " + client.get());
        } else {
            System.out.println("Aucun client trouvé avec cet ID.");
        }
    }


    public void updateClient() {
        System.out.print("Entrez l'ID du client à mettre à jour: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Client> clientOpt = clientService.findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            System.out.println("Client actuel: " + client);

            System.out.print("Nom [" + client.getNom() + "]: ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) client.setNom(nom);

            System.out.print("Adresse [" + client.getAdresse() + "]: ");
            String adresse = scanner.nextLine();
            if (!adresse.isEmpty()) client.setAdresse(adresse);

            System.out.print("Téléphone [" + client.getTelephone() + "]: ");
            String telephone = scanner.nextLine();
            if (!telephone.isEmpty()) client.setTelephone(telephone);

            System.out.print("Est-ce un client professionnel (true/false) [" + client.isEstProfessionnel() + "]? ");
            String estProfessionnelInput = scanner.nextLine();
            if (!estProfessionnelInput.isEmpty()) client.setEstProfessionnel(Boolean.parseBoolean(estProfessionnelInput));

            System.out.print("Remise [" + client.getRemise() + "]: ");
            String remiseInput = scanner.nextLine();
            if (!remiseInput.isEmpty()) client.setRemise(Double.parseDouble(remiseInput));

            clientService.update(client);
            System.out.println("Client mis à jour avec succès!");
        } else {
            System.out.println("Client non trouvé.");
        }
    }


    public void deleteClient() {
        System.out.print("Entrez l'ID du client à supprimer: ");
        int id = scanner.nextInt();
        scanner.nextLine();



        clientService.delete(id);
        System.out.println("Client supprimé   avec   succès.");
    }




    public void displayAllClients() {
        System.out.println("--- Liste des clients ---");
        clientService.findAll().forEach(System.out::println);
    }
}
