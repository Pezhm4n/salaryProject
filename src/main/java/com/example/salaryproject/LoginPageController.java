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

import java.io.IOException;
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

    private RotateTransition rotateInTransition;
    private RotateTransition rotateOutTransition;
    private boolean isRotating = false;

    private ScaleTransition scaleUpTransition;
    private ScaleTransition scaleDownTransition;

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
        } else if (username.equals("admin") && password.equals("admin")) {
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
}
