package com.example.salaryproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class RegisterAdminPageController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Please fill in all fields!");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match!");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (isUsernameTaken(username)) {
            messageLabel.setText("Username already exists!");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (!isValidUsername(username)) {
            messageLabel.setText("Invalid username format!\nUsername should be between 4 and 20 characters,\nnot start with a number or underscore,\nand can contain underscores.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else if (!isValidPassword(password)) {
            messageLabel.setText("Invalid password format!\nPassword should be between 4 and 16 characters.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            String hashedPassword = hashPassword(password);
            saveAdminInfo(username, hashedPassword);
            messageLabel.setText("Admin registered successfully!");
            messageLabel.setStyle("-fx-text-fill: green;");
            clearFields();
        }
    }

    private boolean isValidUsername(String username) {
        if (username.length() < 4 || username.length() > 20) {
            return false;
        }

        if (Character.isDigit(username.charAt(0)) || username.charAt(0) == '_') {
            return false;
        }

        for (char c : username.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return false;
            }
        }

        return username.charAt(username.length() - 1) != '_';
    }


    private boolean isValidPassword(String password) {
        return password.length() >= 4 && password.length() <= 16;
    }

    private boolean isUsernameTaken(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("admins.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private void saveAdminInfo(String username, String hashedPassword) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("admins.csv", true))) {
            writer.write(username + "," + hashedPassword + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
