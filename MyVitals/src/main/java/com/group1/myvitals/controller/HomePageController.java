package com.group1.myvitals.controller;

import com.group1.myvitals.model.dao.VitalsDAO;
import com.group1.myvitals.model.Session;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomePageController implements MainMenuAware {

    @FXML private Label HomePageTodaysDate;
    @FXML private Label HomePageNumberOfDataPointsRecorded;
    @FXML private StackedBarChart<String, Number> HomePageDataPointsInLastMonthGraph;
    @FXML private Button HomePageRecordDataButton;
    @FXML private Button HomePageViewDataButton;
    @FXML private Button HomePageCreateReportButton;
    @FXML private Button HomePagePersonDetailsButton;
    @FXML private Button HomePageSettingsButton;
    @FXML private Button HomePageLogoutButton;

    private MainMenuController mainMenuController;

    @Override
    public void setMainMenuController(MainMenuController mmc) {
        this.mainMenuController = mmc;
        wireButtons();
    }

    @FXML
    public void initialize() {
        // Date label
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        HomePageTodaysDate.setText(today);

        // Count today's data points for this user
        int userId = Session.getInstance().getCurrentUserId();
        VitalsDAO db = Session.getInstance().getDb();
        ArrayList<String[]> allPoints = db.getDataPointsByUser(userId);
        String todayPrefix = LocalDate.now().toString(); // YYYY-MM-DD
        long todayCount = allPoints.stream()
            .filter(row -> row[2] != null && row[2].startsWith(todayPrefix))
            .count();
        HomePageNumberOfDataPointsRecorded.setText(String.valueOf(todayCount));

        // Populate chart with data points per day per data type (last 30 days)
        loadChartData(db, userId);
    }

    private void loadChartData(VitalsDAO db, int userId) {
        HomePageDataPointsInLastMonthGraph.getData().clear();
        ArrayList<String[]> dataTypes = db.getDataTypes();

        for (String[] dt : dataTypes) {
            int typeId = Integer.parseInt(dt[0]);
            String typeName = dt[1];
            ArrayList<String[]> points = db.getDataPoints(typeId, userId);

            if (points.isEmpty()) continue;

            // Group by date (first 10 chars of recorded_at = YYYY-MM-DD)
            Map<String, Integer> countByDay = new LinkedHashMap<>();
            for (String[] point : points) {
                if (point[2] == null) continue;
                String day = point[2].length() >= 10 ? point[2].substring(0, 10) : point[2];
                countByDay.merge(day, 1, Integer::sum);
            }

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(typeName);
            for (Map.Entry<String, Integer> entry : countByDay.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            HomePageDataPointsInLastMonthGraph.getData().add(series);
        }
    }

    private void wireButtons() {
        if (mainMenuController == null) return;
        HomePageRecordDataButton.setOnAction(e -> mainMenuController.showRecordDataPage());
        HomePageViewDataButton.setOnAction(e -> mainMenuController.showViewDataPage());
        HomePagePersonDetailsButton.setOnAction(e -> mainMenuController.showPersonDetailsPage());
        HomePageCreateReportButton.setOnAction(e -> mainMenuController.showCreateReportPage());
        HomePageSettingsButton.setOnAction(e -> mainMenuController.showSettingsPage());
        HomePageLogoutButton.setOnAction(e -> mainMenuController.logout());
    }
}
