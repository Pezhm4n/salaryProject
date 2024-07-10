package com.example.salaryproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class DepartmentSelectionController {

    @FXML
    private ListView<Department> departmentListView;

    @FXML
    private Label statusLabel;

    private Organization organization;
    private Department selectedDepartment;

    public void setOrganization(Organization organization) {
        this.organization = organization;
        if (organization != null) {
            departmentListView.setItems(organization.getDepartments());
        } else {
            // Handle the case where organization is null
            System.out.println("Organization is null");
        }
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
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
        selectedDepartment = departmentListView.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            try {
                // انتقال به صفحه دپارتمان با اطلاعات بارگذاری شده
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/salaryproject/DepartmentPage.fxml"));
                Parent root = loader.load();
                DepartmentPageController controller = loader.getController();
                controller.setDepartment(selectedDepartment); // انتقال دپارتمان انتخابی به کنترلر جدید
                controller.setOrganization(organization); // انتقال سازمان به کنترلر جدید
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
    private void handleDeleteDepartment(ActionEvent event) {
        selectedDepartment = departmentListView.getSelectionModel().getSelectedItem();
        Department selectedDepartment = departmentListView.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            // نمایش دیالوگ تایید حذف
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Department");
            alert.setHeaderText("Are you sure you want to delete the department " + selectedDepartment.getName() + "?");
            alert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // فراخوانی متد حذف دپارتمان
                WriteToCSV.deleteDepartment(selectedDepartment);
                organization.getDepartments().remove(selectedDepartment);
                refreshDepartmentList();
                statusLabel.setText("Department " + selectedDepartment.getName() + " deleted successfully.");
            }
        } else {
            statusLabel.setText("Please select a department to delete.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/salaryproject/OrganizationPage.fxml"));
            Parent root = loader.load();
            OrganizationPageController controller = loader.getController();
            controller.setOrganization(organization); // انتقال سازمان جاری به OrganizationPageController
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
