package com.ready2read.dao;

import com.ready2read.db.DBConnection;
import com.ready2read.models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> getAllBooks(int page, int pageSize) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books ORDER BY Title LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) books.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("getAllBooks failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return books;
    }

    public List<Book> getBooksByGenre(String genre, int page, int pageSize) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE Genre = ? ORDER BY Title LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, genre);
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) books.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("getBooksByGenre failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return books;
    }

    public Book getBookByID(int bookID) {
        String sql = "SELECT * FROM Books WHERE BookID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("getBookByID failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public void addBook(Book book) {
        String sql = "INSERT INTO Books (Title, Author, Genre, PublishedYear, ISBN, Description) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getPublishedYear());
            stmt.setString(5, book.getIsbn());
            stmt.setString(6, book.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("addBook failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateBook(Book book) {
        String sql = "UPDATE Books SET Title = ?, Author = ?, Genre = ?, PublishedYear = ?, " +
                     "ISBN = ?, Description = ? WHERE BookID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getPublishedYear());
            stmt.setString(5, book.getIsbn());
            stmt.setString(6, book.getDescription());
            stmt.setInt(7, book.getBookID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateBook failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deleteBook(int bookID) {
        String sql = "DELETE FROM Books WHERE BookID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteBook failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public double getAverageRating(int bookID) {
        String sql = "SELECT AVG(Rating) FROM Reviews WHERE BookID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble(1);
                    return rs.wasNull() ? 0.0 : avg;
                }
            }
        } catch (SQLException e) {
            System.err.println("getAverageRating failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return 0.0;
    }

    public int getTotalBookCount() {
        String sql = "SELECT COUNT(*) FROM Books";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("getTotalBookCount failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int getTotalBookCountByGenre(String genre) {
        String sql = "SELECT COUNT(*) FROM Books WHERE Genre = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, genre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("getTotalBookCountByGenre failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return 0;
    }

    public boolean isbnExists(String isbn, int excludeBookID) {
        String sql = "SELECT COUNT(*) FROM Books WHERE ISBN = ? AND BookID != ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.setInt(2, excludeBookID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("isbnExists failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();
        String sql = "SELECT DISTINCT Genre FROM Books WHERE Genre IS NOT NULL ORDER BY Genre";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) genres.add(rs.getString("Genre"));
        } catch (SQLException e) {
            System.err.println("getAllGenres failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return genres;
    }

    //search books
    public List<Book> searchBooks(String query, String genre, int page, int pageSize) {
        List<Book> books = new ArrayList<>();

        String sql;
        boolean hasGenre = genre != null && !genre.trim().isEmpty();

        if (hasGenre) {
            sql = "SELECT * FROM Books WHERE Genre = ? AND " +
                    "(Title LIKE ? OR Author LIKE ? OR ISBN LIKE ?) " +
                    "ORDER BY Title LIMIT ? OFFSET ?";
        } else {
            sql = "SELECT * FROM Books WHERE " +
                    "(Title LIKE ? OR Author LIKE ? OR ISBN LIKE ?) " +
                    "ORDER BY Title LIMIT ? OFFSET ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String search = "%" + query + "%";
            int index = 1;

            if (hasGenre) {
                stmt.setString(index++, genre);
            }

            stmt.setString(index++, search);
            stmt.setString(index++, search);
            stmt.setString(index++, search);
            stmt.setInt(index++, pageSize);
            stmt.setInt(index, (page - 1) * pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("searchBooks failed: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return books;
    }

    //count search results
    public int getSearchBookCount(String query, String genre) {
        String sql;
        boolean hasGenre = genre != null && !genre.trim().isEmpty();

        if (hasGenre) {
            sql = "SELECT COUNT(*) FROM Books WHERE Genre = ? AND " +
                    "(Title LIKE ? OR Author LIKE ? OR ISBN LIKE ?)";
        } else {
            sql = "SELECT COUNT(*) FROM Books WHERE " +
                    "(Title LIKE ? OR Author LIKE ? OR ISBN LIKE ?)";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String search = "%" + query + "%";
            int index = 1;

            if (hasGenre) {
                stmt.setString(index++, genre);
            }

            stmt.setString(index++, search);
            stmt.setString(index++, search);
            stmt.setString(index, search);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("getSearchBookCount failed: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return 0;
    }
    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt("BookID"),
            rs.getString("Title"),
            rs.getString("Author"),
            rs.getString("Genre"),
            rs.getInt("PublishedYear"),
            rs.getString("ISBN"),
            rs.getString("Description")
        );
    }
}
