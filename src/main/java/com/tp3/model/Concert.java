package com.tp3.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("concert")
@XmlRootElement(name = "concert")
@XmlAccessorType(XmlAccessType.FIELD)
public class Concert extends Evenement {
    private String artiste;
    private  String genreMusical;

    public Concert(String id, String nom,  LocalDateTime dateDebut,LocalDateTime dateFin, String lieu, int capaciteMax, String artiste, String genreMusical) {
       super(id, nom, dateDebut,dateFin, lieu, capaciteMax);
        this.artiste = artiste;
        this.genreMusical = genreMusical;
       
    }

    public Concert() {
        super();
    }
    @Override
    public String getType(){return "concert";}
    public String getGenreMusical() {  return this.genreMusical; }

    public String getArtiste() {return artiste;}

    public void setArtiste(String artiste) {this.artiste = artiste;}
    public void setGenreMusical(String genreMusical) {this.genreMusical = genreMusical;}
}
