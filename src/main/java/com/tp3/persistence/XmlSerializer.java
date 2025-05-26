package com.tp3.persistence;

import com.tp3.model.Evenement;
import com.tp3.model.Organisateur;
import com.tp3.model.Participant;

import javax.xml.bind.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Classe utilitaire pour la sérialisation/désérialisation des objets vers/depuis des fichiers XML
 * Utilise JAXB (Java Architecture for XML Binding)
 */
public class XmlSerializer {

    // ---------------------- EVENEMENTS ----------------------

    /**
     * Sauvegarde une liste d'événements dans un fichier XML.
     * @param evenements Liste d'objets Evenement à sauvegarder.
     * @param filePath Chemin du fichier XML à créer ou écraser.
     * @throws JAXBException si une erreur de sérialisation survient.
     */
    public static void saveEvenementsToXml(List<Evenement> evenements, String filePath) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(EvenementWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // Format lisible (indentation)

        EvenementWrapper wrapper = new EvenementWrapper();
        wrapper.setEvenements(evenements);

        File file = new File(filePath);

        // Création du dossier parent s'il n'existe pas
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Création du fichier s’il n’existe pas
        if (!file.exists()) {
            file.createNewFile();
        }
        marshaller.marshal(wrapper, file);
    }

    /**
     * Charge une liste d'événements depuis un fichier XML.
     * @param filePath Chemin du fichier XML à lire.
     * @return Liste d'objets Evenement désérialisés.
     * @throws JAXBException si une erreur de lecture/désérialisation survient.
     */
    public static List<Evenement> loadEvenementsFromXml(String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(EvenementWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        EvenementWrapper wrapper = (EvenementWrapper) unmarshaller.unmarshal(new File(filePath));
        return wrapper.getEvenements();
    }

    // ---------------------- PARTICIPANTS ----------------------

    /**
     * Sauvegarde une liste de participants dans un fichier XML.
     * @param participants Liste d'objets Participant à sauvegarder.
     * @param filePath Chemin du fichier XML à créer ou écraser.
     * @throws JAXBException si une erreur de sérialisation survient.
     */
    public static void saveParticipantsToXml(List<Participant> participants, String filePath) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(ParticipantWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        ParticipantWrapper wrapper = new ParticipantWrapper();
        wrapper.setParticipants(participants);
        File file = new File(filePath);

        // Création du dossier parent s'il n'existe pas
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Création du fichier s’il n’existe pas
        if (!file.exists()) {
            file.createNewFile();
        }
        marshaller.marshal(wrapper, file);

    }

    /**
     * Charge une liste de participants depuis un fichier XML.
     * @param filePath Chemin du fichier XML à lire.
     * @return Liste d'objets Participant désérialisés.
     * @throws JAXBException si une erreur de lecture/désérialisation survient.
     */
    public static List<Participant> loadParticipantsFromXml(String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ParticipantWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ParticipantWrapper wrapper = (ParticipantWrapper) unmarshaller.unmarshal(new File(filePath));
        return wrapper.getParticipants();
    }

    // ---------------------- ORGANISATEURS ----------------------

    /**
     * Sauvegarde une liste d'organisateurs dans un fichier XML.
     * @param organisateurs Liste d'objets Organisateur à sauvegarder.
     * @param filePath Chemin du fichier XML à créer ou écraser.
     * @throws JAXBException si une erreur de sérialisation survient.
     */
    public static void saveOrganisateursToXml(List<Organisateur> organisateurs, String filePath) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(OrganisateurWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        OrganisateurWrapper wrapper = new OrganisateurWrapper();
        wrapper.setOrganisateurs(organisateurs);

        File file = new File(filePath);

        // Création du dossier parent s'il n'existe pas
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Création du fichier s’il n’existe pas
        if (!file.exists()) {
            file.createNewFile();
        }
        marshaller.marshal(wrapper, file);

    }

    /**
     * Charge une liste d'organisateurs depuis un fichier XML.
     * @param filePath Chemin du fichier XML à lire.
     * @return Liste d'objets Organisateur désérialisés.
     * @throws JAXBException si une erreur de lecture/désérialisation survient.
     */
    public static List<Organisateur> loadOrganisateursFromXml(String filePath) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(OrganisateurWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        File file = new File(filePath);

        // Création du dossier parent s'il n'existe pas
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Création du fichier s’il n’existe pas
        if (!file.exists()) {
            file.createNewFile();
        }
        OrganisateurWrapper wrapper = (OrganisateurWrapper) unmarshaller.unmarshal(file);
        return wrapper.getOrganisateurs();
    }
}
