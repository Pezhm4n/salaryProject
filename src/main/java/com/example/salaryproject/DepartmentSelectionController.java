package com.example.salaryproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentSelectionController {

    @FXML
    private ListView<Department> departmentListView;

    private Organization organization;

    public void setOrganization(Organization organization) {
        this.organization = organization;
        if (organization != null) {
            departmentListView.setItems(organization.getDepartments());
        } else {
            // Handle the case where organization is null
            System.out.println("Organization is null");
        }
    }

    @FXML
    private void initialize() {
        departmentListView.setCellFactory(param -> new ListCell<Department>() {
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
    }

    @FXML
    private void handleDepartmentSelection(ActionEvent event) {
        Department selectedDepartment = departmentListView.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            try {
                ArrayList<String[]> departmentData = WriteToCSV.readCSV(WriteToCSV.RESOURCE_DIRECTORY + selectedDepartment.getOrganization().getName() + "/" + selectedDepartment.getName() + ".csv");

                // Extract financial records
                List<FinancialRecord> financialRecords = new ArrayList<>();
                for (int i = 0; i < departmentData.size(); i++) {
                    if (departmentData.get(i)[0].equals("Financial Records:")) {
                        for (int j = i + 1; j < departmentData.size(); j++) {
                            if (departmentData.get(j)[0].trim().isEmpty()) {
                                break;
                            }
                            try {
                                LocalDate startDate = LocalDate.parse(departmentData.get(j)[0]);
                                LocalDate endDate = departmentData.get(j)[1].isEmpty() ? null : LocalDate.parse(departmentData.get(j)[1]);
                                double budget = Double.parseDouble(departmentData.get(j)[2]);
                                double revenue = departmentData.get(j)[3].isEmpty() ? 0 : Double.parseDouble(departmentData.get(j)[3]);
                                double costs = departmentData.get(j)[4].isEmpty() ? 0 : Double.parseDouble(departmentData.get(j)[4]);
                                FinancialRecord record = new FinancialRecord(startDate, endDate, budget, revenue, costs);
                                financialRecords.add(record);
                            } catch (DateTimeParseException | NumberFormatException e) {
                                // Handle parsing errors
                                System.err.println("Error parsing financial record: " + e.getMessage());
                            }
                        }
                    }
                }
                selectedDepartment.setFinancialRecords(financialRecords);

                // Extract employees and salary records
                List<Employee> employees = new ArrayList<>();
                List<Employee> formerEmployees = new ArrayList<>();
                for (int i = 0; i < departmentData.size(); i++) {
                    if (departmentData.get(i)[0].equals("Employees:")) {
                        for (int j = i + 1; j < departmentData.size(); j++) {
                            if (departmentData.get(j)[0].trim().isEmpty()) {
                                break;
                            }
                            try {
                                // Extract employee details
                                String firstName = departmentData.get(j)[0];
                                String lastName = departmentData.get(j)[1];
                                int nationalId = Integer.parseInt(departmentData.get(j)[2]);
                                LocalDate dateOfBirth = LocalDate.parse(departmentData.get(j)[3]);
                                String email = departmentData.get(j)[4];
                                long phoneNumber = Long.parseLong(departmentData.get(j)[5]);
                                Employee employee = new Employee(firstName, lastName, nationalId, dateOfBirth, email, phoneNumber);

                                // Extract salary records
                                List<SalaryRecord> salaryRecords = new ArrayList<>();
                                for (int k = j + 1; k < departmentData.size(); k++) {
                                    if (departmentData.get(k)[0].trim().isEmpty()) {
                                        break;
                                    }
                                    try {
                                        LocalDate salaryStartDate = LocalDate.parse(departmentData.get(k)[0]);
                                        LocalDate salaryEndDate = departmentData.get(k)[1].isEmpty() ? null : LocalDate.parse(departmentData.get(k)[1]);
                                        Department departmentNameForSalary = selectedDepartment;
                                        Status status = Status.valueOf(departmentData.get(k)[3]);
                                        String employeeType = departmentData.get(k)[4];

                                        SalaryRecord salaryRecord;
                                        switch (EmployeeType.valueOf(employeeType)) {
                                            case FIXED_SALARY:
                                                double baseMonthlySalary = Double.parseDouble(departmentData.get(k)[5]);
                                                double overtimeHours = departmentData.get(k)[6].isEmpty() ? 0 : Double.parseDouble(departmentData.get(k)[6]);
                                                double overtimeRate = departmentData.get(k)[7].isEmpty() ? 0 : Double.parseDouble(departmentData.get(k)[7]);
                                                salaryRecord = new FixedSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, baseMonthlySalary, overtimeHours, overtimeRate);
                                                break;
                                            case COMMISSION_FIXED_SALARY:
                                                double commissionRate = Double.parseDouble(departmentData.get(k)[5]);
                                                double totalSales = Double.parseDouble(departmentData.get(k)[6]);
                                                double fixedAmount = Double.parseDouble(departmentData.get(k)[7]);
                                                salaryRecord = new CommissionPlusFixedSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, commissionRate, totalSales, fixedAmount);
                                                break;
                                            case COMMISSION_SALARY:
                                                commissionRate = Double.parseDouble(departmentData.get(k)[5]);
                                                totalSales = Double.parseDouble(departmentData.get(k)[6]);
                                                salaryRecord = new CommissionSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, commissionRate, totalSales);
                                                break;
                                            case HOURLY_SALARY:
                                                double hourlyRate = Double.parseDouble(departmentData.get(k)[5]);
                                                double hoursWorked = Double.parseDouble(departmentData.get(k)[6]);
                                                salaryRecord = new HourlySalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, hourlyRate, hoursWorked);
                                                break;
                                            case MANAGER:
                                                baseMonthlySalary = Double.parseDouble(departmentData.get(k)[5]);
                                                salaryRecord = new ManagerSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, baseMonthlySalary);
                                                break;
                                            default:
                                                throw new IllegalArgumentException("Unknown employee type: " + employeeType);
                                        }
                                        salaryRecords.add(salaryRecord);
                                    } catch (DateTimeParseException |
                                             IllegalArgumentException e) {
                                        // Handle parsing errors
                                        System.err.println("Error parsing salary record: " + e.getMessage());
                                    }
                                }
                                employee.setSalaryRecords(salaryRecords);
                                employees.add(employee);
                            } catch (DateTimeParseException | NumberFormatException e) {
                                // Handle parsing errors for employee details
                                System.err.println("Error parsing employee details: " + e.getMessage());
                            }
                        }
                    } else if (departmentData.get(i)[0].equals("Former Employees:")) {
                        for (int j = i + 1; j < departmentData.size(); j++) {
                            if (departmentData.get(j)[0].trim().isEmpty()) {
                                break;
                            }
                            try {
                                // Extract former employee details
                                String firstName = departmentData.get(j)[0];
                                String lastName = departmentData.get(j)[1];
                                int nationalId = Integer.parseInt(departmentData.get(j)[2]);
                                LocalDate dateOfBirth = LocalDate.parse(departmentData.get(j)[3]);
                                String email = departmentData.get(j)[4];
                                long phoneNumber = Long.parseLong(departmentData.get(j)[5]);
                                Employee employee = new Employee(firstName, lastName, nationalId, dateOfBirth, email, phoneNumber);

                                // Extract salary records
                                List<SalaryRecord> salaryRecords = new ArrayList<>();
                                for (int k = j + 1; k < departmentData.size(); k++) {
                                    if (departmentData.get(k)[0].trim().isEmpty()) {
                                        break;
                                    }
                                    try {
                                        LocalDate salaryStartDate = LocalDate.parse(departmentData.get(k)[0]);
                                        LocalDate salaryEndDate = departmentData.get(k)[1].isEmpty() ? null : LocalDate.parse(departmentData.get(k)[1]);
                                        Department departmentNameForSalary = selectedDepartment;
                                        Status status = Status.valueOf(departmentData.get(k)[3]);
                                        String employeeType = departmentData.get(k)[4];

                                        SalaryRecord salaryRecord;
                                        switch (EmployeeType.valueOf(employeeType)) {
                                            case FIXED_SALARY:
                                                double baseMonthlySalary = Double.parseDouble(departmentData.get(k)[5]);
                                                double overtimeHours = departmentData.get(k)[6].isEmpty() ? 0 : Double.parseDouble(departmentData.get(k)[6]);
                                                double overtimeRate = departmentData.get(k)[7].isEmpty() ? 0 : Double.parseDouble(departmentData.get(k)[7]);
                                                salaryRecord = new FixedSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, baseMonthlySalary, overtimeHours, overtimeRate);
                                                break;
                                            case COMMISSION_FIXED_SALARY:
                                                double commissionRate = Double.parseDouble(departmentData.get(k)[5]);
                                                double totalSales = Double.parseDouble(departmentData.get(k)[6]);
                                                double fixedAmount = Double.parseDouble(departmentData.get(k)[7]);
                                                salaryRecord = new CommissionPlusFixedSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, commissionRate, totalSales, fixedAmount);
                                                break;
                                            case COMMISSION_SALARY:
                                                commissionRate = Double.parseDouble(departmentData.get(k)[5]);
                                                totalSales = Double.parseDouble(departmentData.get(k)[6]);
                                                salaryRecord = new CommissionSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, commissionRate, totalSales);
                                                break;
                                            case HOURLY_SALARY:
                                                double hourlyRate = Double.parseDouble(departmentData.get(k)[5]);
                                                double hoursWorked = Double.parseDouble(departmentData.get(k)[6]);
                                                salaryRecord = new HourlySalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, hourlyRate, hoursWorked);
                                                break;
                                            case MANAGER:
                                                baseMonthlySalary = Double.parseDouble(departmentData.get(k)[5]);
                                                salaryRecord = new ManagerSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, baseMonthlySalary);
                                                break;
                                            default:
                                                throw new IllegalArgumentException("Unknown employee type: " + employeeType);
                                        }
                                        salaryRecords.add(salaryRecord);
                                    } catch (DateTimeParseException | NumberFormatException e) {
                                        // Handle parsing errors for salary records
                                        System.err.println("Error parsing salary record: " + e.getMessage());
                                    }
                                }
                                employee.setSalaryRecords(salaryRecords);
                                employees.add(employee);
                            } catch (DateTimeParseException | NumberFormatException e) {
                                // Handle parsing errors for employee details
                                System.err.println("Error parsing employee details: " + e.getMessage());
                            }
                        }
                    } else if (departmentData.get(i)[0].equals("Former Employees:")) {
                        for (int j = i + 1; j < departmentData.size(); j++) {
                            if (departmentData.get(j)[0].trim().isEmpty()) {
                                break;
                            }
                            try {
                                // Extract former employee details
                                String firstName = departmentData.get(j)[0];
                                String lastName = departmentData.get(j)[1];
                                int nationalId = Integer.parseInt(departmentData.get(j)[2]);
                                LocalDate dateOfBirth = LocalDate.parse(departmentData.get(j)[3]);
                                String email = departmentData.get(j)[4];
                                long phoneNumber = Long.parseLong(departmentData.get(j)[5]);
                                Employee employee = new Employee(firstName, lastName, nationalId, dateOfBirth, email, phoneNumber);

                                // Extract salary records
                                List<SalaryRecord> salaryRecords = new ArrayList<>();
                                for (int k = j + 1; k < departmentData.size(); k++) {
                                    if (departmentData.get(k)[0].trim().isEmpty()) {
                                        break;
                                    }
                                    try {
                                        LocalDate salaryStartDate = LocalDate.parse(departmentData.get(k)[0]);
                                        LocalDate salaryEndDate = departmentData.get(k)[1].isEmpty() ? null : LocalDate.parse(departmentData.get(k)[1]);
                                        Department departmentNameForSalary = selectedDepartment;
                                        Status status = Status.valueOf(departmentData.get(k)[3]);
                                        String employeeType = departmentData.get(k)[4];

                                        SalaryRecord salaryRecord;
                                        switch (EmployeeType.valueOf(employeeType)) {
                                            case FIXED_SALARY:
                                                double baseMonthlySalary = Double.parseDouble(departmentData.get(k)[5]);
                                                double overtimeHours = departmentData.get(k)[6].isEmpty() ? 0 : Double.parseDouble(departmentData.get(k)[6]);
                                                double overtimeRate = departmentData.get(k)[7].isEmpty() ? 0 : Double.parseDouble(departmentData.get(k)[7]);
                                                salaryRecord = new FixedSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, baseMonthlySalary, overtimeHours, overtimeRate);
                                                break;
                                            case COMMISSION_FIXED_SALARY:
                                                double commissionRate = Double.parseDouble(departmentData.get(k)[5]);
                                                double totalSales = Double.parseDouble(departmentData.get(k)[6]);
                                                double fixedAmount = Double.parseDouble(departmentData.get(k)[7]);
                                                salaryRecord = new CommissionPlusFixedSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, commissionRate, totalSales, fixedAmount);
                                                break;
                                            case COMMISSION_SALARY:
                                                commissionRate = Double.parseDouble(departmentData.get(k)[5]);
                                                totalSales = Double.parseDouble(departmentData.get(k)[6]);
                                                salaryRecord = new CommissionSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, commissionRate, totalSales);
                                                break;
                                            case HOURLY_SALARY:
                                                double hourlyRate = Double.parseDouble(departmentData.get(k)[5]);
                                                double hoursWorked = Double.parseDouble(departmentData.get(k)[6]);
                                                salaryRecord = new HourlySalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, hourlyRate, hoursWorked);
                                                break;
                                            case MANAGER:
                                                baseMonthlySalary = Double.parseDouble(departmentData.get(k)[5]);
                                                salaryRecord = new ManagerSalary(salaryStartDate, salaryEndDate, departmentNameForSalary, status, baseMonthlySalary);
                                                break;
                                            default:
                                                throw new IllegalArgumentException("Unknown employee type: " + employeeType);
                                        }
                                        salaryRecords.add(salaryRecord);
                                    } catch (DateTimeParseException |
                                             IllegalArgumentException e) {
                                        // Handle parsing errors for salary records
                                        System.err.println("Error parsing salary record: " + e.getMessage());
                                    }
                                }
                                employee.setSalaryRecords(salaryRecords);
                                formerEmployees.add(employee);
                            } catch (NumberFormatException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }

        @FXML private void handleBack (ActionEvent event){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("OrganizationPage.fxml"));
                Parent root = loader.load();
                OrganizationPageController controller = loader.getController();
                controller.setOrganization(organization); // Pass the current organization to the OrganizationPageController
                Scene scene = new Scene(root, 400, 555);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void refreshDepartmentList () {
            if (organization != null) {
                Platform.runLater(() -> {
                    departmentListView.setItems(null);
                    departmentListView.setItems(organization.getDepartments());
                });
            }
        }
    }