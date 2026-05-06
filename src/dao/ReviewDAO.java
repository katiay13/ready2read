package dao;

import db.DBConnection;
import models.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public List<Review> getReviewsByBook(int bookID) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.Username FROM Reviews r " +
                     "JOIN Users u ON r.UserID = u.UserID " +
                     "WHERE r.BookID = ? ORDER BY r.DateCreated DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Review review = mapRow(rs);
                    review.setUsername(rs.getString("Username"));
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            System.err.println("getReviewsByBook failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return reviews;
    }

    public Review getReviewByUserAndBook(int userID, int bookID) {
        String sql = "SELECT * FROM Reviews WHERE UserID = ? AND BookID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, bookID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("getReviewByUserAndBook failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public void addReview(Review review) {
        String sql = "INSERT INTO Reviews (UserID, BookID, Rating, ReviewText, DateCreated) " +
                     "VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, review.getUserID());
            stmt.setInt(2, review.getBookID());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getReviewText());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("addReview failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateReview(Review review) {
        String sql = "UPDATE Reviews SET Rating = ?, ReviewText = ?, DateModified = NOW() " +
                     "WHERE ReviewID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, review.getRating());
            stmt.setString(2, review.getReviewText());
            stmt.setInt(3, review.getReviewID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateReview failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deleteReview(int reviewID) {
        String sql = "DELETE FROM Reviews WHERE ReviewID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reviewID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteReview failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Review> getReviewsByUser(int userID) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, b.Title FROM Reviews r " +
                     "JOIN Books b ON r.BookID = b.BookID " +
                     "WHERE r.UserID = ? ORDER BY r.DateCreated DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Review review = mapRow(rs);
                    review.setBookTitle(rs.getString("Title"));
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            System.err.println("getReviewsByUser failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return reviews;
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
