package com.tp3.utils;

import com.tp3.model.Evenement;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EvenementUtils {

    /**
     * Retourne les événements à venir
     */
    public static List<Evenement> filtrerEvenementsAVenir(List<Evenement> evenements) {
        return evenements.stream()
                .filter(e -> e.getDateDebut().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

    }

    /**
     * Retourne les événements triés par date
     */
    public static List<Evenement> trierParDate(List<Evenement> evenements) {
        return evenements.stream()
                .sorted(Comparator.comparing(Evenement::getDateDebut))
                .collect(Collectors.toList());
    }

    /**
     * Recherche par mot-clé dans le nom
     */
    public static List<Evenement> rechercherParMotCle(List<Evenement> evenements, String motCle) {
        return evenements.stream()
                .filter(e -> e.getNom().toLowerCase().contains(motCle.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Groupement par lieu
     */
    public static Map<String, List<Evenement>> grouperParLieu(List<Evenement> evenements) {
        return evenements.stream()
                .collect(Collectors.groupingBy(Evenement::getLieu));
    }

    /**
     * Événements complets (capacité atteinte)
     */
    public static List<Evenement> filtrerComplets(List<Evenement> evenements) {
        return evenements.stream()
                .filter(e -> e.getCapaciteMax() <= e.getNombreParticipants())
                .collect(Collectors.toList());
    }
}