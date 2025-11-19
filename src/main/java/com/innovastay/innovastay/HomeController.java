package com.innovastay.innovastay;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeController {

    // === SEARCH FIELDS ===
    @FXML private TextField searchDestinationField;
    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;
    @FXML private TextField guestsField;
    @FXML private Label noResultsLabel;

    // === HOTEL CARD FIELDS ===
    @FXML private HBox limketkaiCard;
    @FXML private HBox redPlanetCard;
    @FXML private HBox countryVillageCard;

    private List<Hotel> hotels = new ArrayList<>();

    // === INITIALIZE METHOD ===
    @FXML
    public void initialize() {
        System.out.println("=== HOME CONTROLLER INITIALIZED ===");
        initializeHotels();
        if (noResultsLabel != null) {
            noResultsLabel.setVisible(false);
        }
    }

    // === NAVIGATION METHODS ===
    @FXML
    public void goToProfile(MouseEvent event) throws IOException {
        System.out.println("Navigating to profile page from home...");
        navigateToFXML("/com/innovastay/innovastay/profile-view.fxml", event);
    }

    @FXML
    public void goHome(MouseEvent event) throws IOException {
        navigateToFXML("/com/innovastay/innovastay/Home-view.fxml", event);
    }

    // === FIX: ADD THIS METHOD ===
    @FXML
    private void goToHotelDetails(ActionEvent event) {
        try {
            Button button = (Button) event.getSource();
            String hotelId = (String) button.getUserData();

            System.out.println("Opening hotel details for: " + hotelId);

            String fxmlPath = "";
            switch (hotelId) {
                case "limketkai":
                    fxmlPath = "/com/innovastay/innovastay/klh-view.fxml";
                    break;
                case "redplanet":
                    fxmlPath = "/com/innovastay/innovastay/rph-view.fxml";
                    break;
                case "countryvillage":
                    fxmlPath = "/com/innovastay/innovastay/cvh-view.fxml";
                    break;
                default:
                    showAlert("Error", "Hotel details not found for: " + hotelId);
                    return;
            }

            System.out.println("Loading FXML: " + fxmlPath);
            navigateToFXML(fxmlPath, event);

        } catch (Exception e) {
            System.err.println("Error navigating to hotel details: " + e.getMessage());
            e.printStackTrace();
            showAlert("Navigation Error", "Cannot open hotel details: " + e.getMessage());
        }
    }

    // === SEARCH METHODS ===
    @FXML
    private void searchHotels(ActionEvent event) {
        try {
            String destination = searchDestinationField.getText().trim().toLowerCase();
            String guests = guestsField.getText().trim();
            filterHotels(destination, guests);
        } catch (Exception e) {
            System.err.println("SEARCH ERROR: " + e.getMessage());
        }
    }

    @FXML
    private void clearSearch(ActionEvent event) {
        try {
            if (searchDestinationField != null) searchDestinationField.setText("");
            if (checkInDatePicker != null) checkInDatePicker.setValue(null);
            if (checkOutDatePicker != null) checkOutDatePicker.setValue(null);
            if (guestsField != null) guestsField.setText("");

            for (Hotel hotel : hotels) {
                hotel.getCard().setVisible(true);
                hotel.getCard().setManaged(true);
            }

            if (noResultsLabel != null) {
                noResultsLabel.setVisible(false);
            }
        } catch (Exception e) {
            System.err.println("CLEAR SEARCH ERROR: " + e.getMessage());
        }
    }

    // === HELPER METHODS ===
    private void initializeHotels() {
        hotels.clear();

        if (limketkaiCard != null) {
            hotels.add(new Hotel("Limketkai Luxe Hotel", "Limketkai Ave., Cagayan de Oro City",
                    "This property offers comfort and modern facilities.", "PHP 4,161", "kt.jpg", limketkaiCard));
        }
        if (redPlanetCard != null) {
            hotels.add(new Hotel("Red Planet Cagayan de Oro", "CM Recto Avenue, Cagayan de Oro City Center",
                    "Modern rooms with easy city access.", "PHP 1,747", "rp.jpeg", redPlanetCard));
        }
        if (countryVillageCard != null) {
            hotels.add(new Hotel("Country Village Hotel", "Villarin Street, Carmen, Cagayan de Oro City",
                    "Enjoy a relaxing stay with pool and dining.", "PHP 1,617", "cv.jpg", countryVillageCard));
        }

        System.out.println("Initialized " + hotels.size() + " hotels");
    }

    private void filterHotels(String destination, String guests) {
        boolean anyVisible = false;
        for (Hotel hotel : hotels) {
            boolean shouldShow = true;

            if (!destination.isEmpty()) {
                String hotelName = hotel.getName().toLowerCase();
                String hotelAddress = hotel.getAddress().toLowerCase();
                shouldShow = hotelName.contains(destination) || hotelAddress.contains(destination);
            }

            hotel.getCard().setVisible(shouldShow);
            hotel.getCard().setManaged(shouldShow);

            if (shouldShow) anyVisible = true;
        }

        if (noResultsLabel != null) {
            noResultsLabel.setVisible(!anyVisible);
        }
    }

    // === NAVIGATION HELPERS ===
    private boolean navigateToFXML(String fxmlPath, MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            return true;
        } catch (Exception e) {
            System.err.println("NAVIGATION ERROR: " + e.getMessage());
            e.printStackTrace();
            showAlert("Navigation Error", "Cannot navigate to: " + fxmlPath);
            return false;
        }
    }

    private boolean navigateToFXML(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            return true;
        } catch (Exception e) {
            System.err.println("NAVIGATION ERROR: " + e.getMessage());
            e.printStackTrace();
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

    // === HOTEL DATA CLASS ===
    public class Hotel {
        private String name;
        private String address;
        private String description;
        private String price;
        private String image;
        private HBox card;

        public Hotel(String name, String address, String description, String price, String image, HBox card) {
            this.name = name;
            this.address = address;
            this.description = description;
            this.price = price;
            this.image = image;
            this.card = card;
        }

        public String getName() { return name; }
        public String getAddress() { return address; }
        public String getDescription() { return description; }
        public String getPrice() { return price; }
        public String getImage() { return image; }
        public HBox getCard() { return card; }
    }
}