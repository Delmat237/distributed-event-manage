package com.tp3.controller;

import com.tp3.model.Evenement;
import com.tp3.model.Organisateur;
import com.tp3.singleton.GestionEvenements;
import com.tp3.utils.SceneTransitionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class MesEvenementsController {

    @FXML
    private Button btnRetour;

    @FXML
    private TableView<Evenement> tableMesEvenements;

    @FXML
    private TableColumn<Evenement, String> colNom;

    @FXML
    private TableColumn<Evenement, LocalDateTime> colDate;

    @FXML
    private TableColumn<Evenement, String> colStatut;

    @FXML
    private TableColumn<Evenement, Void> colActions;

    @FXML
    public void initialize() {
        // Initialisation des colonnes
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Récupération des événements de l'organisateur connecté
        Organisateur organisateur = DashboardOrganisateurController.organisateur;

        if (organisateur != null) {
            tableMesEvenements.setItems(FXCollections.observableArrayList(organisateur.getEvenementsOrganises()));
        }

        // Ajout des boutons d'action (modifier/supprimer) dans colActions si nécessaire
        // addActionButtonsToTable();

        btnRetour.setOnAction(e -> {
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            SceneTransitionManager.switchSceneWithTransition(stage, "DashboardOrganisateurView", SceneTransitionManager.TransitionType.SLIDE_RIGHT);
        });
    }
}
