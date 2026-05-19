package com.group1.myvitals.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;
    private BorderPane mainMenu; 
    
    private SceneManager() {}

    public static SceneManager getInstance(){
        if (instance == null){
            instance = new SceneManager();
        }
        return instance;
    }

    public void initialise(Stage stage) {
        this.primaryStage = stage;
    }

    public void loadMainMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group1/myvitals/view/MainMenu.fxml"));
            this.mainMenu = loader.load();
            Scene scene = new Scene(mainMenu);
            primaryStage.setScene(scene);
        } catch(IOException e) {
            System.err.println("Error: Could not load MainMenu.fxml");
            e.printStackTrace();
        }
    }

    public void switchPage(String fxmlFile) {
        if (mainMenu == null) {
            loadMainMenu();
        }

        try {
            String path = "/com/group1/myvitals/view/" + fxmlFile;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent nextPage = loader.load();
            mainMenu.setCenter(nextPage);
        } catch(IOException e) {
            System.err.println("Error: Could not load page " + fxmlFile);
            e.printStackTrace();
        }
    }
}