package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;


public class DepartmentReportPageController {

    private Department department;


    @FXML
    private void generateDepartmentPerformanceReport() {
        if (department == null) {
            showErrorAlert("Department is not set. Please set the department first.");
            return;
        }

        int fixedSalaryCount = 0;
        int commissionPlusFixedSalaryCount = 0;
        int commissionSalaryTotalSales = 0;
        int commissionPlusFixedSalaryTotalSales = 0;
        int hourlySalaryTotalHours = 0;
        int activeCount = 0;
        int paidLeaveCount = 0;
        int unpaidLeaveCount = 0;
        int terminatedCount = 0;

        for (Employee emp : department.getEmployees()) {
            SalaryRecord record = emp.getCurrentSalaryRecord();
            if (record instanceof FixedSalary) {
                fixedSalaryCount++;
            } else if (record instanceof CommissionPlusFixedSalary) {
                commissionPlusFixedSalaryCount++;
                commissionPlusFixedSalaryTotalSales += ((CommissionPlusFixedSalary) record).getTotalSales();
            } else if (record instanceof CommissionSalary) {
                commissionSalaryTotalSales += ((CommissionSalary) record).getTotalSales();
            } else if (record instanceof HourlySalary) {
                hourlySalaryTotalHours += ((HourlySalary) record).getHoursWorked();
            }

            switch (record.getStatus()) {
                case ACTIVE:
                    activeCount++;
                    break;
                case PAID_LEAVE:
                    paidLeaveCount++;
                    break;
                case UNPAID_LEAVE:
                    unpaidLeaveCount++;
                    break;
                case TERMINATED:
                    terminatedCount++;
                    break;
            }
        }

        // Create the X and Y axes for the bar charts
        CategoryAxis xAxis1 = new CategoryAxis();
        NumberAxis yAxis1 = new NumberAxis();
        CategoryAxis xAxis2 = new CategoryAxis();
        NumberAxis yAxis2 = new NumberAxis();

        // Create the BarCharts
        BarChart<String, Number> typeChart = new BarChart<>(xAxis1, yAxis1);
        typeChart.setTitle("Employee Count by Salary Type");

        BarChart<String, Number> statusChart = new BarChart<>(xAxis2, yAxis2);
        statusChart.setTitle("Employee Count by Status");

        // Create the data series for salary types
        XYChart.Series<String, Number> typeSeries = new XYChart.Series<>();
        typeSeries.setName("Employee Count");
        typeSeries.getData().add(new XYChart.Data<>("Fixed Salary", fixedSalaryCount));
        typeSeries.getData().add(new XYChart.Data<>("Commission Plus Fixed Salary", commissionPlusFixedSalaryCount));

        // Create the data series for statuses
        XYChart.Series<String, Number> statusSeries = new XYChart.Series<>();
        statusSeries.setName("Employee Count");
        statusSeries.getData().add(new XYChart.Data<>("Active", activeCount));
        statusSeries.getData().add(new XYChart.Data<>("Paid Leave", paidLeaveCount));
        statusSeries.getData().add(new XYChart.Data<>("Unpaid Leave", unpaidLeaveCount));
        statusSeries.getData().add(new XYChart.Data<>("Terminated", terminatedCount));

        // Add the data series to the charts
        typeChart.getData().add(typeSeries);
        statusChart.getData().add(statusSeries);

        // Display the information and charts in an alert
        showDepartmentReportAlert("Department Performance Report", fixedSalaryCount, commissionPlusFixedSalaryCount,
                commissionSalaryTotalSales, commissionPlusFixedSalaryTotalSales, hourlySalaryTotalHours, activeCount,
                paidLeaveCount, unpaidLeaveCount, terminatedCount, typeChart, statusChart);
    }

    private void showDepartmentReportAlert(String title, int fixedSalaryCount, int commissionPlusFixedSalaryCount,
                                           int commissionSalaryTotalSales, int commissionPlusFixedSalaryTotalSales,
                                           int hourlySalaryTotalHours, int activeCount, int paidLeaveCount,
                                           int unpaidLeaveCount, int terminatedCount, BarChart<String, Number> typeChart,
                                           BarChart<String, Number> statusChart) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Title Label
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setPadding(new Insets(10, 0, 10, 0));

        // Information Labels
        Label infoLabel = new Label(String.format(
                "Fixed Salary employees: %d\nCommission Plus Fixed Salary employees: %d\nTotal Sales (Commission Salary): %s\n"
                        + "Total Sales (Commission Plus Fixed Salary): %s\nTotal Hours (Hourly Salary): %d\n"
                        + "Active Employees Count: %d\nPaid Leave Employees Count: %d\nUnpaid Leave Employees Count: %d\nTerminated Employees Count: %d",
                fixedSalaryCount, commissionPlusFixedSalaryCount, formatNumber(commissionSalaryTotalSales),
                formatNumber(commissionPlusFixedSalaryTotalSales), hourlySalaryTotalHours, activeCount, paidLeaveCount, unpaidLeaveCount,
                terminatedCount));
        infoLabel.setPadding(new Insets(10));

        // Create a VBox to hold the information and charts
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        // Add the information and charts to the VBox
        vBox.getChildren().addAll(titleLabel, infoLabel, typeChart, statusChart);

        // Create a ScrollPane to hold the VBox
        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setPrefSize(600, 700);

        // Set the ScrollPane as the content of the alert
        alert.getDialogPane().setContent(scrollPane);
        alert.getDialogPane().setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        alert.showAndWait();
    }

    @FXML
    private void generateDepartmentActionReport() {
        if (department == null) {
            showErrorAlert("Department is not set. Please set the department first.");
            return;
        }

        int totalEmployees = department.getEmployees().size();
        long fixedSalaryCount = department.getEmployees().stream().filter(emp -> emp.getCurrentSalaryRecord() instanceof FixedSalary).count();
        long hourlySalaryCount = department.getEmployees().stream().filter(emp -> emp.getCurrentSalaryRecord() instanceof HourlySalary).count();
        long commissionSalaryCount = department.getEmployees().stream().filter(emp -> emp.getCurrentSalaryRecord() instanceof CommissionSalary).count();
        long commissionPlusFixedSalaryCount = department.getEmployees().stream().filter(emp -> emp.getCurrentSalaryRecord() instanceof CommissionPlusFixedSalary).count();

        // Create the X and Y axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Employee Type");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Employees");

        // Create the BarChart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Employee Distribution by Salary Type");

        // Create the data series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Employees");

        // Add data to the series
        series.getData().add(new XYChart.Data<>("Total Employees", totalEmployees));
        series.getData().add(new XYChart.Data<>("Fixed Salary", fixedSalaryCount));
        series.getData().add(new XYChart.Data<>("Hourly Salary", hourlySalaryCount));
        series.getData().add(new XYChart.Data<>("Commission Salary", commissionSalaryCount));
        series.getData().add(new XYChart.Data<>("Commission Plus Fixed Salary", commissionPlusFixedSalaryCount));

        // Add the data series to the chart
        barChart.getData().add(series);

        // Show the bar chart in an alert
        showBarChartAlert("Department Action Report", barChart);
    }

    private void showBarChartAlert(String title, BarChart<String, Number> barChart) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Title Label
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setPadding(new Insets(10, 0, 10, 0));

        // Create a VBox to hold the chart
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        // Add the chart to the VBox
        vBox.getChildren().addAll(titleLabel, barChart);

        // Set the VBox as the content of the alert
        alert.getDialogPane().setContent(vBox);
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

    @FXML
    private void generateManagersReport() {
        if (department == null) {
            showErrorAlert("Department is not set. Please set the department first.");
            return;
        }

        // Collect current and former managers
        ObservableList<Employee> currentManagers = FXCollections.observableArrayList();
        Employee currentManager = department.getCurrentManager();
        if (currentManager != null) {
            currentManagers.add(currentManager);
        }

        ObservableList<Employee> formerManagers = department.getFormerManagers();

        // Create a VBox to hold the information
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        // Add current managers information
        if (!currentManagers.isEmpty()) {
            Label currentManagersLabel = new Label("Current Managers:");
            currentManagersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            vBox.getChildren().add(currentManagersLabel);

            for (Employee manager : currentManagers) {
                vBox.getChildren().add(createManagerInfoBox(manager));
            }
        }

        // Add former managers information
        if (!formerManagers.isEmpty()) {
            Label formerManagersLabel = new Label("Former Managers:");
            formerManagersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            vBox.getChildren().add(formerManagersLabel);

            for (Employee manager : formerManagers) {
                vBox.getChildren().add(createManagerInfoBox(manager));
            }
        }

        // Create a ScrollPane and set its content to the VBox
        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(280, 420);

        // Show the information in an alert
        showManagersReportAlert("Managers Report", scrollPane);
    }

    private void showManagersReportAlert(String title, ScrollPane content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Title Label
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setPadding(new Insets(12, 0, 12, 0));

        // Create a VBox to hold the title and content
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        // Add the title and content to the VBox
        vBox.getChildren().addAll(titleLabel, content);

        // Set the VBox as the content of the alert
        alert.getDialogPane().setContent(vBox);
        alert.getDialogPane().setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        alert.showAndWait();
    }

    private VBox createManagerInfoBox(Employee manager) {
        VBox managerBox = new VBox();
        managerBox.setPadding(new Insets(5));
        managerBox.setSpacing(5);
        managerBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f9f9f9;");

        Label nameLabel = new Label("Name: " + manager.getFirstName() + manager.getLastName() );
        Label nationalIdLabel = new Label("National Id: " + manager.getNationalId());
        Label emailLabel = new Label("Email: " + manager.getEmail());
        Label phoneNumberLabel = new Label("Phone Number: " + manager.getPhoneNumber());
        Label departmentLabel = new Label("Department: " + (manager.getCurrentDepartment() != null ? manager.getCurrentDepartment().getName() : "N/A"));
        Label startDateLabel = new Label("Start Date: " + (manager.getCurrentSalaryRecord() != null ? manager.getCurrentSalaryRecord().getStartDate().toString() : "N/A"));
        Label endDateLabel = new Label("End Date: " + (manager.getCurrentSalaryRecord() != null && manager.getCurrentSalaryRecord().getEndDate() != null ? manager.getCurrentSalaryRecord().getEndDate().toString() : "N/A"));
        Label statusLabel = new Label("Status: " + (manager.getCurrentSalaryRecord() != null ? manager.getCurrentSalaryRecord().getStatus().toString() : "N/A"));

        managerBox.getChildren().addAll(nameLabel, nationalIdLabel, emailLabel, phoneNumberLabel, departmentLabel, startDateLabel, endDateLabel, statusLabel);
        return managerBox;
    }

    @FXML
    private void generateEmployeesReport() {
        if (department == null) {
            showErrorAlert("Department is not set. Please set the department first.");
            return;
        }

        // Create a VBox to hold the information
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        // Add current employees information
        Label currentEmployeesLabel = new Label("Current Employees:");
        currentEmployeesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vBox.getChildren().add(currentEmployeesLabel);

        ObservableList<Employee> currentEmployees = department.getEmployees();
        for (Employee employee : currentEmployees) {
            vBox.getChildren().add(createEmployeeInfoBox(employee));
        }

        // Add former employees information
        Label formerEmployeesLabel = new Label("Former Employees:");
        formerEmployeesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vBox.getChildren().add(formerEmployeesLabel);

        ObservableList<Employee> formerEmployees = department.getFormerEmployees();
        for (Employee employee : formerEmployees) {
            vBox.getChildren().add(createEmployeeInfoBox(employee));
        }

        // Create a ScrollPane and set its content to the VBox
        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(280, 420);

        // Show the information in an alert
        showManagersReportAlert("Employees Report", scrollPane);
    }

    private VBox createEmployeeInfoBox(Employee employee) {
        VBox employeeBox = new VBox();
        employeeBox.setPadding(new Insets(5));
        employeeBox.setSpacing(5);
        employeeBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f9f9f9;");

        Label nameLabel = new Label("Name: " + employee.getFirstName() + " " + employee.getLastName());
        Label nationalIdLabel = new Label("National Id: " + employee.getNationalId());
        Label emailLabel = new Label("Email: " + employee.getEmail());
        Label phoneNumberLabel = new Label("Phone Number: " + employee.getPhoneNumber());
        Label departmentLabel = new Label("Department: " + (employee.getCurrentDepartment() != null ? employee.getCurrentDepartment().getName() : "N/A"));
        Label employeeTypeLabel = new Label("Employee Type: " + (employee.getCurrentEmployeeType() != null ? employee.getCurrentEmployeeType().toString() : "N/A"));
        Label startDateLabel = new Label("Start Date: " + (employee.getCurrentSalaryRecord() != null ? employee.getCurrentSalaryRecord().getStartDate().toString() : "N/A"));
        Label endDateLabel = new Label("End Date: " + (employee.getCurrentSalaryRecord() != null && employee.getCurrentSalaryRecord().getEndDate() != null ? employee.getCurrentSalaryRecord().getEndDate().toString() : "N/A"));
        Label statusLabel = new Label("Status: " + (employee.getCurrentStatus() != null ? employee.getCurrentStatus().toString() : "N/A"));

        employeeBox.getChildren().addAll(nameLabel, nationalIdLabel, emailLabel, phoneNumberLabel, departmentLabel, employeeTypeLabel, startDateLabel, endDateLabel, statusLabel);
        return employeeBox;
    }


    @FXML
    private void handleBack(ActionEvent event) {
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

    public void setDepartment(Department departments) {
        this.department = departments;
    }

    private String formatNumber(double number) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return formatter.format(number);
    }
}

