package com.example.salaryproject;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class EmployeeReportPageController {

    private Employee employee;

    @FXML
    private void generateEmployeePerformanceReport() {
        if (employee == null) {
            showErrorAlert("Employee is not set. Please set the employee first.");
            return;
        }

        // Performance Report Logic
        double totalSalary = employee.getCurrentSalary();
        int totalHoursWorked = employee.getSalaryRecords().stream()
                .filter(record -> record instanceof HourlySalary)
                .mapToInt(record -> (int) ((HourlySalary) record).getHoursWorked())
                .sum();
        int totalSales = employee.getSalaryRecords().stream()
                .filter(record -> record instanceof CommissionSalary)
                .mapToInt(record -> (int) ((CommissionSalary) record).getTotalSales())
                .sum();

        // Display the information
        showEmployeeReportAlert("Employee Performance Report", totalSalary, totalHoursWorked, totalSales);
    }

    @FXML
    private void employeeSalaryRecordsReport() {
        if (employee == null) {
            showErrorAlert("Employee is not set. Please set the employee first.");
            return;
        }

        // Salary Records Report Logic
        ObservableList<SalaryRecord> salaryRecords = employee.getSalaryRecords();

        StringBuilder records = new StringBuilder();
        salaryRecords.forEach(record -> records.append(record.toString()).append("\n\n"));

        // Display the information
        showTextReportAlert("Employee Salary Records Report", records.toString());
    }

    @FXML
    private void generateEmployeeTypeReport() {
        if (employee == null) {
            showErrorAlert("Employee is not set. Please set the employee first.");
            return;
        }

        // Employee Type Report Logic
        EmployeeType currentType = employee.getCurrentEmployeeType();

        showTextReportAlert("Employee Type Report", "Current Employee Type: " + currentType);
    }

    @FXML
    private void generateEmployeeTypeChangeReport() {
        if (employee == null) {
            showErrorAlert("Employee is not set. Please set the employee first.");
            return;
        }

        ObservableList<SalaryRecord> salaryRecords = employee.getSalaryRecords();

        StringBuilder typeChanges = new StringBuilder();
        salaryRecords.forEach(record -> typeChanges.append("Date: ")
                .append(record.getStartDate())
                .append(" - Type: ")
                .append(record.getType())
                .append("\n"));

        showTextReportAlert("Employee Type Change Report", typeChanges.toString());
    }

    @FXML
    private void generateEmployeeStatusReport() {
        if (employee == null) {
            showErrorAlert("Employee is not set. Please set the employee first.");
            return;
        }

        Status currentStatus = employee.getCurrentStatus();

        showTextReportAlert("Employee Status Report", "Current Employee Status: " + currentStatus);
    }

    @FXML
    private void generateEmployeeStatusChangeReport() {
        if (employee == null) {
            showErrorAlert("Employee is not set. Please set the employee first.");
            return;
        }

        ObservableList<SalaryRecord> salaryRecords = employee.getSalaryRecords();

        StringBuilder statusChanges = new StringBuilder();
        salaryRecords.forEach(record -> statusChanges.append("Date: ")
                .append(record.getStartDate())
                .append(" - Status: ")
                .append(record.getStatus())
                .append("\n"));

        showTextReportAlert("Employee Status Change Report", statusChanges.toString());
    }

    @FXML
    private void generateEmployeePromotionReport() {
        if (employee == null) {
            showErrorAlert("Employee is not set. Please set the employee first.");
            return;
        }

        // Employee Promotion Report Logic
        ObservableList<SalaryRecord> salaryRecords = employee.getSalaryRecords();

        StringBuilder promotions = new StringBuilder();
        salaryRecords.forEach(record -> {
            if (record instanceof ManagerSalary) {
                promotions.append("Promoted to Manager on: ")
                        .append(record.getStartDate())
                        .append("\n");
            }
        });

        showTextReportAlert("Employee Promotion Report", promotions.toString());
    }

    private void showEmployeeReportAlert(String title, double totalSalary, int totalHoursWorked, int totalSales) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setPadding(new Insets(10, 0, 10, 0));

        Label infoLabel = new Label(String.format(
                "Total Salary: %s\nTotal Hours Worked: %d\nTotal Sales: %s",
                formatNumber(totalSalary), totalHoursWorked, formatNumber(totalSales)));
        infoLabel.setPadding(new Insets(10));

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        vBox.getChildren().addAll(titleLabel, infoLabel);

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setPrefSize(300, 400);

        alert.getDialogPane().setContent(scrollPane);
        alert.getDialogPane().setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        alert.showAndWait();
    }

    private void showTextReportAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setPadding(new Insets(10, 0, 10, 0));

        Label contentLabel = new Label(content);
        contentLabel.setPadding(new Insets(10));

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        vBox.getChildren().addAll(titleLabel, contentLabel);

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setPrefSize(300, 400);

        alert.getDialogPane().setContent(scrollPane);
        alert.getDialogPane().setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        alert.showAndWait();
    }



    private void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    @FXML
    public void handleBack(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ReportPage.fxml"));
            Scene scene = new Scene(loader.load(), 400, 555);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatNumber(double number) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return formatter.format(number);
    }
}
