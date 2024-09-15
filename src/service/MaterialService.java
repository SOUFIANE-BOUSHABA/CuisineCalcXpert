package service;

import model.Material;
import repository.MateriauxRepository;
import repository.impl.MateriauxRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class MaterialService {

    private MateriauxRepository materiauxRepository;

    public MaterialService() {
        this.materiauxRepository = new MateriauxRepositoryImpl();
    }

    public Optional<Material> findMaterialById(int id) {
        return materiauxRepository.findById(id);
    }

    public List<Material> getAllMaterials() {
        return materiauxRepository.findAll();
    }

    public void createMaterial(Material material) {
        materiauxRepository.save(material);
    }

    public void updateMaterial(Material material) {
        Optional<Material> existingMaterial = materiauxRepository.findById(material.getId());
        if (existingMaterial.isPresent()) {
            materiauxRepository.update(material);
        } else {
            System.out.println("Material not found with ID: " + material.getId());
        }
    }

    public void deleteMaterial(int id) {
        Optional<Material> existingMaterial = materiauxRepository.findById(id);
        if (existingMaterial.isPresent()) {
            materiauxRepository.deleteById(id);
        } else {
            System.out.println("Material not found with ID: " + id);
        }
    }

    public List<Material> getMaterialsByProjectId(int projectId) {
        return materiauxRepository.findByProjectId(projectId);
    }


    public double calculateMaterialCost(int materialId) {
        Optional<Material> material = materiauxRepository.findById(materialId);
        if (material.isPresent()) {
            Material mat = material.get();
            return mat.getCoutUnitaire() * mat.getQuantite();
        } else {
            System.out.println("Material not found with ID: " + materialId);
            return 0;
        }
    }
}
