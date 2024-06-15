package com.example.salaryproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Comparator;

public class DepartmentPageController {
    @FXML
    private Label departmentNameLabel;

    private Department selectedDepartment;

    public void setDepartment(Department department) {
        this.selectedDepartment = department;
        departmentNameLabel.setText(department.getName());
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DepartmentSelectionPage.fxml"));
            Parent root = loader.load();
            DepartmentSelectionController controller = loader.getController();
            controller.setOrganization(selectedDepartment.getOrganization());
            Scene scene = new Scene(root, 400, 555);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddEmployee(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AddEmployeePage.fxml"));
            Parent root = loader.load();
            AddEmployeePageController controller = loader.getController();
            controller.setDepartment(selectedDepartment);  // انتقال selectedDepartment به کنترلر AddEmployeePageController
            Scene scene = new Scene(root, 400, 555);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveEmployee(ActionEvent event) {
        // Show list of employees and handle selection
        // Remove selected employee from department
        // ...
        showAlert("Employee removed successfully!");
    }

    @FXML
    private void handleGetEmployees(ActionEvent event) {
        if (selectedDepartment != null) {
            ObservableList<Employee> employees = selectedDepartment.getEmployees();
            if (!employees.isEmpty()) {
                // مرتب سازی کارمندان بر اساس نام و نام خانوادگی
                employees.sort(Comparator.comparing(Employee::getFirstName).thenComparing(Employee::getLastName));

                // ایجاد TableView برای نمایش لیست کارمندان
                TableView<Employee> tableView = new TableView<>();
                tableView.setItems(employees);

                // ایجاد ستون‌های جدول
                TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
                nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));

                TableColumn<Employee, String> phoneColumn = new TableColumn<>("Phone Number");
                phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPhoneNumber())));

                TableColumn<Employee, String> nationalIdColumn = new TableColumn<>("National ID");
                nationalIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNationalId())));

                TableColumn<Employee, String> salaryTypeColumn = new TableColumn<>("Salary Type");
                salaryTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurrentEmployeeType().toString()));

                TableColumn<Employee, String> dateAddedColumn = new TableColumn<>("Date Added");
                dateAddedColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedDate()));

                tableView.getColumns().addAll(nameColumn, phoneColumn, nationalIdColumn, salaryTypeColumn, dateAddedColumn);

                // نمایش TableView در یک Dialog
                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Employees");
                dialog.getDialogPane().setContent(tableView);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.showAndWait();
            } else {
                showAlert("No employees found in the department.");
            }
        } else {
            showAlert("No department selected.");
        }
    }




    @FXML
    private void handleGetEmployeeByNationalId(ActionEvent event) {
        // Show dialog to enter national ID
        // Search for employee by national ID and display information
        // ...
        showAlert("Employee information retrieved successfully!");
    }

    @FXML
    private void handleMergeInto(ActionEvent event) {
        // Show list of departments and handle selection
        // Merge current department into selected department
        // ...
        showAlert("Department merged successfully!");
    }

    @FXML
    private void handleSplitInto(ActionEvent event) {
        // Show dialog to enter new department name
        // Split current department into new department
        // ...
        showAlert("Department split successfully!");
    }

    @FXML
    private void handleActivate(ActionEvent event) {
        // Activate the department
        // ...
        showAlert("Department activated successfully!");
    }

    @FXML
    private void handleDeactivate(ActionEvent event) {
        // Deactivate the department
        // ...
        showAlert("Department deactivated successfully!");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}