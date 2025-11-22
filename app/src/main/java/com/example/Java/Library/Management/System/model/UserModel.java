package com.example.Java.Library.Management.System.model;

public class UserModel {
    final String id;
    final String fullname;
    final String username;
    final String role;

    public UserModel(String id, String fullname, String username, String role) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.role = role;
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

    public String getRole(){
        return role;
    }


}
