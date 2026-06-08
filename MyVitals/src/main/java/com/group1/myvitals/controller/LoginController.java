package com.group1.myvitals.controller;

import com.group1.myvitals.model.Session;
import com.group1.myvitals.view.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField LoginUsernameTextBox;
    @FXML private PasswordField LoginPasswordBox;
    @FXML private Button LoginSignInButton;
    @FXML private Button LoginSignUpButton;

    @FXML
    private void handleSignIn() {
        String username = LoginUsernameTextBox.getText().trim();
        String password = LoginPasswordBox.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter your username and password.");
            return;
        }

        int userId = Session.getInstance().getDb().verifyLogin(username, password);
        if (userId == -1) {
            showError("Invalid username or password.");
            LoginPasswordBox.clear();
        } else {
            Session.getInstance().login(userId, username);
            SceneManager.getInstance().showMainMenu();
        }
    }

    @FXML
    private void handleGoToSignupPage() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group1/myvitals/view/Signup.fxml")
            );
            Parent card = loader.load();
            StackPane root = new StackPane(card);
            root.setStyle("-fx-background-color: #f4ede8;");

            Stage dialog = new Stage();
            dialog.initOwner(SceneManager.getInstance().getPrimaryStage());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Sign Up");
            dialog.setScene(new Scene(root, 400, 400));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
