/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.StylesCSS;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.createUtilisateur;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author jules
 */
public class VueInscription extends GridPane {

    private FenetrePrincipale main;
    
    private TextField tfNom;
    private TextField tfPrenom;
    private TextField tfEmail;
    private PasswordField pfPass;

    private Text tInscription;
    
    private Button bInscription;
    private Button bLogin;

    private Label lInscription;
    
    private VueCarte carte;

    public VueInscription(FenetrePrincipale main) {
        this.main = main;
        this.setId("vue-connexion-inscription");

        this.tfNom = new TextField();
        this.tfPrenom = new TextField();
        this.tfEmail = new TextField();
        
        this.carte = new VueCarte();

        this.tfNom.setPromptText("Nom");
        this.tfPrenom.setPromptText("Prenom");
        this.tfEmail.setPromptText("Adresse e-mail");

        this.pfPass = new PasswordField();
        this.pfPass.setPromptText("Mot de passe");

        this.bInscription = new Button("Inscription");
        this.bInscription.setId("bouton-bleu");
        
        this.bLogin = new Button("Connection");
        this.bLogin.setId("bouton-vert");

        this.lInscription = new Label("Inscription");
        this.lInscription.setId("grand-texte");

        this.add(this.lInscription, 0, 5);
        this.add(this.tfNom, 0, 10);
        this.add(this.tfPrenom, 0, 11);
        this.add(this.tfEmail, 0, 12);
        this.add(this.pfPass, 0, 13);
        this.add(this.carte, 0, 14);
        this.add(this.bInscription, 0, 15);
        this.add(this.bLogin, 0, 16);

        this.setVgap(20);
        this.setAlignment(Pos.TOP_CENTER);
        this.bInscription.setMaxWidth(Double.MAX_VALUE);
        this.bLogin.setMaxWidth(Double.MAX_VALUE);
        this.setHalignment(this.lInscription, HPos.CENTER);

        JavaFXUtils.DesactiveAutoFocus(tfNom);
        JavaFXUtils.DesactiveAutoFocus(tfEmail);
        JavaFXUtils.DesactiveAutoFocus(tfPrenom);
        JavaFXUtils.DesactiveAutoFocus(pfPass);


        this.bInscription.setOnAction((t) -> {
            doInscription();
        });
        
        this.bLogin.setOnAction((t) -> {
            this.main.setCenter(new VueLogin(main));
        });

    }

    private void doInscription() {
        try {
            if (this.tfEmail.getText().isEmpty() || this.pfPass.getText().isEmpty()) {
                JavaFXUtils.showErrorInAlert("Erreur", "Entrer un email", "");
                
            } else {
             Connection con = this.main.getBDD();
            createUtilisateur(con, this.tfNom.getText(), this.pfPass.getText(), this.tfPrenom.getText(), this.tfEmail.getText(),null);
            System.out.println("utilisateur créé");
            this.main.setCenter(new VueLogin(main));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(VueInscription.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
