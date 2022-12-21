/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.createEnchere;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 *
 * @author jules
 */
public class VueAnnonceDetaille extends ScrollPane {
    private FenetrePrincipale main;
    private GridPane gridPnae;
    private Label desc;
    private Text tDesc;
    private Label lEnchere;
    private TextField tfEnchere;
    private Button bEnchere;
    
    private HBox content;
    private int idObj;
    private Label prix;
    
    

    
    
    public VueAnnonceDetaille(FenetrePrincipale main, GridPane grid, int idObj,ImageView imageV){
        try {
            recupereObjet(idObj);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(VueAnnonceDetaille.class.getName()).log(Level.SEVERE, null, ex);
        }
        imageV.setFitHeight(600);
        imageV.setFitWidth(600);
        this.idObj=idObj;
        this.desc = new Label("Description");
        this.content= new HBox();
        this.content.setId("annonce");
        this.main = main;
        this.gridPnae = grid;
        this.lEnchere=new Label("Faire une enchere");
        this.tfEnchere = new TextField("Montant");
        this.tfEnchere.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.matches("\\d*")) {
                int value = Integer.parseInt(newValue);
            } else {
                tfEnchere.setText(oldValue);
            }
        });
        this.bEnchere = new Button("Encherir");
        this.gridPnae.getChildren().remove(1, 2);
        this.gridPnae.add(this.desc, 0, 6);
        this.gridPnae.add(this.tDesc, 0, 7, 2, 1);
        this.gridPnae.add(this.lEnchere, 0, 8, 2, 1);
        this.gridPnae.add(this.tfEnchere, 0, 9, 2, 1);
        this.gridPnae.add(this.bEnchere, 0, 10, 2, 1);
        this.gridPnae.add(imageV, 3, 0, 10, 10);
        
        
        this.content.getChildren().addAll(imageV,this.gridPnae);
        this.setContent(this.content);
        
        this.bEnchere.setOnAction((t) -> {
            faireEnchere();
        });
    }
    
    private void recupereObjet(int id)
            throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");

        try (PreparedStatement st = con.prepareStatement("select * from objet where id = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                this.tDesc = new Text(res.getString("bio"));
                this.prix = new Label(res.getString("prixactuel"));
                        
                        

            }
        }
    
}

    private void faireEnchere() {
        Connection con = this.main.getBDD();
        int idUser = this.main.getSessionInfo().getUserID();
        
        try {
            createEnchere(con, idObj, idUser, Integer.parseInt(this.tfEnchere.getText()));
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(VueAnnonceDetaille.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
