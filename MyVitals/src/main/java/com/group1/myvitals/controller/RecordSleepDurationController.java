package com.group1.myvitals.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class RecordSleepDurationController {
    @FXML private DatePicker RecordSleepDurationDate;
    @FXML private TextField RecordSleepDurationValue;
    @FXML private Button RecordSleepDurationRecordDataButton;
    @FXML private Button RecordSleepDurationClearButton;

    @FXML
    public void initialize() {
        // Default to current data
        RecordSleepDurationDate.setValue(LocalDate.now());
    }

    @FXML
    private void recordDataButtonPressed() {
        // Take user inputs
        LocalDate selectedDate = RecordSleepDurationDate.getValue();
        String sleepDurationRaw = RecordSleepDurationValue.getText().trim();

        // Ensure sleep duration value isnt blank
        if (sleepDurationRaw.isEmpty()) {
            System.err.println("Validation Error: Please fill in all fields before recording");
            return;
        }

        // Default to current data if blank
        if(selectedDate == null){
            selectedDate = LocalDate.now();
        }

        try {
            // Parse input
            double sleepDuration = Double.parseDouble(sleepDurationRaw);

            // Ensure sleep duration rate is not negative
            if (sleepDuration<0) {
                System.err.println("Validation Error: Sleep Duration cannot be negative");
                return;
            }

            // TO DO save to database here

            // Reset inputs ready for next input
            RecordSleepDurationValue.clear();
            RecordSleepDurationDate.setValue(LocalDate.now()); // Reverts back to today

        } catch (NumberFormatException e) {
            System.err.println("Input Error");
        }
    }

    // Clear inputs and reset data
    @FXML
    private void clearButtonPressed() {
        RecordSleepDurationValue.clear();
        RecordSleepDurationDate.setValue(LocalDate.now());
    }
    
}
