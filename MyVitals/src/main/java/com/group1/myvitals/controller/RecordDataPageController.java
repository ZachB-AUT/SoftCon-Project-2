package com.group1.myvitals.controller;
 
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class RecordDataPageController {
    @FXML private ComboBox<String> RecordDataDataTypeDropDown;
    @FXML private VBox RecordDataDefaultBlankVBox;

    @FXML
    public void initialize() {
        // Add data types to drop down
        RecordDataDataTypeDropDown.getItems().addAll("Blood Pressure", "Heart Rate", "Sleep Duration", "Respiratory Rate");

        // Check for data type being selected
        RecordDataDataTypeDropDown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadLayout(newValue);
            }
        });
    }

    private void loadLayout(String selectedType) {
        String fxmlPath = "";

        // Change the path depending on the selected data type
        switch (selectedType) {
            case "Blood Pressure":
                fxmlPath = "/com/group1/myvitals/view/RecordBloodPressure.fxml";
                break;
            case "Heart Rate":
                fxmlPath = "/com/group1/myvitals/view/RecordHeartRate.fxml";
                break;
            case "Sleep Duration":
                fxmlPath = "/com/group1/myvitals/view/RecordSleepDuration.fxml";
                break;
            case "Respiratory Rate":
                fxmlPath = "/com/group1/myvitals/view/RecordRespiratoryRate.fxml";
                break;
            default:
                return;
        }

        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent subViewNode = loader.load();
            RecordDataDefaultBlankVBox.getChildren().setAll(subViewNode);
            
        } catch (IOException e) {
            System.err.println("Execution Error: Failed to cleanly load dynamic layout asset: " + fxmlPath);
            e.printStackTrace();
        }
    }
    
}
