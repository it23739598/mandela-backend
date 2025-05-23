package com.example.management_system.dto;

public class LoginResponse {
    private String firstName;
    private String lastName;

    public LoginResponse(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
