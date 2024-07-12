package com.example.salaryproject;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class OrganizationSelectionController {

    @FXML
    private ListView<Organization> organizationListView;

    @FXML
    private Label statusLabel;

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
        Path resourceDirectory = Paths.get(FileHandler.RESOURCE_DIRECTORY);
        try {
            Files.list(resourceDirectory).forEach(path -> {
                if (Files.isDirectory(path) && !path.endsWith("css")) {
                    Path organizationInfoFile = path.resolve("organization_info.csv");
                    if (Files.exists(organizationInfoFile)) {
                        Organization organization = readOrganizationFromFile(path);
                        if (organization != null) {
                            System.out.println("Loaded organization: " + organization.getName());
                            organizations.add(organization);
                        } else {
                            System.out.println("Failed to load organization from path: " + path);
                        }
                    } else {
                        System.out.println("File not found: " + organizationInfoFile);
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
            List<String[]> data = FileHandler.readCSV(organizationInfoFile.toString(), true);
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
                    Organization updatedOrganization = FileHandler.readOrganizationAndDepartments(name);
                    updatedOrganization.getDepartments().forEach(organization::addDepartment);

                    return organization;
                } else {
                    System.out.println("Invalid data format in file: " + organizationInfoFile);
                }
            } else {
                System.out.println("No data found in file: " + organizationInfoFile);
            }
        } else {
            System.out.println("File not found: " + organizationInfoFile);
        }
        return null;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/salaryproject/MainPage.fxml"));
            Scene scene = new Scene(loader.load(), 400, 555);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrganizationSelection(ActionEvent event) {
        Organization selectedOrganization = organizationListView.getSelectionModel().getSelectedItem();
        if (selectedOrganization != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/salaryproject/OrganizationPage.fxml"));
                Parent root = loader.load();
                OrganizationPageController controller = loader.getController();
                controller.setOrganization(selectedOrganization);
                Scene scene = new Scene(root, 400, 555);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeleteOrganization(ActionEvent event) {
        Organization selectedOrganization = organizationListView.getSelectionModel().getSelectedItem();
        if (selectedOrganization != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Organization");
            alert.setHeaderText("Are you sure you want to delete the organization " + selectedOrganization.getName() + "?");
            alert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                FileHandler.deleteOrganization(selectedOrganization);
                refreshOrganizationList();
                statusLabel.setText("Organization " + selectedOrganization.getName() + " deleted successfully.");
            }
        } else {
            statusLabel.setText("Please select an organization to delete.");
        }
    }

    public void refreshOrganizationList() {
        Platform.runLater(() -> {
            organizations.clear();
            loadOrganizationsFromFiles();
        });
    }
}
