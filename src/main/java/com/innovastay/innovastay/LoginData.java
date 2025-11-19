package com.innovastay.innovastay;

public class LoginData {

    private static String email = "admin@gmail.com";      // DEFAULT email
    private static String password = "12345";             // DEFAULT password

    private String usernameOrEmail;
    private String inputPassword;

    // ------------------ CONSTRUCTORS ------------------
    public LoginData() {}

    public LoginData(String usernameOrEmail, String inputPassword) {
        this.usernameOrEmail = usernameOrEmail;
        this.inputPassword = inputPassword;
    }

    // ------------------ SETTERS ------------------
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public void setPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    // ------------------ GETTERS ------------------
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public String getInputPassword() {
        return inputPassword;
    }

    // ------------------ DEFAULT LOGIN DATA (STATIC) ------------------
    public static String getEmail() {
        return email;
    }

    public static String getPassword() {
        return password;
    }

    // YOU CAN UPDATE THE STORED EMAIL + PASSWORD (optional)
    public static void setEmail(String newEmail) {
        email = newEmail;
    }

    public static void setPasswordData(String newPassword) {
        password = newPassword;
    }

    // ------------------ VALIDATION ------------------
    public boolean isValid() {
        return usernameOrEmail != null && !usernameOrEmail.isEmpty()
                && inputPassword != null && !inputPassword.isEmpty();
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "usernameOrEmail='" + usernameOrEmail + '\'' +
                ", inputPassword='" + inputPassword + '\'' +
                '}';
    }




}
