/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.composants;

import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 *
 * @author jules
 */
public class Categories extends Accordion{
    private TitledPane TVehicule;
    //private VBox 
    private CheckBox Voiture;
    private CheckBox Motos;
    private CheckBox Caravaning;

  
    
    
    private TitledPane TImmobilier;
    private TitledPane TMode;    
    private TitledPane TMaison;
    private TitledPane TMultimedia;
    private TitledPane TLoisir;

      public Categories() {
          
          this.Voiture = new CheckBox("Voitures");
          this.Motos = new CheckBox("Motos");
          this.Caravaning = new CheckBox("Caravaning");
          this.TVehicule = new TitledPane();
          this.TVehicule.setText("Véhicules");
          this.TVehicule.setContent(TMode);
          ImageView im = new ImageView(new Image("fdfc"));
          this.getPanes().addAll(this.TVehicule);
    }
    
    
    
}
