package com.example.salaryproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class DepartmentPageController {
    @FXML
    private Label departmentNameLabel;

    @FXML
    private Button addEmployeeButton;
    @FXML
    private Button removeEmployeeButton;
    @FXML
    private Button changeManagerButton;
    @FXML
    private Button currentManagerButton;
    @FXML
    private Button getEmployeesButton;
    @FXML
    private Button backButton;

    private Department selectedDepartment;
    private Organization organization;

    public void setDepartment(Department department) {
        this.selectedDepartment = department;
        departmentNameLabel.setText(department.getName());

        boolean isManagerPresent = department.getCurrentManager() != null;
        getEmployeesButton.setDisable(!isManagerPresent);
        currentManagerButton.setDisable(!isManagerPresent);
        backButton.setDisable(!isManagerPresent);
    }
    public void setOrganization(Organization organization) {
        this.organization = organization;
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
        if (selectedDepartment != null) {
            // Check if the department is at full capacity
            if (selectedDepartment.getEmployees().size() >= selectedDepartment.getCapacity()) {
                showAlert("The department is at full capacity. Cannot add more employees.");
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("AddEmployeePage.fxml"));
                    Parent root = loader.load();
                    AddEmployeePageController controller = loader.getController();
                    controller.setDepartment(selectedDepartment);
                    Scene scene = new Scene(root, 400, 555);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            showAlert("No department selected.");
        }
    }


    @FXML
    private void handleRemoveEmployee(ActionEvent event) {
        if (selectedDepartment != null) {
            ObservableList<Employee> employees = selectedDepartment.getEmployees();
            if (!employees.isEmpty()) {
                Employee selectedEmployee = showEmployeeListDialog(employees);
                if (selectedEmployee != null) {
                    selectedDepartment.removeEmployee(selectedEmployee);
                    showAlert("Employee removed successfully!");
                }
            } else {
                showAlert("No employees found in the department.");
            }
        } else {
            showAlert("No department selected.");
        }
    }

    private Employee showEmployeeListDialog(ObservableList<Employee> employees) {
        employees.sort(Comparator.comparing(Employee::getFirstName).thenComparing(Employee::getLastName));

        TableView<Employee> tableView = new TableView<>();
        tableView.setItems(employees);

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

        TextField searchField = new TextField();
        searchField.setPromptText("Enter National ID");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String searchQuery = searchField.getText().trim();
            if (!searchQuery.isEmpty()) {
                ObservableList<Employee> filteredEmployees = employees.stream()
                        .filter(emp -> String.valueOf(emp.getNationalId()).contains(searchQuery))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                tableView.setItems(filteredEmployees);
            } else {
                tableView.setItems(employees);
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
    private void handleGetEmployees(ActionEvent event) {
        if (selectedDepartment != null) {
            ObservableList<Employee> employees = selectedDepartment.getEmployees();
            if (!employees.isEmpty()) {
                Employee selectedEmployee = showEmployeeListDialog(employees);
                if (selectedEmployee != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("EmployeeManagement.fxml"));
                        Parent root = loader.load();
                        EmployeeManagementController controller = loader.getController();
                        controller.setEmployee(selectedEmployee);
                        controller.setDepartment(selectedDepartment);
                        controller.setOrganization(selectedDepartment.getOrganization());
                        Scene scene = new Scene(root, 400, 555);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setTitle("Employee Management");
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                showAlert("No employees found in the department.");
            }
        } else {
            showAlert("No department selected.");
        }
    }

    @FXML
    private void handleCurrentManager(ActionEvent event) {
        if (selectedDepartment != null && selectedDepartment.getCurrentManager() != null) {
            Employee currentManager = selectedDepartment.getCurrentManager();
            ManagerSalary currentManagerSalary = (ManagerSalary) currentManager.getCurrentSalaryRecord();

            // Create a dialog to display the manager information
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Current Manager Information");

            // Create a GridPane to layout the manager information
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Add manager information to the GridPane
            grid.add(new Label("First Name:"), 0, 0);
            grid.add(new Label(currentManager.getFirstName()), 1, 0);
            grid.add(new Label("Last Name:"), 0, 1);
            grid.add(new Label(currentManager.getLastName()), 1, 1);
            grid.add(new Label("National ID:"), 0, 2);
            grid.add(new Label(String.valueOf(currentManager.getNationalId())), 1, 2);
            grid.add(new Label("Phone Number:"), 0, 3);
            grid.add(new Label(String.valueOf(currentManager.getPhoneNumber())), 1, 3);
            grid.add(new Label("Email:"), 0, 4);
            grid.add(new Label(currentManager.getEmail()), 1, 4);
            grid.add(new Label("Base Monthly Salary:"), 0, 5);
            grid.add(new Label(String.valueOf(currentManagerSalary.getBaseMonthlySalary())), 1, 5);
            grid.add(new Label("Commission Rate:"), 0, 6);
            grid.add(new Label(String.valueOf(currentManagerSalary.getCommissionRate())), 1, 6);
            grid.add(new Label("Net Profit of Department:"), 0, 7);
            grid.add(new Label(String.valueOf(currentManagerSalary.getNetProfitOfDepartment())), 1, 7);
            grid.add(new Label("Shares Granted:"), 0, 8);
            grid.add(new Label(String.valueOf(currentManagerSalary.getSharesGranted())), 1, 8);
            grid.add(new Label("Current Share Price:"), 0, 9);
            grid.add(new Label(String.valueOf(currentManagerSalary.getCurrentSharePrice())), 1, 9);
            grid.add(new Label("Bonus:"), 0, 10);
            grid.add(new Label(String.valueOf(currentManagerSalary.getBonus())), 1, 10);
            grid.add(new Label("Total Salary: "), 0, 11);
            grid.add(new Label(String.valueOf(currentManagerSalary.calculateTotalSalary())), 1, 11);

            // Set the content of the dialog
            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            // Show the dialog
            dialog.showAndWait();
        } else {
            showAlert("No current manager found for this department.");
        }
    }


    @FXML
    private void handleChangeManager(ActionEvent event) {
        if (selectedDepartment != null) {
            ObservableList<Employee> employees = selectedDepartment.getEmployees();
            if (!employees.isEmpty()) {
                Employee selectedEmployee = showEmployeeListDialog(employees);
                if (selectedEmployee != null) {
                    Employee currentManager = selectedDepartment.getCurrentManager();

                    // چک کردن اینکه آیا کارمند انتخاب شده همان مدیر فعلی است یا نه
                    if (selectedEmployee.equals(currentManager)) {
                        showAlert("کارمند انتخاب شده مدیر فعلی است. اگر قصد تغییر اندازه حقوق او را دارید به بخش View and Manage Employees بروید.");
                        return;
                    }

                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Change Manager");
                    dialog.setHeaderText("Enter the required information for the new manager");

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(20, 150, 10, 10));

                    TextField baseMonthlySalaryField = new TextField();
                    baseMonthlySalaryField.setPromptText("Base Monthly Salary");
                    TextField commissionRateField = new TextField();
                    commissionRateField.setPromptText("Commission Rate");
                    TextField netProfitOfDepartmentField = new TextField();
                    netProfitOfDepartmentField.setPromptText("Net Profit of Department");
                    TextField sharesGrantedField = new TextField();
                    sharesGrantedField.setPromptText("Shares Granted");
                    TextField currentSharePriceField = new TextField();
                    currentSharePriceField.setPromptText("Current Share Price");
                    TextField bonusField = new TextField();
                    bonusField.setPromptText("Bonus");

                    grid.add(new Label("Base Monthly Salary:"), 0, 0);
                    grid.add(baseMonthlySalaryField, 1, 0);
                    grid.add(new Label("Commission Rate:"), 0, 1);
                    grid.add(commissionRateField, 1, 1);
                    grid.add(new Label("Net Profit of Department:"), 0, 2);
                    grid.add(netProfitOfDepartmentField, 1, 2);
                    grid.add(new Label("Shares Granted:"), 0, 3);
                    grid.add(sharesGrantedField, 1, 3);
                    grid.add(new Label("Current Share Price:"), 0, 4);
                    grid.add(currentSharePriceField, 1, 4);
                    grid.add(new Label("Bonus:"), 0, 5);
                    grid.add(bonusField, 1, 5);

                    dialog.getDialogPane().setContent(grid);

                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        try {
                            double baseMonthlySalary = Double.parseDouble(baseMonthlySalaryField.getText());
                            double commissionRate = Double.parseDouble(commissionRateField.getText());
                            double netProfitOfDepartment = Double.parseDouble(netProfitOfDepartmentField.getText());
                            double sharesGranted = Double.parseDouble(sharesGrantedField.getText());
                            double currentSharePrice = Double.parseDouble(currentSharePriceField.getText());
                            double bonus = Double.parseDouble(bonusField.getText());

                            // تغییر مدیر دپارتمان
                            selectedEmployee.newManagerSalaryRecord(selectedDepartment, selectedEmployee.getCurrentStatus(), baseMonthlySalary, commissionRate, netProfitOfDepartment, sharesGranted, currentSharePrice, bonus);

                            // فراخوانی متد changeEmployeeType برای مدیر قبلی
                            if (currentManager != null) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeManagement.fxml"));
                                Parent root = loader.load();
                                EmployeeManagementController controller = loader.getController();
                                controller.setEmployee(currentManager);
                                controller.setDepartment(selectedDepartment);
                                controller.setOrganization(selectedDepartment.getOrganization());

                                // غیرفعال کردن دسترسی به سایر اجزای برنامه
                                getEmployeesButton.setDisable(true);
                                backButton.setDisable(true);
                                currentManagerButton.setDisable(true);

                                controller.changeEmployeeType();

                                // فعال کردن دسترسی به سایر اجزای برنامه
                                getEmployeesButton.setDisable(false);
                                backButton.setDisable(false);
                                currentManagerButton.setDisable(false);
                            }

                            showAlert("Manager changed successfully!");
                        } catch (NumberFormatException e) {
                            showAlert("Invalid input. Please enter valid numbers.");
                        } catch (IOException e) {
                            showAlert("An error occurred while loading the employee management page.");
                        }
                    }
                }
            } else {
                showAlert("No employees found in the department.");
            }
        } else {
            showAlert("No department selected.");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
