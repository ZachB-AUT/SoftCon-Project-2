package com.group1.myvitals.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class RecordRespRateController {
    @FXML private DatePicker RecordRespRateDate;
    @FXML private TextField RecordRespRateValue;
    @FXML private Button RecordRespRateRecordDataButton;
    @FXML private Button RecordRespRateClearButton;

    @FXML
    public void initialize() {
        // Default to current data
        RecordRespRateDate.setValue(LocalDate.now());
    }

    @FXML
    private void recordDataButtonPressed() {
        // Take user inputs
        LocalDate selectedDate = RecordRespRateDate.getValue();
        String respRateRaw = RecordRespRateValue.getText().trim();

        // Ensure respiratory rate value isnt blank
        if (respRateRaw.isEmpty()) {
            System.err.println("Validation Error: Please fill in all fields before recording");
            return;
        }

        // Default to current data if blank
        if(selectedDate == null){
            selectedDate = LocalDate.now();
        }

        try {
            // Parse input
            int respRate = Integer.parseInt(respRateRaw);

            // Ensure respiratory rate is not negative
            if (respRate<0) {
                System.err.println("Validation Error: Respiratory Rate cannot be negative");
                return;
            }

            // TO DO save to database here

            // Reset inputs ready for next input
            RecordRespRateValue.clear();
            RecordRespRateDate.setValue(LocalDate.now()); // Reverts back to today

        } catch (NumberFormatException e) {
            System.err.println("Input Error: Respiratory Rate must be a whole number");
        }
    }

    // Clear inputs and reset data
    @FXML
    private void clearButtonPressed() {
        RecordRespRateValue.clear();
        RecordRespRateDate.setValue(LocalDate.now());
    }
}