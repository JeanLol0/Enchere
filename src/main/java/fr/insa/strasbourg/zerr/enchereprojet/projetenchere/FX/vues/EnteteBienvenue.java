/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.vues;

import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.VuePrincipale;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author jules
 */
public class EnteteBienvenue extends VBox {
    private VuePrincipale main;
    private Label tfBienvenue;
    

    public EnteteBienvenue(VuePrincipale main) {
        this.main = main;
        this.tfBienvenue = new Label("Veuilliez créer un utilisateur ou vous connecter !!");
        this.getChildren().add(this.tfBienvenue);
        
        
    }
    
}
