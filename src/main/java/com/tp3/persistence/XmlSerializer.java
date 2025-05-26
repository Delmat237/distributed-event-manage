package com.tp3.persistence;

import com.tp3.model.Evenement;
import com.tp3.model.Organisateur;
import com.tp3.model.Participant;

import javax.xml.bind.*;
import java.io.File;
import java.util.List;

public class XmlSerializer {

    // ---------------- EVENEMENTS ----------------

    public static void saveEvenementsToXml(List<Evenement> evenements, String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(EvenementWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        EvenementWrapper wrapper = new EvenementWrapper();
        wrapper.setEvenements(evenements);

        marshaller.marshal(wrapper, new File(filePath));
    }

    public static List<Evenement> loadEvenementsFromXml(String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(EvenementWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        EvenementWrapper wrapper = (EvenementWrapper) unmarshaller.unmarshal(new File(filePath));
        return wrapper.getEvenements();
    }

    // ---------------- PARTICIPANTS ----------------

    public static void saveParticipantsToXml(List<Participant> participants, String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ParticipantWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        ParticipantWrapper wrapper = new ParticipantWrapper();
        wrapper.setParticipants(participants);

        marshaller.marshal(wrapper, new File(filePath));
    }

    public static List<Participant> loadParticipantsFromXml(String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ParticipantWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ParticipantWrapper wrapper = (ParticipantWrapper) unmarshaller.unmarshal(new File(filePath));
        return wrapper.getParticipants();
    }

    // ---------------- ORGANISATEURS ----------------

    public static void saveOrganisateursToXml(List<Organisateur> organisateurs, String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(OrganisateurWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        OrganisateurWrapper wrapper = new OrganisateurWrapper();
        wrapper.setOrganisateurs(organisateurs);

        marshaller.marshal(wrapper, new File(filePath));
    }

    public static List<Organisateur> loadOrganisateursFromXml(String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(OrganisateurWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        OrganisateurWrapper wrapper = (OrganisateurWrapper) unmarshaller.unmarshal(new File(filePath));
        return wrapper.getOrganisateurs();
    }
}
