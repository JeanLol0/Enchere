/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.StylesCSS;
import java.sql.SQLException;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author jules
 */
public class VuePrincipale extends ScrollPane {
    
    private FenetrePrincipale main;
    private VueAnnonces vAnnonce;
    
    

    public VuePrincipale(FenetrePrincipale main) throws SQLException {
        this.main = main;
        this.setContent(new VueAnnonces(this.main));
        //this.topBar = new TopBar(main);
        
        
        //this.setStyle("-fx-border-color :red;");
        //StylesCSS.DarkTheme(this);
        //this.topBar.setStyle("-fx-background-color:#122e47;");
    }
    

}

