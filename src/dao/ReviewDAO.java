package dao;

import db.DBConnection;
import models.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public void insert(Review review) throws SQLException {
        String sql = "INSERT INTO Reviews (UserID, BookID, Rating, ReviewText) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, review.getUserID());
            stmt.setInt(2, review.getBookID());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getReviewText());
            stmt.executeUpdate();
        }
    }

    public Review findByID(int reviewID) throws SQLException {
        String sql = "SELECT * FROM Reviews WHERE ReviewID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reviewID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Review> findByBook(int bookID) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews WHERE BookID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) reviews.add(mapRow(rs));
        }
        return reviews;
    }

    public List<Review> findByUser(int userID) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) reviews.add(mapRow(rs));
        }
        return reviews;
    }

    public void update(Review review) throws SQLException {
        String sql = "UPDATE Reviews SET Rating = ?, ReviewText = ? WHERE ReviewID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, review.getRating());
            stmt.setString(2, review.getReviewText());
            stmt.setInt(3, review.getReviewID());
            stmt.executeUpdate();
        }
    }

    public void delete(int reviewID) throws SQLException {
        String sql = "DELETE FROM Reviews WHERE ReviewID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reviewID);
            stmt.executeUpdate();
        }
    }

    private Review mapRow(ResultSet rs) throws SQLException {
        Timestamp modified = rs.getTimestamp("DateModified");
        return new Review(
            rs.getInt("ReviewID"),
            rs.getInt("UserID"),
            rs.getInt("BookID"),
            rs.getInt("Rating"),
            rs.getString("ReviewText"),
            rs.getTimestamp("DateCreated").toLocalDateTime(),
            modified != null ? modified.toLocalDateTime() : null
        );
    }
}
