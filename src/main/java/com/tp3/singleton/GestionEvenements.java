package com.tp3.singleton;
import com.tp3.exceptions.EvenementDejaExistantException;
import com.tp3.model.Evenement;
import com.tp3.persistence.JsonSerializer;
import com.tp3.persistence.XmlSerializer;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class GestionEvenements {
    // Singleton
    // Instance unique de la classe
    private static GestionEvenements instance;
    private Map<String, Evenement> evenements = new HashMap<>();
    private Logger LOGGER = Logger.getLogger(GestionEvenements.class.getName());
    private GestionEvenements() {}

    public static GestionEvenements getInstance() {
        /*
         Si aucune instance n'est lancée , on créée ,
         si non on renvoie l'instance est cours

         */
        if (instance == null) {
            instance = new GestionEvenements();
        }
        return instance;
    }

    public void ajouterEvenement(Evenement event) throws EvenementDejaExistantException, IOException {
        // Étape 1 : Charger les événements JSON existants (s’ils existent)
        try {
            JsonSerializer.loadEvenementFromJson(this,"evenements.json");

        } catch (IOException e) {
            System.err.println("⚠️ Aucune donnée JSON existante ou erreur de lecture.");
        }

        // Étape 2 : Vérifier doublon
        if (getEvenements().containsKey(event.getId())) {
            throw new EvenementDejaExistantException("⚠️ Evenement déjà existant avec l'ID : " + event.getId());
        }

        // Étape 3 : Ajouter
        evenements.put(event.getId(), event);
        LOGGER.info("✅ AJOUT DE L'EVENEMENT '" + event.getNom() + "' RÉUSSI");

        // Étape 4 : Sérialiser en JSON
        try {
            JsonSerializer.saveEvenementToJson(this, "evenements.json");
            System.out.println("✅ Données enregistrées en JSON.");
        } catch (IOException e) {
            System.err.println("❌ Erreur JSON: " + e.getMessage());
        }

        // Étape 5 : Sérialiser en XML
        try {
            List<Evenement> listeEvenements = new ArrayList<>(this.getEvenements().values());
            XmlSerializer.saveEvenementsToXml(listeEvenements, "evenements.xml");
            System.out.println("✅ Données enregistrées en XML.");
        } catch (JAXBException e) {
            System.err.println("❌ Erreur XML: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void supprimerEvenement(String id) throws IOException {
        LOGGER.info("SUPPRESSION DE L'EVENEMENT "+ getEvenements().get(id).getNom() + " REUSSI");
        evenements.remove(id);

    }

    public Optional<Evenement> rechercherEvenement(String id) throws IOException {
        LOGGER.info("RECHERCHE DE L'EVENEMENT "+ getEvenements().get(id).getNom() + "REUSSI");
        return Optional.ofNullable(evenements.get(id));
    }

    public Map<String, Evenement> getEvenements() throws IOException {
        return  JsonSerializer.loadEvenementFromJson(this,"evenements.json");
    }

    public void setEvenements(Map<String, Evenement> evenements) {
        this.evenements = evenements;
    }
}
