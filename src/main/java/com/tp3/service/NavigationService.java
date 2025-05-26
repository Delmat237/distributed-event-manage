package com.tp3.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class NavigationService {

    private static NavigationService instance;

    private NavigationService() {}

    public static NavigationService getInstance() {
        if (instance == null) {
            instance = new NavigationService();
        }
        return instance;
    }

    /**
     * Change complètement la scène (fenêtre) à partir d'un fichier FXML.
     */
    public void goTo(String fxmlPath, Node triggerNode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) triggerNode.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur de navigation vers : " + fxmlPath);
        }
    }

    /**
     * Version simplifiée : navigue sans `triggerNode`, si la scène courante est déjà connue.
     */
    public void goTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur de navigation vers : " + fxmlPath);
        }
    }
}

