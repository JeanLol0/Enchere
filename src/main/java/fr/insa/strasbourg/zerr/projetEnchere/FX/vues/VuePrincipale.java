/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.StylesCSS;
import fr.insa.strasbourg.zerr.projetEnchere.FX.composants.TopBar;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author jules
 */
public class VuePrincipale extends BorderPane {
    
    private FenetrePrincipale main;
    
    private TopBar topBar;

    public VuePrincipale(FenetrePrincipale main) {
        this.main = main;
        //this.topBar = new TopBar(main);
        this.setTop(topBar);
        
        //this.setStyle("-fx-border-color :red;");
        StylesCSS.DarkTheme(this);
        //this.topBar.setStyle("-fx-background-color:#122e47;");
    }

}
