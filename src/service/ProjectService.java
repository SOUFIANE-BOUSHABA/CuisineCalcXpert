package service;

import model.Devis;
import model.Material;
import model.Project;
import model.Workforce;
import repository.MateriauxRepository;
import repository.ProjectRepository;
import repository.WorkforceRepository;
import repository.impl.MateriauxRepositoryImpl;
import repository.impl.ProjectRepositoryImpl;
import repository.impl.WorkforceRepositoryImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

    public Optional<Project> findById(int id){
        return projectRepository.findById(id);
    }

    public List<Project> findAll(){
        return  projectRepository.findAll();
    }



    public double calculateTotalProjectCost(int projectId) {
        List<Material> materials = materiauxRepository.findByProjectId(projectId);
        List<Workforce> workforces = workforceRepository.findByProjectId(projectId);

        double totalMaterialCost = materials.stream()
                .mapToDouble(material -> material.getCoutUnitaire() * material.getQuantite() * material.getCoefficientQualite() + material.getCoutTransport() )
                .sum();

        double totalWorkforceCost = workforces.stream()
                .mapToDouble(workforce -> workforce.getTauxHoraire() * workforce.getHeuresTravail() * workforce.getProductiviteOuvrier())
                .sum();

        return totalMaterialCost + totalWorkforceCost;

    }


    public void addMaterialToProject(int projectId, Material material) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isPresent()) {
            MaterialService materialService = new MaterialService();
            materialService.createMaterial(material);
        } else {
            throw new IllegalArgumentException("Project with ID " + projectId + " not found.");
        }
    }


    public void addWorkforceToProject(int projectId, Workforce workforce) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isPresent()) {
            WorkforceService workforceService = new WorkforceService();
            workforceService.createWorkforce(workforce);
        } else {
            throw new IllegalArgumentException("Project with ID " + projectId + " not found.");
        }
    }


    public double calculateTotalCost(int projectId, int clientId, double vatRate, double marginRate) {
        ClientService clientService = new ClientService();
        double discountedCost = clientService.applyClientDiscount(clientId, projectId);

        if (discountedCost == -1) {
            throw new IllegalArgumentException("Invalid client or project ID.");
        }

        double totalCost = discountedCost;

        if (vatRate > 0) {
            totalCost += totalCost * (vatRate / 100);
        }

        if (marginRate > 0) {
            totalCost += totalCost * (marginRate / 100);
        }

        return totalCost;
    }


    public Project getProjectDetails(int projectId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isPresent()) {
            return projectOpt.get();
        } else {
            throw new IllegalArgumentException("Project with ID " + projectId + " not found.");
        }
    }

    public void createDevis(int projectId, String issueDate, String validityDate, double totalCost) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();


            LocalDate issueLocalDate = LocalDate.parse(issueDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate validityLocalDate = LocalDate.parse(validityDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            DevisService devisService = new DevisService();
            List<Devis> existingDevis = devisService.findDevisByProjectId(projectId);
            if (!existingDevis.isEmpty()) {
                Devis devis = existingDevis.get(0);
                devis.setMontantEstime(totalCost);
                devis.setDateEmission(issueLocalDate);
                devis.setDateValidite(validityLocalDate);
                devis.setAccepte(false);
                devisService.updateDevis(devis);
            } else {
                Devis devis = new Devis(
                        0,
                        totalCost,
                        issueLocalDate,
                        validityLocalDate,
                        false,
                        projectId
                );
                devisService.createDevis(devis);
            }

            project.setCoutTotal(totalCost);
            projectRepository.update(project);
        } else {
            throw new IllegalArgumentException("Project with ID " + projectId + " not found.");
        }
    }





}
