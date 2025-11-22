package com.example.Java.Library.Management.System.model;

public class UserModel {
    final String id;
    final String fullname;
    final String username;

    public UserModel(String id, String fullname, String username) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }


}
