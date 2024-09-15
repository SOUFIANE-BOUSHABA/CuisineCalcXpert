package service;

import model.Project;
import repository.ProjectRepository;
import repository.impl.ProjectRepositoryImpl;

import java.util.List;

public class ProjectService {

    private ProjectRepository projectRepository;

    public ProjectService(){
        projectRepository = new ProjectRepositoryImpl();
    }

    public void create(Project project){
        projectRepository.create(project);
    }

    public void update(Project project){
        projectRepository.update(project);
    }

    public void delete(int id){
        projectRepository.delete(id);
    }

    public void findById(int id){
        projectRepository.findById(id);
    }

    public List<Project> findAll(){
        return  projectRepository.findAll();
    }



}
