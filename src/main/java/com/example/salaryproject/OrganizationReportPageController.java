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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        // Implement the logic to generate the organization financial summary report
    }

    @FXML
    private void showDepartments() {
        if (organization == null) {
            showErrorAlert("Organization is not set. Please set the organization first.");
            return;
        }

        StringBuilder report = new StringBuilder("Organization Department Report:\n\n");
        for (Department department : organization.getDepartments()) {
            report.append("Department Name: ").append(department.getName()).append("\n");
            report.append("Number of Employees: ").append(department.getEmployees().size()).append("\n");
            report.append("Current Manager: ").append(department.getCurrentManager() != null ? department.getCurrentManager().getFirstName() + " " + department.getCurrentManager().getLastName() : "None").append("\n\n");
        }

        showScrollableInformationAlert("Department Report", report.toString());
    }

    private void showScrollableInformationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Title Label
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setPadding(new Insets(10, 0, 10, 0));

        // Content TextArea
        TextArea textArea = new TextArea(content);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setPrefWidth(400);
        textArea.setPrefHeight(400);

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportWidth(400);
        scrollPane.setPrefViewportHeight(440);
        scrollPane.setContent(textArea);

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

    private void showInformationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
