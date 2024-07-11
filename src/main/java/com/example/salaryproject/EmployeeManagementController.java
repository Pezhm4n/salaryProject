package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.salaryproject.WriteToCSV.updateFieldOfSalaryRecord;

public class EmployeeManagementController {

    @FXML
    private Label employeeNameLabel;

    private Employee currentEmployee;

    private Organization organization;


    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setEmployee(Employee employee) {
        this.currentEmployee = employee;
        employeeNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
    }

    @FXML
    private void changeDepartment(ActionEvent event) {
        try {
            if (currentEmployee != null) {
                if (organization == null) {
                    showErrorAlert("Organization is not set. Please set the organization first.");
                    return;
                }

                // بررسی اینکه آیا کارمند مدیر دپارتمان فعلی‌اش هست
                Department currentDepartment = currentEmployee.getCurrentDepartment();
                Employee currentManager = currentDepartment.getCurrentManager();
                if (currentEmployee.equals(currentManager)) {
                    showErrorAlert("The current employee is the manager of their department.\nThey cannot change departments.");
                    return;
                }

                List<Department> availableDepartments = organization.getDepartments().stream()
                        .filter(department -> !department.equals(currentEmployee.getCurrentDepartment()))
                        .collect(Collectors.toList());

                if (availableDepartments.isEmpty()) {
                    showInformationAlert("Change Department", "No available departments to change to.");
                    return;
                }

                ListView<Department> departmentListView = new ListView<>();
                departmentListView.setItems(FXCollections.observableArrayList(availableDepartments));
                departmentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // Set a cell factory to display the name of the department
                departmentListView.setCellFactory(param -> new ListCell<>() {
                    @Override
                    protected void updateItem(Department department, boolean empty) {
                        super.updateItem(department, empty);
                        if (empty || department == null) {
                            setText(null);
                        } else {
                            setText(department.getName());
                        }
                    }
                });

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Select New Department");
                alert.setHeaderText("Select a new department for " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName());
                alert.getDialogPane().setContent(departmentListView);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Department selectedDepartment = departmentListView.getSelectionModel().getSelectedItem();
                    if (selectedDepartment != null) {
                        currentDepartment.removeEmployee(currentEmployee);

                        // Get salary records and remove all except the latest one
                        List<SalaryRecord> salaryRecords = currentEmployee.getSalaryRecords();
                        if (salaryRecords.size() > 1) {
                            SalaryRecord latestRecord = salaryRecords.get(salaryRecords.size() - 1);
                            salaryRecords.clear();
                            salaryRecords.add(latestRecord);

                            latestRecord.setDepartment(selectedDepartment);
                            currentEmployee.setSalaryRecords(salaryRecords);
                        }
                        currentEmployee.setDepartment(selectedDepartment);

                        // Write the changes to file
                        selectedDepartment.addEmployee(currentEmployee, true);

                        showInformationAlert("Change Department", "Department changed successfully.");
                    } else {
                        showErrorAlert("No department selected.");
                    }
                }
            } else {
                showErrorAlert("Current employee is not set. Please set the current employee first.");
            }
        } catch (Exception e) {
            showErrorAlert("An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void showEmployeeInfo() {
        showInformationAlert("Employee Info", "Name: " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() +
                "\nNational ID: " + currentEmployee.getNationalId() +
                "\nDate of Birth: " + currentEmployee.getDateOfBirth() +
                "\nEmail: " + currentEmployee.getEmail() +
                "\nPhone Number: " + currentEmployee.getPhoneNumber());
    }

    @FXML
    private void getReceivedSalary() {
        double receivedSalary = currentEmployee.getCurrentSalary();
        showInformationAlert("Received Salary", "Received Salary: " + receivedSalary);
    }

    @FXML
    private void getCurrentSalaryRecord() {
        SalaryRecord currentRecord = currentEmployee.getCurrentSalaryRecord();
        showInformationAlert("Current Salary Record", currentRecord != null ? currentRecord.toString() : "No current salary record.");
    }

    @FXML
    private void addNewSalaryRecord() {
        SalaryRecord currentRecord = currentEmployee.getCurrentSalaryRecord();
        if (currentRecord == null) {
            showErrorAlert("No current salary record found.");
            return;
        }

        Department newDepartment = currentEmployee.getCurrentDepartment();
        Status newStatus = currentEmployee.getCurrentStatus();

        try {
            String input;
            if (currentRecord instanceof FixedSalary) {
                input = getUserInput("New Fixed Salary Record", "Enter new base monthly salary\noverTimeHours\noverTimeRate\n(comma separated):");
                if (input == null) return;  // Exit if input is null (Cancel button clicked or window closed)
                String[] parts = validateAndParseInput(input, 3);
                double newBaseMonthlySalary = Double.parseDouble(parts[0]);
                double overTimeHours = Double.parseDouble(parts[1]);
                double overTimeRate = Double.parseDouble(parts[2]);
                currentEmployee.newFixedSalaryRecord(newDepartment, newStatus, newBaseMonthlySalary, overTimeHours, overTimeRate);
            } else if (currentRecord instanceof CommissionSalary) {
                input = getUserInput("New Commission Salary Record", "Enter new commission rate and total sales\n(comma separated):");
                if (input == null) return;
                String[] parts = validateAndParseInput(input, currentRecord instanceof CommissionPlusFixedSalary ? 3 : 2);
                double newCommissionRate = Double.parseDouble(parts[0]);
                double newTotalSales = Double.parseDouble(parts[1]);
                if (currentRecord instanceof CommissionPlusFixedSalary) {
                    double newFixedAmount = Double.parseDouble(parts[2]);
                    currentEmployee.newCommissionPlusFixedSalaryRecord(newDepartment, newStatus, newCommissionRate, newTotalSales, newFixedAmount);
                } else {
                    currentEmployee.newCommissionSalaryRecord(newDepartment, newStatus, newCommissionRate, newTotalSales);
                }
            } else if (currentRecord instanceof HourlySalary) {
                input = getUserInput("New Hourly Salary Record", "Enter new hourly rate and hours worked\n(comma separated):");
                if (input == null) return;
                String[] parts = validateAndParseInput(input, 2);
                double newHourlyRate = Double.parseDouble(parts[0]);
                double newHoursWorked = Double.parseDouble(parts[1]);
                currentEmployee.newHourlySalaryRecord(newDepartment, newStatus, newHourlyRate, newHoursWorked);
            } else if (currentRecord instanceof ManagerSalary) {
                input = getUserInput("New Manager Salary Record", "Enter new base monthly salary\ncommission rate\nnet profit\nshares granted\nshare price\nand bonus\n(comma separated):");
                if (input == null) return;
                String[] parts = validateAndParseInput(input, 6);
                double newBaseMonthlySalary = Double.parseDouble(parts[0]);
                double newCommissionRate = Double.parseDouble(parts[1]);
                double newNetProfit = Double.parseDouble(parts[2]);
                double newSharesGranted = Double.parseDouble(parts[3]);
                double newSharePrice = Double.parseDouble(parts[4]);
                double newBonus = Double.parseDouble(parts[5]);
                currentEmployee.newManagerSalaryRecord(newDepartment, newStatus, newBaseMonthlySalary, newCommissionRate, newNetProfit, newSharesGranted, newSharePrice, newBonus);
            }
            showInformationAlert("New Salary Record", "New salary record added successfully.");
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter valid numbers for the salary details.");
        } catch (Exception e) {
            showErrorAlert("An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void getAllSalaryRecords() {
        StringBuilder records = new StringBuilder();
        for (SalaryRecord record : currentEmployee.getSalaryRecords()) {
            records.append(record.toString()).append("\n");
        }

        String allRecords = records.length() > 0 ? records.toString() : "No salary records found.";

        TextArea textArea = new TextArea(allRecords);
        textArea.setEditable(false); // غیر قابل ویرایش
        textArea.setWrapText(true); // پیچیدن متن در خطوط

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true); // تنظیم عرض اسکرول‌پن به اندازه محتوای آن
        scrollPane.setFitToHeight(true); // تنظیم ارتفاع اسکرول‌پن به اندازه محتوای آن

        scrollPane.setPrefSize(400, 555);

        VBox vBox = new VBox(scrollPane);
        vBox.setPrefSize(400, 555);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("All Salary Records");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(vBox);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.showAndWait();
    }

    @FXML
    private void getCurrentDepartment() {
        Department currentDepartment = currentEmployee.getCurrentDepartment();
        showInformationAlert("Current Department", currentDepartment != null ? currentDepartment.getName() : "No current department.");
    }

    @FXML
    private void getCurrentEmployeeType() {
        EmployeeType currentType = currentEmployee.getCurrentEmployeeType();
        showInformationAlert("Current Employee Type", currentType != null ? currentType.toString() : "No current employee type.");
    }

    @FXML
    public void changeEmployeeType() {
        Department currentDepartment = currentEmployee.getCurrentDepartment();
        Status currentStatus = currentEmployee.getCurrentStatus();

        ChoiceDialog<String> typeDialog = new ChoiceDialog<>("Fixed Salary", "Fixed Salary", "Commission Salary", "Commission Plus Fixed Salary", "Hourly Salary", "Manager Salary");
        typeDialog.setTitle("Change Employee Type");
        typeDialog.setHeaderText("Select new salary type:");
        Optional<String> typeResult = typeDialog.showAndWait();

        if (typeResult.isPresent()) {
            String selectedType = typeResult.get();
            try {
                String input;
                switch (selectedType) {
                    case "Fixed Salary" -> {
                        input = getUserInput("New Fixed Salary Record", "Enter base monthly salary, overTimeHours, overTimeRate:");
                        if (input != null) {
                            String[] parts = validateAndParseInput(input, 3);
                            double newBaseMonthlySalary = Double.parseDouble(parts[0]);
                            double overTimeHours = Double.parseDouble(parts[1]);
                            double overTimeRate = Double.parseDouble(parts[2]);
                            currentEmployee.newFixedSalaryRecord(currentDepartment, currentStatus, newBaseMonthlySalary, overTimeHours, overTimeRate);
                        }
                    }
                    case "Commission Salary" -> {
                        input = getUserInput("New Commission Salary Record", "Enter commission rate, total sales:");
                        if (input != null) {
                            String[] parts = validateAndParseInput(input, 2);
                            double commissionRate = Double.parseDouble(parts[0]);
                            double totalSales = Double.parseDouble(parts[1]);
                            currentEmployee.newCommissionSalaryRecord(currentDepartment, currentStatus, commissionRate, totalSales);
                        }
                    }
                    case "Commission Plus Fixed Salary" -> {
                        input = getUserInput("New Commission Plus Fixed Salary Record", "Enter commission rate, total sales, fixed amount:");
                        if (input != null) {
                            String[] parts = validateAndParseInput(input, 3);
                            double commissionRate = Double.parseDouble(parts[0]);
                            double totalSales = Double.parseDouble(parts[1]);
                            double fixedAmount = Double.parseDouble(parts[2]);
                            currentEmployee.newCommissionPlusFixedSalaryRecord(currentDepartment, currentStatus, commissionRate, totalSales, fixedAmount);
                        }
                    }
                    case "Hourly Salary" -> {
                        input = getUserInput("New Hourly Salary Record", "Enter hourly rate, hours worked:");
                        if (input != null) {
                            String[] parts = validateAndParseInput(input, 2);
                            double hourlyRate = Double.parseDouble(parts[0]);
                            double hoursWorked = Double.parseDouble(parts[1]);
                            currentEmployee.newHourlySalaryRecord(currentDepartment, currentStatus, hourlyRate, hoursWorked);
                        }
                    }
                    case "Manager Salary" -> {
                        input = getUserInput("New Manager Salary Record", "Enter base monthly salary, commission rate, net profit, shares granted, share price, bonus:");
                        if (input != null) {
                            String[] parts = validateAndParseInput(input, 6);
                            double newBaseMonthlySalary = Double.parseDouble(parts[0]);
                            double newCommissionRate = Double.parseDouble(parts[1]);
                            double newNetProfit = Double.parseDouble(parts[2]);
                            double newSharesGranted = Double.parseDouble(parts[3]);
                            double newSharePrice = Double.parseDouble(parts[4]);
                            double newBonus = Double.parseDouble(parts[5]);
                            currentEmployee.newManagerSalaryRecord(currentDepartment, currentStatus, newBaseMonthlySalary, newCommissionRate, newNetProfit, newSharesGranted, newSharePrice, newBonus);
                        }
                    }
                }
                showInformationAlert("Change Employee Type", "Employee type changed successfully.");
            } catch (NumberFormatException e) {
                showErrorAlert("Please enter valid numbers for the salary details.");
            } catch (Exception e) {
                showErrorAlert("An error occurred: " + e.getMessage());
            }
        }
    }

    private String getUserInput(String title, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(content);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);  // Return null if the dialog was canceled or closed
    }

    private String[] validateAndParseInput(String input, int expectedParts) throws IllegalArgumentException {
        String[] parts = input.split(",");
        if (parts.length != expectedParts) {
            throw new IllegalArgumentException("Invalid input format.");
        }
        return parts;
    }

    private void showInformationAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
        try {
            // نمایش لیستی از وضعیت‌ها برای انتخاب
            ChoiceDialog<Status> dialog = new ChoiceDialog<>(Status.ACTIVE, Status.values());
            dialog.setTitle("Change Status");
            dialog.setHeaderText("Select a new status");
            dialog.setContentText("New status:");

            Optional<Status> result = dialog.showAndWait();
            result.ifPresent(newStatus -> {
                SalaryRecord currentSalaryRecord = currentEmployee.getCurrentSalaryRecord();
                Department currentDepartment = currentEmployee.getCurrentDepartment();

                // به‌روزرسانی وضعیت حقوقی کارمند
                currentSalaryRecord.setStatus(newStatus);

                // ذخیره کردن تغییرات در فایل CSV
                updateFieldOfSalaryRecord(currentDepartment, currentEmployee, currentSalaryRecord, 3, newStatus.toString());

                // نمایش پیغام تایید
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Change Status");
                alert.setHeaderText(null);
                alert.setContentText("Status changed successfully.");
                alert.showAndWait();
            });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred: " + e.getMessage());
            alert.showAndWait();
        }
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
