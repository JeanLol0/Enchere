/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

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
        this.bAfficheAnnonce = new Button("Afficher les annoces en cours ");
        this.add(this.bAfficheAnnonce, 0, 0);
        this.bAfficheAnnonce.setOnAction((t) -> {
            this.main.setCenter(new VuePrincipale(this.main));
        });
    }
    
}
