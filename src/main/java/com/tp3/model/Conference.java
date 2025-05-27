package com.tp3.model;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tp3.observer.ParticipantObserver;

import javax.xml.bind.annotation.*;

@JsonTypeName("conference")
@XmlRootElement(name = "conference")
@XmlAccessorType(XmlAccessType.FIELD)
public class Conference extends Evenement {
    private String theme;
    private List<String> intervenants;

    public Conference(String id, String nom, LocalDateTime dateDebut,LocalDateTime dateFin, String lieu, int capaciteMax, String theme, List<String> intervenants) {
        super(id, nom, dateDebut,dateFin, lieu, capaciteMax);
        this.theme = theme;
        this.intervenants = intervenants;
    }

    public Conference() {
        super();
    }

    @Override
    public String getType(){return "conference";}
    public String getTheme() {return theme; }
    public void setTheme(String theme){ this.theme = theme;}
    public List<String> getIntervenants() {return intervenants; }

    public void setIntervenants(List<String> intervenants){this.intervenants = intervenants;}

    public void supprimerParticipant(Participant participant) {
        this.intervenants.remove(participant);
    }


}
