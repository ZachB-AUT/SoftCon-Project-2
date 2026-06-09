package com.group1.myvitals.controller;

import com.group1.myvitals.model.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the Add/Edit/Remove Medication dialogs.
 * The same FXML fields are reused across all three modes.
 * Pattern: Observer (lightweight) — onComplete callback notifies
 * PersonDetailsPageController to refresh its tables after the operation.
 */
public class PersonController {

    public enum Mode { ADD_MEDICATION, EDIT_MEDICATION, REMOVE_MEDICATION }

    @FXML private TextField MedicationNameTextBox;
    @FXML private TextField MedicationDosageTextBox;
    @FXML private ComboBox<String> MedicationFreqDropDown;
    @FXML private Button MedicationAddButton;

    private Mode mode = Mode.ADD_MEDICATION;
    private Runnable onComplete;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setOnComplete(Runnable callback) {
        this.onComplete = callback;
    }

    @FXML
    public void initialize() {
        MedicationFreqDropDown.getItems().addAll(
            "Once daily", "Twice daily", "Three times daily", "As needed"
        );
        MedicationFreqDropDown.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleAction() {
        String name = MedicationNameTextBox.getText().trim();

        if (name.isEmpty()) {
            return;
        }

        int userId = Session.getInstance().getCurrentUserId();
        var db = Session.getInstance().getDb();

        switch (mode) {
            case ADD_MEDICATION -> {
                int dosage = parseDosage(MedicationDosageTextBox.getText().trim());
                db.addMedication(userId, name, dosage);
            }
            case EDIT_MEDICATION -> {
                int dosage = parseDosage(MedicationDosageTextBox.getText().trim());
                db.removeMedication(userId, name);
                db.addMedication(userId, name, dosage);
            }
            case REMOVE_MEDICATION -> {
                db.removeMedication(userId, name);
            }
        }

        if (onComplete != null) onComplete.run();
        closeWindow();
    }

    @FXML
    private void handleExit() {
        closeWindow();
    }

    private int parseDosage(String text) {
        try { return Integer.parseInt(text); } catch (NumberFormatException e) { return 0; }
    }

    private void closeWindow() {
        Stage stage = (Stage) MedicationAddButton.getScene().getWindow();
        stage.close();
    }
}
