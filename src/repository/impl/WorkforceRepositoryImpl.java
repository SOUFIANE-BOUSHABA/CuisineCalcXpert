package repository.impl;

import model.Project;
import model.Workforce;
import repository.WorkforceRepository;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkforceRepositoryImpl implements WorkforceRepository {

    private Connection connection;

    public WorkforceRepositoryImpl() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }

    @Override
    public Optional<Workforce> findById(int id) {
        String sql = "SELECT * FROM workforce WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Workforce workforce = new Workforce(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("taux_tva"),
                        rs.getDouble("taux_horaire"),
                        rs.getDouble("heures_travail"),
                        rs.getDouble("productivite_ouvrier"),
                        new Project(
                                rs.getInt("projet_id"))
                );
                return Optional.of(workforce);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Workforce> findAll() {
        String sql = "SELECT * FROM workforce";
        List<Workforce> workforces = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Workforce workforce = new Workforce(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("taux_tva"),
                        rs.getDouble("taux_horaire"),
                        rs.getDouble("heures_travail"),
                        rs.getDouble("productivite_ouvrier"),
                        new Project(
                                rs.getInt("projet_id"))
                );
                workforces.add(workforce);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workforces;
    }

    @Override
    public void save(Workforce workforce) {
        String sql = "INSERT INTO workforce (nom,  taux_tva, taux_horaire, heures_travail, productivite_ouvrier , projet_id  , type_composant) VALUES (?, ?, ?, ?, ?,? , ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, workforce.getNom());
            stmt.setDouble(2, workforce.getTauxTVA());
            stmt.setDouble(3, workforce.getTauxHoraire());
            stmt.setDouble(4, workforce.getHeuresTravail());
            stmt.setDouble(5, workforce.getProductiviteOuvrier());
            stmt.setInt(6, workforce.getProject().getId());
            stmt.setString(7, workforce.getTypeComposant());


            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Workforce workforce) {
        String sql = "UPDATE workforce SET nom = ?, taux_tva = ?, taux_horaire = ?, heures_travail = ?, productivite_ouvrier = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, workforce.getNom());
            stmt.setDouble(2, workforce.getTauxTVA());
            stmt.setDouble(3, workforce.getTauxHoraire());
            stmt.setDouble(4, workforce.getHeuresTravail());
            stmt.setDouble(5, workforce.getProductiviteOuvrier());
            stmt.setInt(6, workforce.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM workforce WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Workforce> findByProjectId(int projectId) {
        String sql = "SELECT * FROM workforce WHERE projet_id = ?";
        List<Workforce> workforces = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Workforce workforce = new Workforce(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("taux_tva"),
                        rs.getDouble("taux_horaire"),
                        rs.getDouble("heures_travail"),
                        rs.getDouble("productivite_ouvrier"),
                        new Project(
                                rs.getInt("projet_id"))
                );
                workforces.add(workforce);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workforces;
    }


    @Override
    public void updateTva(Workforce workforce) {
        String sql = "UPDATE workforce SET taux_tva = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, workforce.getTauxTVA());
            stmt.setInt(2, workforce.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
