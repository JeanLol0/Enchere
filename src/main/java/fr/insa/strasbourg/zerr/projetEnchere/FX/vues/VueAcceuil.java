/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author jules
 */
public class VueAcceuil extends GridPane{
    private FenetrePrincipale main;
    private Button bAffAnnonce;
    private Button bAffMesAnnonces;
    private Button bAffMesEnchere;
    private Label tBienvenue;

    public VueAcceuil(FenetrePrincipale main) {
        this.setAlignment(Pos.CENTER_RIGHT);
        this.main = main;
        this.setId("vue-acceuil"); 
        
        
        Image image = getImage("ressources/acceuil.png");
        Background bg = new Background(new BackgroundImage(image, BackgroundRepeat.SPACE, BackgroundRepeat.SPACE, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
        this.setBackground(bg);
        
        this.bAffAnnonce = new Button("Afficher les annonces en cours ");
        this.bAffAnnonce.setId("bouton-annonce-en-cours");
        this.bAffMesAnnonces = new Button("Afficher mes annonces en cours ");
        this.bAffMesAnnonces.setId("bouton-mes-annonces");
        this.bAffMesEnchere = new Button("Afficher mes encheres en cours ");
        this.bAffMesEnchere.setId("bouton-mes-enchere");
        this.tBienvenue = new Label("Bienvenue");
        this.tBienvenue.setId("grand-titre-bienvenue");
        this.add(this.tBienvenue, 0, 0);
        this.add(this.bAffAnnonce, 0, 4);
        this.add(this.bAffMesAnnonces, 0, 6);
        this.add(this.bAffMesEnchere, 0, 8);
        
        this.setHalignment(this.tBienvenue, HPos.CENTER);
        this.setHalignment(this.bAffAnnonce, HPos.CENTER);
        this.setHalignment(this.bAffMesAnnonces, HPos.CENTER);
        this.setHalignment(this.bAffMesEnchere, HPos.CENTER);
        
        this.bAffAnnonce.setOnAction((t) -> {
            try {
                this.main.setCenter(new VuePrincipale(this.main));
            } catch (SQLException | ClassNotFoundException | IOException ex) {
                Logger.getLogger(VueAcceuil.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        this.bAffMesAnnonces.setOnAction((t) -> {
            try {
                this.main.setCenter(new VueMesAnnonces(this.main));
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(VueAcceuil.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        this.bAffMesEnchere.setOnAction((t) -> {
            try {
                this.main.setCenter(new VueMesEnchere(this.main));
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(VueAcceuil.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    private Image getImage(String resourcePath) {
        InputStream input //
                = this.getClass().getResourceAsStream(resourcePath);
        Image image = new Image(input);
        return image;
    }
    
}
