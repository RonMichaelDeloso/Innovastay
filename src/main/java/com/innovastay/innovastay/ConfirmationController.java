package com.innovastay.innovastay;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfirmationController {

    // === LABELS FOR DISPLAYING BOOKING INFO ===
    @FXML private Label confirmationNumberLabel;
    @FXML private Label confirmationNumberValue;
    @FXML private Label hotelNameLabel;
    @FXML private Label checkInDateLabel;
    @FXML private Label checkOutDateLabel;
    @FXML private Label roomTypeLabel;
    @FXML private Label guestsLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label statusLabel;
    @FXML private Label guestNameLabel;
    @FXML private Label guestEmailLabel;
    @FXML private Label guestPhoneLabel;

    // === BUTTONS ===
    @FXML private Button viewBookingsButton;
    @FXML private Button printConfirmationButton;
    @FXML private Button backToHomeButton;

    // === BOOKING DATA ===
    private String hotelName = "Limketkai Luxe Hotel";
    private String amount = "PHP 4,161.00";
    private String paymentMethod = "Credit Card";
    private String referenceNumber = "";

    // === NAVIGATION METHODS ===
    @FXML
    public void goHome(MouseEvent event) throws IOException {
        System.out.println("Navigating to home from confirmation...");
        navigateToFXML("/com/innovastay/innovastay/Home-view.fxml", event);
    }

    @FXML
    public void viewMyBookings() {
        System.out.println("View My Bookings clicked - feature coming soon!");
        showAlert("Coming Soon", "My Bookings feature will be available soon!");
    }

    @FXML
    public void printConfirmation() {
        System.out.println("Print Confirmation clicked");
        if (confirmationNumberValue != null) {
            showAlert("Print", "Confirmation sent to printer!\nBooking: " + confirmationNumberValue.getText());
        } else {
            showAlert("Print", "Confirmation sent to printer!");
        }
    }

    @FXML
    public void backToHome() {
        System.out.println("Back to Home clicked");
        try {
            navigateToFXML("/com/innovastay/innovastay/Home-view.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to navigate to home: " + e.getMessage());
        }
    }

    // === INITIALIZE METHOD ===
    @FXML
    public void initialize() {
        System.out.println("=== CONFIRMATION CONTROLLER INITIALIZED ===");

        // Debug: Check which components are loaded
        debugComponentStatus();

        // Generate and set default booking data
        setDefaultBookingData();
    }

    // === DEBUG COMPONENT STATUS ===
    private void debugComponentStatus() {
        System.out.println("=== COMPONENT STATUS ===");
        System.out.println("confirmationNumberValue: " + (confirmationNumberValue != null ? "LOADED" : "NULL"));
        System.out.println("hotelNameLabel: " + (hotelNameLabel != null ? "LOADED" : "NULL"));
        System.out.println("checkInDateLabel: " + (checkInDateLabel != null ? "LOADED" : "NULL"));
        System.out.println("checkOutDateLabel: " + (checkOutDateLabel != null ? "LOADED" : "NULL"));
        System.out.println("roomTypeLabel: " + (roomTypeLabel != null ? "LOADED" : "NULL"));
        System.out.println("guestsLabel: " + (guestsLabel != null ? "LOADED" : "NULL"));
        System.out.println("totalAmountLabel: " + (totalAmountLabel != null ? "LOADED" : "NULL"));
        System.out.println("statusLabel: " + (statusLabel != null ? "LOADED" : "NULL"));
        System.out.println("guestNameLabel: " + (guestNameLabel != null ? "LOADED" : "NULL"));
        System.out.println("guestEmailLabel: " + (guestEmailLabel != null ? "LOADED" : "NULL"));
        System.out.println("guestPhoneLabel: " + (guestPhoneLabel != null ? "LOADED" : "NULL"));
        System.out.println("backToHomeButton: " + (backToHomeButton != null ? "LOADED" : "NULL"));
    }

    // === SET DEFAULT BOOKING DATA ===
    private void setDefaultBookingData() {
        try {
            System.out.println("Setting default booking data...");

            // Generate confirmation number
            String confirmationNumber = "INNOVA" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            // Set dates (today + 2 days for check-in, +4 days for check-out)
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            Date checkIn = new Date(System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000));
            Date checkOut = new Date(System.currentTimeMillis() + (4 * 24 * 60 * 60 * 1000));

            // Get current user data if available
            String guestName = "Your Name";
            String guestEmail = "yourname@gmail.com";
            String guestPhone = "+63 912 345 6789";

            SignUpData currentUser = getCurrentUser();
            if (currentUser != null) {
                guestName = currentUser.getFullName();
                guestEmail = currentUser.getEmail();
                System.out.println("User data loaded: " + guestName + ", " + guestEmail);
            } else {
                System.out.println("No user data found, using defaults");
            }

            // Safe label updates with debugging
            updateLabel(confirmationNumberValue, confirmationNumber, "Confirmation Number");
            updateLabel(hotelNameLabel, hotelName, "Hotel Name");
            updateLabel(checkInDateLabel, dateFormat.format(checkIn), "Check-in Date");
            updateLabel(checkOutDateLabel, dateFormat.format(checkOut), "Check-out Date");
            updateLabel(roomTypeLabel, "Deluxe King Room", "Room Type");
            updateLabel(guestsLabel, "2 Adults", "Guests");
            updateLabel(totalAmountLabel, amount, "Total Amount");
            updateLabel(statusLabel, "CONFIRMED", "Status");
            updateLabel(guestNameLabel, guestName, "Guest Name");
            updateLabel(guestEmailLabel, guestEmail, "Guest Email");
            updateLabel(guestPhoneLabel, guestPhone, "Guest Phone");

            System.out.println("Default booking data set successfully!");

        } catch (Exception e) {
            System.err.println("Error in setDefaultBookingData: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // === SAFE LABEL UPDATE ===
    private void updateLabel(Label label, String value, String fieldName) {
        if (label != null) {
            label.setText(value);
            System.out.println(fieldName + " set to: " + value);
        } else {
            System.err.println("Label is null for: " + fieldName);
        }
    }

    // === METHOD TO SET BOOKING DETAILS FROM PAYMENT ===
    public void setBookingDetails(String hotel, String totalAmount, String paymentType, String reference) {
        System.out.println("Setting booking details: " + hotel + ", " + totalAmount);

        this.hotelName = hotel;
        this.amount = totalAmount;
        this.paymentMethod = paymentType;
        this.referenceNumber = reference;

        // Update the labels if they are already initialized
        updateLabel(hotelNameLabel, hotelName, "Hotel Name");
        updateLabel(totalAmountLabel, amount, "Total Amount");

        System.out.println("Booking details updated successfully!");
    }

    // === GET CURRENT USER ===
    private SignUpData getCurrentUser() {
        try {
            // Access the static list from LoginController
            if (!LoginController.getRegisteredUsers().isEmpty()) {
                // Get the current user email from LoginController
                String currentUserEmail = LoginController.getCurrentUserEmail();

                if (currentUserEmail != null) {
                    // Find the user by email
                    for (SignUpData user : LoginController.getRegisteredUsers()) {
                        if (user.getEmail().equals(currentUserEmail)) {
                            System.out.println("Found user: " + user.getFullName());
                            return user;
                        }
                    }
                }

                // If no current user email, return the last registered user
                SignUpData lastUser = LoginController.getRegisteredUsers().get(LoginController.getRegisteredUsers().size() - 1);
                System.out.println("Using last registered user: " + lastUser.getFullName());
                return lastUser;
            }
        } catch (Exception e) {
            System.err.println("Error getting current user: " + e.getMessage());
        }
        System.out.println("No user data available");
        return null;
    }

    // === NAVIGATION HELPER ===
    private void navigateToFXML(String fxmlPath, MouseEvent event) throws IOException {
        try {
            System.out.println("Attempting to navigate to: " + fxmlPath);

            // Check if resource exists
            if (getClass().getResource(fxmlPath) == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage;
            if (event != null) {
                stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            } else {
                // Get stage from any available component
                if (backToHomeButton != null && backToHomeButton.getScene() != null) {
                    stage = (Stage) backToHomeButton.getScene().getWindow();
                } else if (hotelNameLabel != null && hotelNameLabel.getScene() != null) {
                    stage = (Stage) hotelNameLabel.getScene().getWindow();
                } else {
                    throw new IOException("Cannot determine current stage");
                }
            }

            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("Navigation successful to: " + fxmlPath);

        } catch (Exception e) {
            System.err.println("NAVIGATION ERROR: " + e.getMessage());
            e.printStackTrace();
            showAlert("Navigation Error", "Cannot navigate to requested page: " + e.getMessage());
            throw new IOException(e);
        }
    }

    // === ALERT HELPER ===
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}