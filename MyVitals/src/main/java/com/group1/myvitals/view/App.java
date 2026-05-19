package com.group1.myvitals.view;

import javafx.application.Application;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.getInstance().initialise(primaryStage);
        SceneManager.getInstance().loadMainMenu();
        SceneManager.getInstance().switchPage("Home.fxml");
        primaryStage.setTitle("MyVitals Health Data Manager");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}