package com.group1.myvitals.controller;
 
import com.group1.myvitals.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomePageController {
    @FXML private Label HomePageTodaysDate;
    @FXML private Label HomePageNumberOfDataPointsRecorded;
    @FXML private LineChart<String, Number> HomePageDataPointsInLastMonthGraph;
    @FXML private Button HomePageRecordDataButton;
    @FXML private Button HomePageViewDataButton;
    @FXML private Button HomePageCreateReportButton;
    @FXML private Button HomePagePersonDetailsButton;
    @FXML private Button HomePageSettingsButton;
    @FXML private Button HomePageLogoutButton;

    private LocalDate today;
    private DateTimeFormatter mainDateFormatter;
    private DateTimeFormatter graphDateFormatter;

    @FXML
    public void initialize() {
        // Display todays date
        today = LocalDate.now();
        mainDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String mainFormattedDate = today.format(mainDateFormatter);
        HomePageTodaysDate.setText(mainFormattedDate);
        graphDateFormatter = DateTimeFormatter.ofPattern("dd/MM");

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

        // Create 4 categories of data points 
        XYChart.Series<String, Number> heartRate = new XYChart.Series<>();
        heartRate.setName("Heart Rate");

        XYChart.Series<String, Number> sleepDuration = new XYChart.Series<>();
        sleepDuration.setName("Sleep Duration");

        XYChart.Series<String, Number> bloodPressure = new XYChart.Series<>();
        bloodPressure.setName("Blood Pressure");

        XYChart.Series<String, Number> respRate = new XYChart.Series<>();
        respRate.setName("Respiratory Rate");

        // Add data for each day to the different data series
        for (int i = 29; i >= 0; i--) {
            String date = today.minusDays(i).format(graphDateFormatter);
            
            int heartRateCount = 3; // Placeholder TO DO!!  replace with call to database?
            int sleepCount = 1; // Placeholder TO DO!!  replace with call to database?
            int bpCount = 2; // Placeholder TO DO!!  replace with call to database?
            int respRateCount = 4; // Placeholder TO DO!!  replace with call to database?

            // Populate each series with the number of data points for each day
            heartRate.getData().add(new XYChart.Data<>(date, heartRateCount));
            sleepDuration.getData().add(new XYChart.Data<>(date, sleepCount));
            bloodPressure.getData().add(new XYChart.Data<>(date, bpCount));
            respRate.getData().add(new XYChart.Data<>(date, respRateCount));
        }

        // Add all data to graph
        HomePageDataPointsInLastMonthGraph.getData().add(heartRate);
        HomePageDataPointsInLastMonthGraph.getData().add(sleepDuration);
        HomePageDataPointsInLastMonthGraph.getData().add(bloodPressure);
        HomePageDataPointsInLastMonthGraph.getData().add(respRate);
    }
}