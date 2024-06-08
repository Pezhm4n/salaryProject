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

public class OrganizationPageController {
    @FXML
    private Label organizationNameLabel;

    private Organization selectedOrganization;

    @FXML
    private void initialize() {
        // Set organization name label
        if (selectedOrganization != null) {
            organizationNameLabel.setText(selectedOrganization.getName());
        }
    }

    public void setOrganization(Organization organization) {
        this.selectedOrganization = organization;
    }

    @FXML
    private void handleRegisterDepartment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegisterDepartmentPage.fxml"));
        Scene scene = new Scene(root , 400 , 555);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleEnterDepartment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("DepartmentSelectionPage.fxml"));
        Scene scene = new Scene(loader.load(), 400, 555);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("OrganizationSelectionPage.fxml"));
            Scene scene = new Scene(loader.load(), 400, 555);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}