/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
    private Text tBienvenue;

    public VueAcceuil(FenetrePrincipale main) {
        this.setAlignment(Pos.TOP_CENTER);
        this.main = main;
        this.setId("vue-acceuil"); 
        this.bAffAnnonce = new Button("Afficher les annonces en cours ");
        this.bAffMesAnnonces = new Button("Afficher mes annonces en cours ");
        this.bAffMesEnchere = new Button("Afficher mes encheres en cours ");
        this.tBienvenue = new Text("Bienvenue");
        this.add(this.tBienvenue, 0, 0);
        this.add(this.bAffAnnonce, 0, 4);
        this.add(this.bAffMesAnnonces, 0, 6);
        this.add(this.bAffMesEnchere, 0, 8);
        this.bAffAnnonce.setOnAction((t) -> {
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
