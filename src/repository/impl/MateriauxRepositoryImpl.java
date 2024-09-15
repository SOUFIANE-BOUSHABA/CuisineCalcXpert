package repository.impl;

import model.Material;
import repository.MateriauxRepository;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MateriauxRepositoryImpl implements MateriauxRepository {

    private Connection connection;

    public MateriauxRepositoryImpl() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }


    @Override
    public Optional<Material> findById(int id) {
        String sql = "SELECT * FROM materiaux WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Material material = new Material(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("cout_unitaire"),
                        rs.getDouble("quantite"),
                        rs.getDouble("taux_tva"),
                        rs.getDouble("cout_transport"),
                        rs.getDouble("coefficient_qualite")
                );
                return Optional.of(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }



    @Override
    public void save(Material material) {
        String sql = "INSERT INTO materiaux (nom, cout_unitaire, quantite, taux_tva, cout_transport, coefficient_qualite) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, material.getNom());
            stmt.setDouble(2, material.getCoutUnitaire());
            stmt.setDouble(3, material.getQuantite());
            stmt.setDouble(4, material.getTauxTVA());
            stmt.setDouble(5, material.getCoutTransport());
            stmt.setDouble(6, material.getCoefficientQualite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Material material) {
        String sql = "UPDATE materiaux SET nom = ?, cout_unitaire = ?, quantite = ?, taux_tva = ?, cout_transport = ?, coefficient_qualite = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, material.getNom());
            stmt.setDouble(2, material.getCoutUnitaire());
            stmt.setDouble(3, material.getQuantite());
            stmt.setDouble(4, material.getTauxTVA());
            stmt.setDouble(5, material.getCoutTransport());
            stmt.setDouble(6, material.getCoefficientQualite());
            stmt.setInt(7, material.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM materiaux WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Material> findAll() {
        String sql = "SELECT * FROM materiaux";
        List<Material> materials = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Material material = new Material(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("cout_unitaire"),
                        rs.getDouble("quantite"),
                        rs.getDouble("taux_tva"),
                        rs.getDouble("cout_transport"),
                        rs.getDouble("coefficient_qualite")
                );
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }



    @Override
    public List<Material> findByProjectId(int projetId) {
        String sql = "SELECT * FROM materiaux WHERE projet_id = ?";
        List<Material> materials = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, projetId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Material material = new Material(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("cout_unitaire"),
                        rs.getDouble("quantite"),
                        rs.getDouble("taux_tva"),
                        rs.getDouble("cout_transport"),
                        rs.getDouble("coefficient_qualite")
                );
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }


}
