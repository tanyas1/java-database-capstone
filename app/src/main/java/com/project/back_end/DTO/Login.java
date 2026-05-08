package com.project.back_end.DTO;

public class Login {

    // 1. Email field
    private String email;

    // 2. Password field
    private String password;


    // 4. Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}