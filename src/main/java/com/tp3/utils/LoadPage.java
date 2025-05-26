package com.tp3.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class LoadPage {
    /**
     * Charge dynamiquement une page FXML donnée.
     *
     * @param view nom du fichier FXML (sans extension)
     */

    public static void load(TextField widget, String view, SceneTransitionManager.TransitionType transitionType) throws IOException {
        try {
            URL url = LoadPage.class.getResource("/view/" + view + ".fxml");
            if (url == null) {
                System.out.println("Fichier " + view + ".fxml non trouvé !");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            Stage stage = (Stage) widget.getScene().getWindow();

            // Utilisation de SceneTransitionManager pour une transition animée
            SceneTransitionManager.switchSceneWithTransition(stage,view,transitionType);

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
