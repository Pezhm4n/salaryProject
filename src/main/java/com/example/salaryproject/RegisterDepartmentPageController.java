package com.example.salaryproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterDepartmentPageController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField capacityField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Label messageLabel;

    private Organization selectedOrganization;
    private DepartmentSelectionController departmentSelectionController;

    public void setOrganization(Organization organization) {
        this.selectedOrganization = organization;
    }

    public void setDepartmentSelectionController(DepartmentSelectionController controller) {
        this.departmentSelectionController = controller;
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String name = nameField.getText();
        String capacityText = capacityField.getText();
        String description = descriptionField.getText();

        if (name.isEmpty() || capacityText.isEmpty() || description.isEmpty()) {
            messageLabel.setText("Please fill in all fields!");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (!isValidName(name)) {
            messageLabel.setText("Invalid department name!\nIt should not contain numbers and be less than 30 characters.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (!isValidCapacity(capacityText)) {
            messageLabel.setText("Invalid employee capacity!\nIt should be a number less than or equal to 100.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (!isValidDescription(description)) {
            messageLabel.setText("Invalid description!\nIt should be less than or equal to 120 characters.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            int capacity = Integer.parseInt(capacityText);

            // Create new Department object
            Department newDepartment = new Department(name, capacity, description);

            // Ensure selectedOrganization is not null
            if (selectedOrganization != null) {
                // Add department to the organization
                selectedOrganization.addDepartment(newDepartment);
                // Update the department list in the DepartmentSelectionPage
                departmentSelectionController.refreshDepartmentList();
                messageLabel.setText("Department registered successfully!");
                messageLabel.setStyle("-fx-text-fill: green;");
                clearFields();

                // Navigate back to the DepartmentSelectionPage
                handleBack(event);
            } else {
                messageLabel.setText("No organization selected!");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }


    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]{1,30}");
    }

    private boolean isValidCapacity(String capacity) {
        try {
            int value = Integer.parseInt(capacity);
            return value > 0 && value <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDescription(String description) {
        return description.length() <= 120;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DepartmentSelectionPage.fxml"));
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
        capacityField.clear();
        descriptionField.clear();
    }
}
