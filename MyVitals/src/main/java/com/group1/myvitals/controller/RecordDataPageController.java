package com.group1.myvitals.controller;

import com.group1.myvitals.model.dao.VitalsDAO;
import com.group1.myvitals.model.Session;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RecordDataPageController {

    @FXML private ComboBox<String> RecordDataTypeComboBox;
    @FXML private TextField RecordDataValueTextField;
    @FXML private TextField RecordDataDateTextField;
    @FXML private Button RecordDataSubmitButton;
    @FXML private Label RecordDataStatusLabel;

    private VitalsDAO db;
    private int userId;

    @FXML
    public void initialize() {
        db = Session.getInstance().getDb();
        userId = Session.getInstance().getCurrentUserId();

        // Populate data type combo box
        ArrayList<String[]> types = db.getDataTypes();
        for (String[] type : types) {
            RecordDataTypeComboBox.getItems().add(type[1] + " (" + type[2] + ")");
        }
        if (!RecordDataTypeComboBox.getItems().isEmpty()) {
            RecordDataTypeComboBox.getSelectionModel().selectFirst();
        }

        // Pre-fill date with current timestamp
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        RecordDataDateTextField.setText(now);
    }

    @FXML
    private void handleSubmit() {
        String selectedType = RecordDataTypeComboBox.getValue();
        String value = RecordDataValueTextField.getText().trim();
        String dateText = RecordDataDateTextField.getText().trim();

        if (selectedType == null || value.isEmpty()) {
            RecordDataStatusLabel.setText("Please select a data type and enter a value.");
            return;
        }

        // Extract type name from "typename (unit)" format
        String typeName = selectedType.contains("(")
            ? selectedType.substring(0, selectedType.indexOf('(')).trim()
            : selectedType;

        try {
            // Validate timestamp format
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").parse(dateText);

            // Insert data point with user id and timestamp
            ArrayList<String[]> types = db.getDataTypes();
            String[] matchedType = types.stream()
                .filter(t -> t[1].equals(typeName))
                .findFirst()
                .orElse(null);

            if (matchedType == null) {
                RecordDataStatusLabel.setText("Unknown data type.");
                return;
            }

            int typeId = Integer.parseInt(matchedType[0]);
            // Use the low-level insert which takes typeId, userId, value, timestamp
            db.insert_data_point(typeId, userId, value, dateText);

            RecordDataStatusLabel.setText("Recorded successfully.");
            RecordDataValueTextField.clear();
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            RecordDataDateTextField.setText(now);
        } catch (Exception ex) {
            RecordDataStatusLabel.setText("Invalid date format. Use YYYY-MM-DDTHH:MM:SS.");
        }
    }
}
