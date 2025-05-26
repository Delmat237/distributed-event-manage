package com.tp3.persistence;

import com.tp3.model.Organisateur;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "organisateurs")
public class OrganisateurWrapper {

    private List<Organisateur> organisateurs;

    @XmlElement(name = "organisateur")
    public List<Organisateur> getOrganisateurs() {
        return organisateurs;
    }

    public void setOrganisateurs(List<Organisateur> organisateurs) {
        this.organisateurs = organisateurs;
    }
}
