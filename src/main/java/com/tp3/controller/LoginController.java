package com.tp3.controller;

import com.tp3.model.Organisateur;
import com.tp3.model.Participant;
import com.tp3.persistence.JsonSerializer;
import com.tp3.utils.LoadPage;
import com.tp3.utils.PasswordUtils;
import com.tp3.utils.SceneTransitionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginController {

    @FXML private ComboBox<String> roleComboBox;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;

    //utlise pour charger les vues
    static String roleCurrentUser;
    
    private Logger logger = Logger.getLogger(LoginController.class.getName());


    /**
     * Handler pour la connexion de l'utilisateur.
     * Charge les utilisateurs depuis les fichiers (JSON ou XML) et vérifie les identifiants.
     */
    public void handleLogin(ActionEvent actionEvent) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getValue();

        roleCurrentUser = selectedRole;

        if (selectedRole == null || email.isBlank() || password.isBlank()) {
           logger.info("Veuillez remplir tous les champs.");
            return;
        }

        boolean isAuthenticated = false;

        if ("Organisateur".equals(selectedRole)) {
            Organisateur organisateur =  JsonSerializer.loadOrganisateur("organisateur.json");

            //verification du mot de passe
            boolean isMatch = PasswordUtils.checkPassword(password, organisateur.getPassword());

            if (isMatch && email.equals(organisateur.getEmail())) {
                isAuthenticated = true;
            }
        } else  {
            Participant participant = JsonSerializer.loadParticipant("participant.json");
            //verification du mot de passe
            boolean isMatch = PasswordUtils.checkPassword(password, participant.getPassword()); // true
            if (isMatch && email.equals(participant.getEmail())) {
                isAuthenticated = true;
            }
        }


        if (!isAuthenticated)
           logger.info("Identifiants incorrects ou utilisateur non trouvé.");
        else
            LoadPage.load(emailField, "AcceuilView", SceneTransitionManager.TransitionType.SLIDE_DOWN);
    }

    /**
     * Redirige vers la page d'enregistrement.
     */
    public void goToRegister(ActionEvent actionEvent) throws IOException {
        LoadPage.load(emailField, "RegisterView", SceneTransitionManager.TransitionType.SLIDE_LEFT);
    }
}
