package repository.impl;

import model.Client;
import repository.ClientRepository;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class ClientRepositoryImpl implements ClientRepository {

    private Connection connection;

    public ClientRepositoryImpl() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }

    public void create(Client client) {
        try {
            String query = "INSERT INTO client (nom, adresse, telephone, estprofessionnel, remise) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query , PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getAdresse());
            stmt.setString(3, client.getTelephone());
            stmt.setBoolean(4, client.isEstProfessionnel());
            stmt.setDouble(5, client.getRemise());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating client failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Optional<Client> findById(int id ){
        try {
            String query = "SELECT * FROM client WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getBoolean("estprofessionnel"),
                        rs.getDouble("remise")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        try {
            String query = "SELECT * FROM client";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getBoolean("estprofessionnel"),
                        rs.getDouble("remise")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return clients;
    }

    public void update(Client client) {
        try {
            String query = "UPDATE client SET nom = ?, adresse = ?, telephone = ?, estprofessionnel = ?, remise = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getAdresse());
            stmt.setString(3, client.getTelephone());
            stmt.setBoolean(4, client.isEstProfessionnel());
            stmt.setDouble(5, client.getRemise());
            stmt.setInt(6, client.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            String query = "DELETE FROM client WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Optional<Client> findByNom(String nom) {
        try {
            String query = "SELECT * FROM client WHERE nom = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getBoolean("estprofessionnel"),
                        rs.getDouble("remise")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return Optional.empty();

    }




    public Map<Client, Integer> getClientWithCountProject(){
        Map<Client , Integer> clientProjectCount = new HashMap<>();
        String query = "SELECT client.id, client.nom, COUNT(project.id) as count FROM client LEFT JOIN project ON client.id = project.client_id GROUP BY client.id";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getBoolean("estprofessionnel"),
                        rs.getDouble("remise")
                );
                clientProjectCount.put(client, rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientProjectCount;

    }




}

