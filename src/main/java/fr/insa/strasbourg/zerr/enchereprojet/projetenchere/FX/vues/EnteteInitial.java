/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.vues;

import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.VuePrincipale;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 *
 * @author jules
 */
public class EnteteInitial extends HBox {

    private VuePrincipale main;
    private Button bLogin;
    private Button bNouvelUtilisateur;

    public EnteteInitial(VuePrincipale main) {
        this.main = main;
//        TextField tfLogin = new  TextField("Connection");
//                TextField tfNouvelUtilisateur = new  TextField("Nouvel Utilisateur");

        this.bLogin = new Button("Connection");
        this.bNouvelUtilisateur = new Button("Nouvel Utilisateur");
        this.getChildren().addAll(this.bLogin, this.bNouvelUtilisateur);

        this.bLogin.setOnAction((t) -> {
            this.main.setCenter(new EnteteLogin(this.main));
        });

        this.bNouvelUtilisateur.setOnAction((t) -> {
            this.main.setCenter(new EnteteNouveauUtilisateur(this.main));
        });

    
}
}
