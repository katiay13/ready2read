package dao;

import db.DBConnection;
import models.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {

    public void insert(Author author) throws SQLException {
        String sql = "INSERT INTO Authors (FirstName, LastName, Bio) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setString(3, author.getBio());
            stmt.executeUpdate();
        }
    }

    public Author findByID(int authorID) throws SQLException {
        String sql = "SELECT * FROM Authors WHERE AuthorID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Author> findAll() throws SQLException {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT * FROM Authors";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) authors.add(mapRow(rs));
        }
        return authors;
    }

    public void update(Author author) throws SQLException {
        String sql = "UPDATE Authors SET FirstName = ?, LastName = ?, Bio = ? WHERE AuthorID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setString(3, author.getBio());
            stmt.setInt(4, author.getAuthorID());
            stmt.executeUpdate();
        }
    }

    public void delete(int authorID) throws SQLException {
        String sql = "DELETE FROM Authors WHERE AuthorID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorID);
            stmt.executeUpdate();
        }
    }

    private Author mapRow(ResultSet rs) throws SQLException {
        return new Author(
            rs.getInt("AuthorID"),
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("Bio")
        );
    }
}
