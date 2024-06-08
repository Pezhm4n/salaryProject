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

    private Organization selectedOrganization;

    public void setOrganization(Organization organization) {
        this.selectedOrganization = organization;
        departmentListView.setItems(organization.getDepartments());
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
    private void handleAddDepartment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("RegisterDepartmentPage.fxml"));
            Parent root = loader.load();
            RegisterDepartmentPageController controller = loader.getController();
            controller.setOrganization(selectedOrganization);
            controller.setDepartmentSelectionController(this);
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

    public void refreshDepartmentList() {
        Platform.runLater(() -> {
            departmentListView.setItems(null);
            departmentListView.setItems(selectedOrganization.getDepartments());
        });
    }
}
