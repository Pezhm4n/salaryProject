package com.example.salaryproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class OrganizationPageController {
    @FXML
    private Label organizationNameLabel;

    private Organization selectedOrganization;

    public void setOrganization(Organization organization) {
        this.selectedOrganization = organization;
        organizationNameLabel.setText(organization.getName());
    }

    @FXML
    private void handleRegisterDepartment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RegisterDepartmentPage.fxml"));
        Parent root = loader.load();
        RegisterDepartmentPageController controller = loader.getController();
        controller.setOrganization(selectedOrganization);
        Scene scene = new Scene(root, 400, 555);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleEnterDepartment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DepartmentSelectionPage.fxml"));
            Parent root = loader.load();
            DepartmentSelectionController controller = loader.getController();
            controller.setOrganization(selectedOrganization);
            Scene scene = new Scene(root, 400, 555);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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