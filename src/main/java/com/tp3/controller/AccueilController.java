package com.tp3.controller;

import com.tp3.exceptions.CapaciteMaxAtteinteException;
import com.tp3.model.Evenement;
import com.tp3.model.Participant;
import com.tp3.persistence.JsonSerializer;
import com.tp3.singleton.GestionEvenements;
import com.tp3.utils.EvenementUtils;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur de la vue d'accueil, gérant l'affichage, la recherche et l'inscription aux événements.
 */
public class AccueilController implements Initializable {

    // Références vers les éléments de l'interface FXML
    @FXML private VBox detailsPane, emptyEventContainer;
    @FXML private Label nomLabel, dateLabel, lieuLabel, capacityLabel, dateFinLabel, placeLabel;
    @FXML private FlowPane evenementListContainer;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> lieuComboBox;
    @FXML private CheckBox avenirCheckBox;
    @FXML private CheckBox completCheckBox;

    // Liste des événements originaux et filtrés
    private List<Evenement> evenementsOriginaux;
    private List<Evenement> evenementsFiltres;

    // Événement sélectionné et utilisateur courant
    private Evenement evenementSelected;
    private Participant user;

    /**
     * Méthode appelée à l'initialisation du contrôleur.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Récupération des événements depuis le singleton
        Map<String, Evenement> evenementMap = GestionEvenements.getInstance().getEvenements();
        List<Evenement> evenements = evenementMap.values().stream().toList();

        // Affichage d'un conteneur vide si aucun événement
        if (evenements == null || evenements.isEmpty()) {
            emptyEventContainer.setVisible(true);
        } else {
            emptyEventContainer.setVisible(false);
            for (Evenement e : evenements) {
                afficherEvenement(e);
            }
        }

        // Initialisation des listes et filtres
        this.evenementsOriginaux = evenements;
        this.lieuComboBox.getItems().add("Tous");
        this.lieuComboBox.getItems().addAll(
                evenementsOriginaux.stream()
                        .map(Evenement::getLieu)
                        .distinct()
                        .collect(Collectors.toList())
        );
        this.lieuComboBox.getSelectionModel().select("Tous");
        appliquerFiltres(); // Filtrage initial
    }

    /**
     * Affiche un événement sous forme de carte dans l'interface.
     */
    private void afficherEvenement(Evenement evenement) {
        VBox card = new VBox();

        // Animation d'apparition
        FadeTransition fade = new FadeTransition(Duration.millis(500), card);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        card.getStyleClass().add("evenement-card");

        // Infos de l'événement
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
        });

        card.getChildren().addAll(title, date, desc, detailsBtn);
        evenementListContainer.getChildren().add(card);
    }

    /**
     * Gère la fermeture du panneau de détails.
     */
    @FXML
    public void handleFermerDetails() {
        detailsPane.setVisible(false);
        detailsPane.setManaged(false);
    }

    /**
     * Affiche les détails d’un événement sélectionné.
     */
    public void afficherDetails(Evenement evenement) {
        nomLabel.setText("📛 Nom : " + evenement.getNom());
        dateLabel.setText("📅 Date debut : " + evenement.getDateDebut());
        dateFinLabel.setText("📅 Date fin : " + evenement.getDateFin());
        lieuLabel.setText("📍 Lieu : " + evenement.getLieu());
        capacityLabel.setText("👥 Capacité max :  " + evenement.getCapaciteMax() + " participants");

        int nb = evenement.getCapaciteMax() - evenement.getNombreParticipants();
        placeLabel.setText("👥 Nombre de Place restant : " + nb);

        detailsPane.setVisible(true);
        detailsPane.setManaged(true);
    }

    /**
     * Gère l’inscription de l’utilisateur à un événement.
     */
    @FXML
    public void handleinscription() throws IOException, CapaciteMaxAtteinteException {
        if (LoginController.roleCurrentUser == null)
            return;

        // Charger l'utilisateur connecté selon son rôle
        if (LoginController.roleCurrentUser.equals("Organisateur"))
            user = JsonSerializer.loadOrganisateur("organisateur.json");
        else if (LoginController.roleCurrentUser.equals("Participant"))
            user = JsonSerializer.loadOrganisateur("participant.json");

        // Inscription à l’événement
        evenementSelected.ajouterParticipant(user);
    }

    /**
     * Applique tous les filtres : mot-clé, lieu, à venir, complet.
     */
    private void appliquerFiltres() {
        List<Evenement> liste = new ArrayList<>(evenementsOriginaux);

        // Filtre par mot-clé
        String motCle = searchField.getText();
        if (motCle != null && !motCle.isEmpty()) {
            liste = EvenementUtils.rechercherParMotCle(liste, motCle);
        }

        // Filtre par lieu
        String lieuSelectionne = lieuComboBox.getValue();
        if (lieuSelectionne != null && !"Tous".equals(lieuSelectionne)) {
            liste = liste.stream()
                    .filter(e -> e.getLieu().equalsIgnoreCase(lieuSelectionne))
                    .collect(Collectors.toList());
        }

        // Événements à venir uniquement
        if (avenirCheckBox.isSelected()) {
            liste = EvenementUtils.filtrerEvenementsAVenir(liste);
        }

        // Événements complets uniquement
        if (completCheckBox.isSelected()) {
            liste = EvenementUtils.filtrerComplets(liste);
        }

        this.evenementsFiltres = liste;
        afficherEvenements(liste);
    }

    /**
     * Affiche une liste d’événements.
     */
    private void afficherEvenements(List<Evenement> liste) {
        evenementListContainer.getChildren().clear();

        if (liste.isEmpty()) {
            emptyEventContainer.setVisible(true);
        } else {
            emptyEventContainer.setVisible(false);
            for (Evenement e : liste) {
                afficherEvenement(e);
            }
        }
    }

    // Handlers pour les différents filtres (champs de recherche, comboBox, checkBox)
    @FXML
    private void handleSearch() {
        appliquerFiltres();
    }

    @FXML
    private void handleFiltreLieu() {
        appliquerFiltres();
    }

    @FXML
    private void handleFiltreAvenir() {
        appliquerFiltres();
    }

    @FXML
    private void handleFiltreComplet() {
        appliquerFiltres();
    }

    /**
     * Réinitialise tous les filtres et recharge tous les événements.
     */
    @FXML
    private void handleResetFiltre() {
        searchField.clear();
        lieuComboBox.getSelectionModel().select("Tous");
        avenirCheckBox.setSelected(false);
        completCheckBox.setSelected(false);
        appliquerFiltres();
    }
}
