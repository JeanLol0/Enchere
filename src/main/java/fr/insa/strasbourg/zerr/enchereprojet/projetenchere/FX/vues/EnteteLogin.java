/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.vues;

import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.BDD;
import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.VuePrincipale;
import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.model.Utilisateur;
import java.sql.SQLException;
import java.util.Optional;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * @author jules
 */
public class EnteteLogin extends HBox {

    private VuePrincipale main;

    private TextField tfNom;
    private PasswordField pfPass;

    public EnteteLogin(VuePrincipale main) {
        this.main = main;
        this.getChildren().add(new Label("nom :"));
        this.tfNom = new TextField();
        this.getChildren().add(this.tfNom);
        this.getChildren().add(new Label("pass :"));
        this.pfPass = new PasswordField();
        this.getChildren().add(this.pfPass);
        Button bLogin = new Button("Login");
        bLogin.setOnAction((t) -> {
            doLogin();
        });
        this.getChildren().add(bLogin);

    }

    public void doLogin() {
        String nom = this.tfNom.getText();
        String pass = this.pfPass.getText();
        try {
            Optional<Utilisateur> user
                    = BDD.login(this.main.getBDD(), nom, pass);
            if (user.isEmpty()) {
                JavaFXUtils.showErrorInAlert("Erreur", "utilisateur invalide", "");
            } else {
                this.main.getSessionInfo().setCurUser(user);
                this.main.setEntete(new Label("connection OK"));
                //this.main.setMainContent(new Label("vous êtes " + user));
            }

        } catch (SQLException ex) {
            JavaFXUtils.showErrorInAlert("Pb bdd", "Erreur",
                    ex.getMessage());
        }
    }
}
