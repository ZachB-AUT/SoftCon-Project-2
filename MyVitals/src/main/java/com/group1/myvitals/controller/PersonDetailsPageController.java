package com.group1.myvitals.controller;

import com.group1.myvitals.model.DB_DataInterface;
import com.group1.myvitals.model.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PersonDetailsPageController {

    @FXML private TextField PersonalDetailsFirstNameTextBox;
    @FXML private TextField PersonalDetailsLastNameTextBox;
    @FXML private TextField PersonalDetailsNHITextBox;
    @FXML private TextField PersonalDetailsGenderTextBox;
    @FXML private TextField PersonalDetailsDOBTextBox;
    @FXML private TextField PersonalDetailsAgeTextBox;
    @FXML private TextField PersonalDetailsHeightTextBox;

    @FXML private Button PersonalDetailsEditFirstNameButton;
    @FXML private Button PersonalDetailsEditLastNameButton;
    @FXML private Button PersonalDetailsEditNHIButton;
    @FXML private Button PersonalDetailsEditGenderButton;
    @FXML private Button PersonalDetailsEditDOBButton;
    @FXML private Button PersonalDetailsEditAgeButton;
    @FXML private Button PersonalDetailsEditHeightTextBox;
    @FXML private Button PersonalDetailsAddMedicationButton;
    @FXML private Button PersonalDetailsEditMedicationButton;
    @FXML private Button PersonalDetailsRemoveMedicationButton;
    @FXML private Button PersonalDetailsAddAllergyButton;
    @FXML private Button PersonalDetailsRemoveAllergyButton;

    @FXML private TableView<String[]> PersonalDetailsMedicationsTable;
    @FXML private TableView<String> PersonalDetailsAllergiesTable;

    private DB_DataInterface db;
    private int userId;

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize() {
        db = Session.getInstance().getDb();
        userId = Session.getInstance().getCurrentUserId();

        // Load user profile
        String[] user = db.get_user(userId);
        if (user != null) {
            // DB stores full name in the 'name' column; split for first/last
            String fullName = user[1] != null ? user[1] : "";
            int spaceIdx = fullName.indexOf(' ');
            if (spaceIdx > 0) {
                PersonalDetailsFirstNameTextBox.setText(fullName.substring(0, spaceIdx));
                PersonalDetailsLastNameTextBox.setText(fullName.substring(spaceIdx + 1));
            } else {
                PersonalDetailsFirstNameTextBox.setText(fullName);
            }
            PersonalDetailsNHITextBox.setText(user[6] != null ? user[6] : "");
            PersonalDetailsGenderTextBox.setText(user[5] != null ? user[5] : "");
            PersonalDetailsDOBTextBox.setText(user[3] != null ? user[3] : "");
            PersonalDetailsAgeTextBox.setText(user[2] != null ? user[2] : "");
            PersonalDetailsHeightTextBox.setText(user[4] != null ? user[4] : "");
        }

        // Start all fields as non-editable
        setAllEditable(false);

        // Wire edit buttons to toggle editable and save
        wireEditButton(PersonalDetailsEditFirstNameButton, PersonalDetailsFirstNameTextBox, "firstName");
        wireEditButton(PersonalDetailsEditLastNameButton, PersonalDetailsLastNameTextBox, "lastName");
        wireEditButton(PersonalDetailsEditNHIButton, PersonalDetailsNHITextBox, "nhi");
        wireEditButton(PersonalDetailsEditGenderButton, PersonalDetailsGenderTextBox, "gender");
        wireEditButton(PersonalDetailsEditDOBButton, PersonalDetailsDOBTextBox, "dob");
        wireEditButton(PersonalDetailsEditAgeButton, PersonalDetailsAgeTextBox, "age");
        wireEditButton(PersonalDetailsEditHeightTextBox, PersonalDetailsHeightTextBox, "height");

        // Configure medications table columns
        TableColumn<String[], String> medNameCol = new TableColumn<>("Name");
        medNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[0]));
        medNameCol.setPrefWidth(240);
        TableColumn<String[], String> medDoseCol = new TableColumn<>("Dosage (mg)");
        medDoseCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[1]));
        PersonalDetailsMedicationsTable.getColumns().setAll(medNameCol, medDoseCol);

        // Configure allergies table column
        TableColumn<String, String> allergyCol = new TableColumn<>("Name");
        allergyCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()));
        allergyCol.setPrefWidth(210);
        PersonalDetailsAllergiesTable.getColumns().setAll(allergyCol);

        refreshMedications();
        refreshAllergies();

        // Wire medication/allergy dialog buttons
        PersonalDetailsAddMedicationButton.setOnAction(e -> openMedDialog("AddMedication.fxml", PersonController.Mode.ADD_MEDICATION));
        PersonalDetailsEditMedicationButton.setOnAction(e -> openMedDialog("EditMedication.fxml", PersonController.Mode.EDIT_MEDICATION));
        PersonalDetailsRemoveMedicationButton.setOnAction(e -> openMedDialog("RemoveMedication.fxml", PersonController.Mode.REMOVE_MEDICATION));
        PersonalDetailsAddAllergyButton.setOnAction(e -> openAllergyDialog("AddAllergy.fxml", AllergyController.Mode.ADD_ALLERGY));
        PersonalDetailsRemoveAllergyButton.setOnAction(e -> openAllergyDialog("RemoveAllergy.fxml", AllergyController.Mode.REMOVE_ALLERGY));
    }

    private void setAllEditable(boolean editable) {
        PersonalDetailsFirstNameTextBox.setEditable(editable);
        PersonalDetailsLastNameTextBox.setEditable(editable);
        PersonalDetailsNHITextBox.setEditable(editable);
        PersonalDetailsGenderTextBox.setEditable(editable);
        PersonalDetailsDOBTextBox.setEditable(editable);
        PersonalDetailsAgeTextBox.setEditable(editable);
        PersonalDetailsHeightTextBox.setEditable(editable);
    }

    private void wireEditButton(Button btn, TextField field, String fieldKey) {
        btn.setOnAction(e -> {
            if (!field.isEditable()) {
                field.setEditable(true);
                btn.setText("Save");
            } else {
                saveField(fieldKey, field.getText());
                field.setEditable(false);
                btn.setText("Edit");
            }
        });
    }

    private void saveField(String fieldKey, String value) {
        String[] user = db.get_user(userId);
        if (user == null) return;
        String name = PersonalDetailsFirstNameTextBox.getText().trim()
            + " " + PersonalDetailsLastNameTextBox.getText().trim();
        int age = parseIntSafe(PersonalDetailsAgeTextBox.getText());
        double height = parseDoubleSafe(PersonalDetailsHeightTextBox.getText());
        db.update_user(userId, name.trim(), age, PersonalDetailsDOBTextBox.getText(),
            height, PersonalDetailsGenderTextBox.getText(), PersonalDetailsNHITextBox.getText());
    }

    private void refreshMedications() {
        HashMap<String, Integer> meds = db.getMedications(userId);
        ObservableList<String[]> data = FXCollections.observableArrayList();
        meds.forEach((name, dose) -> data.add(new String[]{name, String.valueOf(dose)}));
        PersonalDetailsMedicationsTable.setItems(data);
    }

    private void refreshAllergies() {
        HashSet<String> allergies = db.getAllergies(userId);
        ObservableList<String> data = FXCollections.observableArrayList(allergies);
        PersonalDetailsAllergiesTable.setItems(data);
    }

    private void openMedDialog(String fxmlName, PersonController.Mode mode) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group1/myvitals/view/" + fxmlName)
            );
            Parent card = loader.load();
            PersonController ctrl = loader.getController();
            ctrl.setMode(mode);
            ctrl.setOnComplete(() -> refreshMedications());

            Stage dialog = new Stage();
            dialog.initOwner(PersonalDetailsAddMedicationButton.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(new StackPane(card)));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openAllergyDialog(String fxmlName, AllergyController.Mode mode) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group1/myvitals/view/" + fxmlName)
            );
            Parent card = loader.load();
            AllergyController ctrl = loader.getController();
            ctrl.setMode(mode);
            ctrl.setup();
            ctrl.setOnComplete(() -> refreshAllergies());

            Stage dialog = new Stage();
            dialog.initOwner(PersonalDetailsAddAllergyButton.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(new StackPane(card)));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private double parseDoubleSafe(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0.0; }
    }
}
