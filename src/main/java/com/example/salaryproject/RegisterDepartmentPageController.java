package com.example.salaryproject;

import com.example.salaryproject.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

public class RegisterDepartmentPageController {
    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Employee> managerComboBox;

    @FXML
    private void initialize() {
        // Populate manager combo box with employees
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String name = nameField.getText();
        Employee manager = managerComboBox.getValue();

        // Create new Department object
        // Save department to file or database
        // Show success message and navigate back to OrganizationPage.fxml
    }
}
