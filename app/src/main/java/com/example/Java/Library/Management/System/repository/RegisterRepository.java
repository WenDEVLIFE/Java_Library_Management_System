package com.example.Java.Library.Management.System.repository;

abstract class RegisterRepository {
    public abstract void registerUser(String username, String password, String fullnanme);
}


class AdminRegisterRepository extends RegisterRepository {
    @Override
    public void registerUser(String username, String password, String fullname) {
        // Implementation for registering an admin user
        System.out.println("Registering admin user: " + username);
        // Add database logic here
    }
}