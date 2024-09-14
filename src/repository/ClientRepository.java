package repository;

import model.Client;
import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    void create(Client client);
    Optional<Client> findById(int id);
    List<Client> findAll();
    void update(Client client);
    void delete(int id);
}
