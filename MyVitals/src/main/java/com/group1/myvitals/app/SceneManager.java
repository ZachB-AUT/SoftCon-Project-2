package com.group1.myvitals.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {

    private static SceneManager instance;
    private Stage stage;

    private SceneManager(Stage stage) {
        this.stage = stage;
    }

    public static void initialise(Stage stage) {
        instance = new SceneManager(stage);
    }

    public static SceneManager getInstance() {
        return instance;
    }
}