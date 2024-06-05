package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class DepartmentPageController {
    @FXML
    private Label departmentNameLabel;

    @FXML
    private void initialize() {
        // Set department name label
        // ...
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("OrganizationSelectionPage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddEmployee(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddEmployeePage.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleRemoveEmployee(ActionEvent event) {
        // Show list of employees and handle selection
        // Remove selected employee from department
        // ...
        showAlert("Employee removed successfully!");
    }

    @FXML
    private void handleGetEmployees(ActionEvent event) {
        // Show list of employees in the department
        // ...
        showAlert("Employees retrieved successfully!");
    }

    @FXML
    private void handleGetEmployeeByNationalId(ActionEvent event) {
        // Show dialog to enter national ID
        // Search for employee by national ID and display information
        // ...
        showAlert("Employee information retrieved successfully!");
    }

    @FXML
    private void handleMergeInto(ActionEvent event) {
        // Show list of departments and handle selection
        // Merge current department into selected department
        // ...
        showAlert("Department merged successfully!");
    }

    @FXML
    private void handleSplitInto(ActionEvent event) {
        // Show dialog to enter new department name
        // Split current department into new department
        // ...
        showAlert("Department split successfully!");
    }

    @FXML
    private void handleActivate(ActionEvent event) {
        // Activate the department
        // ...
        showAlert("Department activated successfully!");
    }

    @FXML
    private void handleDeactivate(ActionEvent event) {
        // Deactivate the department
        // ...
        showAlert("Department deactivated successfully!");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
