package repository;

import model.Workforce;
import java.util.List;
import java.util.Optional;

public interface WorkforceRepository {

    Optional<Workforce> findById(int id);

    List<Workforce> findAll();

    void save(Workforce workforce);

    void update(Workforce workforce);

    void deleteById(int id);

    List<Workforce> findByProjectId(int projectId);

    void updateTva(Workforce workforce);
}
