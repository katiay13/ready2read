package dao;

import db.DBConnection;
import models.User;

import java.sql.*;

public class UserDAO {

    public User login(String username, String password) {
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    public void registerUser(User user) {
        String sql = "INSERT INTO Users (Username, Email, Password, Role, JoinDate) " +
                     "VALUES (?, ?, ?, 'user', CURDATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("registerUser failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

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

    private User mapRow(ResultSet rs) throws SQLException {
        Date joinDate = rs.getDate("JoinDate");
        return new User(
            rs.getInt("UserID"),
            rs.getString("Username"),
            rs.getString("Email"),
            rs.getString("Password"),
            rs.getString("Role"),
            joinDate != null ? joinDate.toLocalDate() : null,
            rs.getString("ProfileBio"),
            rs.getString("AvatarURL")
        );
    }
}
