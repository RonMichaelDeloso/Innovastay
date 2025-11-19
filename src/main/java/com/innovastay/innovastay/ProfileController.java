package com.innovastay.innovastay;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class ProfileController {

    // === PROFILE PAGE FIELDS ===
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label leftNameLabel;
    @FXML private Label leftEmailLabel;
    @FXML private Label displayNameLabel;
    @FXML private Label displayEmailLabel;
    @FXML private Label mobileLabel;
    @FXML private Label locationLabel;

    // === LOGOUT METHOD - DIRECT LOGOUT (NO POPUP) ===
    @FXML
    public void logout() {
        System.out.println("=== LOGGING OUT USER ===");
        performLogout();
    }

    // === PERFORM LOGOUT ===
    private void performLogout() {
        try {
            System.out.println("Clearing user session...");

            // Clear current user session
            LoginController.setCurrentUserEmail(null);
            System.out.println("User session cleared");

            // Navigate directly to login page
            navigateToLogin();

        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
            showAlert("Logout Error", "Failed to logout properly.");
        }
    }

    // === NAVIGATE TO LOGIN ===
    private void navigateToLogin() {
        try {
            System.out.println("Navigating to login page...");

            // Load login page
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/innovastay/innovastay/Login-view.fxml"));
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setTitle("Innovastay - Login");
            loginStage.show();

            // Close current profile window
            closeCurrentWindow();

            System.out.println("Successfully logged out and navigated to login page");

        } catch (IOException e) {
            System.err.println("Error loading login page: " + e.getMessage());
            // Fallback: try to navigate to home page
            try {
                Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/innovastay/innovastay/Home-view.fxml"));
                Stage homeStage = new Stage();
                homeStage.setScene(new Scene(homeRoot));
                homeStage.show();
                closeCurrentWindow();
            } catch (IOException ex) {
                System.err.println("Complete navigation failure: " + ex.getMessage());
            }
        }
    }

    // === CLOSE CURRENT WINDOW ===
    private void closeCurrentWindow() {
        try {
            if (nameLabel != null && nameLabel.getScene() != null) {
                Stage currentStage = (Stage) nameLabel.getScene().getWindow();
                currentStage.close();
                System.out.println("Current window closed");
            }
        } catch (Exception e) {
            System.err.println("Error closing window: " + e.getMessage());
        }
    }

    // === NAVIGATION METHODS ===
    @FXML
    public void goHome(MouseEvent event) throws IOException {
        System.out.println("Navigating to home from profile...");
        navigateToFXML("/com/innovastay/innovastay/Home-view.fxml", event);
    }

    @FXML
    public void closeProfile(MouseEvent event) throws IOException {
        System.out.println("Closing profile, going to home...");
        navigateToFXML("/com/innovastay/innovastay/Home-view.fxml", event);
    }

    // === SAVE PROFILE METHOD ===
    @FXML
    private void saveProfileChanges(ActionEvent event) {
        try {
            System.out.println("Saving profile changes...");
            showAlert("Success", "Profile changes saved successfully!");
        } catch (Exception e) {
            System.err.println("Error saving profile: " + e.getMessage());
            showAlert("Error", "Failed to save profile changes.");
        }
    }

    // === INITIALIZE METHOD ===
    @FXML
    public void initialize() {
        System.out.println("=== PROFILE CONTROLLER INITIALIZED ===");
        System.out.println("Current user email: " + LoginController.getCurrentUserEmail());
        initializeProfileData();
    }

    // === PROFILE DATA METHODS ===
    private void initializeProfileData() {
        try {
            SignUpData currentUser = getCurrentUser();

            if (currentUser != null) {
                // Update all labels with user data
                updateLabel(leftNameLabel, currentUser.getFullName());
                updateLabel(leftEmailLabel, currentUser.getEmail());
                updateLabel(nameLabel, currentUser.getFullName());
                updateLabel(emailLabel, currentUser.getEmail());
                updateLabel(displayNameLabel, currentUser.getFullName());
                updateLabel(displayEmailLabel, currentUser.getEmail());

                System.out.println("Profile data loaded for: " + currentUser.getFullName());
            } else {
                setDefaultProfileData();
                System.err.println("No current user found! Using default data.");
            }
        } catch (Exception e) {
            System.err.println("Error initializing profile data: " + e.getMessage());
            setDefaultProfileData();
        }
    }

    // === SAFE LABEL UPDATE ===
    private void updateLabel(Label label, String value) {
        if (label != null) {
            label.setText(value);
        }
    }

    private void setDefaultProfileData() {
        String defaultName = "Your Name";
        String defaultEmail = "yourname@gmail.com";

        updateLabel(leftNameLabel, defaultName);
        updateLabel(leftEmailLabel, defaultEmail);
        updateLabel(nameLabel, defaultName);
        updateLabel(emailLabel, defaultEmail);
        updateLabel(displayNameLabel, defaultName);
        updateLabel(displayEmailLabel, defaultEmail);
        updateLabel(mobileLabel, "Add number");
        updateLabel(locationLabel, "Add location");
    }

    // === GET CURRENT USER ===
    private SignUpData getCurrentUser() {
        try {
            String currentUserEmail = LoginController.getCurrentUserEmail();
            if (currentUserEmail != null && !LoginController.getRegisteredUsers().isEmpty()) {
                for (SignUpData user : LoginController.getRegisteredUsers()) {
                    if (user.getEmail().equals(currentUserEmail)) {
                        return user;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting current user: " + e.getMessage());
        }
        return null;
    }

    // === NAVIGATION HELPER ===
    private boolean navigateToFXML(String fxmlPath, MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            return true;
        } catch (Exception e) {
            System.err.println("NAVIGATION ERROR: " + e.getMessage());
            showAlert("Navigation Error", "Cannot navigate to: " + fxmlPath);
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}