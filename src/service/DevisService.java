package service;

import model.Devis;
import repository.DevisRepository;
import repository.impl.DevisRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DevisService {

    private DevisRepository devisRepository;

    public DevisService() {
        this.devisRepository = new DevisRepositoryImpl();
    }


    public void createDevis(double montantEstime, LocalDate dateEmission, LocalDate dateValidite, boolean accepte, int projetId) {
        Devis devis = new Devis(0, montantEstime, dateEmission, dateValidite, accepte, projetId);
        devisRepository.save(devis);
    }


    public void updateDevis(int id, double montantEstime, LocalDate dateEmission, LocalDate dateValidite, boolean accepte, int projetId) {
        Devis devis = new Devis(id, montantEstime, dateEmission, dateValidite, accepte, projetId);
        devisRepository.update(devis);
    }


    public Optional<Devis> findDevisById(int id) {
        return devisRepository.findById(id);
    }


    public List<Devis> findAllDevis() {
        return devisRepository.findAll();
    }


    public void deleteDevisById(int id) {
        devisRepository.deleteById(id);
    }


    public List<Devis> findDevisByProjectId(int projectId) {
        return devisRepository.findByProjectId(projectId);
    }


    public boolean isDevisValid(Devis devis) {
        LocalDate today = LocalDate.now();
        return devis.getDateValidite().isAfter(today) && !devis.isAccepte();
    }


    public void acceptDevis(int id) {
        Optional<Devis> optionalDevis = devisRepository.findById(id);
        if (optionalDevis.isPresent()) {
            Devis devis = optionalDevis.get();
            devis.setAccepte(true);
            devisRepository.update(devis);
        } else {
            throw new RuntimeException("Devis not found with id: " + id);
        }
    }

    public void generateDevisForProject(int projectId, double estimatedCost, LocalDate dateValidite) {

        LocalDate dateEmission = LocalDate.now();
        boolean accepte = false;
        createDevis(estimatedCost, dateEmission, dateValidite, accepte, projectId);
    }
}
