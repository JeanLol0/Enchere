/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.composants.VueImage;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.createUtilisateur;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ContextMenu;

import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 *
 * @author jules
 */
public class VueInscription extends ScrollPane {

    private FenetrePrincipale main;
    private GridPane gridpane;
    private TextField tfNom;
    private TextField tfPrenom;
    private TextField tfEmail;
    private TextField tfCodePostal;

    private PasswordField pfPass;

    private Text tInscription;

    private Button bInscription;
    private Button bLogin;
    private Button bChoixPos;
    private Image image;
    private Label lInscription;

    private Circle avatar;

    private int coordonnee;
    private double longitude;
    private double latitude;

    public VueInscription(FenetrePrincipale main) {
        this.coordonnee = 0;
        this.main = main;
        this.setId("vue-connexion-inscription");

        Image image = getImage("ressources/background.png");
        Background bg = new Background(new BackgroundImage(image, BackgroundRepeat.SPACE, BackgroundRepeat.SPACE, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
        this.setBackground(bg);

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
        this.gridpane = new GridPane();
        this.gridpane.setAlignment(Pos.CENTER);
        this.setFitToWidth(true);
        this.gridpane.add(this.lInscription, 0, 0);
        this.gridpane.add(this.avatar, 0, 4);
        this.gridpane.add(this.tfNom, 0, 5);
        this.gridpane.add(this.tfPrenom, 0, 6);
        this.gridpane.add(this.tfEmail, 0, 7);
        this.gridpane.add(this.pfPass, 0, 8);
        this.gridpane.add(this.tfCodePostal, 0, 9);
        this.gridpane.add(this.bChoixPos, 0, 10);
        this.gridpane.add(this.bInscription, 0, 11);
        this.gridpane.add(this.bLogin, 0, 12);
        gridpane.setVgap(20);
        gridpane.setHgap(20);
        gridpane.setAlignment(Pos.TOP_CENTER);
        this.gridpane.setVgap(20);
        this.gridpane.setAlignment(Pos.CENTER);
        this.gridpane.setHalignment(this.lInscription, HPos.CENTER);
        this.gridpane.setHalignment(this.avatar, HPos.CENTER);
        this.gridpane.setHalignment(this.lInscription, HPos.CENTER);
        this.gridpane.setHalignment(this.avatar, HPos.CENTER);
        this.gridpane.setHalignment(this.bChoixPos, HPos.CENTER);
        this.gridpane.setHalignment(this.bInscription, HPos.CENTER);
        this.gridpane.setHalignment(this.bLogin, HPos.CENTER);
        this.setContent(this.gridpane);
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
            File file = fileChooser.showOpenDialog(getContextMenu());
            VueImage image2 = null; 
            try {
                image2 = new VueImage();
            } catch (IOException ex) {
                Logger.getLogger(VueInscription.class.getName()).log(Level.SEVERE, null, ex);
            }
            image2.mouseClicked(file);
            Image image3= image2.getImage();
            this.avatar.setFill(new ImagePattern(image3));
        });

    }

    

    public VueInscription(FenetrePrincipale main, double longitude, double latitude) {
        this.coordonnee = 1;
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
        this.latitude = latitude;
        this.bChoixPos.setOnAction((t) -> {
            try {
                this.main.setCenter(new VueCarte(this.main));
            } catch (IOException ex) {
                Logger.getLogger(VueInscription.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        this.lInscription = new Label("Inscription");
        this.lInscription.setId("grand-texte");

        this.gridpane.add(this.lInscription, 0, 0);
        this.gridpane.add(this.avatar, 0, 4);
        this.gridpane.add(this.tfNom, 0, 5);
        this.gridpane.add(this.tfPrenom, 0, 6);
        this.gridpane.add(this.tfEmail, 0, 7);
        this.gridpane.add(this.pfPass, 0, 8);
        this.gridpane.add(this.tfCodePostal, 0, 9);
        this.gridpane.add(this.bChoixPos, 0, 10);
        this.gridpane.add(this.bInscription, 0, 11);
        this.gridpane.add(this.bLogin, 0, 12);

        this.gridpane.setVgap(20);
        this.gridpane.setAlignment(Pos.CENTER);
        this.gridpane.setHalignment(this.lInscription, HPos.CENTER);
        this.gridpane.setHalignment(this.avatar, HPos.CENTER);
        this.gridpane.setHalignment(this.bChoixPos, HPos.CENTER);
        this.gridpane.setHalignment(this.bInscription, HPos.CENTER);
        this.gridpane.setHalignment(this.bLogin, HPos.CENTER);
        this.setContent(this.gridpane);
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
            File file = fileChooser.showOpenDialog(getContextMenu());
            VueImage image2 = null; 
            try {
                image2 = new VueImage();
            } catch (IOException ex) {
                Logger.getLogger(VueInscription.class.getName()).log(Level.SEVERE, null, ex);
            }
            image2.mouseClicked(file);
            Image image3= image2.getImage();
            this.avatar.setFill(new ImagePattern(image3));
        });

    }

    private void doInscription() {
        try {
            if (this.tfEmail.getText().isEmpty() || this.pfPass.getText().isEmpty() || this.coordonnee == 0) {
                JavaFXUtils.showErrorInAlert("Erreur", "Pas toutes les informations ont été rentrées", "");

            } else {
                Connection con = this.main.getBDD();
                createUtilisateur(con, this.tfNom.getText(), this.pfPass.getText(), this.tfPrenom.getText(), this.tfEmail.getText(), null, this.latitude, this.longitude);
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
