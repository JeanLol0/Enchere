/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.beuvron.utils.SceneManager;
import fr.insa.strasbourg.zerr.projetEnchere.FX.StylesCSS;
import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author jules
 */
public class VuePrincipale extends ScrollPane {
    
    private FenetrePrincipale main;
    private VueAnnonces vAnnonce;
    
    

    public VuePrincipale(FenetrePrincipale main) throws SQLException, ClassNotFoundException, IOException {
        
        this.main = main;
        this.setContent(new VueAnnonces(this.main));
        this.setFitToWidth(true);
        this.setId("scroll-annonce");
        //this.topBar = new TopBar(main);
        
        
        //this.setStyle("-fx-border-color :red;");
        //StylesCSS.DarkTheme(this);
        //this.topBar.setStyle("-fx-background-color:#122e47;");
    }
    

}

