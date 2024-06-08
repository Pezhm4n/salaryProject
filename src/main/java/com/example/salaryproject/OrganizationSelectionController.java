package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class OrganizationSelectionController {
    @FXML
    private ListView<Organization> organizationListView;

    public static ObservableList<Organization> organizations = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        organizationListView.setItems(organizations);
        organizationListView.setCellFactory(param -> new ListCell<Organization>() {
            @Override
            protected void updateItem(Organization organization, boolean empty) {
                super.updateItem(organization, empty);
                if (empty || organization == null) {
                    setText(null);
                } else {
                    setText(organization.getName());
                }
            }
        });
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/salaryproject/MainPage.fxml"));
            Scene scene = new Scene(loader.load(), 400, 555);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrganizationSelection(ActionEvent event) {
        Organization selectedOrganization = organizationListView.getSelectionModel().getSelectedItem();
        if (selectedOrganization != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/salaryproject/OrganizationPage.fxml"));
                Parent root = loader.load();
                OrganizationPageController controller = loader.getController();
                controller.setOrganization(selectedOrganization);
                Scene scene = new Scene(root, 400, 555);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}