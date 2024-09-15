package repository.impl;

import model.Project;
import repository.ProjectRepository;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProjectRepositoryImpl implements ProjectRepository {

    private Connection connection;

    public ProjectRepositoryImpl() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }



    public void create(Project project) {
        String sql = "INSERT INTO project (nom_projet, marge_beneficiaire, cout_total, etat_projet, client_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, project.getNomProjet());
            stmt.setDouble(2, project.getMargeBeneficiaire());
            stmt.setDouble(3, project.getCoutTotal());
            stmt.setString(4, project.getEtatProjet());
            stmt.setInt(5, project.getClientId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Project project) {
        String sql = "UPDATE project SET nom_projet = ?, marge_beneficiaire = ?, cout_total = ?, etat_projet = ?, client_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, project.getNomProjet());
            stmt.setDouble(2, project.getMargeBeneficiaire());
            stmt.setDouble(3, project.getCoutTotal());
            stmt.setString(4, project.getEtatProjet());
            stmt.setInt(5, project.getClientId());
            stmt.setInt(6, project.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM project WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Optional<Project> findById(int id) {
        String sql = "SELECT * FROM project WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Project(
                        rs.getInt("id"),
                        rs.getString("nom_projet"),
                        rs.getDouble("marge_beneficiaire"),
                        rs.getDouble("cout_total"),
                        Project.EtatProjet.valueOf(rs.getString("etat_projet").toUpperCase()),
                        rs.getInt("client_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return Optional.empty();
    }


    public List<Project> findAll() {

        String sql = "SELECT * FROM project";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            var rs = stmt.executeQuery();
            List<Project> projects = new ArrayList<>();
            while (rs.next()) {
                projects.add(new Project(
                        rs.getInt("id"),
                        rs.getString("nom_projet"),
                        rs.getDouble("marge_beneficiaire"),
                        rs.getDouble("cout_total"),
                        Project.EtatProjet.valueOf(rs.getString("etat_projet").toUpperCase()),
                        rs.getInt("client_id")
                ));
            }
            return projects;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}


