package com.app.dao;

import com.app.model.User;
import com.app.model.Material;
import java.sql.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppDAO {

    private Connection connection;

    public AppDAO() {
        try {
            // **CRITICAL for Render Deployment:** Get DB config from Environment Variable
            String dbUrl = System.getenv("DATABASE_URL");

            if (dbUrl == null || dbUrl.isEmpty()) {
                throw new RuntimeException("DATABASE_URL environment variable is not set. Cannot connect to database.");
            }

            Class.forName("org.postgresql.Driver");

            // 1. Extract components from URL
            URI dbUri = new URI(dbUrl);
            
            // Extract User/Password
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            
            String host = dbUri.getHost();
            
            // 2. FIX: Handle invalid port (-1) returned by URI parsing by defaulting to 5432
            int port = dbUri.getPort();
            if (port == -1) {
                // Default PostgreSQL port
                port = 5432; 
            }
            
            String path = dbUri.getPath();

            // 3. Construct the correct JDBC connection string
            String dbConnString = "jdbc:postgresql://" + host + ':' + port + path;
            
            // 4. Establish connection
            this.connection = DriverManager.getConnection(dbConnString, username, password);
            System.out.println("Database connection established successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed during startup.", e);
        }
    }

    // --- Authentication Methods ---

    public User validateUser(String username, String password) throws SQLException {
        String sql = "SELECT id, username, role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password); // NOTE: Use HASHING in production!
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"));
            }
        }
        return null;
    }

    // --- Material CRUD Methods ---

    public void addMaterial(Material material) throws SQLException {
        String sql = "INSERT INTO materials (user_id, title, course, description, file_name, stored_name) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, material.getUserId());
            statement.setString(2, material.getTitle());
            statement.setString(3, material.getCourse());
            statement.setString(4, material.getDescription());
            statement.setString(5, material.getFileName());
            statement.setString(6, material.getStoredName());
            statement.executeUpdate();
        }
    }

    // **Data Isolation:** Only retrieve materials uploaded by the current user
    public List<Material> getAllMaterialsByUserId(int userId) throws SQLException {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT id, title, course, description, file_name, stored_name, upload_date FROM materials WHERE user_id = ? ORDER BY upload_date DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Material material = new Material();
                material.setId(rs.getInt("id"));
                material.setTitle(rs.getString("title"));
                material.setCourse(rs.getString("course"));
                material.setDescription(rs.getString("description"));
                material.setFileName(rs.getString("file_name"));
                material.setStoredName(rs.getString("stored_name"));
                // Assuming upload_date is stored as a Timestamp
                Timestamp ts = rs.getTimestamp("upload_date");
                if (ts != null) {
                    material.setUploadDate(ts.toLocalDateTime());
                }
                materials.add(material);
            }
        }
        return materials;
    }

    // Retrieve material by ID and user_id (Security Check for download/delete)
    public Material getMaterialByIdAndUser(int materialId, int userId) throws SQLException {
        String sql = "SELECT title, file_name, stored_name FROM materials WHERE id = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, materialId);
            statement.setInt(2, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Material material = new Material();
                material.setTitle(rs.getString("title"));
                material.setFileName(rs.getString("file_name"));
                material.setStoredName(rs.getString("stored_name"));
                return material;
            }
        }
        return null;
    }

    // **Data Isolation:** Ensure only the owner can delete
    public boolean deleteMaterial(int materialId, int userId) throws SQLException {
        String sql = "DELETE FROM materials WHERE id = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, materialId);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        }
    }
}
