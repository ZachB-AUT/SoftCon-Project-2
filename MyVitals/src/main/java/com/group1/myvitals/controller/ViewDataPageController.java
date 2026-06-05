package com.group1.myvitals.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class ViewDataPageController {
    @FXML private StackedBarChart<String, Number> ViewDataBloodPressureGraph;
    @FXML private LineChart<String, Number> ViewDataHeartRateGraph;
    @FXML private LineChart<String, Number> ViewDataSleepDurationGraph;
    @FXML private LineChart<String, Number> ViewDataRespRateGraph;

    private LocalDate today;
    private DateTimeFormatter dateFormatter;

    @FXML
    public void initialize() {
        today = LocalDate.now();
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
        updateViewDataBloodPressureGraph();
        updateViewDataHeartRateGraph();
        updateViewDataSleepDurationGraph();
        updateViewDataRespRateGraph();
    }

    public void updateViewDataBloodPressureGraph(){
        // Clear current data
        ViewDataBloodPressureGraph.getData().clear();

        // Create 2 categories of data points 
        XYChart.Series<String, Number> diastolic = new XYChart.Series<>();
        diastolic.setName("Diastolic");

        XYChart.Series<String, Number> systolic = new XYChart.Series<>();
        systolic.setName("Systolic");

        // Add data for each day to the different data series
        for (int i = 13; i >= 0; i--) {
            String date = today.minusDays(i).format(dateFormatter);
            
            int diastolicValue = 1; // Placeholder TO DO!!  replace with call to database?
            int rawSystolicValue = 2; // Placeholder TO DO!!  replace with call to database?
            int systolicValue = rawSystolicValue - diastolicValue; 

            // Populate the different series with data for that day
            diastolic.getData().add(new XYChart.Data<>(date, diastolicValue));
            systolic.getData().add(new XYChart.Data<>(date, systolicValue));
        }
        // Add all data to graph
        ViewDataBloodPressureGraph.getData().add(diastolic);
        ViewDataBloodPressureGraph.getData().add(systolic);
    }

    public void updateViewDataHeartRateGraph(){
        // Clear current data
        ViewDataHeartRateGraph.getData().clear();

        // Create a category for data 
        XYChart.Series<String, Number> heartRate = new XYChart.Series<>();

        // Add data for each day to the series
        for (int i = 13; i >= 0; i--) {
            String date = today.minusDays(i).format(dateFormatter);

            int heartRateValue = 60; // Placeholder TO DO!!  replace with call to database

            // Populate series with data for that day
            heartRate.getData().add(new XYChart.Data<>(date, heartRateValue));
        }
        // Add data to graph
        ViewDataHeartRateGraph.getData().add(heartRate);
    }

    public void updateViewDataSleepDurationGraph(){
        // Clear current data
        ViewDataSleepDurationGraph.getData().clear();

        // Create a category for data 
        XYChart.Series<String, Number> sleepDuration = new XYChart.Series<>();

        // Add data for each day to the series
        for (int i = 13; i >= 0; i--) {
            String date = today.minusDays(i).format(dateFormatter);

            double sleepDurationValue = 8.5; // Placeholder TO DO!!  replace with call to database

            // Populate series with data for that day
            sleepDuration.getData().add(new XYChart.Data<>(date, sleepDurationValue));
        }
        // Add data to graph
        ViewDataSleepDurationGraph.getData().add(sleepDuration);
    }

    public void updateViewDataRespRateGraph(){
        // Clear current data
        ViewDataRespRateGraph.getData().clear();

        // Create a category for data 
        XYChart.Series<String, Number> respRate = new XYChart.Series<>();

        // Add data for each day to the series
        for (int i = 13; i >= 0; i--) {
            String date = today.minusDays(i).format(dateFormatter);

            int respRateValue = 15; // Placeholder TO DO!!  replace with call to database

            // Populate series with data for that day
            respRate.getData().add(new XYChart.Data<>(date, respRateValue));
        }
        // Add data to graph
        ViewDataRespRateGraph.getData().add(respRate);
    }
}

// Systolic - Top number - unit: mm Hg
// Diastolic - Bottom number - unit: mm Hg