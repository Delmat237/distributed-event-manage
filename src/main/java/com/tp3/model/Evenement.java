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
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Conference.class),
        @JsonSubTypes.Type(value = Concert.class)
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

    @javax.xml.bind.annotation.XmlTransient //tempor//   ire , ne pas serializer
    @com.fasterxml.jackson.annotation.JsonIgnore
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
    public List<ParticipantObserver> getParticipants(){return this.participants;}
    public EvenementStatut getStatut(){
        mettreAJourStatut();
        return this.statut;
    }


    //Setter
    public void setId(String id){this.id = id;}

    public void setNom(String nom){this.nom = nom;}
    public void setLieu(String lieu){this.lieu = lieu;}
    public void setCapaciteMax(int  capaciteMax){this.capaciteMax = capaciteMax;}
    public void setDateDebut(LocalDateTime date){this.dateDebut = date;}
    public void setDateFin(LocalDateTime date){this.dateFin = date;}
    public void setStatut(EvenementStatut statut){this.statut = statut;}

    //leve l'exception CapaciteMaxAtteinteException
 
    public void ajouterParticipant(Participant p) throws CapaciteMaxAtteinteException {
        if (participants.size() >= capaciteMax) {
            p.notifier("L’événement est complet !");
            throw new CapaciteMaxAtteinteException("Capacité maximale atteinte pour l'événement : " + nom);
        }
        p.notifier("Inscription confirmée à l’événement : " + nom);
        ajouterObservateur(p);

    }

    public void annuler() {
        notifierObservateurs("L’événement '" + this.nom + "' a été annulé.");
        participants.clear();
    }

    public void afficherDetails() {
        System.out.println("Événement : " + nom);
        System.out.println("Type : " + getType());
        System.out.println("Lieu : " + lieu);
        System.out.println("Début : " + dateDebut);
        System.out.println("Fin : " + dateFin);
        System.out.println("Capacité : " + capaciteMax);
        System.out.println("Statut : " + statut);
        System.out.println("Participants inscrits : " + participants.size());
    }


    @Override
    public void ajouterObservateur(ParticipantObserver p) {
        if (!participants.contains(p)) {
            participants.add(p);
        }
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

    /*
    * Methode permettant d emettre a jour le'etat d'un evenement
     */
    public void mettreAJourStatut() {
        LocalDateTime maintenant = LocalDateTime.now();
        if (statut != EvenementStatut.ANNULE) {
            if (maintenant.isBefore(dateDebut)) {
                statut = EvenementStatut.A_VENIR;
            } else if (maintenant.isAfter(dateFin)) {
                statut = EvenementStatut.TERMINE;
            } else {
                statut = EvenementStatut.EN_COURS;
            }
        }
    }


}
