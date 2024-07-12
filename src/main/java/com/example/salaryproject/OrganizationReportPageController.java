package com.example.salaryproject;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrganizationReportPageController {

    private Organization organization;
    private ObservableList<Employee> allEmployees; // Define allEmployees field

    // Constructor or initialize method to initialize allEmployees
    public OrganizationReportPageController() {
        // Initialize allEmployees here, perhaps retrieve from organization or database
        allEmployees = FXCollections.observableArrayList();
    }

    @FXML
    private void generateOrganizationFinancialSummaryReport() {
        if (organization == null) {
            showErrorAlert("Organization is not set. Please set the organization first.");
            return;
        }

        TextFlow report = new TextFlow();
        report.setPadding(new Insets(20));

        addTextToReport(report, "Organization Name: " + organization.getName() + "\n\n\n");
        addTextToReport(report, "Industry: " + organization.getIndustry() + "\n\n");
        addTextToReport(report, "Foundation Year: " + organization.getFoundationYear() + "\n\n");
        addTextToReport(report, "Headquarters: " + organization.getHeadquarters() + "\n\n");
        addTextToReport(report, "CEO: " + organization.getCEO() + "\n\n");

        // Adding bold text for totalShares and sharePrice
        addBoldTextToReport(report, "Total Shares: " + organization.getTotalShares() + "\n\n");
        addBoldTextToReport(report, "Share Price: " + organization.getSharePrice() + "\n\n");

        addTextToReport(report, "Market Capitalization: " + organization.calculateMarketCapitalization() + "\n\n");
        addTextToReport(report, "Total Revenue: " + organization.calculateTotalRevenue() + "\n\n");

        addBoldTextToReport(report, "Total Costs: " + organization.calculateTotalCosts() + "\n\n");
        addBoldTextToReport(report, "Total Budget: " + organization.calculateTotalBudget() + "\n\n");

        showScrollableInformationAlert("Organization Financial Summary Report", report);
    }


    private void addBoldTextToReport(TextFlow report, String text) {
        Text boldText = new Text(text);
        boldText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        report.getChildren().add(boldText);
    }

    private void showScrollableInformationAlert(String title, TextFlow content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Title Label
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setPadding(new Insets(10, 0, 10, 0));

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportWidth(400);
        scrollPane.setPrefViewportHeight(440);

        // VBox Layout
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(titleLabel, scrollPane);

        alert.getDialogPane().setContent(vBox);
        alert.getDialogPane().setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        alert.showAndWait();
    }



    @FXML
    private void showDepartments() {
        if (organization == null) {
            showErrorAlert("Organization is not set. Please set the organization first.");
            return;
        }

        int totalEmployees = organization.getDepartments().stream().mapToInt(dept -> dept.getEmployees().size()).sum();
        int totalDepartments = organization.getDepartments().size();

        // Create the X and Y axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Departments");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Employees");

        // Create the BarChart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Number of Employees in Departments");

        // Create the data series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Employees");

        for (Department department : organization.getDepartments()) {
            series.getData().add(new XYChart.Data<>(department.getName(), department.getEmployees().size()));
        }

        // Add the data series to the chart
        barChart.getData().add(series);

        // Show the bar chart in an alert
        showBarChartAlert("Organization Departments Report", barChart, totalEmployees, totalDepartments);
    }

    private void showBarChartAlert(String title, BarChart<String, Number> barChart, int totalEmployees, int totalDepartments) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Title Label
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setPadding(new Insets(10, 0, 10, 0));

        // Create a VBox to hold the chart and the summary text
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        // Add the summary text with green bold style
        TextFlow summaryText = new TextFlow();
        addGreenBoldTextToReport(summaryText, "Total Employees: " + totalEmployees + "\n");
        addGreenBoldTextToReport(summaryText, "Total Departments: " + totalDepartments + "\n\n");

        // Add the chart and the summary text to the VBox
        vBox.getChildren().addAll(titleLabel, summaryText, barChart);

        // Set the VBox as the content of the alert
        alert.getDialogPane().setContent(vBox);
        alert.getDialogPane().setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        alert.showAndWait();
    }

    private void addTextToReport(TextFlow report, String text) {
        Text regularText = new Text(text);
        report.getChildren().add(regularText);
    }

    private void addGreenBoldTextToReport(TextFlow report, String text) {
        Text greenBoldText = new Text(text);
        greenBoldText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        greenBoldText.setFill(Color.GREEN);
        report.getChildren().add(greenBoldText);
    }


    @FXML
    private void generateDepartmentReport(ActionEvent event) {
        if (organization == null) {
            showErrorAlert("Organization is not set. Please set the organization first.");
            return;
        }

        ObservableList<Department> departments = FXCollections.observableArrayList(organization.getDepartments());

        Department selectedDepartment = showDepartmentListDialog(departments); // Use departments here
        if (selectedDepartment != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("DepartmentReportPage.fxml"));
                Scene scene = new Scene(loader.load(), 400, 555);
                DepartmentReportPageController controller = loader.getController();
                controller.setDepartment(selectedDepartment);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Department showDepartmentListDialog(ObservableList<Department> departments) {
        ListView<Department> listView = new ListView<>();
        listView.setItems(departments);

        Dialog<Department> dialog = new Dialog<>();
        dialog.setTitle("Select Department");
        dialog.getDialogPane().setContent(listView);
        ButtonType okButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        Optional<Department> result = dialog.showAndWait();
        return result.orElse(null);
    }


    @FXML
    private void generateEmployeeReport(ActionEvent event) {
        if (organization == null) {
            showErrorAlert("Organization is not set. Please set the organization first.");
            return;
        }

        Employee selectedEmployee = showEmployeeListDialog(); // Use allEmployees here
        if (selectedEmployee != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("EmployeeReportPage.fxml"));
                Scene scene = new Scene(loader.load(), 400, 555);
                EmployeeReportPageController controller = loader.getController();
                controller.setEmployee(selectedEmployee);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Employee showEmployeeListDialog() {

        TableView<Employee> tableView = new TableView<>();
        tableView.setItems(allEmployees);

        TableColumn<Employee, String> departmentColumn = new TableColumn<>("Department");
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurrentDepartment().getName()));

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

        tableView.getColumns().addAll(departmentColumn, nameColumn, phoneColumn, nationalIdColumn, salaryTypeColumn, dateAddedColumn);

        TextField searchField = new TextField();
        searchField.setPromptText("Enter National ID");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String searchQuery = searchField.getText().trim();
            if (!searchQuery.isEmpty()) {
                ObservableList<Employee> filteredEmployees = allEmployees.stream()
                        .filter(emp -> String.valueOf(emp.getNationalId()).contains(searchQuery))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                tableView.setItems(filteredEmployees);
            } else {
                tableView.setItems(allEmployees);
            }
        });

        GridPane searchPane = new GridPane();
        searchPane.setHgap(10);
        searchPane.setVgap(10);
        searchPane.setPadding(new Insets(10));
        searchPane.add(new Label("National ID:"), 0, 0);
        searchPane.add(searchField, 1, 0);
        searchPane.add(searchButton, 2, 0);

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(searchPane, tableView);

        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Employees");
        dialog.getDialogPane().setContent(vbox);
        ButtonType okButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return tableView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        Optional<Employee> result = dialog.showAndWait();
        return result.orElse(null);
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

    public void setOrganization(Organization organization) {
        this.organization = organization;
        // Populate allEmployees with employees from all departments of the organization
        allEmployees.clear();
        for (Department department : organization.getDepartments()) {
            allEmployees.addAll(department.getEmployees());
        }
    }

    private void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
