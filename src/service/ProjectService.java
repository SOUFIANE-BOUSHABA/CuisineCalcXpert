package service;

import model.Material;
import model.Project;
import model.Workforce;
import repository.MateriauxRepository;
import repository.ProjectRepository;
import repository.WorkforceRepository;
import repository.impl.MateriauxRepositoryImpl;
import repository.impl.ProjectRepositoryImpl;
import repository.impl.WorkforceRepositoryImpl;

import java.util.List;

public class ProjectService {

    private ProjectRepository projectRepository;
    private MateriauxRepository materiauxRepository;
    private WorkforceRepository workforceRepository;

    public ProjectService() {
        this.projectRepository = new ProjectRepositoryImpl();
        this.materiauxRepository = new MateriauxRepositoryImpl();
        this.workforceRepository = new WorkforceRepositoryImpl();
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



    public double calculateTotalProjectCost(int projectId) {
        List<Material> materials = materiauxRepository.findByProjectId(projectId);
        List<Workforce> workforces = workforceRepository.findByProjectId(projectId);

        double totalMaterialCost = materials.stream()
                .mapToDouble(material -> material.getCoutUnitaire() * material.getQuantite())
                .sum();

        double totalWorkforceCost = workforces.stream()
                .mapToDouble(workforce -> workforce.getCoutUnitaire() * workforce.getQuantite() * workforce.getHeuresTravail())
                .sum();

        return totalMaterialCost + totalWorkforceCost;

    }

}
