package com.group1.myvitals.controller;
 
import com.group1.myvitals.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomePageController {
    @FXML private Label HomePageTodaysDate;
    @FXML private Label HomePageNumberOfDataPointsRecorded;
    @FXML private StackedBarChart HomePageDataPointsInLastMonthGraph;
    @FXML private Button HomePageRecordDataButton;
    @FXML private Button LoginSignUpButton;
    @FXML private Button HomePageViewDataButton;
    @FXML private Button HomePageCreateReportButton;
    @FXML private Button HomePagePersonDetailsButton;
    @FXML private Button HomePageSettingsButton;
    @FXML private Button HomePageLogoutButton;
}
