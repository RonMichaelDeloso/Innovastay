package com.innovastay.innovastay;

public class SignUpData {
    private String name;
    private String email;
    private String password;

    public SignUpData() {}

    public SignUpData(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return name; // Fixed to return actual name
    }

    public boolean isValid() {
        return name != null && !name.isEmpty() &&
                email != null && !email.isEmpty() &&
                password != null && !password.isEmpty();
    }

    @Override
    public String toString() {
        return "SignUpData { name='" + name + "', email='" + email + "', password='" + password + "'}";
    }
}