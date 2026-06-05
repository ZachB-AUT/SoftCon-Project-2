package com.group1.myvitals.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import java.time.LocalDate;

public class CreateReportPageController {
    // Data type CheckBoxes
    @FXML private CheckBox createReportBloodPressureCheckBox;
    @FXML private CheckBox createReportSleepDurationCheckBox;
    @FXML private CheckBox createReportHeartRateCheckBox; 
    @FXML private CheckBox createReportRespRateCheckBox; 

    // Date Range
    @FXML private DatePicker createReportStartDate;
    @FXML private DatePicker createReportEndDate;

    // Generate Report Button 
    @FXML private Button createReportGenerateReportButton;

    // Status Message Label
    @FXML private Label createReportStatusMessage;

    @FXML
    public void initialize() {
        // Hide message on initialisation
        createReportStatusMessage.setVisible(false);

        // Set default dates to the past 30 days
        createReportEndDate.setValue(LocalDate.now());
        createReportStartDate.setValue(LocalDate.now().minusDays(30));
    }

    @FXML
    public void generateReportButtonPressed() {
        // Hide message so can be made visible if successful
        createReportStatusMessage.setVisible(false);

        // Check one type of data at least has been selected
        if (!(createReportBloodPressureCheckBox.isSelected() || createReportSleepDurationCheckBox.isSelected() || createReportHeartRateCheckBox.isSelected() || createReportRespRateCheckBox.isSelected())) {
            updateStatusLabel("Error: Select at least one metric.", "#d7827a");
            return;
        }

        // Get selected dates
        LocalDate startDate = createReportStartDate.getValue();
        LocalDate endDate = createReportEndDate.getValue();

        // If either date is empty use default dates as the past 30 days
        if (startDate == null) {createReportStartDate.setValue(LocalDate.now().minusDays(30));} 
        if (endDate == null) {createReportEndDate.setValue(LocalDate.now());} 
        
        // Ensure chronological order
        if (startDate.isAfter(endDate)) {
            updateStatusLabel("Error: Invalid date range", "#d7827a");
            return;
        }

        // Extract data selections
        boolean includeBloodPressure = createReportBloodPressureCheckBox.isSelected();
        boolean includeSleep = createReportSleepDurationCheckBox.isSelected();
        boolean includeHeartRate = createReportHeartRateCheckBox.isSelected();
        boolean includeRespRate = createReportRespRateCheckBox.isSelected();

        // TO DO: extract needed data from database and use to create report

        // Update message if successful
        updateStatusLabel("Report Created!", "#d7827a");
    }

    private void updateStatusLabel(String message, String hexColor) {
        createReportStatusMessage.setText(message);
        createReportStatusMessage.setStyle("-fx-text-fill: " + hexColor + "; -fx-font-family: 'Aptos Mono'; -fx-font-weight: Bold; -fx-font-size: 14;");
        createReportStatusMessage.setVisible(true);
    }
}
