package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

public class ReportPageController {
    private Organization organization;
    private ArrayList<Department> departments;
    private ArrayList<Employee> employees;

    @FXML
    private Button backButton;

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainPage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void generateOrganizationReports(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("OrganizationReportPage.fxml"));
            Scene scene = new Scene(loader.load());
            OrganizationReportPageController controller = loader.getController();
            controller.setOrganization(organization);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void generateDepartmentReports(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DepartmentReportPage.fxml"));
            Scene scene = new Scene(loader.load());
            DepartmentReportPageController controller = loader.getController();
            controller.setDepartments(departments);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void generateEmployeeReports(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("EmployeeReportPage.fxml"));
            Scene scene = new Scene(loader.load());
            EmployeeReportPageController controller = loader.getController();
            controller.setEmployees(employees);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
