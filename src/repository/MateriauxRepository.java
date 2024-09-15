package repository;

import model.Material;
import java.util.List;
import java.util.Optional;

public interface MateriauxRepository {

   Optional<Material> findById(int id);

    List<Material> findAll();

    void save(Material materiaux);

    void update(Material materiaux);

    void deleteById(int id);


    List<Material> findByProjectId(int projectId);
}
