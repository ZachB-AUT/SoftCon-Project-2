package com.group1.myvitals.controller;

import com.group1.myvitals.model.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class SettingsPageController {

    @FXML private PasswordField SettingsCurrentPasswordField;
    @FXML private PasswordField SettingsNewPasswordField;
    @FXML private PasswordField SettingsConfirmPasswordField;
    @FXML private Button SettingsSaveButton;
    @FXML private Label SettingsStatusLabel;

    @FXML
    private void handleChangePassword() {
        String currentPassword = SettingsCurrentPasswordField.getText();
        String newPassword = SettingsNewPasswordField.getText();
        String confirmPassword = SettingsConfirmPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            SettingsStatusLabel.setText("All fields are required.");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            SettingsStatusLabel.setText("New passwords do not match.");
            return;
        }
        if (newPassword.length() < 6) {
            SettingsStatusLabel.setText("New password must be at least 6 characters.");
            return;
        }

        String username = Session.getInstance().getUsername();
        int userId = Session.getInstance().getCurrentUserId();
        int verified = Session.getInstance().getDb().verifyLogin(username, currentPassword);

        if (verified == -1) {
            SettingsStatusLabel.setText("Current password is incorrect.");
        } else {
            Session.getInstance().getDb().updatePassword(userId, newPassword);
            SettingsStatusLabel.setText("Password changed successfully.");
            SettingsCurrentPasswordField.clear();
            SettingsNewPasswordField.clear();
            SettingsConfirmPasswordField.clear();
        }
    }
}
