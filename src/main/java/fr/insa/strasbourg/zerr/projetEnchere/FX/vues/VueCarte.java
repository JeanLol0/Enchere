/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author jules
 */
public class VueCarte extends BorderPane{
    private FenetrePrincipale main;
    private Button bRetour;

    public VueCarte(FenetrePrincipale main) {
        this.main = main ;
        this.bRetour = new Button("Retour à l'inscription");
        this.setPrefSize(500, 400);
        this.setStyle("-fx-background-color:white");
        this.bRetour.setOnAction((t) -> {
            this.main.setCenter(new VueInscription(this.main));
        });
        this.setTop(this.bRetour);
    }
    
    
}
