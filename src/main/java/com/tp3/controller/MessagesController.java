// messagesController.java
package com.tp3.controller;

import com.tp3.utils.SceneTransitionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MessagesController {


    @FXML
    private TextArea txtMessage;

    @FXML
    private Button btnEnvoyer,btnRetour;

    @FXML
    public void initialize() {
        btnEnvoyer.setOnAction(e -> envoyerMessage());
        btnRetour.setOnAction(e -> {
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            SceneTransitionManager.switchSceneWithTransition(stage, "DashboardOrganisateurView", SceneTransitionManager.TransitionType.SLIDE_RIGHT);
        });
    }

    private void envoyerMessage() {
        String contenu = txtMessage.getText();
        if (!contenu.isBlank()) {
            System.out.println("Message envoyé : " + contenu);
            txtMessage.clear();
        }
    }
}