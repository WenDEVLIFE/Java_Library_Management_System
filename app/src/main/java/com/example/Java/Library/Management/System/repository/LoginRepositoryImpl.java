/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Java.Library.Management.System.repository;

import com.example.Java.Library.Management.System.auth.LoginView;
import com.example.Java.Library.Management.System.services.SQliteConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author wendevlife
 */

abstract class LoginRepository {
    public abstract void loginUser(Map<String, Object> credentials, LoginView login);
}
public class LoginRepositoryImpl  extends  LoginRepository {
    @Override
    public void loginUser(Map<String, Object> credentials, LoginView login) {

        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";

        // login function here
        try (Connection conn = SQliteConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, (String) credentials.get("username"));
            ps.setString(2, (String) credentials.get("password"));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    System.out.println("Login successful for user: " + credentials.get("username") + " with role: " + role);
                    JOptionPane.showMessageDialog(null, "Login Successful! Welcome, " + rs.getString("fullname"));
                } else {
                    System.out.println("Invalid username or password for user: " +  credentials.get("username"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to login user", e);
        }

    }
    
}
