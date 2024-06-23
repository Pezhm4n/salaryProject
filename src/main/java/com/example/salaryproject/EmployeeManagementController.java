package com.example.salaryproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeManagementController {

    @FXML
    private Label employeeNameLabel;

    private Employee currentEmployee;

    public void setEmployee(Employee employee) {
        this.currentEmployee = employee;
        employeeNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
    }

    @FXML
    private void showEmployeeInfo() {
        // نمایش اطلاعات کارمند
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Employee Info");
        alert.setHeaderText(null);
        alert.setContentText("Name: " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() +
                "\nNational ID: " + currentEmployee.getNationalId() +
                "\nDate of Birth: " + currentEmployee.getDateOfBirth() +
                "\nEmail: " + currentEmployee.getEmail() +
                "\nPhone Number: " + currentEmployee.getPhoneNumber());
        alert.showAndWait();
    }

    @FXML
    private void getReceivedSalary() {
        // دریافت حقوق دریافتی
        double receivedSalary = currentEmployee.getCurrentSalary();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Received Salary");
        alert.setHeaderText(null);
        alert.setContentText("Received Salary: " + receivedSalary);
        alert.showAndWait();
    }

    @FXML
    private void getCurrentSalaryRecord() {
        // دریافت رکورد حقوق فعلی
        SalaryRecord currentRecord = currentEmployee.getCurrentSalaryRecord();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Current Salary Record");
        alert.setHeaderText(null);
        alert.setContentText(currentRecord != null ? currentRecord.toString() : "No current salary record.");
        alert.showAndWait();
    }

    @FXML
    private void addNewSalaryRecord() {
        // افزودن رکورد حقوق جدید
        // اینجا باید کد مربوط به افزودن رکورد حقوق جدید را اضافه کنید
    }

    @FXML
    private void getAllSalaryRecords() {
        // دریافت تمامی رکوردهای حقوق
        StringBuilder records = new StringBuilder();
        for (SalaryRecord record : currentEmployee.getSalaryRecords()) {
            records.append(record.toString()).append("\n");
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("All Salary Records");
        alert.setHeaderText(null);
        alert.setContentText(records.toString());
        alert.showAndWait();
    }

    @FXML
    private void getCurrentDepartment() {
        // دریافت دپارتمان فعلی
        Department currentDepartment = currentEmployee.getCurrentDepartment();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Current Department");
        alert.setHeaderText(null);
        alert.setContentText(currentDepartment != null ? currentDepartment.getName() : "No current department.");
        alert.showAndWait();
    }

    @FXML
    private void changeDepartment() {
        // تغییر دپارتمان
        // اینجا باید کد مربوط به تغییر دپارتمان را اضافه کنید
    }

    @FXML
    private void getCurrentEmployeeType() {
        // دریافت نوع کارمند فعلی
        EmployeeType currentType = currentEmployee.getCurrentEmployeeType();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Current Employee Type");
        alert.setHeaderText(null);
        alert.setContentText(currentType != null ? currentType.toString() : "No current employee type.");
        alert.showAndWait();
    }

    @FXML
    private void changeEmployeeType() {
        // تغییر نوع کارمند
        // اینجا باید کد مربوط به تغییر نوع کارمند را اضافه کنید
    }

    @FXML
    private void getCurrentStatus() {
        // دریافت وضعیت فعلی
        Status currentStatus = currentEmployee.getCurrentStatus();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Current Status");
        alert.setHeaderText(null);
        alert.setContentText(currentStatus != null ? currentStatus.toString() : "No current status.");
        alert.showAndWait();
    }

    @FXML
    private void changeStatus() {
        // تغییر وضعیت
        // اینجا باید کد مربوط به تغییر وضعیت را اضافه کنید
    }

    private Department selectedDepartment;

    public void setDepartment(Department department) {
        this.selectedDepartment = department;
    }

    @FXML
    private void handleBack(ActionEvent event) {
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
