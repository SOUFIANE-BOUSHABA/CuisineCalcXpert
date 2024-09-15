package service;

import model.Client;
import repository.ClientRepository;
import repository.impl.ClientRepositoryImpl;

import java.util.List;
import java.util.Optional;


public class ClientService {

   private ClientRepository clientRepository;

   public ClientService (){

       clientRepository = new ClientRepositoryImpl();
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


}
