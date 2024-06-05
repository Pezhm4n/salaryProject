package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class MainPageController {
    @FXML
    private void handleRegisterAdmin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegisterAdminPage.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleRegisterOrganization(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegisterOrganizationPage.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleEnterOrganization(ActionEvent event) throws IOException {
        // Show list of organizations and handle selection
        // Load OrganizationPage.fxml
    }

    @FXML
    private void handleReports(ActionEvent event) throws IOException {
        // Load ReportsPage.fxml
    }
}
