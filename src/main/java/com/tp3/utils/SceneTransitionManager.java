package com.tp3.utils;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneTransitionManager {

    // Enumération des types de transitions disponibles
    public enum TransitionType {
        FADE,
        SLIDE_LEFT,
        SLIDE_RIGHT,
        SLIDE_UP,
        SLIDE_DOWN
    }

    /**
     * Méthode pour changer de scène avec une animation donnée
     */
    public static void switchSceneWithTransition(Stage stage, String fxmlName, TransitionType type) {
        Parent currentRoot = stage.getScene().getRoot();


        double width = stage.getScene().getWidth();
        double height = stage.getScene().getHeight();

        switch (type) {
            case FADE:
                playFadeOut(currentRoot, () -> loadScene(stage, fxmlName, type, width, height));
                break;
            case SLIDE_LEFT:
            case SLIDE_RIGHT:
            case SLIDE_UP:
            case SLIDE_DOWN:
                playSlideOut(currentRoot, () -> loadScene(stage, fxmlName, type, width, height), width, height, type);
                break;
        }
    }

    /**
     * Charge une nouvelle scène avec animation d'entrée
     */
    public static void loadScene(Stage stage, String fxmlName, TransitionType type, double width, double height) {
        try {
            Parent newRoot = FXMLLoader.load(SceneTransitionManager.class.getResource("/view/" + fxmlName + ".fxml"));
            Scene newScene = new Scene(newRoot);
            stage.setScene(newScene);


            if (type == TransitionType.FADE) {
                // Fade in
                FadeTransition fadeIn = new FadeTransition(Duration.millis(400), newRoot);
                newRoot.setOpacity(0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            } else {
                // Déterminer direction initiale selon le type
                switch (type) {
                    case SLIDE_LEFT -> newRoot.setTranslateX(width);
                    case SLIDE_RIGHT -> newRoot.setTranslateX(-width);
                    case SLIDE_UP -> newRoot.setTranslateY(height);
                    case SLIDE_DOWN ->newRoot.setTranslateY(-height);

                }

                // Animation de retour à la position normale
                TranslateTransition slideIn = new TranslateTransition(Duration.millis(400), newRoot);
                slideIn.setToX(0);
                slideIn.setToY(0);
                slideIn.play();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Animation de fondu sortant
     */
    public static void playFadeOut(Parent root, Runnable afterFinish) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> afterFinish.run());
        fadeOut.play();
    }

    /**
     * Animation de glissement sortant selon la direction
     */
    public static void playSlideOut(Parent root, Runnable afterFinish, double width, double height, TransitionType type) {
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), root);

        // Déplacement selon le type
        switch (type) {
            case SLIDE_LEFT -> slideOut.setToX(-width);
            case SLIDE_RIGHT -> slideOut.setToX(width);
            case SLIDE_UP -> slideOut.setToY(-height);
            case SLIDE_DOWN -> slideOut.setToY(height);
        }

        slideOut.setOnFinished(e -> afterFinish.run());
        slideOut.play();
    }

    public static void slideTo(Node container, Node newContent) {
        newContent.setTranslateX(container.getBoundsInLocal().getWidth());

        ((BorderPane) container).setCenter(newContent);

        TranslateTransition tt = new TranslateTransition(Duration.millis(400), newContent);
        tt.setFromX(container.getBoundsInLocal().getWidth());
        tt.setToX(0);
        tt.setInterpolator(Interpolator.EASE_BOTH);
        tt.play();
    }
}
