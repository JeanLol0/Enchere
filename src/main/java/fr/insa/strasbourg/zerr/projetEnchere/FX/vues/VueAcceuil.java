/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 *
 * @author jules
 */
public class VueAcceuil extends GridPane{
    private FenetrePrincipale main;
    private Button bAfficheAnnonce;

    public VueAcceuil(FenetrePrincipale main) {
        this.main = main;
        this.setId("vue-acceuil"); 
        this.bAfficheAnnonce = new Button("Afficher les annonces en cours ");
        this.add(this.bAfficheAnnonce, 0, 0);
        this.bAfficheAnnonce.setOnAction((t) -> {
            try {
                this.main.setCenter(new VuePrincipale(this.main));
            } catch (SQLException ex) {
                Logger.getLogger(VueAcceuil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VueAcceuil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VueAcceuil.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
}
