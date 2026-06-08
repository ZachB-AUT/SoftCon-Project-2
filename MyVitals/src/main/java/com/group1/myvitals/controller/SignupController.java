package com.group1.myvitals.controller;

import com.group1.myvitals.model.Session;
import com.group1.myvitals.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController {

    @FXML private TextField SignUpFullNameTextBox;
    @FXML private TextField SignUpUsernameTextBox;
    @FXML private PasswordField SignUpPasswordTextBox;
    @FXML private Button SignUpSignUpButton;
    @FXML private Button SignUpSignInButton;
    @FXML private Button SignUpExitButton;
    @FXML private Label SignUpErrorLabel;

    @FXML
    private void handleSignUp() {
        String name = SignUpFullNameTextBox.getText().trim();
        String username = SignUpUsernameTextBox.getText().trim();
        String password = SignUpPasswordTextBox.getText();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            SignUpErrorLabel.setText("All fields are required.");
            return;
        }
        if (password.length() < 6) {
            SignUpErrorLabel.setText("Password must be at least 6 characters.");
            return;
        }

        int userId = Session.getInstance().getDb()
            .registerUser(username, password, name, 0, "", 0.0, "", "");

        if (userId == -1) {
            SignUpErrorLabel.setText("Username already taken. Choose another.");
        } else {
            Session.getInstance().login(userId, username);
            closeWindow();
            SceneManager.getInstance().showMainMenu();
        }
    }

    @FXML
    private void handleGoToSignIn() {
        closeWindow();
    }

    @FXML
    private void handleExit() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) SignUpExitButton.getScene().getWindow();
        stage.close();
    }
}
