package com.example.Java.Library.Management.System;

import com.example.Java.Library.Management.System.repository.RegisterRepositoryImpl;
import com.example.Java.Library.Management.System.services.SQliteConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterUser {
    private RegisterRepositoryImpl repo;
    @Test
    void registerUser_insertsAndIsUserExistsReturnsTrue() throws SQLException {
        repo = new RegisterRepositoryImpl();
        boolean inserted = repo.registerUser("testuser", "password123", "Test User");
        assertTrue(inserted, "registerUser should return true on success");
        assertTrue(repo.isUserExists("testuser"), "isUserExists should return true for inserted user");

        // cleanup
        try (Connection conn = SQliteConnection.connect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM user WHERE username = ?")) {
            ps.setString(1, "testuser");
            ps.executeUpdate();
        }
    }
}
