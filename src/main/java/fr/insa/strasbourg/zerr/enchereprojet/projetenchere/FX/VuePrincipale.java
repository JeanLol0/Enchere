/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX;


import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.BDD;
import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.vues.EnteteLogin;
import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.SessionInfo;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author jules
 */
public class VuePrincipale extends BorderPane {

    private Pane mainPane;
    private SessionInfo sessionInfo;

    public void setEntete(Node c) {
        this.setTop(c);
    }

    public VuePrincipale() {
        
        
        this.sessionInfo = new SessionInfo();
        this.mainPane = new Pane();
        
        try {
            this.sessionInfo.setConBdD(BDD.defautConnect());
            //JavaFXUtils.addSimpleBorder(this.mainPane);
            this.setCenter(this.mainPane);
            //this.setMainContent(new Label("merci de vous connecter"));
            this.setEntete(new EnteteLogin(this));

        } catch (ClassNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("PB");
            alert.setHeaderText("pas de driver");
            alert.setContentText("pareil");
            alert.showAndWait();

        } catch (SQLException ex) {
            
        }

        //this.sessionInfo.setConBdD(BDD.defautConnect());

        this.setCenter(this.mainPane);
        //this.setMainPane(new Label("merci de vous connecter"));
        this.setEntete(new EnteteLogin(this));
    }

    /**
     * @return the mainPane
     */
    public Pane getMainPane() {
        return mainPane;
    }

    /**
     * @param mainPane the mainPane to set
     */
    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }
    
    public Connection getBDD(){
        return  this.sessionInfo.getConBdD();
    }
}
