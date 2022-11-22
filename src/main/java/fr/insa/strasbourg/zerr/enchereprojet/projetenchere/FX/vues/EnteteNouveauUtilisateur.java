/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.vues;

import static fr.insa.strasbourg.zerr.enchereprojet.projetenchere.BDD.*;
import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.VuePrincipale;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author jules
 */
public class EnteteNouveauUtilisateur extends VBox {

    private VuePrincipale main;

    private TextField tfNom;
    private TextField tfPrenom;
    private TextField tfEmail;

    private PasswordField pfPass;

    public EnteteNouveauUtilisateur(VuePrincipale main) {
        this.main = main;
        this.tfNom = new TextField();
        this.tfEmail = new TextField();
        this.tfPrenom = new TextField();
        this.pfPass = new PasswordField();

        this.getChildren().addAll(new Label("nom :"), this.tfNom,
                new Label("prenom"), this.tfPrenom,
                new Label("email"), this.tfEmail,
                new Label("pass"), this.pfPass);

        Connection con = this.main.getBDD();
        Button bNouveauUtilisateur = new Button("Créer Utilisateur");

        bNouveauUtilisateur.setOnAction((t) -> {
            try {
                //            String nom1 = this.tfNom.getText();
//            String pass1 = this.pfPass.getText();
//            String prenom1 = this.tfPrenom.getText();
//            String email1 = this.tfEmail.getText();
//            try {
//                createUtilisateur(con, nom1, pass1, prenom1, email1);
//            } catch (SQLException ex) {
//                Logger.getLogger(EnteteNouveauUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
//            }
               
                createUtilisateur(con, this.tfNom.getText(), this.pfPass.getText(), this.tfPrenom.getText(), this.tfEmail.getText());
                System.out.println("utilisateur créé");
                this.main.setCenter(new EnteteLogin(main));
            } catch (SQLException ex) {
                Logger.getLogger(EnteteNouveauUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.getChildren().add(bNouveauUtilisateur);

    }
}
