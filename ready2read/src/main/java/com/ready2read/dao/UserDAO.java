package com.ready2read.dao;

import com.ready2read.db.DBConnection;
import com.ready2read.models.User;

import java.sql.*;

public class UserDAO {

    /**
     * Authenticates a user by matching username and password in db
     * Returns the matching User, or null if credentials don't match
     * Credentials are compared in SQL, not Java
     */
    public User login(String username, String password) {
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        // Auto-closes DB resources on exit, even if an error occurs
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set query values safely —> prevents SQL injection
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("login failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Inserts a new user row. Role defaults to 'user' and JoinDate is set
     * server-side via CURDATE() so it always reflects the database server's timezone
     */
    public void registerUser(User user) {
        String sql = "INSERT INTO Users (Username, Email, Password, Role, JoinDate) " +
                     "VALUES (?, ?, ?, 'user', CURDATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            // executeUpdate() is used for INSERT/UPDATE/DELETE —> returns row count, not ResultSet
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("registerUser failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /** Updates the password for the given user. */
    public void updatePassword(int userID, String newPassword) {
        String sql = "UPDATE Users SET Password = ? WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updatePassword failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /** Permanently removes a user row by primary key. */
    public void deleteUser(int userID) {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteUser failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /** Fetches a single user by primary key. Returns null if not found. */
    public User getUserByID(int userID) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("getUserByID failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    /** Checks uniqueness using COUNT(*) */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("usernameExists failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }

    /** Same COUNT(*) pattern as usernameExists but for Email */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("emailExists failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }

    /** Converts a DB row into a User object. Handles NULL join date */
    private User mapRow(ResultSet rs) throws SQLException {
        Date joinDate = rs.getDate("JoinDate");
        return new User(
            rs.getInt("UserID"),
            rs.getString("Username"),
            rs.getString("Email"),
            rs.getString("Password"),
            rs.getString("Role"),
            joinDate != null ? joinDate.toLocalDate() : null
        );
    }
}
