package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    }

    @FXML
    private void handleGetEmployees(ActionEvent event) {
        // Show list of employees in the department
    }

    @FXML
    private void handleGetEmployeeByNationalId(ActionEvent event) {
        // Show dialog to enter national ID
        // Search for employee by national ID and display information
    }

    @FXML
    private void handleMergeInto(ActionEvent event) {
        // Show list of departments and handle selection
        // Merge current department into selected department
    }

    @FXML
    private void handleSplitInto(ActionEvent event) {
        // Show dialog to enter new department name
        // Split current department into new department
    }

    @FXML
    private void handleActivate(ActionEvent event) {
        // Activate the department
    }

    @FXML
    private void handleDeactivate(ActionEvent event) {
        // Deactivate the department
    }
}
