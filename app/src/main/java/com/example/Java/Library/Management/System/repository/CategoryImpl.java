package com.example.Java.Library.Management.System.repository;

import com.example.Java.Library.Management.System.model.CategoryModel;
import com.example.Java.Library.Management.System.services.SQliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

abstract class CategoryRepository {

    public abstract boolean AddCategory(String categoryName);

    public abstract List<CategoryModel> GetAllCategories();

    public abstract boolean editCategory(String id, String categoryName);

    public abstract boolean deleteCategory(String id);

    public abstract boolean isCategoryExist(String categoryName);
}

public class CategoryImpl extends CategoryRepository {

    @Override
    public boolean AddCategory(String categoryName) {
        String sql = "INSERT INTO category (category_name, created_at) VALUES (?,?)";

         try (Connection conn = SQliteConnection.connect();
              PreparedStatement ps = conn.prepareStatement(sql)) {

             ps.setString(1, categoryName);
             ps.setString(2, java.time.LocalDateTime.now().toString());
             int rowsAffected = ps.executeUpdate();

             return rowsAffected > 0;
         } catch (SQLException e) {
             System.out.println("Error adding category: " + e.getMessage());
             return false;
         }

    }

    @Override
    public List<CategoryModel> GetAllCategories() {
         String sql = "SELECT * FROM category";

         try (Connection conn = SQliteConnection.connect();
              PreparedStatement ps = conn.prepareStatement(sql)) {

             var rs = ps.executeQuery();
             List<CategoryModel> categories = new java.util.ArrayList<>();

             while (rs.next()) {
                 String id = rs.getString("categoryId");
                 String name = rs.getString("category_name");
                 String createdAt = rs.getString("created_at");

                 categories.add(new CategoryModel(id, name, createdAt));
             }

             return categories;
         } catch (SQLException e) {
             System.out.println("Error retrieving categories: " + e.getMessage());
             return java.util.Collections.emptyList();
         }
    }

    @Override
    public boolean editCategory(String id, String categoryName) {
        String sql = "UPDATE category SET category_name = ? WHERE categoryId = ?";

         try (Connection conn = SQliteConnection.connect();
              PreparedStatement ps = conn.prepareStatement(sql)) {

             ps.setString(1, categoryName);
             ps.setString(2, id);
             int rowsAffected = ps.executeUpdate();

             return rowsAffected > 0;
         } catch (SQLException e) {
             System.out.println("Error editing category: " + e.getMessage());
             return false;
         }
    }

    @Override
    public boolean deleteCategory(String id) {
        String sql = "DELETE FROM category WHERE categoryId = ?";

         try (Connection conn = SQliteConnection.connect();
              PreparedStatement ps = conn.prepareStatement(sql)) {

             ps.setString(1, id);
             int rowsAffected = ps.executeUpdate();

             return rowsAffected > 0;
         } catch (SQLException e) {
             System.out.println("Error deleting category: " + e.getMessage());
             return false;
         }
    }

    @Override
    public boolean isCategoryExist(String categoryName) {
        String sql = "SELECT COUNT(*) AS count FROM category WHERE category_name = ?";

         try (Connection conn = SQliteConnection.connect();
              PreparedStatement ps = conn.prepareStatement(sql)) {

             ps.setString(1, categoryName);
             var rs = ps.executeQuery();

             if (rs.next()) {
                 int count = rs.getInt("count");
                 return count > 0;
             }

             return false;
         } catch (SQLException e) {
             System.out.println("Error checking category existence: " + e.getMessage());
             return false;
         }
    }


}
