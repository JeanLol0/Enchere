/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.composants.BarRecherche;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.DistanceObjetFromUtiilisateur;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.ValiditeDateEnchere;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

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
    private Region region;

    public VueAnnonces(FenetrePrincipale main) throws SQLException, ClassNotFoundException, IOException {
        this.main = main;
        this.setPrefWidth(this.main.getWidth() / 2);
        this.gridPane = new GridPane();

        this.barRe = new BarRecherche(this.main);
        this.idAnnonce = new ArrayList<Integer>();
        this.con = this.main.getBDD();
        recupereIdAnnonce();
        afficheAnnonce();
        this.gridPane.setAlignment(Pos.CENTER);
        this.gridPane.setVgap(15);
        this.main.setLeft(this.barRe);
        //this.HBox.getChildren().addAll(this.gridPane);
        this.setCenter(this.gridPane);
        this.barRe.getButtonRecherche().setOnAction((t) -> {
            String texte = this.barRe.getTextField().getText();
            try {
                this.idAnnonce = ObjetRechercheMots(texte);
                afficheAnnonce();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.barRe.getTrieCombo().setOnAction((t) -> {
            String typeTri = this.barRe.getTrieCombo().getValue().toString();
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

        });
        this.barRe.getbCategorie().setOnAction((t) -> {
            try {
                String categoriSelect = this.barRe.getCategorie().getTextCategorieSelected();
                int idCat = getIdCategorie(categoriSelect);
                this.idAnnonce = TriCategorie(idCat);
                afficheAnnonce();
            } catch (SQLException | ClassNotFoundException | IOException ex) {
                Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        this.barRe.getbDistance().setOnAction((t) -> {
            try {
                String StringDistMax = getNbr(this.barRe.getValeurDist());
                int distMax = Integer.parseInt(StringDistMax);
                int idUtil = this.main.getSessionInfo().getUserID();
                System.out.println("distance cur"+distMax);
                this.idAnnonce = TriDistanceObjet(idUtil, distMax);
                afficheAnnonce();
            } catch (ClassNotFoundException | SQLException | IOException ex) {
                Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        this.barRe.getbReinitialise().setOnAction((t) -> {
            try {
                recupereIdAnnonce();
                afficheAnnonce();
            } catch (SQLException | ClassNotFoundException | IOException ex) {
                Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    public void afficheTousLesUtilisateur(Connection con)
            throws SQLException {
        try ( Statement st = con.createStatement()) {
            ResultSet res = st.executeQuery("select * from utilisateur");
            while (res.next()) {
                String lenom = res.getString("nom");
                String pass = res.getString("pass");
                String prenom = res.getString("prenom");
                String mail = res.getString("email");
//                System.out.println("utilisateur " + lenom + " " + prenom + " ("
//                        + pass + ")" + mail);
            }
        }
    }

    public void recupereIdAnnonce()
            throws SQLException, ClassNotFoundException {
        try ( PreparedStatement st = this.con.prepareStatement("select id from objet")) {
            ResultSet res = st.executeQuery();
            while (res.next()) {
                if (ValiditeDateEnchere(res.getInt("id")) == false) {
                    this.idAnnonce.add(res.getInt("id"));
                }
//                System.out.println("ids recupéré nb:" + this.idAnnonce.size());
//            if (ValiditeDateEnchere(res.getInt("id")) == false) {
//                int size = this.idAnnonce.size();
//                this.idAnnonce.remove(size-1);
//            }
            }
        }
    }

    private void afficheAnnoncePrixDecroissante() throws SQLException, IOException {
        try {
//            System.out.println("idAnnonce1" + this.idAnnonce);
            this.idAnnonce.clear();
//            System.out.println("idAnnonce2" + this.idAnnonce);
            this.idAnnonce = TriDateDecroissant();
//            System.out.println("idAnnonce3" + this.idAnnonce);
            this.gridPane.getChildren().clear();
//            System.out.println(this.gridPane.getChildren());
            int nbAnnonce = this.idAnnonce.size();
            for (int i = 0; i < nbAnnonce; i++) {
                this.gridPane.add(new Annonce(this.main, this.idAnnonce.get(i) + 1), 0, i);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void afficheAnnonce() throws SQLException, ClassNotFoundException, IOException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        this.gridPane.getChildren().clear();
        int nbAnnonce = this.idAnnonce.size();

        try ( PreparedStatement st = con.prepareStatement("select id from objet where proposerpar = ?")) {
            st.setInt(1, this.main.getSessionInfo().getUserID());
//            System.out.println(this.main.getSessionInfo().getUserID());
            ResultSet res = st.executeQuery();
            while (res.next()) {
                int resultat = res.getInt("id");
//                System.out.println(resultat);
                for (int i = 0; i < nbAnnonce; i++) {
                    if (this.idAnnonce.get(i) == resultat) {
                        this.idAnnonce.remove(i);
                        nbAnnonce = nbAnnonce - 1;
                    }
                }
            }

        }
        nbAnnonce = this.idAnnonce.size();
        for (int j = 0; j < nbAnnonce; j++) {
            this.gridPane.add(new Annonce(this.main, this.idAnnonce.get(j)), 0, j);
        }
    }

//    private void afficheAnnonceSansTri() throws SQLException, ClassNotFoundException, IOException {
//        recupereIdAnnonce();
//        int nbAnnonce = this.idAnnonce.size();
////        for (int i = 0; i < nbAnnonce; i++) {
////            System.out.println(i);
//            this.gridPane.add(new Annonce(this.main, 7), 0, 1);
////        
//    }
    public static ArrayList<Integer> TriCategorie(int idCategorie) throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select id from objet where categorie = ?")) {
            st.setInt(1, idCategorie);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                int resultat = res.getInt("id");
                if (ValiditeDateEnchere(resultat) == false) {
                    TriCat.add(resultat);
                }
            }
        }
        return TriCat;
    }

    public static ArrayList<Integer> TriPrixCroissant() throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select id from objet order by prixactuel asc")) {
            ResultSet res = st.executeQuery();
            while (res.next()) {
                int resultat = res.getInt("id");
                if (ValiditeDateEnchere(resultat) == false) {
                    TriCat.add(resultat);
                }
            }
        }
        return TriCat;
    }

    public static ArrayList<Integer> TriPrixDecroissant() throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select id from objet order by prixactuel desc")) {
            ResultSet res = st.executeQuery();
            while (res.next()) {
                int resultat = res.getInt("id");
                if (ValiditeDateEnchere(resultat) == false) {
                    TriCat.add(resultat);
                }
            }
        }
        return TriCat;
    }

    public static ArrayList<Integer> TriDateDecroissant() throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try ( Statement st = con.createStatement()) {
            ResultSet res = st.executeQuery("select id from objet order by fin desc");
            while (res.next()) {
                int resultat = res.getInt("id");
                if (ValiditeDateEnchere(resultat) == false) {
                    TriCat.add(resultat);
                }
            }
        }
        return TriCat;
    }

    public static ArrayList<Integer> TriDateCroissant() throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> TriCat;
        TriCat = new ArrayList<>();
        try ( Statement st = con.createStatement()) {
            ResultSet res = st.executeQuery("select id from objet order by fin asc");
            while (res.next()) {
                int resultat = res.getInt("id");
                if (ValiditeDateEnchere(resultat) == false) {
                    TriCat.add(resultat);
                }
            }
        }
        return TriCat;
    }

    public int getIdCategorie(String textCat) throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
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

    public static ArrayList<Integer> ObjetRechercheMots(String texte) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        System.out.println("on est ici");
        try ( PreparedStatement st = con.prepareStatement("select * from objet where titre like ?")) {
            st.setString(1, "%" + texte + "%");
            ResultSet res = st.executeQuery();
            while (res.next()) {
                int resultat = res.getInt("id");
                if (ValiditeDateEnchere(resultat) == false) {
                    List.add(resultat);
                }
            }
        }
        try ( PreparedStatement st = con.prepareStatement("select * from objet where bio like ?")) {
            st.setString(1, "%" + texte + "%");
            ResultSet res = st.executeQuery();
            while (res.next()) {
                int resultat = res.getInt("id");
                if (ValiditeDateEnchere(resultat) == false) {
                    List.add(resultat);
                }
            }
        }
        Set<Integer> set = new LinkedHashSet<>(List);
        ArrayList<Integer> sansdoublons = new ArrayList<>(set);

//        try ( Statement st = con.createStatement()) {
//            String queryssg = ""select * from objet where titre like '%""" + texte + "%'";
//            try ( ResultSet tlu = st.executeQuery(queryssg)) {
//                System.out.println("on est la ");
//                while (tlu.next()) {
//                    int resultats = tlu.getInt("id");
//                    System.out.println("resultat");
//                    List.add(resultats);
//                }
//            }
//
//        }
//        try ( Statement st = con.createStatement()) {
//            ResultSet res2 = st.executeQuery("select * from objet where titre like '%" + texte + "%'");
//            while (res2.next()) {
//                int resultat2 = res2.getInt("id");
//                System.out.println("resultat" + resultat2);
//                if (ValiditeDateEnchere(resultat2) == false) {
//                    List.add(resultat2);
//                }
//            }
//        }
        return sansdoublons;

    }
    public static ArrayList<Integer> TriDistanceObjet(int idUtil, double distancemax) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
//        TreeMap<Double, Integer> map = new TreeMap();
        ArrayList<Integer> map = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select id from objet")) {
            ResultSet res = st.executeQuery();

            while (res.next()) {
                if (DistanceObjetFromUtiilisateur(idUtil, res.getInt("id")) <= distancemax) {
                    map.add(res.getInt("id"));
                }
                    System.out.println("Objet : "+res.getInt("id"));
            }
        }

//        Set set = map.entrySet();
//        Iterator it = set.iterator();
//        ArrayList<Integer> List = new ArrayList<>();
//        while (it.hasNext()) {
//            Map.Entry me = (Map.Entry) it.next();
//            List.add((Integer) me.getValue());
//            System.out.println(me.getValue());
//        }

        return map;
        //tri du plus proche au plus éloigné 
    }
    
    public static ArrayList<Integer> TriDistanceObjetDsc(int idUtil) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        TreeMap<Double, Integer> map = new TreeMap();

        try ( PreparedStatement st = con.prepareStatement("select id from objet")) {
            ResultSet res = st.executeQuery();

            while (res.next()) {
                map.put(DistanceObjetFromUtiilisateur(idUtil, res.getInt("id")), res.getInt("id"));
            }
        }

        Set set = map.entrySet();
        Iterator it = set.iterator();
        ArrayList<Integer> List = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            List.add((Integer) me.getValue());
        }

        return List;
        //tri du plus proche au plus éloigné 
    }
    static String getNbr(String str) { 
        // Remplacer chaque nombre non numérique par un espace
        str = str.replaceAll("[^\\d]", " "); 
        // Supprimer les espaces de début et de fin 
        str = str.trim(); 
        // Remplacez les espaces consécutifs par un seul espace
        str = str.replaceAll(" +", ""); 
  
        return str; 
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
