/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX;

import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.vues.EnteteBienvenue;
import static fr.insa.strasbourg.zerr.enchereprojet.projetenchere.BDD.*;

import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.BDD;
import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.vues.EnteteInitial;
import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.SessionInfo;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author jules
 */
public class VuePrincipale extends BorderPane {
    private Stage fenetre;
    private Pane mainPane;
    private SessionInfo sessionInfo;

    public void setEntete(Node c) {
        this.setTop(c);
    }

    public VuePrincipale(Stage fenetre) {
        this.fenetre = fenetre;
        
        this.sessionInfo = new SessionInfo();
        this.mainPane = new Pane();

        try {
            this.sessionInfo.setConBdD(BDD.defautConnect());
            //JavaFXUtils.addSimpleBorder(this.mainPane);
            this.setCenter(this.mainPane);
            Connection con = this.getBDD();
            recreeTout(con);
            JavaFXUtils.redimentionnerFenetre(this.fenetre, 400, 300);
            this.setBottom(new EnteteBienvenue(this));

            this.setEntete(new EnteteInitial(this));

        } catch (ClassNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("PB");
            alert.setHeaderText("pas de driver");
            alert.setContentText("pareil");
            alert.showAndWait();

        } catch (SQLException ex) {

        }

        //this.sessionInfo.setConBdD(BDD.defautConnect());
        //this.setCenter(this.mainPane);
        //this.setMainPane(new Label("merci de vous connecter"));
        //this.setEntete(new EnteteLogin(this));
//        this.setBottom(new EnteteNouveauUtilisateur(this));
    }

    public Stage getFenetre() {
        return fenetre;
    }

    /**
     * @return the mainPane
     */
    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }

    public Connection getBDD() {
        return this.sessionInfo.getConBdD();
    }
}
