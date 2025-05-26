package com.tp3.controller;

import com.tp3.model.Evenement;
import com.tp3.model.EvenementStatut;
import com.tp3.model.Organisateur;
import com.tp3.persistence.JsonSerializer;
import com.tp3.singleton.GestionEvenements;
import com.tp3.utils.SceneTransitionManager;
import com.tp3.utils.SceneTransitionManager.TransitionType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DashboardOrganisateurController {

    @FXML
    private BorderPane rootPane;
    @FXML
    private Button btnMessages, btnMesEvenements, btnCreer,btnLoggout;

    @FXML
    private TableView<Evenement> tableEvenements;

    @FXML
    private TableColumn<Evenement, String> colNom, colStatut;

    @FXML
    private TableColumn<Evenement, LocalDateTime> colDate;

    @FXML
    private Label lblOrganised, lblCancelled, lblOngoing, lblUpcoming;

    protected static Organisateur organisateur;

    /**
     * Méthode appelée automatiquement après le chargement du fichier FXML
     */
    @FXML
    public void initialize() throws IOException {
        // Initialisation des colonnes de la table
        initialiserColonnesTableau();

        // Chargement des statistiques globales
        chargerStatistiques();

        // Affichage des événements dans le tableau
        chargerEvenements();

        //Chargement des informations depuis le fichier json
        organisateur = JsonSerializer.loadOrganisateur("organisateur.json");

        // Configuration des boutons de navigation avec transition animée
        btnCreer.setOnAction(e -> {
            transitionnerVersVue("CreerEvenementView");
                    try {
                        refreshDashboard();
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }
                }
        );
        btnMesEvenements.setOnAction(e -> transitionnerVersVue("MesEvenementsView"));
        btnMessages.setOnAction(e -> transitionnerVersVue("MessagesView"));
        btnLoggout.setOnAction(e -> {
            Stage stage = (Stage) btnLoggout.getScene().getWindow();
            SceneTransitionManager.switchSceneWithTransition(stage, "LoginView", TransitionType.FADE);
        });
    }

    /**
     * Getter de l'organisateur connecté
     */
    public Organisateur getOrganisateur() {return this.organisateur;}

    /**
     * Setter de l'organisateur
     */

    public void setOrganisateur(Organisateur organisateur) {this.organisateur = organisateur;}

    /**
     * Initialise les colonnes du tableau avec les propriétés du modèle Evenement
     */
    private void initialiserColonnesTableau() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    /**
     * Charge les statistiques des événements pour l'affichage sur le tableau de bord
     */
    private void chargerStatistiques() throws IOException {
        List<Evenement> evenements = GestionEvenements.getInstance().getEvenements().values().stream().toList();

        long totalOrganises = evenements.size();
        long totalAnnules = evenements.stream()
                .filter(e -> EvenementStatut.ANNULE.equals(e.getStatut()))
                .count();
        long totalEnCours = evenements.stream()
                .filter(e -> EvenementStatut.EN_COURS.equals(e.getStatut()) &&
                        !e.getDateDebut().toLocalDate().isAfter(LocalDate.now()) &&
                        !e.getDateFin().toLocalDate().isBefore(LocalDate.now()))
                .count();
        long totalAVenir = evenements.stream()
                .filter(e -> e.getDateDebut().isAfter(LocalDateTime.now()) &&
                        !EvenementStatut.ANNULE.equals(e.getStatut()))
                .count();

        lblOrganised.setText(String.valueOf(totalOrganises));
        lblCancelled.setText(String.valueOf(totalAnnules));
        lblOngoing.setText(String.valueOf(totalEnCours));
        lblUpcoming.setText(String.valueOf(totalAVenir));
    }

    /**
     * Charge les événements à afficher dans le tableau
     */
    private void chargerEvenements() throws IOException {
        ObservableList<Evenement> data = FXCollections.observableArrayList(
                GestionEvenements.getInstance().getEvenements().values()
        );
        tableEvenements.setItems(data);
    }

    public void refreshDashboard() throws IOException {
        chargerStatistiques();
        chargerEvenements();
    }

    /**
     * Charge une vue dans la zone centrale avec une animation glissante.
     *
     * @param viewName nom du fichier FXML sans l'extension
     */
    private void transitionnerVersVue(String viewName) {
        System.out.println("Chargement de " + viewName);
        try {
            // Construction de l'URL vers la vue FXML
            URL url = getClass().getResource("/view/" + viewName + ".fxml");
            if (url == null) {
                System.out.println("Fichier " + viewName + " non trouvé !");
                return;
            }

            // Chargement de la vue depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(url);
            Node nouvelleVue = loader.load();

            // Transition glissante avec SceneTransitionManager
            SceneTransitionManager.slideTo(rootPane, nouvelleVue);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la vue : " + viewName);
            e.printStackTrace();
            // Tu peux afficher une alerte utilisateur ici
        }
    }
}
