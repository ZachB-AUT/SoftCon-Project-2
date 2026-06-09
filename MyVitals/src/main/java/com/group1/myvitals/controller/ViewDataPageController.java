package com.group1.myvitals.controller;

import com.group1.myvitals.model.dao.VitalsDAO;
import com.group1.myvitals.model.Session;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ViewDataPageController {

    @FXML private VBox chartsContainer;

    @FXML
    public void initialize() {
        VitalsDAO db = Session.getInstance().getDb();
        int userId = Session.getInstance().getCurrentUserId();

        ArrayList<String[]> dataTypes = db.getDataTypes();

        HBox currentRow = null;
        int col = 0;

        for (String[] dt : dataTypes) {
            String typeName = dt[1];
            int typeId;
            try {
                typeId = db.getDataTypeId(typeName);
            } catch (Exception e) {
                continue;
            }

            ArrayList<String[]> points = db.getDataPoints(typeId, userId);
            if (points.isEmpty()) continue;

            boolean isDualValue = points.stream().anyMatch(p -> p[1] != null && p[1].contains("/"));

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(typeName);
            XYChart.Series<String, Number> series2 = isDualValue ? new XYChart.Series<>() : null;
            if (series2 != null) series2.setName("Diastolic");

            for (String[] point : points) {
                String label = point[2] != null && point[2].length() >= 10
                    ? point[2].substring(0, 10) : point[2];
                try {
                    if (isDualValue && point[1] != null && point[1].contains("/")) {
                        String[] parts = point[1].split("/");
                        double systolic = Double.parseDouble(parts[0].trim());
                        double diastolic = Double.parseDouble(parts[1].trim());
                        series.getData().add(new XYChart.Data<>(label, systolic));
                        series2.getData().add(new XYChart.Data<>(label, diastolic));
                    } else {
                        double val = Double.parseDouble(point[1]);
                        series.getData().add(new XYChart.Data<>(label, val));
                    }
                } catch (NumberFormatException ignored) {}
            }
            if (series.getData().isEmpty()) continue;

            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
            if (isDualValue) {
                series.setName("Systolic");
                chart.getData().add(series);
                chart.getData().add(series2);
                chart.setLegendVisible(true);
            } else {
                chart.getData().add(series);
                chart.setLegendVisible(false);
            }
            chart.setPrefSize(295, 180);

            String displayName = typeName.replace("_", " ");
            displayName = Character.toUpperCase(displayName.charAt(0)) + displayName.substring(1);
            Text label = new Text(displayName);
            label.setFill(javafx.scene.paint.Color.web("#d7827a"));
            label.setUnderline(true);
            try {
                label.setFont(Font.font("Aptos Mono Bold", 18));
            } catch (Exception ignored) {}

            VBox chartBox = new VBox(5, label, chart);
            chartBox.setAlignment(Pos.CENTER);
            chartBox.setPrefWidth(305);

            if (col == 0) {
                currentRow = new HBox(40);
                currentRow.setAlignment(Pos.CENTER_LEFT);
                currentRow.setPadding(new Insets(0));
                chartsContainer.getChildren().add(currentRow);
            }
            currentRow.getChildren().add(chartBox);
            col++;
            if (col == 2) col = 0;
        }

        if (chartsContainer.getChildren().isEmpty()) {
            Text empty = new Text("No data recorded yet.");
            empty.setFill(javafx.scene.paint.Color.web("#575279"));
            chartsContainer.getChildren().add(empty);
        }
    }
}
