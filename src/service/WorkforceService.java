package service;

import model.Workforce;
import repository.WorkforceRepository;
import repository.impl.WorkforceRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class WorkforceService {

    private WorkforceRepository workforceRepository;

    public WorkforceService() {
        this.workforceRepository = new WorkforceRepositoryImpl();
    }

    public Optional<Workforce> findWorkforceById(int id) {
        return workforceRepository.findById(id);
    }

    public List<Workforce> getAllWorkforces() {
        return workforceRepository.findAll();
    }

    public void createWorkforce(Workforce workforce) {
        workforceRepository.save(workforce);
    }

    public void updateWorkforce(Workforce workforce) {
        Optional<Workforce> existingWorkforce = workforceRepository.findById(workforce.getId());
        if (existingWorkforce.isPresent()) {
            workforceRepository.update(workforce);
        } else {
            System.out.println("Workforce not found with ID: " + workforce.getId());
        }
    }

    public void deleteWorkforce(int id) {
        Optional<Workforce> existingWorkforce = workforceRepository.findById(id);
        if (existingWorkforce.isPresent()) {
            workforceRepository.deleteById(id);
        } else {
            System.out.println("Workforce not found with ID: " + id);
        }
    }

    public List<Workforce> getWorkforcesByProjectId(int projectId) {
        return workforceRepository.findByProjectId(projectId);
    }

    public void updateTva(Workforce workforce) {
        workforceRepository.updateTva(workforce);
    }


    public double calculateWorkforceCost(int workforceId) {
        Optional<Workforce> workforce = workforceRepository.findById(workforceId);
        if (workforce.isPresent()) {
            Workforce wf = workforce.get();
            return wf.getTauxHoraire() * wf.getHeuresTravail() * wf.getProductiviteOuvrier();
        } else {
            System.out.println("Workforce not found with ID: " + workforceId);
            return 0;
        }
    }


}
