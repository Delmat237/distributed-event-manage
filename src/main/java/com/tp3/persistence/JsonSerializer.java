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

    // Utilisation de Jackson pour sérialiser/désérialiser les objets
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Support des types Java 8 comme LocalDateTime
        mapper.registerModule(new JavaTimeModule());

        // Formatage lisible (indente joliment le JSON)
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Les dates sont sérialisées comme du texte ISO-8601 (ex: "2025-05-20T15:00:00")
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //Ignoré les champs inconnus
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Sérialise les événements contenus dans l'objet `GestionEvenements` vers un fichier JSON.
     *
     * @param gestion  Singleton contenant les événements à enregistrer
     * @param filename Nom du fichier (ex: "evenements.json")
     * @throws IOException en cas d'erreur d'écriture
     */
    public static void saveEvenementToJson(GestionEvenements gestion, String filename) throws IOException {
        mapper.writeValue(new File(filename), gestion.getEvenements());
    }

    /**
     * Charge les événements depuis un fichier JSON et les insère dans l'objet GestionEvenements passé en paramètre.
     *
     * @param gestion  L'instance singleton dans laquelle injecter les événements chargés
     * @param filename Le fichier JSON source (ex: "evenements.json")
     * @return
     * @throws IOException en cas d'erreur de lecture
     */
    public static Map<String, Evenement> loadEvenementFromJson(GestionEvenements gestion, String filename) throws IOException {
        // Lit le JSON comme une Map<String, Evenement>
        var loaded = mapper.readValue(new File(filename),
                mapper.getTypeFactory().constructMapType(
                        java.util.HashMap.class, String.class, Evenement.class));

        // Remplace les événements actuels par ceux chargés depuis le fichier
        gestion.setEvenements((Map<String, Evenement>) loaded);
        return (Map<String, Evenement>) loaded;
    }

    // --- PARTICIPANTS ---
    public static void saveParticipantsToJson(Participant participant, String filename) throws IOException {
        mapper.writeValue(new File(filename), participant);
    }

    public static Participant loadParticipant(String filename) throws IOException {
        return mapper.readValue(new File(filename), Participant.class);
    }



    // --- ORGANISATEURS ---
    public static void saveOrganisateurToJson(Organisateur organisateur, String filename) throws IOException {
        mapper.writeValue(new File(filename), organisateur);
    }

    //Chargement
    public static Organisateur loadOrganisateur(String filename) throws IOException {
        return mapper.readValue(new File(filename), Organisateur.class);
    }


}
