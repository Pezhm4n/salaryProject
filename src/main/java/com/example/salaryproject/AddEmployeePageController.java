package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class AddEmployeePageController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField nationalIdField;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private ComboBox<String> salaryTypeComboBox;
    @FXML
    private TextField baseMonthlySalaryField;
    @FXML
    private TextField overtimeHoursField;
    @FXML
    private TextField overtimeRateField;
    @FXML
    private TextField commissionRateField;
    @FXML
    private TextField totalSalesField;
    @FXML
    private TextField fixedAmountField;
    @FXML
    private TextField hourlyRateField;
    @FXML
    private TextField hoursWorkedField;
    @FXML
    private Label messageLabel;

    private Department selectedDepartment;

    public void setDepartment(Department department) {
        this.selectedDepartment = department;
    }

    @FXML
    private void handleSalaryTypeChange() {
        String salaryType = salaryTypeComboBox.getValue();

        // غیرفعال کردن همه فیلدها
        baseMonthlySalaryField.setDisable(true);
        overtimeHoursField.setDisable(true);
        overtimeRateField.setDisable(true);
        commissionRateField.setDisable(true);
        totalSalesField.setDisable(true);
        fixedAmountField.setDisable(true);
        hourlyRateField.setDisable(true);
        hoursWorkedField.setDisable(true);

        if (salaryType != null) {
            switch (salaryType) {
                case "FixedSalary":
                    baseMonthlySalaryField.setDisable(false);
                    overtimeHoursField.setDisable(false);
                    overtimeRateField.setDisable(false);
                    break;
                case "HourlySalary":
                    hourlyRateField.setDisable(false);
                    hoursWorkedField.setDisable(false);
                    break;
                case "CommissionSalary":
                    commissionRateField.setDisable(false);
                    totalSalesField.setDisable(false);
                    break;
                case "CommissionPlusFixedSalary":
                    commissionRateField.setDisable(false);
                    totalSalesField.setDisable(false);
                    fixedAmountField.setDisable(false);
                    break;
            }
        }
    }



    @FXML
    private void handleAddEmployee(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String nationalIdText = nationalIdField.getText();
        LocalDate dateOfBirth = dateOfBirthPicker.getValue();
        String email = emailField.getText();
        String phoneNumberText = phoneNumberField.getText();
        String salaryType = salaryTypeComboBox.getValue();
        Status status = Status.ACTIVE;

        if (firstName.isEmpty() || lastName.isEmpty() || nationalIdText.isEmpty() || dateOfBirth == null || email.isEmpty() || phoneNumberText.isEmpty() || salaryType == null) {
            messageLabel.setText("Please fill in all fields!");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (!isValidName(firstName)) {
            messageLabel.setText("Invalid first name format!\nIt should not contain numbers\nand be less than 30 characters.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (!isValidName(lastName)) {
            messageLabel.setText("Invalid last name format!\nIt should not contain numbers\nand be less than 30 characters.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (!isValidNationalId(nationalIdText)) {
            messageLabel.setText("Invalid national ID format!\nIt should be a 10-digit number.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (!isValidPhoneNumber(phoneNumberText)) {
            messageLabel.setText("Invalid phone number format!\nIt should be in the format 09123456789.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (!isValidEmail(email)) {
            messageLabel.setText("Invalid email format!");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            try {
                int nationalId = Integer.parseInt(nationalIdText);
                int phoneNumber = Integer.parseInt(phoneNumberText);

                if (selectedDepartment == null) {
                    messageLabel.setText("No department selected!");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }

                // Create new Employee object
                Employee newEmployee = new Employee(firstName, lastName, nationalId, dateOfBirth, email, phoneNumber);

                switch (salaryType) {
                    case "FixedSalary":
                        double baseMonthlySalary = Double.parseDouble(baseMonthlySalaryField.getText());
                        double overtimeHours = Double.parseDouble(overtimeHoursField.getText());
                        double overtimeRate = Double.parseDouble(overtimeRateField.getText());
                        newEmployee.newFixedSalaryRecord(selectedDepartment, status, baseMonthlySalary, overtimeHours, overtimeRate);
                        break;
                    case "HourlySalary":
                        double hourlyRate = Double.parseDouble(hourlyRateField.getText());
                        double hoursWorked = Double.parseDouble(hoursWorkedField.getText());
                        newEmployee.newHourlySalaryRecord(selectedDepartment, status, hourlyRate, hoursWorked);
                        break;
                    case "CommissionSalary":
                        double commissionRate = Double.parseDouble(commissionRateField.getText());
                        double totalSales = Double.parseDouble(totalSalesField.getText());
                        newEmployee.newCommissionSalaryRecord(selectedDepartment, status, commissionRate, totalSales);
                        break;
                    case "CommissionPlusFixedSalary":
                        double commRate = Double.parseDouble(commissionRateField.getText());
                        double totalSalesAmount = Double.parseDouble(totalSalesField.getText());
                        double fixedAmount = Double.parseDouble(fixedAmountField.getText());
                        newEmployee.newCommissionPlusFixedSalaryRecord(selectedDepartment, status, commRate, totalSalesAmount, fixedAmount);
                        break;
                }

                messageLabel.setText("Employee added successfully!");
                messageLabel.setStyle("-fx-text-fill: green;");
                clearFields();

            } catch (NumberFormatException e) {
                messageLabel.setText("Invalid input format!");
                System.out.println(e.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]{1,30}");
    }

    private boolean isValidNationalId(String nationalId) {
        return nationalId.matches("\\d{10}");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("09\\d{9}");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        if (selectedDepartment == null) {
            System.out.println("selectedDepartment is null");
            return;
        }

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

    @FXML
    private void handleClear(ActionEvent event) {
        clearFields();
    }

    private void clearFields()
    {
        firstNameField.clear();
        lastNameField.clear();
        nationalIdField.clear();
        dateOfBirthPicker.setValue(null);
        emailField.clear();
        phoneNumberField.clear();
        salaryTypeComboBox.setValue(null);
        baseMonthlySalaryField.clear();
        overtimeHoursField.clear();
        overtimeRateField.clear();
        commissionRateField.clear();
        totalSalesField.clear();
        fixedAmountField.clear();
        hourlyRateField.clear();
        hoursWorkedField.clear();
    }
}
