package repository;

import model.Devis;

import java.util.List;
import java.util.Optional;

public interface DevisRepository {

    Optional<Devis> findById(int id);

    List<Devis> findAll();

    void save(Devis devis);

    void update(Devis devis);

    void deleteById(int id);

    List<Devis> findByProjectId(int projectId);
}
