package repository.impl;

import model.Devis;
import repository.DevisRepository;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DevisRepositoryImpl implements DevisRepository {

    private Connection connection;

    public DevisRepositoryImpl() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }

    @Override
    public Optional<Devis> findById(int id) {
        String sql = "SELECT * FROM devis WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Devis devis = new Devis(
                        rs.getInt("id"),
                        rs.getDouble("montant_estime"),
                        rs.getDate("date_emission").toLocalDate(),
                        rs.getDate("date_validite").toLocalDate(),
                        rs.getBoolean("accepte"),
                        rs.getInt("projet_id")
                );
                return Optional.of(devis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Devis> findAll() {
        String sql = "SELECT * FROM devis";
        List<Devis> devisList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Devis devis = new Devis(
                        rs.getInt("id"),
                        rs.getDouble("montant_estime"),
                        rs.getDate("date_emission").toLocalDate(),
                        rs.getDate("date_validite").toLocalDate(),
                        rs.getBoolean("accepte"),
                        rs.getInt("projet_id")
                );
                devisList.add(devis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devisList;
    }

    @Override
    public void save(Devis devis) {
        String sql = "INSERT INTO devis (montant_estime, date_emission, date_validite, accepte, projet_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, devis.getMontantEstime());
            stmt.setDate(2, java.sql.Date.valueOf(devis.getDateEmission()));
            stmt.setDate(3, java.sql.Date.valueOf(devis.getDateValidite()));
            stmt.setBoolean(4, devis.isAccepte());
            stmt.setInt(5, devis.getProjetId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Devis devis) {
        String sql = "UPDATE devis SET montant_estime = ?, date_emission = ?, date_validite = ?, accepte = ?, projet_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, devis.getMontantEstime());
            stmt.setDate(2, java.sql.Date.valueOf(devis.getDateEmission()));
            stmt.setDate(3, java.sql.Date.valueOf(devis.getDateValidite()));
            stmt.setBoolean(4, devis.isAccepte());
            stmt.setInt(5, devis.getProjetId());
            stmt.setInt(6, devis.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM devis WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Devis> findByProjectId(int projectId) {
        String sql = "SELECT * FROM devis WHERE projet_id = ?";
        List<Devis> devisList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Devis devis = new Devis(
                        rs.getInt("id"),
                        rs.getDouble("montant_estime"),
                        rs.getDate("date_emission").toLocalDate(),
                        rs.getDate("date_validite").toLocalDate(),
                        rs.getBoolean("accepte"),
                        rs.getInt("projet_id")
                );
                devisList.add(devis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devisList;
    }
}
