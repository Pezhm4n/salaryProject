package com.example.salaryproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

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
    private Button getEmployeeByNationalIdButton;
    @FXML
    private Button backButton;

    private Department selectedDepartment;

    public void setDepartment(Department department) {
        this.selectedDepartment = department;
        departmentNameLabel.setText(department.getName());

        boolean isManagerPresent = department.getCurrentManager() != null;
        getEmployeesButton.setDisable(!isManagerPresent);
        getEmployeeByNationalIdButton.setDisable(!isManagerPresent);
        currentManagerButton.setDisable(!isManagerPresent);
        backButton.setDisable(!isManagerPresent);

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
        if (selectedDepartment != null) {
            ObservableList<Employee> employees = selectedDepartment.getEmployees();
            if (!employees.isEmpty()) {
                Employee selectedEmployee = showEmployeeListDialog(employees);
                if (selectedEmployee != null) {
                    // حذف کارمند انتخاب شده از دپارتمان
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
        // مرتب‌سازی کارمندان بر اساس نام و نام خانوادگی
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
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Employees");
        dialog.getDialogPane().setContent(tableView);
        ButtonType okButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // تنظیم نتیجه دیالوگ
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
                    // انتقال کارمند انتخاب شده به EmployeeManagementController
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("EmployeeManagement.fxml"));
                        Parent root = loader.load();
                        EmployeeManagementController controller = loader.getController();
                        controller.setEmployee(selectedEmployee);
                        controller.setDepartment(selectedDepartment);
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
    private void handleGetEmployeeByNationalId(ActionEvent event) {
        // Show dialog to enter national ID
        // Search for employee by national ID and display information
        // ...
        showAlert("Employee information retrieved successfully!");
    }

    @FXML
    private void handleCurrentManager(ActionEvent event) {
/*        if (selectedDepartment != null) {
            Employee currentManager = selectedDepartment.getCurrentManager();
            if (currentManager != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Current Manager Information");
                alert.setHeaderText("Manager Details:");

                StringBuilder managerInfo = new StringBuilder();
                managerInfo.append("Name: ").append(currentManager.getFirstName()).append(" ").append(currentManager.getLastName()).append("\n");
                managerInfo.append("National ID: ").append(currentManager.getNationalId()).append("\n");
                managerInfo.append("Date of Employment: ").append(currentManager.getDateOfEmployment()).append("\n");
                managerInfo.append("Base Monthly Salary: ").append(currentManager.getBaseMonthlySalary()).append("\n");
                managerInfo.append("Commission Rate: ").append(currentManager.getCommissionRate()).append("\n");
                managerInfo.append("Net Profit of Department: ").append(currentManager.getNetProfitOfDepartment()).append("\n");
                managerInfo.append("Shares Granted: ").append(currentManager.getSharesGranted()).append("\n");
                managerInfo.append("Current Share Price: ").append(currentManager.getCurrentSharePrice()).append("\n");
                managerInfo.append("Bonus: ").append(currentManager.getBonus()).append("\n");

                alert.setContentText(managerInfo.toString());
                alert.showAndWait();
            } else {
                showAlert("No manager assigned to this department.");
            }
        } else {
            showAlert("No department selected.");
        }*/
    }


    @FXML
    private void handleChangeManager(ActionEvent event) {
        if (selectedDepartment != null) {
            ObservableList<Employee> employees = selectedDepartment.getEmployees();
            if (!employees.isEmpty()) {
                Employee selectedEmployee = showEmployeeListDialog(employees);
                if (selectedEmployee != null) {
                    // دریافت اطلاعات مورد نیاز برای تغییر مدیر
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

                            // انتقال اطلاعات به متد newManagerSalaryRecord
                            selectedEmployee.newManagerSalaryRecord(selectedDepartment, selectedEmployee.getCurrentStatus(), baseMonthlySalary, commissionRate, netProfitOfDepartment, sharesGranted, currentSharePrice, bonus);
                            showAlert("Manager changed successfully!");

                            // بعد از تغییر مدیر، دکمه‌ها را فعال می‌کنیم
                            getEmployeesButton.setDisable(false);
                            getEmployeeByNationalIdButton.setDisable(false);
                            backButton.setDisable(false);
                            currentManagerButton.setDisable(false);
                        } catch (NumberFormatException e) {
                            showAlert("Invalid input. Please enter valid numbers.");
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
