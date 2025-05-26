package com.tp3;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@SuppressWarnings({"CallToPrintStackTrace","FieldMayBeFinal","exports"})

public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        configureStage();
        initRootLayout();
    }

    private void configureStage() {
        primaryStage.setTitle("App déevenement");
        //primaryStage.getIcons().add(new Image("/com/alaanya/resources/com/alaanya/view/images/alaanya-com.png"));
        // primaryStage.setResizable(false);
    }

    public void initRootLayout() {
        try {
            // Load root layout from FXML file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/AcceuilView.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(true);
            primaryStage.setMinHeight(800);
            primaryStage.setMinWidth(800);


        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur de chargement FXML", e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
