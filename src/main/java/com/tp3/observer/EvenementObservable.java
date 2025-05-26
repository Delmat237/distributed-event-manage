package com.tp3.observer;


public interface EvenementObservable {
    void ajouterObservateur(ParticipantObserver p);
    void supprimerObservateur(ParticipantObserver p);
    void notifierObservateurs(String message);
}
