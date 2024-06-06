package com.example.salaryproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class EmployeeReportPageController {

    private ArrayList<Employee> employees;

    @FXML
    private void generateEmployeePerformanceReport() {
        // Implement the logic to generate the employee performance report
    }

    @FXML
    private void employeeSalaryRecordsReport() {
        // Implement the logic to generate the employee salary records report
    }

    @FXML
    private void generateEmployeeTypeReport() {
        // Implement the logic to generate the employee type report
    }

    @FXML
    private void generateEmplyeeTypeChangeReport() {
        // Implement the logic to generate the employee type change report
    }

    @FXML
    private void generateEmployeeStatusReport() {
        // Implement the logic to generate the employee status report
    }

    @FXML
    private void generateEmployeeStatusChangeReport() {
        // Implement the logic to generate the employee status change report
    }

    @FXML
    private void generateEmployeePromotionReport() {
        // Implement the logic to generate the employee promotion report
    }

    // Add more employee report methods as needed

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ReportPage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }
}
