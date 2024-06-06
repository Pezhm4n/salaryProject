package com.example.salaryproject;

import com.example.salaryproject.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterDepartmentPageController {
    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Employee> managerComboBox;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        // Populate manager combo box with employees
        // ...
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String name = nameField.getText();
        Employee manager = managerComboBox.getValue();

        if (name.isEmpty() || manager == null) {
            messageLabel.setText("Please fill in all fields!");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            // Create new Department object
            // Save department to file or database
            messageLabel.setText("Department registered successfully!");
            messageLabel.setStyle("-fx-text-fill: green;");
            clearFields();
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("OrganizationPage.fxml"));
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
        managerComboBox.getSelectionModel().clearSelection();
    }
}
