/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;


import fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD;
import fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.SessionInfo;
import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.composants.TopBar;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.recreeTout;
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
 public class FenetrePrincipale extends BorderPane {

    private Stage fenetre;
    private ScrollPane mainPane;
    private SessionInfo sessionInfo;
    private TopBar topBar;


    public FenetrePrincipale(Stage fenetre) {
        
        this.fenetre = fenetre;

        this.sessionInfo = new SessionInfo();
        this.mainPane = new ScrollPane();
        //JavaFXUtils.addSimpleBorder(mainPane);

        try {
            Connection con = this.getBDD();
            this.sessionInfo.setConBdD(BDD.defautConnect());
            this.setCenter(new VueLogin(this));
            
            //recreeTout(con);
            
        } catch (ClassNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("PB");
            alert.setHeaderText("pas de driver");
            alert.setContentText("pareil");
            alert.showAndWait();

        } catch (SQLException ex) {
        }

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
    
    
    
    public void setEntete(Node c) {
        this.setTop(c);
    }

    public void setMainPane(Node c) {
        this.mainPane.setContent(c);
    }
}
