/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.createUtilisateur;
import static java.awt.SystemColor.window;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
import javax.imageio.ImageIO;

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
    private Image image;
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

        this.add(this.lInscription, 0, 0);
        this.add(this.avatar, 0, 4);
        this.add(this.tfNom, 0, 5);
        this.add(this.tfPrenom, 0, 6);
        this.add(this.tfEmail, 0, 7);
        this.add(this.pfPass, 0, 8);
        this.add(this.bChoixPos, 0, 9);
        this.add(this.bInscription, 0, 10);
        this.add(this.bLogin, 0, 11);

        this.setVgap(20);
        this.setAlignment(Pos.CENTER);
        this.bInscription.setMaxWidth(Double.MAX_VALUE);
        this.bLogin.setMaxWidth(Double.MAX_VALUE);
        this.bChoixPos.setMaxWidth(Double.MAX_VALUE);
        this.setHalignment(this.lInscription, HPos.CENTER);
        this.setHalignment(this.avatar, HPos.CENTER);
        this.setHalignment(this.bLogin, HPos.CENTER);
        this.setHalignment(this.bChoixPos, HPos.CENTER);
        this.setHalignment(this.bInscription, HPos.CENTER);

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
            fileChooser.setTitle("Sélectionnez une image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Images", "*.png")
            );
            File selectedFile = fileChooser.showOpenDialog(main.getFenetre());
            if (selectedFile != null) {
                // Chargement de l'image
                Image image2 = new Image(selectedFile.toURI().toString());
                avatar.setFill(new ImagePattern(image2));
                this.image = image2;
                System.out.println(ImageEnTexte(image2));
            }
        });

    }

    public VueInscription(FenetrePrincipale main, double longitude, double latitude) {
        this.coordonnee = 1;
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

        this.add(this.lInscription, 0, 0);
        this.add(this.avatar, 0, 4);
        this.add(this.tfNom, 0, 5);
        this.add(this.tfPrenom, 0, 6);
        this.add(this.tfEmail, 0, 7);
        this.add(this.pfPass, 0, 8);
        this.add(this.bChoixPos, 0, 9);
        this.add(this.bInscription, 0, 10);
        this.add(this.bLogin, 0, 11);

        this.setVgap(20);
        this.setAlignment(Pos.CENTER);
        this.bInscription.setMaxWidth(Double.MAX_VALUE);
        this.bLogin.setMaxWidth(Double.MAX_VALUE);
        this.bChoixPos.setMaxWidth(Double.MAX_VALUE);
        this.setHalignment(this.lInscription, HPos.CENTER);
        this.setHalignment(this.avatar, HPos.CENTER);
        this.setHalignment(this.bChoixPos, HPos.CENTER);
        this.setHalignment(this.bInscription, HPos.CENTER);
        this.setHalignment(this.bLogin, HPos.CENTER);

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
            fileChooser.setTitle("Sélectionnez une image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Images", "*.png")
            );
            File selectedFile = fileChooser.showOpenDialog(main.getFenetre());
            if (selectedFile != null) {
                // Chargement de l'image
                Image image2 = new Image(selectedFile.toURI().toString());

                avatar.setFill(new ImagePattern(image2));
                this.image = image2;
            }

        });

    }

    public static String ImageEnTexte(Image img) {

        BufferedImage bufi = SwingFXUtils.fromFXImage(img, null);

        System.out.println("1");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufi, "jpg", baos);
            System.out.println("2");
        } catch (IOException ex) {
            throw new Error("pb conv image ; ne devrait pas arriver");
        }
        byte[] bytes = baos.toByteArray();
        System.out.println("3");
        String ImageTexte = Base64.getUrlEncoder().encodeToString(bytes);
        return ImageTexte;
    }

    private void doInscription() {
        try {
            if (this.tfEmail.getText().isEmpty() || this.pfPass.getText().isEmpty() || this.coordonnee == 0) {
                JavaFXUtils.showErrorInAlert("Erreur", "Pas toutes les informations ont été rentrées", "");

            } else {
                Connection con = this.main.getBDD();
                createUtilisateur(con, this.tfNom.getText(), this.pfPass.getText(), this.tfPrenom.getText(), this.tfEmail.getText(), this.image, this.latitude, this.longitude);
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
