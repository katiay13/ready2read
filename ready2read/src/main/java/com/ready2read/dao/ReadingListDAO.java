package com.ready2read.dao;

import com.ready2read.db.DBConnection;
import com.ready2read.models.ReadingList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReadingListDAO {

    /**
     * Fetches a user's entire reading list, most recently added first.
     * The JOIN retrieves Title, Author, and Genre from the Books table in a single
     * query so the list view can display book details without additional lookups.
     */
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
                    // These fields come from the Books JOIN, not the base ReadingList columns.
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

    /**
     * Looks up a specific reading list entry by (userID, bookID) pair.
     * Used to check whether a book is already on the user's list before adding,
     * and to retrieve the EntryID needed for status updates or removal.
     * Returns null if the book is not on the user's list.
     */
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

    /**
     * Adds a book to a user's reading list with an initial status.
     * DateAdded is set server-side via NOW() for consistent timestamping.
     */
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

    /** Updates the reading status ("want_to_read" → "currently_reading" → "finished") for an entry. */
    public void updateStatus(int entryID, String status) {
        String sql = "UPDATE ReadingList SET Status = ? WHERE EntryID = ?";
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

    /** Removes an entry from the reading list by its primary key. */
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

    /**
     * Maps a single ResultSet row to a ReadingList object.
     * Match against getValue() because the DB stores the 
     * display string ("reading"), not the enum name ("READING").
     */
    private ReadingList mapRow(ResultSet rs) throws SQLException {
        String statusValue = rs.getString("Status");
        ReadingList.Status status = null;
        for (ReadingList.Status s : ReadingList.Status.values()) {
            if (s.getValue().equals(statusValue)) {
                status = s;
                break;
            }
        }
        return new ReadingList(
            rs.getInt("EntryID"),
            rs.getInt("UserID"),
            rs.getInt("BookID"),
            status,
            rs.getTimestamp("DateAdded").toLocalDateTime()
        );
    }
}
