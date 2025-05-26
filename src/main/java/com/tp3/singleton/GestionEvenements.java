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

    // Instance unique (singleton)
    private static GestionEvenements instance;

    // Stockage des événements en mémoire (cache)
    private Map<String, Evenement> evenements = new HashMap<>();

    // Logger pour journalisation
    private final Logger LOGGER = Logger.getLogger(GestionEvenements.class.getName());

    // Constructeur privé pour empêcher l’instanciation directe (pattern Singleton)
    private GestionEvenements() {
        try {
            // Chargement des événements depuis le fichier JSON au démarrage
            Map<String, Evenement> loaded = JsonSerializer.loadEvenementFromJson(this, "evenements.json");
            if (loaded != null) {
                this.evenements = loaded;
            }
        } catch (IOException e) {
            LOGGER.warning("⚠️ Aucune donnée JSON chargée au démarrage : " + e.getMessage());
        }
    }

    // Méthode statique pour obtenir l’instance unique de la classe
    public static GestionEvenements getInstance() {
        if (instance == null) {
            instance = new GestionEvenements();
        }
        return instance;
    }

    /**
     * Ajoute un événement dans la liste.
     * @param event L’événement à ajouter
     * @throws EvenementDejaExistantException si un événement avec le même ID existe déjà
     */
    public void ajouterEvenement(Evenement event) throws EvenementDejaExistantException {
        // Vérifie si un événement avec cet ID existe déjà
        if (evenements.containsKey(event.getId())) {
            throw new EvenementDejaExistantException("⚠️ Événement déjà existant avec l'ID : " + event.getId());
        }

        // Ajoute l’événement à la map
        evenements.put(event.getId(), event);
        LOGGER.info("✅ AJOUT : " + event.getNom());

        // Sauvegarde dans les fichiers JSON et XML
        sauvegarder();
    }

    /**
     * Supprime un événement par son ID.
     * @param id L'identifiant de l’événement
     * @throws IOException si une erreur survient lors de la sauvegarde
     */
    public void supprimerEvenement(String id) throws IOException {
        Evenement event = evenements.get(id);

        if (event != null) {
            evenements.remove(id);
            LOGGER.info("🗑️ SUPPRIMÉ : " + event.getNom());
            sauvegarder();
        } else {
            LOGGER.warning("⚠️ Aucun événement trouvé avec ID : " + id);
        }
    }

    /**
     * Recherche un événement par son ID.
     * @param id L’identifiant de l’événement
     * @return Un Optional contenant l’événement s’il est trouvé, sinon vide
     */
    public Optional<Evenement> rechercherEvenement(String id) {
        Evenement event = evenements.get(id);
        if (event != null) {
            LOGGER.info("🔍 Événement trouvé : " + event.getNom());
        } else {
            LOGGER.warning("❌ Aucun événement trouvé avec ID : " + id);
        }
        return Optional.ofNullable(event);
    }

    /**
     * Renvoie tous les événements actuellement chargés en mémoire.
     * @return Une Map des événements
     */
    public Map<String, Evenement> getEvenements() {
        return evenements;
    }

    /**
     * Permet de redéfinir complètement la liste des événements.
     * @param evenements La nouvelle map d’événements
     */
    public void setEvenements(Map<String, Evenement> evenements) {
        this.evenements = evenements;
    }

    /**
     * Sauvegarde les événements en JSON et en XML.
     * Appelée après chaque modification (ajout ou suppression).
     */
    private void sauvegarder() {
        try {
            // Sauvegarde en JSON
            JsonSerializer.saveEvenementToJson(this, "evenements.json");

            // Sauvegarde en XML
            XmlSerializer.saveEvenementsToXml(new ArrayList<>(evenements.values()), "evenements.xml");

            LOGGER.info("✅ Données sauvegardées en JSON et XML.");
        } catch (IOException | JAXBException e) {
            LOGGER.severe("❌ Erreur de sauvegarde : " + e.getMessage());
        }
    }
}
