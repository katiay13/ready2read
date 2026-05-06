package dao;

import db.DBConnection;
import models.ReadingList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReadingListDAO {

    public List<ReadingList> getReadingListByUser(int userID) {
        List<ReadingList> entries = new ArrayList<>();
        String sql = "SELECT rl.*, b.Title, b.Author, b.Genre FROM ReadingList rl " +
                     "JOIN Books b ON rl.BookID = b.BookID " +
                     "WHERE rl.UserID = ? ORDER BY rl.DateAdded DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReadingList entry = mapRow(rs);
                    entry.setBookTitle(rs.getString("Title"));
                    entry.setBookAuthor(rs.getString("Author"));
                    entry.setBookGenre(rs.getString("Genre"));
                    entries.add(entry);
                }
            }
        } catch (SQLException e) {
            System.err.println("getReadingListByUser failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return entries;
    }

    public ReadingList getEntry(int userID, int bookID) {
        String sql = "SELECT * FROM ReadingList WHERE UserID = ? AND BookID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, bookID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("getEntry failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public void addEntry(int userID, int bookID, String status) {
        String sql = "INSERT INTO ReadingList (UserID, BookID, Status, DateAdded) " +
                     "VALUES (?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, bookID);
            stmt.setString(3, status);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("addEntry failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateStatus(int entryID, String status) {
        String sql = "finished".equals(status)
            ? "UPDATE ReadingList SET Status = ?, DateFinished = NOW() WHERE EntryID = ?"
            : "UPDATE ReadingList SET Status = ?, DateFinished = NULL WHERE EntryID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, entryID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateStatus failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void removeEntry(int entryID) {
        String sql = "DELETE FROM ReadingList WHERE EntryID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entryID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("removeEntry failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private ReadingList mapRow(ResultSet rs) throws SQLException {
        String statusValue = rs.getString("Status");
        ReadingList.Status status = null;
        for (ReadingList.Status s : ReadingList.Status.values()) {
            if (s.getValue().equals(statusValue)) {
                status = s;
                break;
            }
        }
        Timestamp dateFinished = rs.getTimestamp("DateFinished");
        return new ReadingList(
            rs.getInt("EntryID"),
            rs.getInt("UserID"),
            rs.getInt("BookID"),
            status,
            rs.getTimestamp("DateAdded").toLocalDateTime(),
            dateFinished != null ? dateFinished.toLocalDateTime() : null
        );
    }
}
