package com.tp3;

import com.tp3.exceptions.CapaciteMaxAtteinteException;
import com.tp3.exceptions.EvenementDejaExistantException;
import com.tp3.model.*;
import com.tp3.persistence.JsonSerializer;
import com.tp3.persistence.XmlSerializer;
import com.tp3.singleton.GestionEvenements;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainTest {

    public static void main(String[] args) throws EvenementDejaExistantException, CapaciteMaxAtteinteException {

        // Création d'un organisateur
        Organisateur organisateur = new Organisateur("O001","AZANGUE","azangueleonel9@gmail.com","2111");

        // Création de participants
        Participant p1 = new Participant("P001", "Alice", "leonel.azangue@facsciences-uy1.cm","1111");
        Participant p2 = new Participant("P002", "Bob", "balaandguefrancoislionnel@mail.com","1111");

        // Création d’une conférence
        Conference conf = new Conference(
                "E001", "JavaConf", LocalDateTime.of(2025, 6, 10, 9, 0),LocalDateTime.of(2025, 7, 5, 20, 0),
                "Amphi A", 100, "POO avancée", new ArrayList<>(List.of("Prof. Martin"))
        );

        // Création d’un concert
        Concert concert = new Concert(
                "E002", "RockFest", LocalDateTime.of(2025, 7, 5, 20, 0),LocalDateTime.of(2025, 7, 5, 22, 0),
                "Stade", 5000, "The Rockers", "Rock"
        );

        // Ajout au gestionnaire

        try {
            organisateur.ajouterEvenement(conf);
          //  organisateur.ajouterEvenement(conf);

            organisateur.ajouterEvenement(concert);

        } catch (EvenementDejaExistantException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Inscription des participants
        conf.ajouterParticipant(p1);
        conf.ajouterParticipant(p2);

        // Annulation = test de notification
        conf.annuler();

        // Sérialisation JSON

        GestionEvenements.getInstance().ajouterEvenement(conf);
        GestionEvenements.getInstance().ajouterEvenement(concert);
        //JsonSerializer.saveEvenementToJson(GestionEvenements.getInstance(), "evenements.json");
        System.out.println("✅ Données enregistrées en JSON.");

        // Désérialisation JSON
        try {
            GestionEvenements loaded = GestionEvenements.getInstance();
            JsonSerializer.loadEvenementFromJson(loaded, "evenements.json");
            System.out.println("✅ Données chargées depuis JSON.");
        } catch (IOException e) {
            System.err.println("Erreur de lecture JSON: " + e.getMessage());
        }

        // Sérialisation XML
        try {
            List<Evenement> evenements = new ArrayList<>(GestionEvenements.getInstance().getEvenements().values());

            XmlSerializer.saveEvenementsToXml(evenements, "evenements.xml");
            System.out.println("✅ Données enregistrées en XML.");
        } catch (JAXBException e) {
            System.err.println("Erreur XML: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Désérialisation XML
        try {
            List<Evenement> events = XmlSerializer.loadEvenementsFromXml("evenements.xml");
            System.out.println("✅ Données chargées depuis XML : " + events + " événements.");
        } catch (JAXBException e) {
            System.err.println("Erreur de lecture XML: " + e.getMessage());
        }

    }
}
