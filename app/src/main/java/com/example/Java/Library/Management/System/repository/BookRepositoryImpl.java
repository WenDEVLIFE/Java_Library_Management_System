package com.example.Java.Library.Management.System.repository;

import com.example.Java.Library.Management.System.model.BookModel;
import com.example.Java.Library.Management.System.services.SQliteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

abstract class BookRepository {
    public abstract List<BookModel> findAll();
    public abstract BookModel findById(Integer bookId);
    public abstract boolean insert(BookModel book);
    public abstract boolean update(BookModel book);
    public abstract boolean deleteById(Integer bookId);
}

public class BookRepositoryImpl extends BookRepository {

    @Override
    public List<BookModel> findAll() {
        List<BookModel> books = new ArrayList<>();
        Connection conn = SQliteConnection.connect();
        if (conn == null) return books;
        String sql = "SELECT id, title, isbn, categoryId, filename, mime_type, file_size, file_bob, description, " +
                "created_at, updated_At FROM books ORDER BY created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        } catch (SQLException ignored) {
        } finally {
            SQliteConnection.close(conn);
        }
        return books;
    }

    @Override
    public BookModel findById(Integer bookId) {
        Connection conn = SQliteConnection.connect();
        if (conn == null) return null;
        String sql = "SELECT id, title, isbn, categoryId, filename, mime_type, file_size, file_bob, description, " +
                "created_at, updated_At FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException ignored) {
        } finally {
            SQliteConnection.close(conn);
        }
        return null;
    }

    @Override
    public boolean insert(BookModel book) {
        Connection conn = SQliteConnection.connect();
        if (conn == null) return false;
        String sql = "INSERT INTO books (title, isbn, categoryId, filename, mime_type, file_size, file_bob, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            if (book.getCategoryId() != null) {
                ps.setInt(3, book.getCategoryId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, book.getFilename());
            ps.setString(5, book.getMimeType());
            if (book.getFileSize() != null) {
                ps.setLong(6, book.getFileSize());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            if (book.getFileData() != null) {
                ps.setBytes(7, book.getFileData());
            } else {
                ps.setNull(7, Types.BLOB);
            }
            ps.setString(8, book.getDescription());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
            return false;
        } finally {
            SQliteConnection.close(conn);
        }
    }

    @Override
    public boolean update(BookModel book) {
        Connection conn = SQliteConnection.connect();
        if (conn == null) return false;

        // If file fields are null, update only metadata; otherwise update all including file
        boolean updateFile = book.getFileData() != null && book.getFileSize() != null && book.getFilename() != null && book.getMimeType() != null;
        String sqlMetaOnly = "UPDATE books SET title = ?, isbn = ?, categoryId = ?, description = ?, updated_At = CURRENT_TIMESTAMP WHERE id = ?";
        String sqlWithFile = "UPDATE books SET title = ?, isbn = ?, categoryId = ?, filename = ?, mime_type = ?, file_size = ?, file_bob = ?, description = ?, updated_At = CURRENT_TIMESTAMP WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(updateFile ? sqlWithFile : sqlMetaOnly)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            if (book.getCategoryId() != null) {
                ps.setInt(3, book.getCategoryId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (updateFile) {
                ps.setString(4, book.getFilename());
                ps.setString(5, book.getMimeType());
                ps.setLong(6, book.getFileSize());
                ps.setBytes(7, book.getFileData());
                ps.setString(8, book.getDescription());
                ps.setInt(9, book.getId());
            } else {
                ps.setString(4, book.getDescription());
                ps.setInt(5, book.getId());
            }
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating book: " + e.getMessage());
            return false;
        } finally {
            SQliteConnection.close(conn);
        }
    }

    @Override
    public boolean deleteById(Integer bookId) {
        Connection conn = SQliteConnection.connect();
        if (conn == null) return false;
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException ignored) {
            return false;
        } finally {
            SQliteConnection.close(conn);
        }
    }

    private BookModel mapRow(ResultSet rs) throws SQLException {
        return new BookModel(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("isbn"),
                rs.getObject("categoryId") != null ? rs.getInt("categoryId") : null,
                rs.getString("filename"),
                rs.getString("mime_type"),
                rs.getObject("file_size") != null ? rs.getLong("file_size") : null,
                rs.getBytes("file_bob"),
                rs.getString("description"),
                rs.getString("created_at"),
                rs.getString("updated_At")
        );
    }
}
