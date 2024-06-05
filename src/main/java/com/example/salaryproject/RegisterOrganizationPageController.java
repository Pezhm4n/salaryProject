package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


import javafx.event.ActionEvent;


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
    private void handleRegister(ActionEvent event) {
        String name = nameField.getText();
        double totalShares = Double.parseDouble(totalSharesField.getText());
        double sharesAllocated = Double.parseDouble(sharesAllocatedField.getText());
        double sharePrice = Double.parseDouble(sharePriceField.getText());

        // Create new Organization object
        // Save organization to file or database
        // Show success message and navigate back to MainPage.fxml
    }

}
