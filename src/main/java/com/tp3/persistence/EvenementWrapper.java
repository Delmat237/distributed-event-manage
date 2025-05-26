package com.tp3.persistence;

import com.tp3.model.Evenement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "evenements")
public class EvenementWrapper {

    private List<Evenement> evenements;

    @XmlElement(name = "evenement")
    public List<Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<Evenement> evenements) {
        this.evenements = evenements;
    }
}