/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.composants.BarRecherche;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.defautConnect;
import java.io.IOException;
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
import javafx.scene.text.Text;

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
    private BarRecherche barRe;

    public VueAnnonces(FenetrePrincipale main) throws SQLException, ClassNotFoundException, IOException {
        this.main = main;
        this.gridPane = new GridPane();
        this.barRe = new BarRecherche(this.main);
        this.idAnnonce = new ArrayList<Integer>();
        this.con = this.main.getBDD();
        afficheAnnonceSansTri();
        this.gridPane.setAlignment(Pos.CENTER);
        this.gridPane.setVgap(15);
        this.setTop(this.barRe);
        this.setCenter(this.gridPane);
        this.barRe.getTrieCombo().setOnAction((t) -> {
            String typeTri = this.barRe.getTrieCombo().getValue().toString();
            System.out.println(this.barRe.getTrieCombo().getValue().toString());
            if ("Prix croissant".equals(typeTri)) {
                try {
                    this.idAnnonce = TriPrixCroissant();
                    afficheAnnonce();
                } catch (SQLException | ClassNotFoundException | IOException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
            if ("Prix décroissant".equals(typeTri)) {
                
                try {
                    this.idAnnonce = TriPrixDecroissant();
                    afficheAnnonce();
                } catch (SQLException ex) {
                    System.out.println("Erreur SQL");
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if ("Dates croissante".equals(typeTri)) {
                try {
                    this.idAnnonce = TriDateCroissant();
                    afficheAnnonce();
                } catch (SQLException | ClassNotFoundException | IOException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if ("Dates decroissante".equals(typeTri)) {
                try {
                    this.idAnnonce = TriDateDecroissant();
                    afficheAnnonce();
                } catch (SQLException | ClassNotFoundException | IOException ex) {
                    Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if ("Catégorie".equals(typeTri)) {
                String categorie = this.barRe.getCategorieCombo().getValue().toString();
                try {
                    int idCat = getIdCategorie(categorie);
                    this.idAnnonce = TriCategorie(idCat);
                    afficheAnnonce();
                } catch (SQLException | ClassNotFoundException | IOException ex) {
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

    private void recupereIdAnnonce()
            throws SQLException {
        try (PreparedStatement st = this.con.prepareStatement("select id from objet")) {
            ResultSet res = st.executeQuery();
            while (res.next()) {
                this.idAnnonce.add(res.getInt("id"));
                //System.out.println("ids recupéré nb:" + this.idAnnonce.size());
            }
        }
    }

    private void afficheAnnoncePrixDecroissante() throws SQLException, IOException {
        try {
            System.out.println("idAnnonce1"+this.idAnnonce);
            this.idAnnonce.clear();
            System.out.println("idAnnonce2"+this.idAnnonce);
            this.idAnnonce = TriDateDecroissant();
            System.out.println("idAnnonce3"+this.idAnnonce);
            this.gridPane.getChildren().clear();
            System.out.println(this.gridPane.getChildren());
            int nbAnnonce = this.idAnnonce.size();
            for (int i = 0; i < nbAnnonce; i++) {
                this.gridPane.add(new Annonce(this.main, this.idAnnonce.get(i)+1), 0, i);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void afficheAnnonce() throws SQLException, ClassNotFoundException, IOException {
        
        this.gridPane.getChildren().clear();
        int nbAnnonce = this.idAnnonce.size();
        for (int i = 0; i < nbAnnonce; i++) {
            this.gridPane.add(new Annonce(this.main, this.idAnnonce.get(i)), 0, i);
        }
    }

    private void afficheAnnonceSansTri() throws SQLException, ClassNotFoundException, IOException {
        recupereIdAnnonce();
        int nbAnnonce = this.idAnnonce.size();
        for (int i = 0; i < nbAnnonce; i++) {
            this.gridPane.add(new Annonce(this.main, i+1), 0, i);
        }
    }

    public static ArrayList<Integer> TriCategorie(int idCategorie) throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("select id from objet where categorie = ?")) {
            st.setInt(1, idCategorie);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                TriCat.add(res.getInt("id"));
            }
        }
        return TriCat;
    }

    public static ArrayList<Integer> TriPrixCroissant() throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("select id from objet order by prixactuel asc")) {
            ResultSet res = st.executeQuery();
            while (res.next()) {
                TriCat.add(res.getInt("id"));
            }
        }
        return TriCat;
    }

    public static ArrayList<Integer> TriPrixDecroissant() throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("select id from objet order by prixactuel desc")) {
            ResultSet res = st.executeQuery();
            while (res.next()) {
                TriCat.add(res.getInt("id"));
            }
        }
        return TriCat;
    }

    public static ArrayList<Integer> TriDateDecroissant() throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try (Statement st = con.createStatement()) {
            ResultSet res = st.executeQuery("select id from objet order by fin desc");
            while (res.next()) {
                TriCat.add(res.getInt("id"));
            }
        }
        return TriCat;
    }

    public static ArrayList<Integer> TriDateCroissant() throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try (Statement st = con.createStatement()) {
            ResultSet res = st.executeQuery("select id from objet order by fin asc");
            while (res.next()) {
                TriCat.add(res.getInt("id"));
            }
        }
        return TriCat;
    }

    public int getIdCategorie(String textCat) throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        try (PreparedStatement st = con.prepareCall("select id from categorie where nom = ?")) {
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
//    public static void afficheUtilisateurParNom(Connection con,
//            String nom)
//            throws SQLException {
//        try ( PreparedStatement st = con.prepareStatement(
//                "select * from utilisateur where nom = ?")) {
//            st.setString(1, nom);
//            ResultSet res = st.executeQuery();
//            while (res.next()) {
//                String lenom = res.getString("nom");
//                String pass = res.getString(2);
//                System.out.println("utilisateur " + lenom + " ("
//                        + pass + ")");
//            }
//        }
//    }
