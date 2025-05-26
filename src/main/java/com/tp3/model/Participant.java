package com.tp3.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tp3.observer.*;
import com.tp3.service.EmailService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.concurrent.CompletableFuture;

//Utile lors de la deserialisation du xml
@JsonTypeName("participant")
@XmlRootElement(name = "participant")
@XmlAccessorType(XmlAccessType.FIELD)
public class Participant implements ParticipantObserver, NotificationService {
    protected String id;
    protected String nom;
    protected String email;
    protected String password;

    public Participant(String id, String nom, String email, String password) {
        this.id = id;
        this.nom = nom;
        this.email = email;

        //ajout du champ password
        this.password = password;
    }
    public Participant(){}

    @Override
    public void notifier(String message) {
        envoyerNotification(message);
    }

    @Override
    public void envoyerNotification(String message) {
        System.out.println("[Notification à " + nom + "] " + message);
        // Envoi d'email
        EmailService service = new EmailService();

        //pour ne pas bloquer le thread principal
        CompletableFuture.runAsync(() ->
             service.envoyerEmail(email, "Notification - Événement", message)
        );
    }

    //Getter
    public String getId() {return this.id;}
    public String getNom(){return this.nom;}
    public String getEmail(){return this.email;}
    public String getPassword() {return this.password;}

    //Setter
    public void setId(String id){ this.id = id;}
    public void setNom(String nom){this.nom = nom;}
    public void setEmail(String email){this.email = email;}
    public void setPassword(String password){this.password = password;}

}
