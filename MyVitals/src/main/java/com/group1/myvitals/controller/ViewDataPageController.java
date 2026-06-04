package com.group1.myvitals.controller;
 
import java.time.LocalDate;

import com.group1.myvitals.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import java.time.format.DateTimeFormatter;

public class ViewDataPageController {
    @FXML private StackedBarChart<String, Number> ViewDataBloodPressureGraph;
    @FXML private LineChart ViewDataHeartRateGraph;
    @FXML private LineChart ViewDataSleepDurationGraph;
    @FXML private LineChart ViewDataSleepDurationGraph2;

    private LocalDate today;
    private DateTimeFormatter dateFormatter;

    @FXML
    public void initialize() {
        today = LocalDate.now();
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
        for (int i = 29; i >= 0; i--) {
            LocalDate targetDate = today.minusDays(i);
            String xValueDate = targetDate.format(dateFormatter);
            
            int diastolicValue = 1; // Placeholder TO DO!!  replace with call to database?
            int systolicValue = 1; // Placeholder TO DO!!  replace with systolic number minus diastolic value to get the difference

            // Populate each series with the number of data points for each day
            diastolic.getData().add(new XYChart.Data<>(xValueDate, diastolicValue));
            systolic.getData().add(new XYChart.Data<>(xValueDate, systolicValue));
        }
        // Add all data to graph
        ViewDataBloodPressureGraph.getData().add(diastolic);
        ViewDataBloodPressureGraph.getData().add(systolic);
    }
}

// Systolic - Top number - unit: mm Hg
// Diastolic - Bottom number - unit: mm Hg