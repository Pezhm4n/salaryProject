package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OrganizationSelectionController {
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
            List<String[]> data = WriteToCSV.readCSV(organizationInfoFile.toString(), true);
            if (data.size() > 0) {
                String[] organizationData = data.get(0);
                String name = organizationData[0];
                String industry = organizationData[1];
                int foundationYear = Integer.parseInt(organizationData[2]);
                String headquarters = organizationData[3];
                String ceo = organizationData[4];
                double totalShares = Double.parseDouble(organizationData[5]);
                double sharePrice = Double.parseDouble(organizationData[6]);

                Organization organization = new Organization(name, industry, foundationYear, headquarters, ceo, totalShares, sharePrice);

                // Load departments
                try {
                    Files.list(organizationPath).forEach(departmentPath -> {
                        if (Files.isRegularFile(departmentPath) && !departmentPath.getFileName().toString().equals("organization_info.csv")) {
                            System.out.println(departmentPath);
                            Department department = readDepartmentFromFile(departmentPath, organization);
                            if (department != null) {
                                organization.addDepartment(department);
                            }
                            else {
                                System.out.println("no department");
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return organization;
            } else {
                System.out.println("No data found in file: " + organizationInfoFile);
            }
        } else {
            System.out.println("File not found: " + organizationInfoFile);
        }
        return null;
    }


    private Department readDepartmentFromFile(Path departmentFilePath, Organization organization) {
        if (Files.exists(departmentFilePath)) {
            List<String> lines = null;
            try {
                lines = Files.readAllLines(departmentFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            if (lines != null && !lines.isEmpty()) {
                String departmentInfoLine = null;
                for (String line : lines) {
                    if (line.startsWith("Department:")) {
                        continue; // Skip the header line
                    } else {
                        departmentInfoLine = line.trim();
                        break;
                    }
                }

                if (departmentInfoLine != null) {
                    String[] departmentInfo = departmentInfoLine.split(",");
                    if (departmentInfo.length >= 4) {
                        String name = departmentInfo[0].trim();
                        int capacity = Integer.parseInt(departmentInfo[1].trim());
                        int headCount = Integer.parseInt(departmentInfo[2].trim());
                        String description = departmentInfo[3].trim();
                        return new Department(organization, name, capacity, headCount, description);
                    } else {
                        System.out.println("Error: Incomplete department information in the file.");
                    }
                } else {
                    System.out.println("Error: No department information found in the file.");
                }
            } else {
                System.out.println("Error: Empty file or no data found in the file.");
            }
        } else {
            System.out.println("Error: Department file does not exist.");
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
}
