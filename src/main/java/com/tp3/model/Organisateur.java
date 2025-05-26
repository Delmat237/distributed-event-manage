package com.tp3.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tp3.exceptions.EvenementDejaExistantException;
import com.tp3.persistence.JsonSerializer;
import com.tp3.singleton.GestionEvenements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

//Utile lors de la deserialisation du xml
@JsonTypeName("organisateur")
@XmlRootElement(name = "organisateur")
@XmlAccessorType(XmlAccessType.FIELD)
public class Organisateur extends Participant {
  private List<Evenement> evenementsOrganises =  new ArrayList<>();;


    public Organisateur(String id, String nom, String email,String password) {
        super(id, nom, email,password);

    }

    public Organisateur(String id, String nom, String email, String password, List<Evenement> evenementsOrganises) {
        super(id, nom, email,password);
        this.evenementsOrganises = evenementsOrganises;

    }
    public Organisateur(){ super();}

    public List<Evenement> getEvenementsOrganises() {
        return this.evenementsOrganises;
    }

    public void setEvenementsOrganises(List<Evenement> evenementsOrganises) {
        this.evenementsOrganises = evenementsOrganises;
    }


        //leve l'exception EvenementDejaExistantException.
    public void ajouterEvenement(Evenement event) throws EvenementDejaExistantException, IOException {
            this.evenementsOrganises.add(event);

            //enregistrer dans le fichier JSON
        JsonSerializer.saveOrganisateurToJson(this,"organisateur.json");
    }


}
