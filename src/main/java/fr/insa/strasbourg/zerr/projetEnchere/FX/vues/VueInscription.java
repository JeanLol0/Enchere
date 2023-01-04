/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.createUtilisateur;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 *
 * @author jules
 */
public class VueInscription extends GridPane {

    private FenetrePrincipale main;
    
    private TextField tfNom;
    private TextField tfPrenom;
    private TextField tfEmail;
    private TextField tfCodePostal;
    
    private PasswordField pfPass;

    private Text tInscription;
    
    private Button bInscription;
    private Button bLogin;
    private Button bChoixPos;

    private Label lInscription;
    
    private Circle avatar;
    
    private int coordonnee;
    private double longitude;
    private double latitude;
    
   
    public VueInscription(FenetrePrincipale main) {
        this.coordonnee=0;
        this.main = main;
        this.setId("vue-connexion-inscription");

        this.tfNom = new TextField();
        this.tfPrenom = new TextField();
        this.tfEmail = new TextField();
        this.tfCodePostal = new TextField();
        this.avatar = new Circle(40, 40, 40);
        this.avatar.setFill(new ImagePattern(getImage("ressources/user.png")));
        

        this.tfNom.setPromptText("Nom");
        this.tfPrenom.setPromptText("Prenom");
        this.tfEmail.setPromptText("Adresse e-mail");
        this.tfCodePostal.setPromptText("Code postal");

        this.pfPass = new PasswordField();
        this.pfPass.setPromptText("Mot de passe");

        this.bInscription = new Button("Inscription");
        this.bInscription.setId("bouton-bleu");
        
        this.bLogin = new Button("Connection");
        this.bLogin.setId("bouton-vert");
        
        this.bChoixPos = new Button("Choisir ma position");
        this.bChoixPos.setId("bouton-rouge");
        
        this.bChoixPos.setOnAction((t) -> {
            try {
                this.main.setCenter(new VueCarte(this.main));
            } catch (IOException ex) {
                Logger.getLogger(VueInscription.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        this.lInscription = new Label("Inscription");
        this.lInscription.setId("grand-texte");

        this.add(this.lInscription, 0, 0);
        this.add(this.avatar, 0, 4);
        this.add(this.tfNom, 0, 5);
        this.add(this.tfPrenom, 0, 6);
        this.add(this.tfEmail, 0, 7);
        this.add(this.pfPass, 0, 8);
        this.add(this.tfCodePostal, 0, 9);
        this.add(this.bChoixPos, 0, 10);
        this.add(this.bInscription, 0, 11);
        this.add(this.bLogin, 0, 12);

        this.setVgap(20);
        this.setAlignment(Pos.CENTER);
        this.bInscription.setMaxWidth(Double.MAX_VALUE);
        this.bLogin.setMaxWidth(Double.MAX_VALUE);
        this.bChoixPos.setMaxWidth(Double.MAX_VALUE);
        this.setHalignment(this.lInscription, HPos.CENTER);
        this.setHalignment(this.avatar, HPos.CENTER);
        

        JavaFXUtils.DesactiveAutoFocus(tfNom);
        JavaFXUtils.DesactiveAutoFocus(tfEmail);
        JavaFXUtils.DesactiveAutoFocus(tfCodePostal);
        JavaFXUtils.DesactiveAutoFocus(tfPrenom);
        JavaFXUtils.DesactiveAutoFocus(pfPass);


        this.bInscription.setOnAction((t) -> {
            doInscription();
        });
        
        this.bLogin.setOnAction((t) -> {
            this.main.setCenter(new VueLogin(main));
        });
        this.avatar.setOnMouseClicked((t) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            
        });

    }
    public VueInscription(FenetrePrincipale main, double longitude, double latitude) {
        this.coordonnee=1;
        this.main = main;
        this.setId("vue-connexion-inscription");

        this.tfNom = new TextField();
        this.tfPrenom = new TextField();
        this.tfEmail = new TextField();
        this.tfCodePostal = new TextField();
        this.avatar = new Circle(40, 40, 40);
        this.avatar.setFill(new ImagePattern(getImage("ressources/user.png")));
        

        this.tfNom.setPromptText("Nom");
        this.tfPrenom.setPromptText("Prenom");
        this.tfEmail.setPromptText("Adresse e-mail");
        this.tfCodePostal.setPromptText("Code postal");

        this.pfPass = new PasswordField();
        this.pfPass.setPromptText("Mot de passe");

        this.bInscription = new Button("Inscription");
        this.bInscription.setId("bouton-bleu");
        
        this.bLogin = new Button("Connection");
        this.bLogin.setId("bouton-vert");
        
        this.bChoixPos = new Button("Modifier ma position");
        this.bChoixPos.setId("bouton-rouge");
        
        this.longitude = longitude;
        this.latitude= latitude;
        this.bChoixPos.setOnAction((t) -> {
            try {
                this.main.setCenter(new VueCarte(this.main));
            } catch (IOException ex) {
                Logger.getLogger(VueInscription.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        this.lInscription = new Label("Inscription");
        this.lInscription.setId("grand-texte");

        this.add(this.lInscription, 0, 0);
        this.add(this.avatar, 0, 4);
        this.add(this.tfNom, 0, 5);
        this.add(this.tfPrenom, 0, 6);
        this.add(this.tfEmail, 0, 7);
        this.add(this.pfPass, 0, 8);
        this.add(this.tfCodePostal, 0, 9);
        this.add(this.bChoixPos, 0, 10);
        this.add(this.bInscription, 0, 11);
        this.add(this.bLogin, 0, 12);

        this.setVgap(20);
        this.setAlignment(Pos.CENTER);
        this.bInscription.setMaxWidth(Double.MAX_VALUE);
        this.bLogin.setMaxWidth(Double.MAX_VALUE);
        this.bChoixPos.setMaxWidth(Double.MAX_VALUE);
        this.setHalignment(this.lInscription, HPos.CENTER);
        this.setHalignment(this.avatar, HPos.CENTER);
        

        JavaFXUtils.DesactiveAutoFocus(tfNom);
        JavaFXUtils.DesactiveAutoFocus(tfEmail);
        JavaFXUtils.DesactiveAutoFocus(tfCodePostal);
        JavaFXUtils.DesactiveAutoFocus(tfPrenom);
        JavaFXUtils.DesactiveAutoFocus(pfPass);


        this.bInscription.setOnAction((t) -> {
            doInscription();
        });
        
        this.bLogin.setOnAction((t) -> {
            this.main.setCenter(new VueLogin(main));
        });
        this.avatar.setOnMouseClicked((t) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            
        });
        

    }

    private void doInscription() {
        try {
            if (this.tfEmail.getText().isEmpty() || this.pfPass.getText().isEmpty()||this.coordonnee == 0) {
                JavaFXUtils.showErrorInAlert("Erreur", "Pas toutes les informations ont été rentrées", "");
                
            } else {
             Connection con = this.main.getBDD();
            createUtilisateur(con, this.tfNom.getText(), this.pfPass.getText(), this.tfPrenom.getText(), this.tfEmail.getText(),null,this.latitude, this.longitude);
            this.main.setCenter(new VueLogin(main));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(VueInscription.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private Image getImage(String resourcePath) {
        InputStream input //
                = this.getClass().getResourceAsStream(resourcePath);
        Image image = new Image(input);
        return image;
    }
    

}
