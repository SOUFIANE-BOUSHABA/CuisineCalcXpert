package repository;
import  model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

void create(Project project);
Optional<Project> findById(int id);
List<Project> findAll();
void update(Project project);
void delete(int id);


}
