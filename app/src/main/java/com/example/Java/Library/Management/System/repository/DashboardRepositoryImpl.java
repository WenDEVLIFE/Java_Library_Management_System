package com.example.Java.Library.Management.System.repository;

import com.example.Java.Library.Management.System.services.SQliteConnection;

import java.sql.SQLException;

abstract class DashboardRepository {
    public abstract int getUserRoleCount();

    public abstract int getAdminRoleCount();

    public abstract int getTotalBooksCount();

    public abstract int getCategoriesCount();
}
public class DashboardRepositoryImpl extends DashboardRepository {
    @Override
    public int getUserRoleCount() {
        String sql = "SELECT COUNT(*) AS user_count FROM user WHERE role = 'user'";

        try (var conn = SQliteConnection.connect();
             var ps = conn.prepareStatement(sql)) {

            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_count");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user role count: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int getAdminRoleCount() {
        String sql = "SELECT COUNT(*) AS admin_count FROM user WHERE role = 'admin'";

        try (var conn = SQliteConnection.connect();
             var ps = conn.prepareStatement(sql)) {

            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("admin_count");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching admin role count: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int getTotalBooksCount() {
        String sql = "SELECT COUNT(*) AS book_count FROM books";

        try (var conn = SQliteConnection.connect();
             var ps = conn.prepareStatement(sql)) {

            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("book_count");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching total books count: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int getCategoriesCount() {
        String sql = "SELECT COUNT(*) AS category_count FROM category";

        try (var conn = SQliteConnection.connect();
             var ps = conn.prepareStatement(sql)) {

            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("category_count");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching categories count: " + e.getMessage());
        }
        return 0;
    }
}
