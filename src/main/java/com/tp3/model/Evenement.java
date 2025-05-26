package com.tp3.model;

import com.tp3.exceptions.CapaciteMaxAtteinteException;
import com.tp3.observer.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp3.utils.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//Utile lors de la deserialisation du xml
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Conference.class, Concert.class})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,//necessaire pour la deserialization
        property = "type" // champ "type" dans le JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Conference.class, name = "conference"),
        @JsonSubTypes.Type(value = Concert.class, name = "concert")
})
public abstract class Evenement implements EvenementObservable {
    protected String id;
    protected String nom;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    protected LocalDateTime dateDebut;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    protected LocalDateTime dateFin;

    protected String lieu;
    protected int capaciteMax;

    @javax.xml.bind.annotation.XmlTransient //tempor    ire , ne pas serializer
    protected List<ParticipantObserver> participants = new ArrayList<>();

    protected EvenementStatut statut; //"       A_VENIR, EN_COURS,ANNULE
    public Evenement(String id, String nom, LocalDateTime dateDebut,LocalDateTime dateFin, String lieu, int capaciteMax) {

        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
        this.statut = EvenementStatut.A_VENIR;
    }

    protected Evenement() {
        // requis par JAXB
    }

    // Getter
    public String getId(){return this.id;}
    public abstract String  getType();
    public String getNom(){return this.nom;}
    public String getLieu(){return this.lieu;}
    public LocalDateTime getDateDebut(){return this.dateDebut;}
    public LocalDateTime getDateFin(){return this.dateFin;}
    public int getCapaciteMax(){return this.capaciteMax;}
    public int getNombreParticipants(){return this.participants.size();}
    public EvenementStatut getStatut(){return this.statut;}


    //Setter
    public void setId(String id){this.id = id;}

    public void setNom(String nom){this.nom = nom;}
    public void setLieu(String lieu){this.lieu = lieu;}
    public void setCapaciteMax(int  capaciteMax){this.capaciteMax = capaciteMax;}
    public void setDateDebut(LocalDateTime date){this.dateDebut = date;}
    public void setDateFin(LocalDateTime date){this.dateFin = date;}
    public void setStatut(EvenementStatut statut){this.statut = statut;}

    //leve l'exception CapaciteMaxAtteinteException
 
    public void ajouterParticipant(ParticipantObserver p) throws CapaciteMaxAtteinteException {
        if (participants.size() >= capaciteMax) {
            p.notifier("L’événement est complet !");
            throw new CapaciteMaxAtteinteException("Capacité maximale atteinte pour l'événement : " + nom);
        }
        p.notifier("Inscription confirmée à l’événement : " + nom);
        participants.add(p);
        ajouterObservateur(p);
    }

    public void annuler() {
        notifierObservateurs("(L’événement '" + this.nom + "' a été annulé.");
        participants.clear();
    }

    public void afficherDetails() {
        System.out.println("Événement : " + this.nom + " | Lieu : " + this.lieu + " | Date : " + this.dateDebut);
    }

    @Override
    public void ajouterObservateur(ParticipantObserver p) {
        participants.add(p);
    }

    @Override
    public void supprimerObservateur(ParticipantObserver p) {
        participants.remove(p);
    }

    @Override
    public void notifierObservateurs(String message) {
        for (ParticipantObserver p : participants) {
            p.notifier(message);
        }
    }

}
