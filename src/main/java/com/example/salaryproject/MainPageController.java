package com.example.salaryproject;

import javafx.animation.ScaleTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class MainPageController {

    private ScaleTransition scaleUpTransition;
    private ScaleTransition scaleDownTransition;

    @FXML
    private Label titleLabel;

    @FXML
    public void initialize() {
        // Initialize scale transitions
        scaleUpTransition = new ScaleTransition(Duration.millis(200), titleLabel);
        scaleUpTransition.setToX(1.5);
        scaleUpTransition.setToY(1.5);

        scaleDownTransition = new ScaleTransition(Duration.millis(200), titleLabel);
        scaleDownTransition.setToX(1.0);
        scaleDownTransition.setToY(1.0);

        // Set mouse event handlers
        titleLabel.setOnMouseEntered(event -> handleTitleEntered());
        titleLabel.setOnMouseExited(event -> handleTitleExited());
    }

    @FXML
    private void handleTitleEntered() {
        scaleUpTransition.playFromStart();
    }

    @FXML
    private void handleTitleExited() {
        scaleDownTransition.playFromStart();
    }

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
        departmentDialog.setContentText("Please enter the number of departments (1-15):");

        Optional<String> departmentResult = departmentDialog.showAndWait();
        if (departmentResult.isPresent()) {
            int numDepartments;
            try {
                numDepartments = Integer.parseInt(departmentResult.get());
                if (numDepartments < 1 || numDepartments > 15) {
                    throw new IllegalArgumentException("Number of departments must be between 1 and 15.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for departments.");
                return;
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", e.getMessage());
                return;
            }

            TextInputDialog minEmployeesDialog = new TextInputDialog();
            minEmployeesDialog.setTitle("Random Organization Generator");
            minEmployeesDialog.setHeaderText("Generate a Random Organization");
            minEmployeesDialog.setContentText("Please enter the minimum number of employees per department (1-80):");

            Optional<String> minEmployeesResult = minEmployeesDialog.showAndWait();
            if (minEmployeesResult.isPresent()) {
                int minEmployees;
                try {
                    minEmployees = Integer.parseInt(minEmployeesResult.get());
                    if (minEmployees < 1 || minEmployees > 80) {
                        throw new IllegalArgumentException("Minimum number of employees must be between 1 and 80.");
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for minimum employees.");
                    return;
                } catch (IllegalArgumentException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", e.getMessage());
                    return;
                }

                TextInputDialog maxEmployeesDialog = new TextInputDialog();
                maxEmployeesDialog.setTitle("Random Organization Generator");
                maxEmployeesDialog.setHeaderText("Generate a Random Organization");
                maxEmployeesDialog.setContentText("Please enter the maximum number of employees per department (" + minEmployees + " -80):");

                Optional<String> maxEmployeesResult = maxEmployeesDialog.showAndWait();
                if (maxEmployeesResult.isPresent()) {
                    int maxEmployees;
                    try {
                        maxEmployees = Integer.parseInt(maxEmployeesResult.get());
                        if (maxEmployees < minEmployees || maxEmployees > 1000) {
                            throw new IllegalArgumentException("Maximum number of employees must be between minimum employees and 1000.");
                        }
                    } catch (NumberFormatException e) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for maximum employees.");
                        return;
                    } catch (IllegalArgumentException e) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Input", e.getMessage());
                        return;
                    }

                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation");
                    confirmationAlert.setHeaderText("Confirm Random Organization Generation");
                    confirmationAlert.setContentText("Are you sure you want to generate a random organization with the following parameters?\n" +
                            "Number of Departments: " + numDepartments + "\n" +
                            "Minimum Employees per Department: " + minEmployees + "\n" +
                            "Maximum Employees per Department: " + maxEmployees);

                    Optional<ButtonType> confirmationResult = confirmationAlert.showAndWait();
                    if (confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK) {
                        Task<Void> task = new Task<>() {
                            @Override
                            protected Void call() {
                                try {
                                    RandomGenerator.createRandomOrganization(numDepartments, minEmployees, maxEmployees);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void succeeded() {
                                showAlert(Alert.AlertType.INFORMATION, "Success", "Random organization generated successfully!");
                            }

                            @Override
                            protected void failed() {
                                showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate random organization. Please try again.");
                            }
                        };

                        new Thread(task).start();
                    }
                }
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleReports(ActionEvent event) throws IOException {
        loadPage(event, "/com/example/salaryproject/ReportPage.fxml");
    }
}
