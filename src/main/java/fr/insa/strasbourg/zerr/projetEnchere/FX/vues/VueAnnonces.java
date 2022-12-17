/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.composants.BarReccherche;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.defautConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author jules
 */
public class VueAnnonces extends BorderPane {

    private FenetrePrincipale main;
    private GridPane gridPane;
    private Annonce anonce;
    private ArrayList<Integer> idAnnonce;
    private Connection con;
    private BarReccherche barRe;

    public VueAnnonces(FenetrePrincipale main) {
        this.main = main;
        this.gridPane = new GridPane();
        this.barRe = new BarReccherche(this.main);
        this.idAnnonce = new ArrayList<Integer>();
        this.con = this.main.getBDD();
        afficheAnnonce();
        this.gridPane.setAlignment(Pos.CENTER);
        this.gridPane.setHgap(30);
        this.setTop(this.barRe);
        this.setCenter(this.gridPane);
        this.barRe.getTrieCombo().setOnAction((t) -> {
            String typeTri = this.barRe.getTrieCombo().getValue().toString();
            if("Prix croissant".equals(typeTri)){
                try {
                    this.idAnnonce = TriPrixCroissant();
                    afficheAnnonce();
                } catch (SQLException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                }
            }if("Prix décroissant".equals(typeTri)){
                try {
                    this.idAnnonce = TriPrixDecroissant();
                    afficheAnnonce();
                } catch (SQLException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                }
            }if("Dates croissante".equals(typeTri)){
                try {
                    this.idAnnonce = TriDateDecroissant();
                    afficheAnnonce();
                } catch (SQLException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                }
            }if("Dates decroissante".equals(typeTri)){
                try {
                    this.idAnnonce = TriDateDecroissant();
                    afficheAnnonce();
                } catch (SQLException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                }
            }if("Catégorie".equals(typeTri)){
                String categorie =this.barRe.getCategorieCombo().getValue().toString();
                try {
                    int idCat = getIdCategorie(categorie);                
                    this.idAnnonce = TriCategorie(idCat);
                    afficheAnnonce();
                } catch (SQLException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public void afficheTousLesUtilisateur(Connection con)
            throws SQLException {
        try (Statement st = con.createStatement()) {
            ResultSet res = st.executeQuery("select * from utilisateur");
            while (res.next()) {
                String lenom = res.getString("nom");
                String pass = res.getString("pass");
                String prenom = res.getString("prenom");
                String mail = res.getString("email");
                System.out.println("utilisateur " + lenom + " " + prenom + " ("
                        + pass + ")" + mail);
            }
        }
    }

    private void recupereIdAnnnce()
            throws SQLException {
        try (Statement st = this.con.createStatement()) {
            ResultSet res = st.executeQuery("select id from objet");
            while (res.next()) {
                this.idAnnonce.add(res.getInt("id"));
                //System.out.println("ids recupéré nb:" + this.idAnnonce.size());
            }
        }
    }

    private void afficheAnnonce() {
        try {
            recupereIdAnnnce();
            int nbAnnonce = this.idAnnonce.size();
            for (int i = 1; i <= nbAnnonce; i++) {
                this.gridPane.add(new Annonce(this.main, i), 0, i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static ArrayList<Integer> TriCategorie(int idCategorie) throws SQLException, ClassNotFoundException{
        Connection con = defautConnect();
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try (Statement st= con.createStatement()){
            ResultSet res = st.executeQuery("select id from objet where categorie = ?");
            while (res.next()){
                TriCat.add(res.getInt("id"));
            }
        }
        return TriCat;
    }
    
    public static ArrayList<Integer> TriPrixCroissant() throws SQLException, ClassNotFoundException{
        Connection con = defautConnect();
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try (Statement st= con.createStatement()){
            ResultSet res = st.executeQuery("select id from objet order by prix asc");
            while (res.next()){
                TriCat.add(res.getInt("id"));
            }
        }
        return TriCat;
    }
    
    public static ArrayList<Integer> TriPrixDecroissant() throws SQLException, ClassNotFoundException{
        Connection con = defautConnect();
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try (Statement st= con.createStatement()){
            ResultSet res = st.executeQuery("select id from objet order by prix desc");
            while (res.next()){
                TriCat.add(res.getInt("id"));
            }
        }
        return TriCat;
    }
    public static ArrayList<Integer> TriDateDecroissant() throws SQLException, ClassNotFoundException{
        Connection con = defautConnect();
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try (Statement st= con.createStatement()){
            ResultSet res = st.executeQuery("select id from objet order by debut desc");
            while (res.next()){
                TriCat.add(res.getInt("id"));
            }
        }
        return TriCat;
    }
    
    public static ArrayList<Integer> TriDateCroissant() throws SQLException, ClassNotFoundException{
        Connection con = defautConnect();
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try (Statement st= con.createStatement()){
            ResultSet res = st.executeQuery("select id from objet order by debut asc");
            while (res.next()){
                TriCat.add(res.getInt("id"));
            }
        }
        return TriCat;
    }
    
    public int getIdCategorie(String textCat) throws SQLException{
        Connection con =this.main.getBDD();
        try ( PreparedStatement st = con.prepareCall("select id from categorie where nom = ?")) {
            st.setString(1, textCat);
            ResultSet res = st.executeQuery();
            if (res.next()) {
            return res.getInt("id");
            } else {
            JavaFXUtils.showErrorInAlert("Erreur", "Selectionner une catégorie", "blabla"); 
            return -1;
            }
        
    }
        
    }

}
