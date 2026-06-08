package com.group1.myvitals.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("MyVitals Health Data Manager");
        stage.setWidth(1000);
        stage.setHeight(700);
        SceneManager.getInstance().initialise(stage);
        SceneManager.getInstance().showLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
