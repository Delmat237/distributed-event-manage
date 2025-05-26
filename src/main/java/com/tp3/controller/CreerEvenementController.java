package com.tp3.controller;

import com.sun.javafx.scene.control.IDisconnectable;
import com.tp3.exceptions.EvenementDejaExistantException;
import com.tp3.model.Concert;
import com.tp3.model.Conference;
import com.tp3.singleton.GestionEvenements;
import com.tp3.utils.SceneTransitionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreerEvenementController {
    @FXML
    private TextField heureDebutField,heureFinField;
    @FXML
    private Button btnCreer;
    @FXML
    private Button btnRetour;

    @FXML
    private ComboBox<String> comboType;
    @FXML
    private VBox boxConcert, boxConference;
    @FXML
    private TextField txtNom, txtLieu, txtCapacite, txtArtiste, txtGenre, txtTheme;
    @FXML
    private TextArea txtDescription, txtIntervenants;
    @FXML
    private DatePicker dateDebutField, dateFinField;

    @FXML
    public void initialize() {
        comboType.setOnAction(event -> handleTypeSelection());
        btnCreer.setOnAction(event -> {
            try {
                creerEvenement();
            } catch (EvenementDejaExistantException | IOException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Événement existant");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
        btnRetour.setOnAction(e -> {
            Stage stage = (Stage) btnRetour.getScene().getWindow();

             // <--- recharge les données
            SceneTransitionManager.switchSceneWithTransition(stage, "DashboardOrganisateurView", SceneTransitionManager.TransitionType.SLIDE_RIGHT);
        });

    }

    private void handleTypeSelection() {
        String selected = comboType.getValue();

        boxConcert.setVisible(false);
        boxConcert.setManaged(false);
        boxConference.setVisible(false);
        boxConference.setManaged(false);

        if ("Concert".equals(selected)) {
            boxConcert.setVisible(true);
            boxConcert.setManaged(true);
        } else if ("Conférence".equals(selected)) {
            boxConference.setVisible(true);
            boxConference.setManaged(true);
        }
    }



    @FXML
    private void creerEvenement() throws EvenementDejaExistantException, IOException {
        String type = comboType.getValue();
        String nom = txtNom.getText();
        String lieu = txtLieu.getText();
        int capacite = Integer.parseInt(txtCapacite.getText());


        LocalDateTime dateDebut = getDateTime( dateDebutField,heureDebutField);
        LocalDateTime dateFin = getDateTime( dateFinField,heureFinField);


        if ("Concert".equals(type)) {
            String artiste = txtArtiste.getText();
            String genre = txtGenre.getText();
            Concert concert = new Concert(UUID.randomUUID().toString(), nom, dateDebut,dateFin, lieu, capacite, artiste, genre);

            //Ajoute l'evenement en utilisant le gestionaire d'evenements
            GestionEvenements.getInstance().ajouterEvenement(concert);

            //Ajoute dasn les evenements de l'organisateurs
            DashboardOrganisateurController.organisateur.ajouterEvenement(concert);
            
        } else if ("Conférence".equals(type)) {
            String theme = txtTheme.getText();
            List<String> intervenants = Arrays.stream(txtIntervenants.getText().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            Conference conf = new Conference(UUID.randomUUID().toString(), nom, dateDebut,dateFin, lieu, capacite, theme, intervenants);

            //Ajoute l'evenement en utilisant le gestionaire d'evenements
            GestionEvenements.getInstance().ajouterEvenement(conf);

            //Ajoute dasn les evenements de l'organisateurs
            DashboardOrganisateurController.organisateur.ajouterEvenement(conf);

        }

        //RETOUR
        //MIS A JOUR


        Stage stage = (Stage) btnRetour.getScene().getWindow();
        SceneTransitionManager.switchSceneWithTransition(stage, "DashboardOrganisateurView", SceneTransitionManager.TransitionType.SLIDE_RIGHT);
    }

    //methode permettant de valider l'heure
    private boolean isHeureValide(String heure) {
        return heure.matches("^([01]\\d|2[0-3]):[0-5]\\d$");
    }

    //methode pour generer un LacalDateTime à partie de dATE ET HEURE

    private LocalDateTime getDateTime(DatePicker datePicker, TextField heureField) {
        LocalDate date = datePicker.getValue();
        String heureText = heureField.getText();

        if (date == null || heureText == null || heureText.isEmpty()|| !isHeureValide((heureText)) ) return null;

        try {
            LocalTime time = LocalTime.parse(heureText); // format: HH:mm
            return LocalDateTime.of(date, time);
        } catch (DateTimeParseException e) {
            // Gère l'erreur (affichage message, logger, etc.)
            System.err.println("Format d'heure invalide : " + heureText);
            return null;
        }
    }

}
