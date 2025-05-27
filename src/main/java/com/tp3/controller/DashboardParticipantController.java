package com.tp3.controller;

import com.tp3.model.Evenement;
import com.tp3.model.EvenementStatut;
import com.tp3.singleton.GestionEvenements;
import com.tp3.utils.SceneTransitionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DashboardParticipantController {

    @FXML
    private Button btnLoggout;
    @FXML
    private BorderPane rootPane;
    @FXML
    private TableView<Evenement> tableEvenementsDisponibles;

    @FXML
    private TableColumn<Evenement, String> colNom;

    @FXML
    private TableColumn<Evenement, String> colDate;

    @FXML
    private TableColumn<Evenement, String> colLieu;

    @FXML
    private TableColumn<Evenement, String> colStatut;

    @FXML
    private Label nbTotalLabel;

    @FXML
    private Label nbAVenirLabel;

    @FXML
    private Label nbEnCoursLabel;

    /**
     * Méthode d'initialisation appelée après le chargement du FXML.
     */
    @FXML
    public void initialize() throws IOException {
        setupTableColumns();
        loadEventStats();
        loadTableData();

        btnLoggout.setOnAction(e -> {
            Stage stage = (Stage) btnLoggout.getScene().getWindow();
            SceneTransitionManager.switchSceneWithTransition(stage, "LoginView", SceneTransitionManager.TransitionType.FADE);
        });
    }

    /**
     * Initialise les colonnes de la table avec les propriétés des objets Evenement.
     */
    private void setupTableColumns() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    /**
     * Charge les statistiques à afficher dans les cartes du tableau de bord.
     */
    private void loadEventStats() throws IOException {
        List<Evenement> allEvents = GestionEvenements.getInstance().getEvenements().values().stream().toList();

        long total = allEvents.size();
        long aVenir = allEvents.stream().filter(e -> e.getDateDebut().isAfter(LocalDateTime.now())).count();
        long enCours = allEvents.stream().filter(e ->
                e.getDateDebut().isEqual(LocalDateTime.now()) && !EvenementStatut.ANNULE.equals(e.getStatut())
        ).count();

        nbTotalLabel.setText(String.valueOf(total));
        nbAVenirLabel.setText(String.valueOf(aVenir));
        nbEnCoursLabel.setText(String.valueOf(enCours));
    }

    /**
     * Charge les données dans la table.
     */
    private void loadTableData() throws IOException {
        ObservableList<Evenement> observableList = FXCollections.observableArrayList(
                GestionEvenements.getInstance().getEvenements().values()
        );
        tableEvenementsDisponibles.setItems(observableList);
    }

    /**
     * Méthode appelée lorsqu'on clique sur "Actualiser"
     */
    @FXML
    private void actualiserTableau() throws IOException {
        loadEventStats();
        loadTableData();
    }

    // BONUS : on peut ajouter ici une méthode pour s'inscrire à un événement plus tard.
}
