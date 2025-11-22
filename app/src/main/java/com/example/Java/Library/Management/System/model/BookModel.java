package com.example.Java.Library.Management.System.model;

public class BookModel {
    final Integer id;
    final String title;
    final String isbn;
    final Integer categoryId;
    final String filename;
    final String mimeType;
    final Long fileSize;
    final byte[] fileData;
    final String description;
    final String createdAt;
    final String updatedAt;

    public BookModel(Integer id, String title, String isbn, Integer categoryId, String filename, String mimeType, Long fileSize, byte[] fileData, String description, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.categoryId = categoryId;
        this.filename = filename;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.fileData = fileData;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getFilename() {
        return filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
