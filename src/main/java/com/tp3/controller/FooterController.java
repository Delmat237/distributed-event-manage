package com.tp3.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Desktop;
import java.net.URI;

public class FooterController {



    @FXML
    public void handleGitHub(ActionEvent event) {
        openLink("https://github.com/Delmat237");
    }

    @FXML
    public void handleFacebook(ActionEvent event) {
        openLink("https://facebook.com/azangue");
    }

    @FXML
    public void handleWhatsApp(ActionEvent event) {
        openLink("https://wa.me/237657450314");
    }

    @FXML
    public void handleLinkedIn(ActionEvent event) {
        openLink("https://linkedin.com/in/leonel-azangue");
    }

    private void openLink(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

