package com.tp3.controller;

import com.tp3.exceptions.CapaciteMaxAtteinteException;
import com.tp3.model.Evenement;
import com.tp3.model.Participant;
import com.tp3.persistence.JsonSerializer;
import com.tp3.singleton.GestionEvenements;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public  class AccueilController implements Initializable {


    @FXML private VBox detailsPane,emptyEventContainer;
    @FXML private Label nomLabel,dateLabel,lieuLabel,capacityLabel,dateFinLabel,placeLabel;
    @FXML
    private FlowPane evenementListContainer;

    private  Evenement evenementSelected;
    private Participant user;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Evenement> evenements = null;

        //Recupere la liste des evenements
        Map<String, Evenement> evenementMap =  GestionEvenements.getInstance().getEvenements();
        evenements = evenementMap.values().stream().toList();


        if (evenements == null || evenements.isEmpty()) {
            emptyEventContainer.setVisible(true);
        } else {
            emptyEventContainer.setVisible(false);
            for (Evenement e : evenements) {
                afficherEvenement(e);
            }
        }

    }

    //Ecouter la liste d'evenments pour savoir le quel est selectionné


    private void afficherEvenement(Evenement evenement) {

        VBox card = new VBox();
        //Ajouter une transition
        FadeTransition fade = new FadeTransition(Duration.millis(500), card);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        card.getStyleClass().add("evenement-card");

        Label title = new Label(evenement.getNom());
        title.getStyleClass().add("evenement-title");

        Label date = new Label("Date : " + evenement.getDateDebut().toString());
        date.getStyleClass().add("evenement-date");

        Label desc = new Label(evenement.getLieu());
        desc.setWrapText(true);
        desc.getStyleClass().add("evenement-description");

        Button detailsBtn = new Button("Voir détails");
        detailsBtn.setOnAction(e -> {
            evenementSelected = evenement;
            afficherDetails(evenement);
        }
        );

        card.getChildren().addAll(title, date, desc, detailsBtn);

        evenementListContainer.getChildren().add(card);
    }

    @FXML
    public void handleFermerDetails() {
        detailsPane.setVisible(false);
        detailsPane.setManaged(false);
    }

    public void afficherDetails(Evenement evenement) {
        nomLabel.setText("📛 Nom : " + evenement.getNom());
        dateLabel.setText("📅 Date debut : " + evenement.getDateDebut().toString());
        dateFinLabel.setText("📅 Date fin : " + evenement.getDateFin().toString());
        lieuLabel.setText("📍 Lieu : " + evenement.getLieu());
        capacityLabel.setText("👥 Capacité max :  " + evenement.getCapaciteMax() + " participants");
        int nb =  evenement.getCapaciteMax() - evenement.getNombreParticipants();
        placeLabel.setText("👥Nombre de Place restant   :  " + nb );

        detailsPane.setVisible(true);
        detailsPane.setManaged(true);
    }



    @FXML
    public void handleinscription() throws IOException, CapaciteMaxAtteinteException {
           // evenementSelected.ajouterParticipant();
        if (LoginController.roleCurrentUser == null) //s'il n'est pas encore logger
            return;
        else if (LoginController.roleCurrentUser.equals("Organisateur"))
            user = JsonSerializer.loadOrganisateur("organisateur.json");

        else if (LoginController.roleCurrentUser.equals("Participant"))
            user = JsonSerializer.loadOrganisateur("participant.json");

        //Ajout du participant
        evenementSelected.ajouterParticipant(user);

    }
}
