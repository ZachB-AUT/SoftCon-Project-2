package com.group1.myvitals.controller;
 
import com.group1.myvitals.view.SceneManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomePageController {
    @FXML private Label HomePageTodaysDate;
    @FXML private Label HomePageNumberOfDataPointsRecorded;
    @FXML private StackedBarChart<String, Number> HomePageDataPointsInLastMonthGraph;
    @FXML private Button HomePageRecordDataButton;
    @FXML private Button HomePageViewDataButton;
    @FXML private Button HomePageCreateReportButton;
    @FXML private Button HomePagePersonDetailsButton;
    @FXML private Button HomePageSettingsButton;
    @FXML private Button HomePageLogoutButton;

    private LocalDate today;
    private DateTimeFormatter dateFormatter;

    @FXML
    public void initialize() {
        // Display todays date
        today = LocalDate.now();
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(dateFormatter);
        HomePageTodaysDate.setText(formattedDate);

        // Display number of data points collected today
        updateNumberOfDataPointsRecorded();

        // Display the data number of data points recorded each day in past 30 days
        updateDataPointsRecordedGraph();
    }

    @FXML
    public void showRecordDataPage() {
        SceneManager.getInstance().switchPage("RecordData.fxml");
    }

    @FXML
    public void showViewDataPage() {
        SceneManager.getInstance().switchPage("ViewData.fxml");
    }

    @FXML
    public void showPersonDetailsPage() {
        SceneManager.getInstance().switchPage("PersonDetails.fxml");
    }

    @FXML
    public void showCreateReportPage() {
        SceneManager.getInstance().switchPage("CreateReport.fxml");
    }

    @FXML
    public void showSettingsPage() {
        SceneManager.getInstance().switchPage("Settings.fxml");
    }

    @FXML
    public void logout() {
        // TO DO!!
    }

    public void updateNumberOfDataPointsRecorded(){
        HomePageNumberOfDataPointsRecorded.setText("0"); // Placeholder replace  replace with call to database? TO DO!! 
    }

    public void updateDataPointsRecordedGraph(){
        // Clear current data
        HomePageDataPointsInLastMonthGraph.getData().clear();

        // Create 3 categories of data points 
        XYChart.Series<String, Number> heartRate = new XYChart.Series<>();
        heartRate.setName("Heart Rate");

        XYChart.Series<String, Number> sleepDuration = new XYChart.Series<>();
        sleepDuration.setName("Sleep Duration");

        XYChart.Series<String, Number> bloodPressure = new XYChart.Series<>();
        bloodPressure.setName("Blood Pressure");

        // Add data for each day to the different data series
        for (int i = 29; i >= 0; i--) {
            LocalDate targetDate = today.minusDays(i);
            String xValueDate = targetDate.format(dateFormatter);
            
            int heartRateCount = 1; // Placeholder TO DO!!  replace with call to database?
            int sleepCount = 1; // Placeholder TO DO!!  replace with call to database?
            int bpCount = 1; // Placeholder TO DO!!  replace with call to database?

            // Populate each series with the number of data points for each day
            heartRate.getData().add(new XYChart.Data<>(xValueDate, heartRateCount));
            sleepDuration.getData().add(new XYChart.Data<>(xValueDate, sleepCount));
            bloodPressure.getData().add(new XYChart.Data<>(xValueDate, bpCount));
        }

        // Add all data to graph
        HomePageDataPointsInLastMonthGraph.getData().add(heartRate);
        HomePageDataPointsInLastMonthGraph.getData().add(sleepDuration);
        HomePageDataPointsInLastMonthGraph.getData().add(bloodPressure);
    }
}