package service;

import model.Client;
import model.Project;
import repository.ClientRepository;
import repository.impl.ClientRepositoryImpl;

import java.util.List;
import java.util.Optional;


public class ClientService {

    private ClientRepository clientRepository;
    private ProjectService projectService;


    public ClientService (){

        clientRepository = new ClientRepositoryImpl();
        projectService = new ProjectService();
    }

    public void create(Client client){
        clientRepository.create(client);
    }

    public Optional<Client> findById(int id){
        return clientRepository.findById(id);
    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    public void update(Client client){
        clientRepository.update(client);
    }

    public void delete(int id){
        clientRepository.delete(id);
    }

    public Optional<Client> findByNom(String nom){
        return clientRepository.findByNom(nom);
    }

    public double applyClientDiscount(int clientId, int projectId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return -1;
        }

        Optional<Project> optionalProject = projectService.findById(projectId);
        if (!optionalProject.isPresent()) {
            return -1;
        }

        Project project = optionalProject.get();
        double projectCost = projectService.calculateTotalProjectCost(projectId);

        if (client.isEstProfessionnel()) {
            double discount = client.getRemise();
            return projectCost * (1 - discount / 100);
        } else {
            return projectCost;
        }
    }


}
