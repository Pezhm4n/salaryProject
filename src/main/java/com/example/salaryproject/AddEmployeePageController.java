package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
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
    private void handleAddEmployee(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        int nationalId = Integer.parseInt(nationalIdField.getText());
        LocalDate dateOfBirth = dateOfBirthPicker.getValue();
        String email = emailField.getText();
        int phoneNumber = Integer.parseInt(phoneNumberField.getText());

        // Create new Employee object
        // Save employee to file or database
        // Show success message and navigate back to DepartmentPage.fxml
    }
}
