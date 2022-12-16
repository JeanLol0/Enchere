/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.composants;

import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.FenetrePrincipale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Accordion;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 *
 * @author jules
 */
public class Categories extends Accordion {
    private FenetrePrincipale main;
    private ToggleGroup tg;
            

    private TitledPane TVehicule;
    private VBox content1;
    private RadioButton Voiture;
    private RadioButton Motos;
    private RadioButton Caravaning;

    private TitledPane TImmobilier;
    private VBox content2;
    private RadioButton Appartement;
    private RadioButton Maison;
    private RadioButton Terrain;

    private TitledPane TMode;
    private VBox content3;
    private RadioButton Vetements;
    private RadioButton Chaussures;
    private RadioButton MontreBijoux;
    private RadioButton Accessoires;
    
    
    private TitledPane TMaison;
    private VBox content4;
    private RadioButton Ameublement;
    private RadioButton Electromenager;
    private RadioButton Decoration;
    private RadioButton Bricolage;
    private RadioButton Jardinage;
    
    
    private TitledPane TMultimedia;
    private VBox content5;
    private RadioButton Informatique;
    private RadioButton ConsolesJV;
    private RadioButton ImageSon;
    private RadioButton Telephonie;
    
    private TitledPane TLoisir;
    private VBox content6;
    private RadioButton DVD_CD;
    private RadioButton Livres;
    private RadioButton Velos;
    private RadioButton SportsHobbies;
    private RadioButton JeuxJouets;

    public Categories(FenetrePrincipale main) {
        this.main = main;

        this.Voiture = new RadioButton("Voitures");
        this.Motos = new RadioButton("Motos");
        this.Caravaning = new RadioButton("Caravaning");
        this.TVehicule = new TitledPane();
        this.TVehicule.setText("Véhicules");
        this.content1 = new VBox();
        this.content1.getChildren().addAll(this.Voiture, this.Motos, this.Caravaning);
        this.TVehicule.setContent(content1);
        
        this.Appartement = new RadioButton("Appartement");
        this.Maison = new RadioButton("Maison");
        this.Terrain = new RadioButton("Terrain");
        this.TImmobilier = new TitledPane();
        this.TImmobilier.setText("Immobilier");
        this.content2 = new VBox(this.Maison,this.Appartement,this.Terrain);
        this.TImmobilier.setContent(content2);
        
        this.Vetements = new RadioButton("Vetements");
        this.Chaussures = new RadioButton("Chaussures");
        this.MontreBijoux = new RadioButton("Montre et Bijoux");
        this.Accessoires = new RadioButton("Accessoires");
        this.TMode = new TitledPane();
        this.TMode.setText("Mode");
        this.content3 = new VBox(this.Vetements,this.Chaussures,this.MontreBijoux,this.Accessoires);
        this.TMode.setContent(content3);
        
        this.Ameublement = new RadioButton("Ameublement");
        this.Electromenager = new RadioButton("Electoménager");
        this.Decoration = new RadioButton("Décoration");
        this.Bricolage = new RadioButton("Bricolage");
        this.Jardinage = new RadioButton("Jardinage");
        this.TMaison = new TitledPane();
        this.TMaison.setText("Maison");
        this.content4 = new VBox(this.Ameublement,this.Electromenager,this.Decoration,this.Bricolage,this.Jardinage);
        this.TMaison.setContent(content4);
        
        
        
        this.Informatique = new RadioButton("Informatique");
        this.ConsolesJV = new RadioButton("Consoles & jeux vidéo");
        this.ImageSon = new RadioButton("Image & Son");
        this.Telephonie = new RadioButton("Téléphonie");
        this.TMultimedia = new TitledPane();
        this.TMultimedia.setText("Multimédia");
        this.content5 = new VBox(this.Informatique,this.ConsolesJV,this.ImageSon,this.Telephonie);
        this.TMultimedia.setContent(content5);
        
        this.DVD_CD = new RadioButton("DVD & CD");
        this.Livres = new RadioButton("Livres");
        this.Velos = new RadioButton("Vélos");
        this.SportsHobbies = new RadioButton("Sports & Hobbies");
        this.JeuxJouets = new RadioButton("Jeux & jouets");
        this.TLoisir = new TitledPane();
        this.TLoisir.setText("Loisirs");
        this.content6 = new VBox(this.DVD_CD,this.Livres,this.Velos,this.SportsHobbies,this.JeuxJouets);
        this.TLoisir.setContent(content6);
        
        
        this.tg = new ToggleGroup();
        tg.getToggles().addAll(this.Voiture, this.Motos,this.Caravaning,
                this.Maison,this.Appartement,this.Terrain,
                this.Vetements,this.Chaussures,this.Accessoires,this.MontreBijoux,
                this.Ameublement,this.Electromenager,this.Decoration, this.Bricolage,this.Jardinage,
                this.Informatique,this.ConsolesJV,this.ImageSon,this.Telephonie,
                this.DVD_CD,this.Livres,this.Velos,this.SportsHobbies,this.JeuxJouets);
        
       
       this.getPanes().addAll(this.TVehicule, this.TImmobilier,this.TMode,this.TMaison,this.TMultimedia,this.TLoisir);
    }

    public ToggleGroup getTg() {
        return tg;
    }
    
    public String getString(){
        return this.getTg().getSelectedToggle().getProperties().getClass().getName();
        
    }
    
    
}
