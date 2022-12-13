/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.StylesCSS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.css.StyleClass;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author jules
 */
public class Annonce extends GridPane{
    private FenetrePrincipale main;
    private String titre;
    private Timestamp debut;
    private Timestamp fin;
    private Integer prixDeBase;
    private Integer categorie;
    private Integer idVendeur;
    private Integer id;
    
    
    
    public  Annonce(FenetrePrincipale main,Integer id){
        this.setId("grille-annonce");
        this.id =id;
        this.main= main;
        try {
            recupereObjet( this.id);
        } catch (SQLException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.add(new Text(this.titre), 0, 1);
        this.add(new Text(String.valueOf(this.prixDeBase)), 0, 2);
        this.add(new Text("Prix de base : "), 0, 3);
        this.add(new Text(String.valueOf(this.prixDeBase)), 1, 3);
        this.add(new Text("Catégorie : "), 0, 4);
        this.add(new Text(String.valueOf(this.categorie)), 1, 4);
        this.add(new Text("ID Vendeur : "), 0, 5);
        this.add(new Text(String.valueOf(this.idVendeur)), 1, 5);
        this.add(new Text("ID Vendeur : "), 0, 5);
        this.add(new Text(String.valueOf(this.idVendeur)), 1, 5);
        
        //StylesCSS.ThemeAnnonce(this);
        
        
        
    }
    
     private void recupereObjet(int id)
            throws SQLException {
         Connection con = this.main.getBDD();
        try ( PreparedStatement st = con.prepareCall("select * from objet where id = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();            
            while (res.next()) {
                this.titre = res.getString("titre");
                this.debut = res.getTimestamp("debut");
                this.fin = res.getTimestamp("fin");
                this.prixDeBase = res.getInt("prixbase");
                this.categorie = res.getInt("categorie");
                this.idVendeur = res.getInt("proposerpar");
                System.out.println("objet recuperé");
            }
        }                                                                                                                           
    }
    
}
