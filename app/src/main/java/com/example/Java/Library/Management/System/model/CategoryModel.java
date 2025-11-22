package com.example.Java.Library.Management.System.model;

public class CategoryModel {
    final String categoryId;
    final String categoryName;
    final String createdAt;

    public CategoryModel(String categoryId, String categoryName, String createdAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
