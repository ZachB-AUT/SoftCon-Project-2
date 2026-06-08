package com.group1.myvitals.view;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.initialise(stage);
        stage.setTitle("MyVitals Health Data Manager");
        stage.setWidth(1000);
        stage.setHeight(700);
        //SceneManager.getInstance().showLogin();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
