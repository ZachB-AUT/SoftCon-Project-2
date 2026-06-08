package com.group1.myvitals.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class RecordHeartRateController {
    @FXML private DatePicker RecordHeartRateDate;
    @FXML private TextField RecordHeartRateValue;
    @FXML private Button RecordHeartRateRecordDataButton;
    @FXML private Button RecordHeartRateClearButton;

    @FXML
    public void initialize() {
        // Default to current data
        RecordHeartRateDate.setValue(LocalDate.now());
    }

    @FXML
    private void recordDataButtonPressed() {
        // Take user inputs
        LocalDate selectedDate = RecordHeartRateDate.getValue();
        String heartRateRaw = RecordHeartRateValue.getText().trim();

        // Ensure heart rate value isnt blank
        if (heartRateRaw.isEmpty()) {
            System.err.println("Validation Error: Please fill in all fields before recording");
            return;
        }

        // Default to current data if blank
        if(selectedDate == null){
            selectedDate = LocalDate.now();
        }

        try {
            // Parse input
            int heartRate = Integer.parseInt(heartRateRaw);

            // Ensure heart rate is not negative
            if (heartRate<0) {
                System.err.println("Validation Error: Heart Rate cannot be negative");
                return;
            }

            // TO DO save to database here

            // Reset inputs ready for next input
            RecordHeartRateValue.clear();
            RecordHeartRateDate.setValue(LocalDate.now()); // Reverts back to today

        } catch (NumberFormatException e) {
            System.err.println("Input Error: Heart Rate must be a whole number");
        }
    }

    // Clear inputs and reset data
    @FXML
    private void clearButtonPressed() {
        RecordHeartRateValue.clear();
        RecordHeartRateDate.setValue(LocalDate.now());
    }
    
}
