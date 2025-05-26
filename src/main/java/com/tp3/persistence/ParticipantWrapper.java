package com.tp3.persistence;

import com.tp3.model.Participant;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "participants")
public class ParticipantWrapper {

    private List<Participant> participants;

    @XmlElement(name = "participant")
    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
