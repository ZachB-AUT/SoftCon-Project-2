package com.group1.myvitals.controller;

import com.group1.myvitals.view.SceneManager;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class MainMenuController {

    @FXML private BorderPane mainMenuBorderPane;

    // ********************************** Sidebar Navigation **********************************

    @FXML
    public void showHomePage() {
        SceneManager.getInstance().switchPage("Home.fxml");
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

}