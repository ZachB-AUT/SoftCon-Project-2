package com.group1.myvitals.controller;

import com.group1.myvitals.view.SceneManager;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class MainMenuController {

    @FXML private BorderPane mainMenuBorderPane;

    @FXML
    public void initialize() {
        // Load the home page as the default centre content on startup
        showHomePage();
    }

    // ===================== Sidebar Navigation =====================

    @FXML
    public void showHomePage() {
        loadCentre("/com/group1/myvitals/view/Home.fxml");
    }

    @FXML
    public void showRecordDataPage() {
        loadCentre("/com/group1/myvitals/view/RecordData.fxml");
    }

    @FXML
    public void showViewDataPage() {
        loadCentre("/com/group1/myvitals/view/ViewData.fxml");
    }

    @FXML
    public void showPersonDetailsPage() {
        loadCentre("/com/group1/myvitals/view/PersonDetails.fxml");
    }

    @FXML
    public void showCreateReportPage() {
        loadCentre("/com/group1/myvitals/view/CreateReport.fxml");
    }

    @FXML
    public void showSettingsPage() {
        loadCentre("/com/group1/myvitals/view/Settings.fxml");
    }

    @FXML
    public void Logout() {
        
    }

    // ===================== Helper =====================

    private void loadCentre(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(fxmlPath)
            );
            Parent content = loader.load();
            mainMenuBorderPane.setCenter(content);
        } catch (IOException e) {
            System.err.println("Error loading centre content: " + fxmlPath);
            e.printStackTrace();
        }
    }
}