package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jdk.internal.classfile.Label;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class OrganizationPageController {
    @FXML
    private Label organizationNameLabel;

    @FXML
    private void initialize() {
        // Set organization name label
    }

    @FXML
    private void handleRegisterDepartment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegisterDepartmentPage.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleEnterDepartment(ActionEvent event) throws IOException {
        // Show list of departments and handle selection
        // Load DepartmentPage.fxml
    }
}
