package dao;

import db.DBConnection;
import models.ReadingList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReadingListDAO {

    public void insert(ReadingList entry) throws SQLException {
        String sql = "INSERT INTO ReadingList (UserID, BookID, Status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entry.getUserID());
            stmt.setInt(2, entry.getBookID());
            stmt.setString(3, entry.getStatus().getValue());
            stmt.executeUpdate();
        }
    }

    public List<ReadingList> findByUser(int userID) throws SQLException {
        List<ReadingList> entries = new ArrayList<>();
        String sql = "SELECT * FROM ReadingList WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) entries.add(mapRow(rs));
        }
        return entries;
    }

    public List<ReadingList> findByUserAndStatus(int userID, ReadingList.Status status) throws SQLException {
        List<ReadingList> entries = new ArrayList<>();
        String sql = "SELECT * FROM ReadingList WHERE UserID = ? AND Status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setString(2, status.getValue());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) entries.add(mapRow(rs));
        }
        return entries;
    }

    public void updateStatus(int entryID, ReadingList.Status status) throws SQLException {
        String sql = "UPDATE ReadingList SET Status = ? WHERE EntryID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.getValue());
            stmt.setInt(2, entryID);
            stmt.executeUpdate();
        }
    }

    public void delete(int entryID) throws SQLException {
        String sql = "DELETE FROM ReadingList WHERE EntryID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entryID);
            stmt.executeUpdate();
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
