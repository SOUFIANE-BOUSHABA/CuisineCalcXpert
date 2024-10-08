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


    public void createDevis(Devis devis) {
        devisRepository.save(devis);
    }


    public void updateDevis(Devis devis) {
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


}
