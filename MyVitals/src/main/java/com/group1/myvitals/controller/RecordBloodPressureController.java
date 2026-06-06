package com.group1.myvitals.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class RecordBloodPressureController {
    @FXML private DatePicker RecordBloodPressureDate;
    @FXML private TextField RecordBloodPressureDiastolicValue;
    @FXML private TextField RecordBloodPressureSystolicValue;
    @FXML private Button RecordBloodPressureRecordDataButton;
    @FXML private Button RecordBloodPressureCancelButton;

    @FXML
    public void initialize() {
        // Default to current data
        RecordBloodPressureDate.setValue(LocalDate.now());
    }

    @FXML
    private void recordDataButtonPressed() {
        // Take user inputs
        LocalDate selectedDate = RecordBloodPressureDate.getValue();
        String diastolicRaw = RecordBloodPressureDiastolicValue.getText().trim();
        String systolicRaw = RecordBloodPressureSystolicValue.getText().trim();

        // Ensure blood pressure values arent blank
        if (diastolicRaw.isEmpty() || systolicRaw.isEmpty()) {
            System.err.println("Validation Error: Please fill in all fields before recording");
            return;
        }

        // Default to current data if blank
        if(selectedDate == null){
            selectedDate = LocalDate.now();
        }

        try {
            // Parse inputs
            int diastolic = Integer.parseInt(diastolicRaw);
            int systolic = Integer.parseInt(systolicRaw);

            // Ensure diastolic value is less than systolic value
            if (diastolic < systolic) {
                System.err.println("Validation Error: Diastolic value must be less than the Systolic value");
                return;
            }

            // TO DO save to database here

            // Reset inputs ready for next input
            RecordBloodPressureDiastolicValue.clear();
            RecordBloodPressureSystolicValue.clear();
            RecordBloodPressureDate.setValue(LocalDate.now()); // Reverts back to today

        } catch (NumberFormatException e) {
            System.err.println("Input Error: Diastolic and Systolic entries must be whole numbers");
        }
    }

    // Clear inputs and reset data
    @FXML
    private void cancelButtonPressed() {
        RecordBloodPressureDiastolicValue.clear();
        RecordBloodPressureSystolicValue.clear();
        RecordBloodPressureDate.setValue(LocalDate.now());
    }
    
}
