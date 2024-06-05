package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterOrganizationPageController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField totalSharesField;

    @FXML
    private TextField sharesAllocatedField;

    @FXML
    private TextField sharePriceField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleRegister(ActionEvent event) {
        String name = nameField.getText();
        String totalSharesText = totalSharesField.getText();
        String sharesAllocatedText = sharesAllocatedField.getText();
        String sharePriceText = sharePriceField.getText();

        if (name.isEmpty() || totalSharesText.isEmpty() || sharesAllocatedText.isEmpty() || sharePriceText.isEmpty()) {
            messageLabel.setText("Please fill in all fields!");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            try {
                double totalShares = Double.parseDouble(totalSharesText);
                double sharesAllocated = Double.parseDouble(sharesAllocatedText);
                double sharePrice = Double.parseDouble(sharePriceText);

                // Create new Organization object
                // Save organization to file or database
                messageLabel.setText("Organization registered successfully!");
                messageLabel.setStyle("-fx-text-fill: green;");
                clearFields();
            } catch (NumberFormatException e) {
                messageLabel.setText("Invalid input format!");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainPage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void clearFields() {
        nameField.clear();
        totalSharesField.clear();
        sharesAllocatedField.clear();
        sharePriceField.clear();
    }
}
