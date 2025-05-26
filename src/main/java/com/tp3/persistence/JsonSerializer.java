package com.tp3.persistence;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tp3.model.Evenement;
import com.tp3.model.Organisateur;
import com.tp3.model.Participant;
import com.tp3.singleton.GestionEvenements;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonSerializer {

    // ObjectMapper de Jackson pour la sérialisation/désérialisation
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Support des types Java 8 (LocalDate, LocalDateTime, etc.)
        mapper.registerModule(new JavaTimeModule());

        // Active le formatage lisible (joli indenté)
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Utilise le format ISO pour les dates (ex: "2025-05-26T14:30:00")
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Ignore les propriétés inconnues dans le JSON lors du chargement
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // ----------------------
    // ÉVÉNEMENTS
    // ----------------------

    /**
     * Sauvegarde tous les événements dans un fichier JSON.
     *
     * @param gestion  L’instance de GestionEvenements contenant les données
     * @param filename Le nom du fichier (ex : "evenements.json")
     * @throws IOException En cas de problème d’écriture
     */
    public static void saveEvenementToJson(GestionEvenements gestion, String filename) throws IOException {
        File file = new File(filename);

        // Création du dossier parent s'il n'existe pas
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Création du fichier s’il n’existe pas
        if (!file.exists()) {
            file.createNewFile();
        }

        // Écriture des événements au format JSON
        mapper.writeValue(file, gestion.getEvenements());
    }

    /**
     * Charge les événements depuis un fichier JSON et les insère dans la gestion.
     *
     * @param gestion  L’objet qui contiendra les événements chargés
     * @param filename Le fichier JSON source
     * @return La map d’événements chargés
     * @throws IOException En cas de lecture invalide
     */
    public static Map<String, Evenement> loadEvenementFromJson(GestionEvenements gestion, String filename) throws IOException {
        // Lecture du fichier JSON en tant que Map<String, Evenement>
        Map<String, Evenement> loaded = mapper.readValue(
                new File(filename),
                mapper.getTypeFactory().constructMapType(
                        java.util.HashMap.class, String.class, Evenement.class
                )
        );

        // Injection dans l’instance de gestion
        gestion.setEvenements(loaded);
        return loaded;
    }

    // ----------------------
    // PARTICIPANTS
    // ----------------------

    /**
     * Sauvegarde un participant dans un fichier JSON.
     *
     * @param participant Le participant à sauvegarder
     * @param filename    Le nom du fichier de destination
     * @throws IOException En cas d’erreur d’écriture
     */
    public static void saveParticipantsToJson(Participant participant, String filename) throws IOException {
        File file = new File(filename);

        // Crée les répertoires parents si besoin
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Crée le fichier s’il n’existe pas
        if (!file.exists()) {
            file.createNewFile();
        }

        // Écriture JSON du participant
        mapper.writeValue(file, participant);
    }

    /**
     * Charge un participant depuis un fichier JSON.
     *
     * @param filename Le nom du fichier contenant les données
     * @return Le participant désérialisé
     * @throws IOException En cas d’erreur de lecture
     */
    public static Participant loadParticipant(String filename) throws IOException {
        return mapper.readValue(new File(filename), Participant.class);
    }

    // ----------------------
    // ORGANISATEURS
    // ----------------------

    /**
     * Sauvegarde un organisateur dans un fichier JSON.
     *
     * @param organisateur L’organisateur à sauvegarder
     * @param filename     Le fichier de destination
     * @throws IOException En cas d’erreur d’écriture
     */
    public static void saveOrganisateurToJson(Organisateur organisateur, String filename) throws IOException {
        File file = new File(filename);

        // Crée les répertoires parents si nécessaire
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Crée le fichier s’il n’existe pas
        if (!file.exists()) {
            file.createNewFile();
        }

        // Écriture JSON de l’organisateur
        mapper.writeValue(file, organisateur);
    }

    /**
     * Charge un organisateur depuis un fichier JSON.
     *
     * @param filename Le fichier source
     * @return L’objet Organisateur désérialisé
     * @throws IOException En cas de lecture invalide
     */
    public static Organisateur loadOrganisateur(String filename) throws IOException {
        return mapper.readValue(new File(filename), Organisateur.class);
    }
}
