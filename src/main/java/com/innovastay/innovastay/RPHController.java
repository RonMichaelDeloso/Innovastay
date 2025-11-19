package com.innovastay.innovastay;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.application.Platform;
import java.io.IOException;

public class RPHController {

    // Store the original event for navigation
    private ActionEvent originalBookEvent;

    // === NAVIGATION METHODS ===
    @FXML
    public void goHome(MouseEvent event) throws IOException {
        navigateToFXML("/com/innovastay/innovastay/Home-view.fxml", event);
    }

    @FXML
    public void closeProfile(MouseEvent event) throws IOException {
        navigateToFXML("/com/innovastay/innovastay/Home-view.fxml", event);
    }

    @FXML
    public void goToProfile(MouseEvent event) throws IOException {
        navigateToFXML("/com/innovastay/innovastay/profile-view.fxml", event);
    }

    @FXML
    public void bookHotel(ActionEvent event) {
        // Store the original event for later navigation
        this.originalBookEvent = event;
        showPaymentOptions("Red Planet Cagayan de Oro");
    }

    // === PAYMENT OPTIONS ===
    private void showPaymentOptions(String hotelName) {
        try {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Payment Method");
            dialog.setHeaderText("Select payment method for:\n" + hotelName + "\nAmount: ₱1,747");

            ButtonType cardButton = new ButtonType("💳 Credit Card");
            ButtonType gcashButton = new ButtonType("📱 GCash");
            dialog.getDialogPane().getButtonTypes().addAll(cardButton, gcashButton, ButtonType.CANCEL);

            VBox content = new VBox(10);
            content.setPadding(new Insets(15));
            content.setAlignment(Pos.CENTER);

            Label label = new Label("How would you like to pay?");
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            content.getChildren().add(label);

            dialog.getDialogPane().setContent(content);

            // Handle button clicks
            dialog.setResultConverter(buttonType -> {
                if (buttonType == cardButton) {
                    Platform.runLater(() -> showCardPayment(hotelName));
                } else if (buttonType == gcashButton) {
                    Platform.runLater(() -> showGCashPayment(hotelName));
                }
                return null;
            });

            dialog.showAndWait();

        } catch (Exception e) {
            showError("Error", "Cannot open payment options");
        }
    }

    // === CARD PAYMENT ===
    private void showCardPayment(String hotelName) {
        try {
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setTitle("Credit Card Payment");
            dialog.setHeaderText("Enter card details for " + hotelName + "\nAmount: ₱1,747");

            ButtonType payButton = new ButtonType("Pay ₱1,747", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(payButton, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            TextField cardNumber = new TextField();
            cardNumber.setPromptText("1234 5678 9012 3456");
            cardNumber.setPrefWidth(200);

            HBox expiryCvv = new HBox(10);
            TextField expiry = new TextField();
            expiry.setPromptText("MM/YY");
            expiry.setPrefWidth(80);
            TextField cvv = new TextField();
            cvv.setPromptText("CVV");
            cvv.setPrefWidth(60);
            expiryCvv.getChildren().addAll(expiry, cvv);

            TextField name = new TextField();
            name.setPromptText("Cardholder Name");
            name.setPrefWidth(200);

            grid.add(new Label("Card Number:"), 0, 0);
            grid.add(cardNumber, 1, 0);
            grid.add(new Label("Expiry/CVV:"), 0, 1);
            grid.add(expiryCvv, 1, 1);
            grid.add(new Label("Cardholder:"), 0, 2);
            grid.add(name, 1, 2);

            dialog.getDialogPane().setContent(grid);

            // Focus first field
            Platform.runLater(cardNumber::requestFocus);

            // Validate and process payment
            dialog.setResultConverter(buttonType -> {
                if (buttonType == payButton) {
                    if (cardNumber.getText().trim().isEmpty()) {
                        showError("Error", "Please enter card number");
                        return false;
                    }
                    if (expiry.getText().trim().isEmpty()) {
                        showError("Error", "Please enter expiry date");
                        return false;
                    }
                    if (cvv.getText().trim().isEmpty()) {
                        showError("Error", "Please enter CVV");
                        return false;
                    }
                    if (name.getText().trim().isEmpty()) {
                        showError("Error", "Please enter cardholder name");
                        return false;
                    }
                    return true;
                }
                return false;
            });

            dialog.showAndWait().ifPresent(success -> {
                if (success) {
                    showSuccess(
                            "🎉 Payment Successful!",
                            "Thank you for your booking!\n\n" +
                                    "🏨 Red Planet Cagayan de Oro\n" +
                                    "💰 Amount: ₱1,747\n" +
                                    "💳 Card: ****" + getLastFour(cardNumber.getText()) + "\n" +
                                    "📅 Booking confirmed!\n\n" +
                                    "Redirecting to confirmation..."
                    );
                    // Navigate to confirmation page
                    navigateToConfirmation(hotelName, "₱1,747", "Credit Card", getLastFour(cardNumber.getText()));
                }
            });

        } catch (Exception e) {
            showError("Error", "Card payment failed");
        }
    }

    // === GCASH PAYMENT ===
    private void showGCashPayment(String hotelName) {
        try {
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setTitle("GCash Payment");
            dialog.setHeaderText("GCash Payment for " + hotelName + "\nAmount: ₱1,747");

            ButtonType payButton = new ButtonType("Confirm GCash Payment", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(payButton, ButtonType.CANCEL);

            VBox content = new VBox(15);
            content.setPadding(new Insets(20));
            content.setAlignment(Pos.CENTER_LEFT);

            // Instructions
            Label instructions = new Label("To complete your payment:");
            instructions.setStyle("-fx-font-weight: bold; -fx-text-fill: #0070BA;");

            VBox steps = new VBox(5);
            steps.getChildren().addAll(
                    new Label("1. Open GCash app"),
                    new Label("2. Tap 'Send Money'"),
                    new Label("3. Enter details below"),
                    new Label("4. Complete payment")
            );

            // Payment details
            VBox details = new VBox(5);
            details.setStyle("-fx-background-color: #e3f2fd; -fx-padding: 15; -fx-border-color: #0070BA; -fx-border-radius: 5;");

            String ref = "RPH" + (System.currentTimeMillis() % 10000);

            details.getChildren().addAll(
                    createDetail("Amount:", "₱1,747"),
                    createDetail("GCash Number:", "0917 123 4567"),
                    createDetail("Account Name:", "InnoVaStay"),
                    createDetail("Reference:", ref)
            );

            // Mobile number input
            Label mobileLabel = new Label("Your GCash mobile number:");
            TextField mobileField = new TextField();
            mobileField.setPromptText("09171234567");
            mobileField.setPrefWidth(200);

            content.getChildren().addAll(instructions, steps, details, mobileLabel, mobileField);
            dialog.getDialogPane().setContent(content);

            Platform.runLater(mobileField::requestFocus);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == payButton) {
                    String mobile = mobileField.getText().trim();
                    if (mobile.isEmpty()) {
                        showError("Error", "Please enter your GCash number");
                        return false;
                    }
                    if (!mobile.matches("09\\d{9}")) {
                        showError("Error", "Please enter valid GCash number (09XXXXXXXXX)");
                        return false;
                    }
                    return true;
                }
                return false;
            });

            dialog.showAndWait().ifPresent(success -> {
                if (success) {
                    showSuccess(
                            "🎉 GCash Payment Successful!",
                            "Thank you for your booking!\n\n" +
                                    "🏨 Red Planet Cagayan de Oro\n" +
                                    "💰 Amount: ₱1,747\n" +
                                    "📱 GCash: " + mobileField.getText() + "\n" +
                                    "🔢 Reference: " + ref + "\n" +
                                    "📅 Booking confirmed!\n\n" +
                                    "Redirecting to confirmation..."
                    );
                    // Navigate to confirmation page
                    navigateToConfirmation(hotelName, "₱1,747", "GCash", ref);
                }
            });

        } catch (Exception e) {
            showError("Error", "GCash payment failed");
        }
    }

    // === NAVIGATE TO CONFIRMATION PAGE ===
    private void navigateToConfirmation(String hotelName, String amount, String paymentMethod, String reference) {
        try {
            System.out.println("Navigating to confirmation page...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/innovastay/innovastay/confirmation.fxml"));
            Parent root = loader.load();

            // Pass booking details to confirmation controller
            ConfirmationController confirmationController = loader.getController();
            confirmationController.setBookingDetails(hotelName, amount, paymentMethod, reference);

            // Use the stored original event for navigation
            if (originalBookEvent != null) {
                Stage stage = (Stage) ((javafx.scene.Node) originalBookEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                showError("Navigation Error", "Cannot navigate to confirmation page.");
            }

        } catch (Exception e) {
            System.err.println("Error navigating to confirmation: " + e.getMessage());
            e.printStackTrace();
            showError("Navigation Error", "Cannot open confirmation page. Please go to home page.");
        }
    }

    // === HELPER METHODS ===
    private HBox createDetail(String label, String value) {
        HBox row = new HBox(10);
        Label key = new Label(label);
        key.setStyle("-fx-font-weight: bold; -fx-min-width: 80;");
        Label val = new Label(value);
        val.setStyle("-fx-font-weight: bold; -fx-text-fill: #0070BA;");
        row.getChildren().addAll(key, val);
        return row;
    }

    private String getLastFour(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return "****";
        }
        String cleanNumber = cardNumber.replaceAll("\\s+", "");
        if (cleanNumber.length() >= 4) {
            return cleanNumber.substring(cleanNumber.length() - 4);
        }
        return cleanNumber;
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Style the success alert
        DialogPane pane = alert.getDialogPane();
        pane.setStyle("-fx-background-color: white; -fx-border-color: #4CAF50; -fx-border-width: 2;");

        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean navigateToFXML(String fxmlPath, MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            return true;
        } catch (Exception e) {
            showError("Navigation Error", "Cannot open page");
            return false;
        }
    }

    // === INITIALIZE METHOD ===
    @FXML
    public void initialize() {
        System.out.println("=== RPH CONTROLLER INITIALIZED ===");
    }
}