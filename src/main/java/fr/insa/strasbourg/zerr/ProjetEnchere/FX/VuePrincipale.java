/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.ProjetEnchere.FX;

import fr.insa.strasbourg.zerr.ProjetEnchere.FX.vues.EnteteBienvenue;

import fr.insa.strasbourg.zerr.ProjetEnchere.GestionBDD.BDD;
import fr.insa.strasbourg.zerr.ProjetEnchere.FX.vues.EnteteInitial;
import fr.insa.strasbourg.zerr.ProjetEnchere.GestionBDD.SessionInfo;
import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.JavaFXUtils;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author jules
 */
public class VuePrincipale extends BorderPane {
    private Stage fenetre;
    private ScrollPane mainPane;
    private SessionInfo sessionInfo;

    public void setEntete(Node c) {
        this.setTop(c);
    }
    
    public void setMainPane(Node c) {
        this.mainPane.setContent(c);
    }

    public VuePrincipale(Stage fenetre) {
        
        this.fenetre = fenetre;
        
        this.sessionInfo = new SessionInfo();
        this.mainPane = new ScrollPane();
        this.setCenter(this.mainPane);
        JavaFXUtils.addSimpleBorder(mainPane);

        try {
            this.sessionInfo.setConBdD(BDD.defautConnect());
            //JavaFXUtils.addSimpleBorder(this.mainPane);
            this.setCenter(this.mainPane);
            Connection con = this.getBDD();
//            recreeTout(con);
            JavaFXUtils.redimentionnerFenetre(this.fenetre, 400, 300);
            //this.getFenetre().setScene(new Scene(new EnteteBienvenue(this)));
            this.setEntete(new EnteteBienvenue(this));

            this.setMainPane(new EnteteInitial(this));

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

this.getStylesheets().add("DarkTheme");
    }

    public Stage getFenetre() {
        return fenetre;
    }

    /**
     * @return the mainPane
     */
    public ScrollPane getMainPane() {
        return mainPane;
    }

//    public void setMainPane(Pane mainPane) {
//        this.mainPane = mainPane;

    public void setMainPane(ScrollPane mainPane) {
        this.mainPane = mainPane;
    }

//    }
    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }

    public Connection getBDD() {
        return this.sessionInfo.getConBdD();
    }

    /**
     * @param fenetre the fenetre to set
     */
    public void setFenetre(Stage fenetre) {
        this.fenetre = fenetre;
    }
}
