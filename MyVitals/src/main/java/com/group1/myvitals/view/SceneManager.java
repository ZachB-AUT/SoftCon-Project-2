package com.group1.myvitals.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public static SceneManager getInstance() {
        return instance;
    }
}