/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author jules
 */
public class VueNouvelleAnnonce extends GridPane{
    private Label label;
    private FenetrePrincipale main;
    private Text titre;
    private TextField tfTitre;
    

    public VueNouvelleAnnonce(FenetrePrincipale main) {
        this.main = main;
        this.label = new Label("page nouvelle annonce ");
        this.add(this.label, 1, 1);
    }
    
    
    
    
}
