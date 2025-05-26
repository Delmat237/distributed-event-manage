package com.tp3.controller;

import com.tp3.model.Organisateur;
import com.tp3.model.Participant;
import com.tp3.persistence.JsonSerializer;
import com.tp3.utils.PasswordUtils;
import com.tp3.utils.SceneTransitionManager;
import com.tp3.utils.LoadPage;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.UUID;

public class RegisterController {

    @FXML private Button registerButton;
    @FXML private Label errorLabel,emailError,passwordError;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private PasswordField passwordField,confirmPasswordField;

    @FXML private TextField emailField, nomField,prenomField;
    @FXML
    private VBox registerContainer;


    @FXML
    public void initialize() {
        registerButton.setOnAction(this::handleRegister);
        FadeTransition ft = new FadeTransition(Duration.millis(200), registerContainer);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    @FXML
    public void handleRegister(ActionEvent actionEvent) {

        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        boolean isValid = true;
        emailError.setText("");
        passwordError.setText("");

        if (nom.isEmpty()|| email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role == null) {
            errorLabel.setText("Tous les champs sont requis.");
            isValid = false;
        }

        if (email.isEmpty() || !email.contains("@")) {
            emailError.setText("Adresse email invalide");
            isValid = false;
        }

        if (!password.equals(confirmPassword)) {
            passwordError.setText("Les mots de passe ne correspondent pas.");
            isValid = false;
        }

        if (password.length() < 6) {
            passwordError.setText("Mot de passe trop court");
            isValid = false;
        }
        System.out.println(isValid);
        if (isValid ) {
            String id = UUID.randomUUID().toString();
            String hashPassword = PasswordUtils.hashPassword(password); //Hacher le mot de passe
            try {
                if ("Participant".equals(role)) {
                    //Enregistrement du partici^pant

                    Participant participant = new Participant(id, nom + " " + prenom, email, hashPassword);
                    JsonSerializer.saveParticipantsToJson(participant,"participant.json");
                } else {
                    Organisateur organisateur = new Organisateur(id, nom + " " + prenom, email, hashPassword);
                    System.out.println("hashpassword "+hashPassword);
                    JsonSerializer.saveOrganisateurToJson(organisateur,"organisateur.json");

                }

                // Redirection vers la page de connexion
                LoadPage.load(emailField, "LoginView", SceneTransitionManager.TransitionType.SLIDE_LEFT);

            } catch (Exception e) {
                errorLabel.setText("Erreur lors de l'inscription.");
                e.printStackTrace();
            }
        }else
            errorLabel.setText("Erreur  de connexion.");
    }

    public void goToLogin(ActionEvent actionEvent) throws IOException {
        LoadPage.load(emailField, "LoginView", SceneTransitionManager.TransitionType.SLIDE_LEFT);
    }
}
