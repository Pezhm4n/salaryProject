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
    private TextField industryField;

    @FXML
    private TextField foundationYearField;

    @FXML
    private TextField headquartersField;

    @FXML
    private TextField ceoField;

    @FXML
    private TextField totalSharesField;

    @FXML
    private TextField sharePriceField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleRegister(ActionEvent event) {
        String name = nameField.getText();
        String foundationYearText = foundationYearField.getText();
        String headquarters = headquartersField.getText();
        String ceo = ceoField.getText();
        String totalSharesText = totalSharesField.getText();
        String sharePriceText = sharePriceField.getText();
        String industry = industryField.getText();

        if (name.isEmpty() || foundationYearText.isEmpty() || headquarters.isEmpty() || ceo.isEmpty() || totalSharesText.isEmpty() || sharePriceText.isEmpty()) {
            messageLabel.setText("Please fill in all fields!");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            try {
                int foundationYear = Integer.parseInt(foundationYearText);
                double totalShares = Double.parseDouble(totalSharesText);
                double sharePrice = Double.parseDouble(sharePriceText);

                // Create new Organization object
                Organization organization = new Organization(name, industry, foundationYear, headquarters, ceo, totalShares, sharePrice);

                // Save organization to CSV file
                FileHandler.creatOrganization(organization);

                // Optionally, add the organization to the static list
                OrganizationSelectionController.organizations.add(organization);

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
            Scene scene = new Scene(loader.load(), 400, 555);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        industryField.clear();
        foundationYearField.clear();
        headquartersField.clear();
        ceoField.clear();
        totalSharesField.clear();
        sharePriceField.clear();
    }
}
