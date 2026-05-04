package dao;

import db.DBConnection;
import models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void insert(Book book) throws SQLException {
        String sql = "INSERT INTO Books (Title, AuthorID, Genre, PublishedYear, ISBN, Description) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorID());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getPublishedYear());
            stmt.setString(5, book.getIsbn());
            stmt.setString(6, book.getDescription());
            stmt.executeUpdate();
        }
    }

    public Book findByID(int bookID) throws SQLException {
        String sql = "SELECT * FROM Books WHERE BookID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) books.add(mapRow(rs));
        }
        return books;
    }

    public List<Book> findByTitle(String title) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE Title LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) books.add(mapRow(rs));
        }
        return books;
    }

    public List<Book> findByAuthor(int authorID) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE AuthorID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) books.add(mapRow(rs));
        }
        return books;
    }

    public void update(Book book) throws SQLException {
        String sql = "UPDATE Books SET Title = ?, Genre = ?, PublishedYear = ?, ISBN = ?, Description = ? " +
                     "WHERE BookID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getGenre());
            stmt.setInt(3, book.getPublishedYear());
            stmt.setString(4, book.getIsbn());
            stmt.setString(5, book.getDescription());
            stmt.setInt(6, book.getBookID());
            stmt.executeUpdate();
        }
    }

    public void delete(int bookID) throws SQLException {
        String sql = "DELETE FROM Books WHERE BookID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookID);
            stmt.executeUpdate();
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt("BookID"),
            rs.getString("Title"),
            rs.getInt("AuthorID"),
            rs.getString("Genre"),
            rs.getInt("PublishedYear"),
            rs.getString("ISBN"),
            rs.getString("Description")
        );
    }
}
