/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import static fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueMesEnchere.EncheresUtilisateurEnCours;
import static fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueMesEnchere.EncheresUtilisateurFini;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.ValiditeDateEnchere;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

/**
 *
 * @author jules
 */
public class VueMesAnnonces extends GridPane {
private TableView tvEnCours;
    private TableView tvTerminee;

    private FenetrePrincipale main;

    private TableColumn<MesAnnonces, String> titreC;
    private TableColumn<MesAnnonces, String> prixActuelC;
    private TableColumn<MesAnnonces, String> prixDepartC;
    private TableColumn<MesAnnonces, String> tRestantC;
    private TableColumn<MesAnnonces, String> DerEnchereC;
    private TableColumn<MesAnnonces, String> nbEnchereC;
    
    private TableColumn<MesAnnonces, String> titreT;
    private TableColumn<MesAnnonces, String> prixActuelT;
    private TableColumn<MesAnnonces, String> prixDepartT;
    private TableColumn<MesAnnonces, String> tRestantT;
    private TableColumn<MesAnnonces, String> DerEnchereT;
    private TableColumn<MesAnnonces, String> nbEnchereT;

    private ArrayList lAnnonceEnCours;
    private ArrayList lAnnonceEnTerminee;
    
    private Label lAnnonceC;
    private Label lAnnonceT;

            
    public VueMesAnnonces(FenetrePrincipale main) throws ClassNotFoundException, SQLException {
        this.main = main;
        this.setPrefWidth(this.main.getWidth());
        this.tvEnCours = new TableView();
        this.tvTerminee = new TableView();
        this.tvEnCours.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.tvTerminee.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.titreC = new TableColumn<>("Titre");
        this.prixActuelC = new TableColumn<>("Prix actuel");
        this.prixDepartC = new TableColumn<>("Prix de départ");
        this.tRestantC = new TableColumn<>("Temps restant");
        this.nbEnchereC = new TableColumn<>("Nombre d'enchere ");
        this.DerEnchereC = new TableColumn<>("Dernier encherisseur");

        titreC.setCellValueFactory(new PropertyValueFactory<>("titre"));
        prixActuelC.setCellValueFactory(new PropertyValueFactory<>("prixActuel"));
        prixDepartC.setCellValueFactory(new PropertyValueFactory<>("prixDepart"));
        DerEnchereC.setCellValueFactory(new PropertyValueFactory<>("Encherisseur"));
        nbEnchereC.setCellValueFactory(new PropertyValueFactory<>("nbEnchere"));
        tRestantC.setCellValueFactory(new PropertyValueFactory<>("tempRestant"));
        
        this.titreT = new TableColumn<>("Titre");
        this.prixActuelT = new TableColumn<>("Prix actuel");
        this.prixDepartT = new TableColumn<>("Prix de départ");
        this.tRestantT = new TableColumn<>("Temps restant");
        this.nbEnchereT = new TableColumn<>("Nombre d'enchere ");
        this.DerEnchereT = new TableColumn<>("Dernier encherisseur");

        titreT.setCellValueFactory(new PropertyValueFactory<>("titre"));
        prixActuelT.setCellValueFactory(new PropertyValueFactory<>("prixActuel"));
        prixDepartT.setCellValueFactory(new PropertyValueFactory<>("prixDepart"));
        DerEnchereT.setCellValueFactory(new PropertyValueFactory<>("Encherisseur"));
        nbEnchereT.setCellValueFactory(new PropertyValueFactory<>("nbEnchere"));
        tRestantT.setCellValueFactory(new PropertyValueFactory<>("tempRestant"));
        
        //prixActuel.setCellValueFactory(new PropertyValueFactory<>("prix"));

        this.lAnnonceEnCours = AnnonceParUtilisateurCours(this.main.getSessionInfo().getUserID());
        this.lAnnonceEnTerminee = AnnonceParUtilisateurFini(this.main.getSessionInfo().getUserID());
        System.out.println(this.main.getSessionInfo().getUserID());
        System.out.println(lAnnonceEnCours);
        ajoutAnnoncesUtilisateurEnCours();
        ajoutAnnoncesUtilisateurFini();

        this.tvEnCours.getColumns().addAll(titreC, prixDepartC, prixActuelC, tRestantC, nbEnchereC, DerEnchereC);
        this.tvEnCours.setPrefWidth(this.getPrefWidth());
        
        this.tvTerminee.getColumns().addAll(titreT, prixDepartT, prixActuelT, tRestantT, nbEnchereT, DerEnchereT);
        this.tvTerminee.setPrefWidth(this.getPrefWidth());
        
        
        this.lAnnonceC = new Label("Annonces en cours");
        this.lAnnonceT = new Label("Annonces terminées");
        this.lAnnonceC.setId("grand-texte");
        this.lAnnonceT.setId("grand-texte");

        this.add(lAnnonceC, 0, 0);
        this.add(this.tvEnCours, 0, 1);
        this.add(lAnnonceT, 0, 2);
        this.add(this.tvTerminee, 0, 3);
    }
    public static ArrayList<Integer> AnnonceParUtilisateurCours(int idUtil) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select * from objet where proposerpar = ? ")) {
            st.setInt(1, idUtil);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                int resultat = res.getInt("id");
                if (ValiditeDateEnchere(resultat) == false) {
                    List.add(res.getInt("id"));
                    //System.out.println("Valeurs" + res.getInt("sur"));
                } else {
                }
            }
            return List;
        }
    }

    public static ArrayList<Integer> AnnonceParUtilisateurFini(int idUtil) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select * from objet where proposerpar = ? ")) {
            st.setInt(1, idUtil);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                int resultat = res.getInt("id");
                if (ValiditeDateEnchere(resultat) == true) {
                    List.add(res.getInt("id"));
                    //System.out.println("Valeurs" + res.getInt("sur"));
                } else {
                }
            }
            return List;
        }
    }
    public void ajoutAnnoncesUtilisateurEnCours() throws SQLException, ClassNotFoundException {
        for (int i = 0; i < this.lAnnonceEnCours.size(); i++) {
            this.tvEnCours.getItems().add(new MesEncheres(this.main, (Integer) lAnnonceEnCours.get(i)));
        }

    }

    private void ajoutAnnoncesUtilisateurFini() throws SQLException, ClassNotFoundException {
        for (int i = 0; i < this.lAnnonceEnTerminee.size(); i++) {
            this.tvTerminee.getItems().add(new MesEncheres(this.main, (Integer) lAnnonceEnTerminee.get(i)));
        }
    }
}
