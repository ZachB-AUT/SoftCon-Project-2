package com.group1.myvitals.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Singleton that owns the primary Stage and switches between top-level scenes.
 * Pattern: Singleton
 */
public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void initialise(Stage stage) {
        this.primaryStage = stage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /** Shows the login card centred on a full-screen background. */
    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group1/myvitals/view/Login.fxml")
            );
            Parent card = loader.load();
            StackPane root = new StackPane(card);
            root.setStyle("-fx-background-color: #f4ede8;");
            primaryStage.setScene(new Scene(root, 1000, 700));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Shows the main menu (sidebar + content area). */
    public void showMainMenu() {
        loadFullScene("/com/group1/myvitals/view/MainMenu.fxml");
    }

    private void loadFullScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(fxmlPath)
            );
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 1000, 700));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
