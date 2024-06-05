package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class AddEmployeePageController {
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField nationalIdField;

    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleAddEmployee(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String nationalIdText = nationalIdField.getText();
        LocalDate dateOfBirth = dateOfBirthPicker.getValue();
        String email = emailField.getText();
        String phoneNumberText = phoneNumberField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || nationalIdText.isEmpty() || dateOfBirth == null || email.isEmpty() || phoneNumberText.isEmpty()) {
            messageLabel.setText("Please fill in all fields!");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            try {
                int nationalId = Integer.parseInt(nationalIdText);
                int phoneNumber = Integer.parseInt(phoneNumberText);

                // Create new Employee object
                Employee newEmployee = new Employee(firstName, lastName, nationalId, dateOfBirth, email, phoneNumber);
                // Save employee to file or database
                // ...
                messageLabel.setText("Employee added successfully!");
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
            loader.setLocation(getClass().getResource("DepartmentPage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        nationalIdField.clear();
        dateOfBirthPicker.setValue(null);
        emailField.clear();
        phoneNumberField.clear();
    }
}
