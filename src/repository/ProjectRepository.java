package repository;
import  model.Project;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectRepository {

void create(Project project);
Optional<Project> findById(int id);
Optional<List<Map<String, Object>>> findAllWithClients();
void update(Project project);
void delete(int id);


}
