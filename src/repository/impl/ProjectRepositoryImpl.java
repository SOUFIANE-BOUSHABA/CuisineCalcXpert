package repository.impl;

import model.Project;
import repository.ProjectRepository;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProjectRepositoryImpl implements ProjectRepository {

    private Connection connection;

    public ProjectRepositoryImpl() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }



    public void create(Project project) {
        String sql = "INSERT INTO project (nom_projet, marge_beneficiaire, cout_total, etat_projet, client_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, project.getNomProjet());
            stmt.setDouble(2, project.getMargeBeneficiaire());
            stmt.setDouble(3, project.getCoutTotal());
            stmt.setString(4, project.getEtatProjet());
            stmt.setInt(5, project.getClientId());

            stmt.executeUpdate();


            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    project.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating project failed, no ID obtained.");
                }
            }
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


    public Optional<List<Map<String, Object>>> findAllWithClients() {
        String sql = "SELECT p.*, c.nom AS client_nom FROM project p JOIN client c ON p.client_id = c.id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            var rs = stmt.executeQuery();
            List<Map<String, Object>> projects = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> projectData = new HashMap<>();
                projectData.put("project_id", rs.getInt("id"));
                projectData.put("project_name", rs.getString("nom_projet"));
                projectData.put("client_name", rs.getString("client_nom"));
                projects.add(projectData);
            }
            return Optional.of(projects);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}


