package com.group1.myvitals.controller;
 
import com.group1.myvitals.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
 
public class LoginController {
 
    @FXML private TextField LoginUsernameTextBox;
    @FXML private PasswordField LoginPasswordBox;
    @FXML private Button LoginSignInButton;
    @FXML private Button LoginSignUpButton;
 
    @FXML
    private void handleSignIn() {
        String username = LoginUsernameTextBox.getText();
        String password = LoginPasswordBox.getText();
        // TODO: verify credentials via UserDAO
        // TODO: if valid, navigate to main menu
        // TODO: if invalid, show error message
    }
 
    @FXML
    private void handleGoToSignupPage() {
        
    }
}