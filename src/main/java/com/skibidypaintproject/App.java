package com.skibidypaintproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);

    private static Stage primaryStage;
    private static final double SMALL_WIDTH = 700;
    private static final double SMALL_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        setRoot("login");
        primaryStage.setTitle("PPG - Planificador Lotes");

        primaryStage.setResizable(true);
        primaryStage.setWidth(SMALL_WIDTH);
        primaryStage.setHeight(SMALL_HEIGHT);
        primaryStage.fullScreenProperty().addListener((obs, wasFullScreen, isNowFullScreen) -> {
            if (!isNowFullScreen) {
                primaryStage.setWidth(SMALL_WIDTH);
                primaryStage.setHeight(SMALL_HEIGHT);
            }
        });

        primaryStage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        logger.info("Loading page: " + fxml);
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/com/skibidypaintproject/Views/" + fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
    }

    public static void main(String[] args) {
        logger.info("Starting PPG Paint Project planification application");
        launch(args);
    }
}
