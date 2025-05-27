package com.tp3.tests;

import com.tp3.exceptions.CapaciteMaxAtteinteException;
import com.tp3.exceptions.EvenementDejaExistantException;
import com.tp3.model.Conference;
import com.tp3.model.Participant;
import com.tp3.persistence.JsonSerializer;
import com.tp3.singleton.GestionEvenements;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EvenementTest {

    private Conference conference;
    private Participant participant;

    @BeforeEach
    void setUp() {
        conference = new Conference("TESTCONF","conf1", LocalDateTime.now(),LocalDateTime.now() ,"ENSPY", 2,"Conférence Java", Collections.singletonList("pio"));
        participant = new Participant("user1", "Alice", "alice@gmail.com","123456789");
    }

    @Test
    @DisplayName("Inscription d'un participant")
    void testAjouterParticipant() throws CapaciteMaxAtteinteException {
        conference.ajouterParticipant(participant);
        assertTrue(conference.getParticipants().contains(participant));
    }

    @Test
    @DisplayName("Désinscription d'un participant")
    void testSupprimerParticipant() throws CapaciteMaxAtteinteException {
        conference.ajouterParticipant(participant);
        conference.supprimerParticipant(participant);
        assertFalse(conference.getIntervenants().contains(participant));
    }

    @Test
    @DisplayName("Exception quand la capacité est atteinte")
    void testCapaciteMaxException() throws CapaciteMaxAtteinteException {
        Participant p2 = new Participant("user2", "Bob", "bob@gmail.com","123456789");
        Participant p3 = new Participant("user3", "Charlie", "charlie@gmail.com","123456789");

        conference.ajouterParticipant(participant);
        conference.ajouterParticipant(p2);

        CapaciteMaxAtteinteException thrown = assertThrows(
                CapaciteMaxAtteinteException.class,
                () -> conference.ajouterParticipant(p3)
        );

        assertEquals("Capacité maximale atteinte pour l'événement : conf1", thrown.getMessage());
    }

    @Test
    @DisplayName("Sérialisation/Désérialisation d'un événement JSON")
    void testJsonSerialization() throws IOException, CapaciteMaxAtteinteException, EvenementDejaExistantException {
        // Ajouter participant
        conference.ajouterParticipant(participant);

        // Injecter dans singleton
        GestionEvenements gestion = GestionEvenements.getInstance();
        gestion.getEvenements().clear();
        gestion.ajouterEvenement(conference);

        // Sauvegarde JSON
        String filename = "evenement_test.json";
        JsonSerializer.saveEvenementToJson(gestion, filename);

        // Nouvelle instance pour vérifier
        GestionEvenements nouvelleGestion = GestionEvenements.getInstance();
        nouvelleGestion.getEvenements().clear();

        // Chargement
        Map<String, ?> loaded = JsonSerializer.loadEvenementFromJson(nouvelleGestion, filename);
        assertTrue(loaded.containsKey("TESTCONF"));

        Conference loadedConf = (Conference) loaded.get("TESTCONF");
        assertEquals("conf1", loadedConf.getNom());
        assertEquals(1, loadedConf.getIntervenants().size());

        // Nettoyage
        new File(filename).delete();
    }
}
