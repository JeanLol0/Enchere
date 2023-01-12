/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.composants.TopBar;
import fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import fr.insa.strasbourg.zerr.projetEnchere.model.Utilisateur;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
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

/**
 *
 * @author jules
 */
public class VueLogin extends GridPane {

    private FenetrePrincipale main;
    private TextField tfEmail;
    private PasswordField pfPass;
    private Button bLogin;

    private Label tInscription;
    private Label tBienvenue;
    private Button bInscription;

    public VueLogin(FenetrePrincipale fenetre) {
        this.setId("vue-connexion-inscription");
        this.main = fenetre;
        
        
        Image image = getImage("ressources/background.png");
        Background bg = new Background(new BackgroundImage(image, BackgroundRepeat.SPACE, BackgroundRepeat.SPACE, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
        this.setBackground(bg);

        this.tfEmail = new TextField();
        this.tfEmail.setPromptText("Adresse e-mail");

        this.pfPass = new PasswordField();
        this.pfPass.setPromptText("Mot de passe");

        this.bLogin = new Button("Se connecter");
        this.bLogin.setId("bouton-vert");

        this.bInscription = new Button("Inscription");
        this.bInscription.setId("bouton-bleu");

        this.tBienvenue = new Label("Bienvenue");
        this.tBienvenue.setId("grand-texte");
        this.tInscription = new Label("Pas encore de compte ?");

        this.add(this.tBienvenue, 0, 5);
        this.add(this.tfEmail, 0, 15);
        this.add(this.pfPass, 0, 16);
        this.add(this.bLogin, 0, 17);
        this.add(this.tInscription, 0, 18);
        this.add(this.bInscription, 0, 19);
        //this.bLogin.setMaxWidth(Double.MAX_VALUE);
        this.setVgap(20);
        this.setAlignment(Pos.TOP_CENTER);
        this.setHalignment(this.bLogin, HPos.CENTER);
        this.setHalignment(this.bInscription, HPos.CENTER);
        this.setHalignment(this.tBienvenue, HPos.CENTER);
        this.setHalignment(this.tInscription, HPos.CENTER);

////////        //this.getStylesheets().add(getClass().getResource("CSS.css").toExternalForm());
////////        //StylesCSS.DarkTheme(this);
////////        //this.setStyle("-fx-border-color :red;");
////////        //StylesCSS.StyleBoutonVert(bLogin);
////////        //StylesCSS.StyleBoutonBleu(bInscription);
////////        StylesCSS.StyleGrandTitre(tBienvenue);
////////        StylesCSS.StyleText(tInscription);
        JavaFXUtils.DesactiveAutoFocus(pfPass);
        JavaFXUtils.DesactiveAutoFocus(tfEmail);

        this.bLogin.setOnAction((t) -> {
            doLogin();
        });

        this.bInscription.setOnAction((t) -> {
            this.main.setCenter(new VueInscription(this.main));
        });

    }

    public void doLogin() {
        String nom = this.tfEmail.getText();
        String pass = this.pfPass.getText();
        try {
            Optional<Utilisateur> user = BDD.login(this.main.getBDD(), nom, pass);
            if (user.isEmpty()) {
                JavaFXUtils.showErrorInAlert("Erreur", "utilisateur invalide", "");

            } else {
                this.main.getSessionInfo().setCurUser(user);
                this.main.setCenter(new VueAcceuil(this.main));
                this.main.setTop(new TopBar(this.main));
                System.out.println(this.main.getBDD());
                //this.main.setMainContent(new Label("vous êtes " + user));
            }

        } catch (SQLException ex) {
            JavaFXUtils.showErrorInAlert("Pb bdd", "Erreur",
                    ex.getMessage());
        }
    }
    
    public int getIdUtilisateur() throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        int resultat = 0;
        try ( PreparedStatement st = con.prepareStatement("select id from utilisateur where email = ? ")) {
            st.setString(1, this.tfEmail.getText());
            ResultSet res = st.executeQuery();
            while (res.next()) {
                resultat = res.getInt("id");
            }
        }
        return resultat;

    }
    private Image getImage(String resourcePath) {
        InputStream input //
                = this.getClass().getResourceAsStream(resourcePath);
        Image image = new Image(input);
        return image;
    }
}