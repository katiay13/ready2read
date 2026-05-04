package dao;

import db.DBConnection;
import models.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO Users (Username, Email, Password, JoinDate, ProfileBio, AvatarURL) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setDate(4, Date.valueOf(user.getJoinDate()));
            stmt.setString(5, user.getProfileBio());
            stmt.setString(6, user.getAvatarURL());
            stmt.executeUpdate();
        }
    }

    public User findByID(int userID) throws SQLException {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) users.add(mapRow(rs));
        }
        return users;
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE Users SET Email = ?, ProfileBio = ?, AvatarURL = ? WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getProfileBio());
            stmt.setString(3, user.getAvatarURL());
            stmt.setInt(4, user.getUserID());
            stmt.executeUpdate();
        }
    }

    public void delete(int userID) throws SQLException {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.executeUpdate();
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("UserID"),
            rs.getString("Username"),
            rs.getString("Email"),
            rs.getString("Password"),
            rs.getDate("JoinDate").toLocalDate(),
            rs.getString("ProfileBio"),
            rs.getString("AvatarURL")
        );
    }
}
