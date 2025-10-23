package com.cinecrave.dao;

import com.cinecrave.model.User;
import com.cinecrave.model.Customer;
import java.sql.*;

public class UserDAO {
    // Assumes phone field holds the 'password' for simplification
    private static final String AUTH_SQL = "SELECT user_id, name, email, phone, role FROM User WHERE email = ? AND phone = ?"; 
    private static final String REGISTER_USER_SQL = "INSERT INTO User (name, email, phone, role) VALUES (?, ?, ?, 'Customer')";
    private static final String REGISTER_CUSTOMER_SQL = "INSERT INTO Customer (user_id) VALUES (?)";

    public User authenticate(String email, String password) throws SQLException {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(AUTH_SQL)) {
            
            stmt.setString(1, email);
            stmt.setString(2, password); 
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    String name = rs.getString("name");
                    String phone = rs.getString("phone");
                    String role = rs.getString("role");
                    
                    if ("Customer".equals(role)) {
                        return new Customer(id, name, email, phone);
                    } 
                }
            }
        }
        return null;
    }

    public boolean register(String name, String email, String phone, String password) throws SQLException {
        Connection conn = null;
        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false); 
            
            
            PreparedStatement userStmt = conn.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, name);
            userStmt.setString(2, email);
            userStmt.setString(3, phone); 
            userStmt.executeUpdate();

            
            ResultSet generatedKeys = userStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                
                PreparedStatement customerStmt = conn.prepareStatement(REGISTER_CUSTOMER_SQL);
                customerStmt.setInt(1, userId);
                customerStmt.executeUpdate();
            }

            conn.commit(); 
            return true;
        } catch (SQLException e) {
            if (conn != null) { conn.rollback(); } 
            throw e; 
        } finally {
            if (conn != null) { conn.setAutoCommit(true); conn.close(); }
        }
    }
}
