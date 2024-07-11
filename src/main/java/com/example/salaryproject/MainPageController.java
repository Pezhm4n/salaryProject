package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;
import java.util.Optional;

public class MainPageController {

    private void loadPage(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene scene = new Scene(root, 400, 555);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("LoginPage.fxml"));
            Scene scene = new Scene(loader.load(), 400, 555);
            scene.getStylesheets().add(getClass().getResource("css/login.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterAdmin(ActionEvent event) throws IOException {
        loadPage(event, "/com/example/salaryproject/RegisterAdminPage.fxml");
    }

    @FXML
    private void handleRegisterOrganization(ActionEvent event) throws IOException {
        loadPage(event, "/com/example/salaryproject/RegisterOrganizationPage.fxml");
    }

    @FXML
    private void handleEnterOrganization(ActionEvent event) throws IOException {
        loadPage(event, "/com/example/salaryproject/OrganizationSelectionPage.fxml");
    }

    @FXML
    private void handleRandomGenerator(ActionEvent event) throws IOException {
        TextInputDialog departmentDialog = new TextInputDialog();
        departmentDialog.setTitle("Random Organization Generator");
        departmentDialog.setHeaderText("Generate a Random Organization");
        departmentDialog.setContentText("Please enter the number of departments:");

        Optional<String> departmentResult = departmentDialog.showAndWait();
        if (departmentResult.isPresent()) {
            int numDepartments = Integer.parseInt(departmentResult.get());

            TextInputDialog minEmployeesDialog = new TextInputDialog();
            minEmployeesDialog.setTitle("Random Organization Generator");
            minEmployeesDialog.setHeaderText("Generate a Random Organization");
            minEmployeesDialog.setContentText("Please enter the minimum number of employees per department:");

            Optional<String> minEmployeesResult = minEmployeesDialog.showAndWait();
            if (minEmployeesResult.isPresent()) {
                int minEmployees = Integer.parseInt(minEmployeesResult.get());

                TextInputDialog maxEmployeesDialog = new TextInputDialog();
                maxEmployeesDialog.setTitle("Random Organization Generator");
                maxEmployeesDialog.setHeaderText("Generate a Random Organization");
                maxEmployeesDialog.setContentText("Please enter the maximum number of employees per department:");

                Optional<String> maxEmployeesResult = maxEmployeesDialog.showAndWait();
                if (maxEmployeesResult.isPresent()) {
                    int maxEmployees = Integer.parseInt(maxEmployeesResult.get());

                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation");
                    confirmationAlert.setHeaderText("Confirm Random Organization Generation");
                    confirmationAlert.setContentText("Are you sure you want to generate a random organization with the following parameters?\n" +
                            "Number of Departments: " + numDepartments + "\n" +
                            "Minimum Employees per Department: " + minEmployees + "\n" +
                            "Maximum Employees per Department: " + maxEmployees);

                    Optional<ButtonType> confirmationResult = confirmationAlert.showAndWait();
                    if (confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK) {
                        try {
                            RandomGenerator.createRandomOrganization(numDepartments, minEmployees, maxEmployees);
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Success");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("Random organization generated successfully!");
                            successAlert.showAndWait();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Error");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Failed to generate random organization. Please try again.");
                            errorAlert.showAndWait();
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void handleReports(ActionEvent event) throws IOException {
        loadPage(event, "/com/example/salaryproject/ReportPage.fxml");
    }
}
