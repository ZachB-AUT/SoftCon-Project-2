package com.group1.myvitals.controller;

import com.group1.myvitals.model.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the Add/Remove Allergy dialogs.
 * Call setMode() then setup() before showing the dialog.
 * Pattern: Observer (lightweight) — onComplete callback notifies
 * PersonDetailsPageController to refresh its tables after the operation.
 */
public class AllergyController {

    public enum Mode { ADD_ALLERGY, REMOVE_ALLERGY }

    @FXML private TextField AllergyNameTextField;
    @FXML private ComboBox<String> AllergyComboBox;
    @FXML private Button AllergyActionButton;

    private Mode mode = Mode.ADD_ALLERGY;
    private Runnable onComplete;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setOnComplete(Runnable callback) {
        this.onComplete = callback;
    }

    /** Call this after setMode() to populate mode-specific data. */
    public void setup() {
        if (mode == Mode.REMOVE_ALLERGY && AllergyComboBox != null) {
            int userId = Session.getInstance().getCurrentUserId();
            var allergies = Session.getInstance().getDb().getAllergies(userId);
            AllergyComboBox.getItems().clear();
            AllergyComboBox.getItems().addAll(allergies);
            if (!AllergyComboBox.getItems().isEmpty()) {
                AllergyComboBox.getSelectionModel().selectFirst();
            }
        }
    }

    @FXML
    private void handleAction() {
        int userId = Session.getInstance().getCurrentUserId();
        var db = Session.getInstance().getDb();

        if (mode == Mode.ADD_ALLERGY) {
            String name = AllergyNameTextField != null ? AllergyNameTextField.getText().trim() : "";
            if (!name.isEmpty()) {
                db.addAllergy(userId, name);
            }
        } else {
            String selected = AllergyComboBox != null ? AllergyComboBox.getValue() : null;
            if (selected != null && !selected.isBlank()) {
                db.removeAllergy(userId, selected);
            }
        }

        if (onComplete != null) onComplete.run();
        closeWindow();
    }

    @FXML
    private void handleExit() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage;
        if (AllergyActionButton != null) {
            stage = (Stage) AllergyActionButton.getScene().getWindow();
        } else if (AllergyNameTextField != null) {
            stage = (Stage) AllergyNameTextField.getScene().getWindow();
        } else if (AllergyComboBox != null) {
            stage = (Stage) AllergyComboBox.getScene().getWindow();
        } else {
            return;
        }
        stage.close();
    }
}
