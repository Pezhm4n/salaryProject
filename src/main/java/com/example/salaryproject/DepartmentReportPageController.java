package com.example.salaryproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class DepartmentReportPageController {

    private ArrayList<Department> departments;

    @FXML
    private void generateDepartmentFinancialSummaryReport() {
        // Implement the logic to generate the department financial summary report
    }

    @FXML
    private void generateDepartmentPerformanceReport() {
        // Implement the logic to generate the department performance report
    }

    @FXML
    private void generateDepartmentActionReport() {
        // Implement the logic to generate the department action report
    }

    @FXML
    private void generateManagersReport() {
        // Implement the logic to generate the managers report
    }

    // Add more department report methods as needed

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

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }
}
