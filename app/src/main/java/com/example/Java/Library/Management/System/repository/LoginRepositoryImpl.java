/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Java.Library.Management.System.repository;

import com.example.Java.Library.Management.System.auth.LoginView;
import com.example.Java.Library.Management.System.books.UserBookView;
import com.example.Java.Library.Management.System.dashboard.AdminDashboardView;
import com.example.Java.Library.Management.System.services.SQliteConnection;
import com.example.Java.Library.Management.System.services.UserSession;
import com.example.Java.Library.Management.System.model.UserModel;

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

        try (Connection conn = SQliteConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, (String) credentials.get("username"));
            ps.setString(2, (String) credentials.get("password"));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    String userId = rs.getString("id");
                    String fullname = rs.getString("fullname");
                    String username = rs.getString("username");
                    
                    // Set current user in session
                    UserModel currentUser = new UserModel(userId, fullname, username, role);
                    UserSession.getInstance().setCurrentUser(currentUser);
                    
                    System.out.println("Login successful for user: " + credentials.get("username") + " with role: " + role);
                    JOptionPane.showMessageDialog(null, "Login Successful! Welcome, " + fullname);

                    // safe null check and content comparison
                    if ("admin".equalsIgnoreCase(role)) {
                        SwingUtilities.invokeLater(() -> {
                            AdminDashboardView view = new AdminDashboardView();
                            view.setVisible(true);
                            login.dispose();
                        });
                    } else if ("user".equalsIgnoreCase(role)) {
                        SwingUtilities.invokeLater(() -> {
                            UserBookView view = new UserBookView();
                            view.setVisible(true);
                            login.dispose();
                        });
                    } else {
                        System.out.println("Unknown role: " + role);
                    }
                } else {
                    System.out.println("Invalid username or password for user: " + credentials.get("username"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to login user", e);
        }
    }
    
}
