package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReportPageController {
    private Organization organization;
    private ArrayList<Department> departments;
    private Employee employees;

    @FXML
    private Button backButton;

    @FXML
    private ListView<Organization> organizationListView;

    public static ObservableList<Organization> organizations = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        loadOrganizationsFromFiles();
        organizationListView.setItems(organizations);
        organizationListView.setCellFactory(param -> new ListCell<Organization>() {
            @Override
            protected void updateItem(Organization organization, boolean empty) {
                super.updateItem(organization, empty);
                if (empty || organization == null) {
                    setText(null);
                } else {
                    setText(organization.getName());
                }
            }
        });
    }

    private void loadOrganizationsFromFiles() {
        organizations.clear();
        Path resourceDirectory = Paths.get(WriteToCSV.RESOURCE_DIRECTORY);
        try {
            Files.list(resourceDirectory).forEach(path -> {
                if (Files.isDirectory(path)) {
                    Path organizationInfoFile = path.resolve("organization_info.csv");
                    if (Files.exists(organizationInfoFile)) {
                        Organization organization = readOrganizationFromFile(path);
                        if (organization != null) {
                            organizations.add(organization);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Organization readOrganizationFromFile(Path organizationPath) {
        Path organizationInfoFile = organizationPath.resolve("organization_info.csv");
        if (Files.exists(organizationInfoFile)) {
            List<String[]> data = WriteToCSV.readCSV(organizationInfoFile.toString(), true);
            if (data.size() > 0) {
                String[] organizationData = data.get(0);
                if (organizationData.length >= 7) {
                    String name = organizationData[0];
                    String industry = organizationData[1];
                    int foundationYear = Integer.parseInt(organizationData[2]);
                    String headquarters = organizationData[3];
                    String ceo = organizationData[4];
                    double totalShares = Double.parseDouble(organizationData[5]);
                    double sharePrice = Double.parseDouble(organizationData[6]);

                    Organization organization = new Organization(name, industry, foundationYear, headquarters, ceo, totalShares, sharePrice);

                    // Load departments
                    Organization updatedOrganization = WriteToCSV.createOrganizationAndDepartments(name);
                    updatedOrganization.getDepartments().forEach(organization::addDepartment);

                    return organization;
                }
            }
        }
        return null;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }

    public void setEmployees(Employee employees) {
        this.employees = employees;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainPage.fxml"));
            Scene scene = new Scene(loader.load(), 400, 555);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void generateOrganizationReports(ActionEvent event) {
        Organization selectedOrganization = organizationListView.getSelectionModel().getSelectedItem();
        if (selectedOrganization != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("OrganizationReportPage.fxml"));
                Scene scene = new Scene(loader.load(), 400, 555);
                OrganizationReportPageController controller = loader.getController();
                controller.setOrganization(selectedOrganization);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
