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

    private void loadPage(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(  "LoginPage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterAdmin(ActionEvent event) throws IOException {
        loadPage(event, "/com/example/salaryproject/RegisterAdminPage.fxml");
    }

    @FXML
    private void handleRegisterOrganization(ActionEvent event) throws IOException {
        loadPage(event, "/com/example/salaryproject/RegisterOrganizationPage.fxml");
    }

    @FXML
    private void handleEnterOrganization(ActionEvent event) throws IOException {
        loadPage(event, "/com/example/salaryproject/OrganizationSelectionPage.fxml");
    }

    @FXML
    private void handleReports(ActionEvent event) throws IOException {
        loadPage(event, "/com/example/salaryproject/ReportPage.fxml");
    }
}
