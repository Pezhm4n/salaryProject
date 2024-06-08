package com.example.salaryproject;

import javafx.application.Platform;
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

public class DepartmentSelectionController {

    @FXML
    private ListView<Department> departmentListView;

    private Organization organization;

    public void setOrganization(Organization organization) {
        this.organization = organization;
        if (organization != null) {
            departmentListView.setItems(organization.getDepartments());
        } else {
            // Handle the case where organization is null
            System.out.println("Organization is null");
        }
    }

    @FXML
    private void initialize() {
        departmentListView.setCellFactory(param -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                if (empty || department == null) {
                    setText(null);
                } else {
                    setText(department.getName());
                }
            }
        });
    }

    @FXML
    private void handleDepartmentSelection(ActionEvent event) {
        Department selectedDepartment = departmentListView.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("DepartmentPage.fxml"));
                Parent root = loader.load();
                DepartmentPageController controller = loader.getController();
                controller.setDepartment(selectedDepartment);
                Scene scene = new Scene(root, 400, 555);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("OrganizationPage.fxml"));
            Parent root = loader.load();
            OrganizationPageController controller = loader.getController();
            controller.setOrganization(organization); // Pass the current organization to the OrganizationPageController
            Scene scene = new Scene(root, 400, 555);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshDepartmentList() {
        if (organization != null) {
            Platform.runLater(() -> {
                departmentListView.setItems(null);
                departmentListView.setItems(organization.getDepartments());
            });
        }
    }
}