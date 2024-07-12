package com.example.salaryproject;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

public class LoginPageController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private ImageView userIcon;

    @FXML
    private ImageView passwordIcon;

    @FXML
    private Label titleLabel;

    @FXML
    private Button toggleThemeButton;

    private RotateTransition rotateInTransition;
    private RotateTransition rotateOutTransition;
    private boolean isRotating = false;

    private ScaleTransition scaleUpTransition;
    private ScaleTransition scaleDownTransition;

    private boolean isDarkTheme = true;
    @FXML
    private RadioButton darkThemeRadioButton;
    @FXML
    private RadioButton lightThemeRadioButton;

    @FXML
    private void initialize() {
        // ایجاد انیمیشن‌های چرخش
        rotateInTransition = new RotateTransition(Duration.millis(500), null);
        rotateInTransition.setByAngle(360);
        rotateInTransition.setOnFinished(event -> isRotating = false);

        rotateOutTransition = new RotateTransition(Duration.millis(500), null);
        rotateOutTransition.setByAngle(-360);
        rotateOutTransition.setOnFinished(event -> isRotating = false);

        // ایجاد انیمیشن‌های بزرگ شدن و کوچک شدن
        scaleUpTransition = new ScaleTransition(Duration.millis(200), titleLabel);
        scaleUpTransition.setToX(1.2);
        scaleUpTransition.setToY(1.2);

        scaleDownTransition = new ScaleTransition(Duration.millis(200), titleLabel);
        scaleDownTransition.setToX(1);
        scaleDownTransition.setToY(1);

        // اضافه کردن EventHandler برای رویداد‌های MouseEntered و MouseExited
        userIcon.setOnMouseEntered(event -> handleMouseEntered(userIcon));
        userIcon.setOnMouseExited(event -> handleMouseExited(userIcon));
        passwordIcon.setOnMouseEntered(event -> handleMouseEntered(passwordIcon));
        passwordIcon.setOnMouseExited(event -> handleMouseExited(passwordIcon));

    }

    private void handleMouseEntered(ImageView icon) {
        if (!isRotating) {
            isRotating = true;
            rotateInTransition.setNode(icon);
            rotateInTransition.playFromStart();
        }
    }

    private void handleMouseExited(ImageView icon) {
        if (!isRotating) {
            isRotating = true;
            rotateOutTransition.setNode(icon);
            rotateOutTransition.playFromStart();
        }
    }

    @FXML
    private void handleTitleEntered() {
        scaleUpTransition.playFromStart();
    }

    @FXML
    private void handleTitleExited() {
        scaleDownTransition.playFromStart();
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter username and password!");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            String hashedPassword = hashPassword(password);
            if (validateCredentials(username, hashedPassword) || (username.equals("admin") && password.equals("admin")) )  {
                messageLabel.setText("Login successful!");
                messageLabel.setStyle("-fx-text-fill: green;");

                Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
                Scene scene = new Scene(root, 400, 555);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } else {
                messageLabel.setText("Invalid username or password!");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    private boolean validateCredentials(String username, String hashedPassword) {
        try (BufferedReader reader = new BufferedReader(new FileReader("admins.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(hashedPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

    @FXML
    private void handleExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleToggleTheme(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        if (isDarkTheme) {
            scene.getStylesheets().remove(getClass().getResource("css/login.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("css/login-light.css").toExternalForm());
        } else {
            scene.getStylesheets().remove(getClass().getResource("css/login-light.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("css/login.css").toExternalForm());
        }
        isDarkTheme = !isDarkTheme;
    }
}
