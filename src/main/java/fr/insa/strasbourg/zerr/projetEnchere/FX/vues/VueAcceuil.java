/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import javafx.scene.layout.GridPane;

/**
 *
 * @author jules
 */
public class VueAcceuil extends GridPane{
    private FenetrePrincipale main;

    public VueAcceuil(FenetrePrincipale main) {
        this.main = main;
        this.setId("vue-acceuil");
    }
    
}
