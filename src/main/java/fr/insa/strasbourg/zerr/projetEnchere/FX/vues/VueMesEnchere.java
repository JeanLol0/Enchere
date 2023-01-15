/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.ValiditeDateEnchere;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 *
 * @author jules
 */
public class VueMesEnchere extends GridPane {

    private TableView tvEnCours;
    private TableView tvTerminee;

    private FenetrePrincipale main;

    private TableColumn<MesEncheres, String> titreC;
    private TableColumn<MesEncheres, String> prixActuelC;
    private TableColumn<MesEncheres, String> prixDepartC;
    private TableColumn<MesEncheres, String> tRestantC;
    private TableColumn<MesEncheres, String> DerEnchereC;
    private TableColumn<MesEncheres, String> nbEnchereC;

    private TableColumn<MesEncheres, String> titreT;
    private TableColumn<MesEncheres, String> prixActuelT;
    private TableColumn<MesEncheres, String> prixDepartT;
    private TableColumn<MesEncheres, String> tRestantT;
    private TableColumn<MesEncheres, String> GagnantEnchereT;
    private TableColumn<MesEncheres, String> nbEnchereT;
    private TableColumn<MesEncheres, String> choixRemiseT;
    private TableColumn<MesEncheres, Void> choixMainpropreT;
    private TableColumn<MesEncheres, Void> choixExpeditionT;

    private ArrayList lEnchereEnCours;
    private ArrayList lEnchereEnTerminee;

    private Label lEncheresC;
    private Label lEncheresT;

    private Button bExp;
    private Button bMain;

    public VueMesEnchere(FenetrePrincipale main) throws SQLException, ClassNotFoundException {
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

        this.titreT = new TableColumn<>("Titre");
        this.prixActuelT = new TableColumn<>("Prix actuel");
        this.prixDepartT = new TableColumn<>("Prix de départ");
        this.tRestantT = new TableColumn<>("Temps restant");
        this.nbEnchereT = new TableColumn<>("Nombre d'enchere ");
        this.GagnantEnchereT = new TableColumn<>("Dernier encherisseur");
        this.choixRemiseT = new TableColumn<>("Choix de remise");
        this.choixExpeditionT = new TableColumn<MesEncheres, Void>("Expedition");
        this.choixMainpropreT = new TableColumn<MesEncheres, Void>("Main propre");

        titreT.setCellValueFactory(new PropertyValueFactory<>("titre"));
        prixActuelT.setCellValueFactory(new PropertyValueFactory<>("prixActuel"));
        prixDepartT.setCellValueFactory(new PropertyValueFactory<>("prixDepart"));
        GagnantEnchereT.setCellValueFactory(new PropertyValueFactory<>("Encherisseur"));
        nbEnchereT.setCellValueFactory(new PropertyValueFactory<>("nbEnchere"));
//        choixExpeditionT.setCellValueFactory(new PropertyValueFactory<MesEncheres, Void>("bExp"));
//        choixMainpropreT.setCellValueFactory(new PropertyValueFactory<MesEncheres, Void>("bMain"));
        //prixActuel.setCellValueFactory(new PropertyValueFactory<>("prix"));

        this.lEnchereEnCours = EncheresUtilisateurEnCours(this.main.getSessionInfo().getUserID());
        this.lEnchereEnTerminee = EncheresUtilisateurFini(this.main.getSessionInfo().getUserID());
        System.out.println(this.main.getSessionInfo().getUserID());
        System.out.println(lEnchereEnCours);
        ajoutEncheresUtilisateurEnCours();
        ajoutEncheresUtilisateurFini();

        this.choixRemiseT.getColumns().addAll(this.choixExpeditionT, this.choixMainpropreT);

        this.tvEnCours.getColumns().addAll(titreC, prixDepartC, prixActuelC, tRestantC, nbEnchereC, DerEnchereC);
        this.tvEnCours.setPrefWidth(this.getPrefWidth());

        this.tvTerminee.getColumns().addAll(titreT, prixDepartT, prixActuelT, tRestantT, nbEnchereT, GagnantEnchereT);
        this.tvTerminee.setPrefWidth(this.getPrefWidth());
        this.bMain = new Button("Mains propre");
        this.bExp = new Button("Expedition");
        addButtonToTable();

        this.lEncheresC = new Label("Encheres en cours");
        this.lEncheresT = new Label("Encheres terminées");
        this.lEncheresC.setId("grand-texte");
        this.lEncheresT.setId("grand-texte");

        
        ToggleGroup tg = new ToggleGroup();
        
        this.bExp.setOnAction((t) -> {
            int index = this.tvTerminee.getSelectionModel().getFocusedIndex();
            System.out.println(index);
            System.out.println("bouton exp cell");
            
//            setEtatLivraison(3, 2);
            this.bExp.setDisable(true);
            this.bMain.setDisable(true);
        });
        this.bMain.setOnAction((t) -> {
            System.out.println("bouton main cell");
            this.bExp.setDisable(true);
            this.bMain.setDisable(true);
        });

        this.add(lEncheresC, 0, 0);
        this.add(this.tvEnCours, 0, 1);
        this.add(lEncheresT, 0, 3);
        this.add(this.tvTerminee, 0, 4);
    }

    private void addButtonToTable() {

//        Callback<TableColumn<MesEncheres, Void>, TableCell<MesEncheres, Void>> cellFactoryBouttonExp = (final TableColumn<MesEncheres, Void> param) -> {
//            final TableCell<MesEncheres, Void> cell = new TableCell<MesEncheres, Void>() {
//                
//                private  Button btn = new Button("Expedition");
//                
//                {
//                    btn.setOnAction((ActionEvent event) -> {
//                        MesEncheres data = getTableView().getItems().get(getIndex());
//                        System.out.println("selectedData: " + data);
//                    });
//                }
//                
//                @Override
//                public void updateItem(Void item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty) {
//                        setGraphic(null);
//                    } else {
//                        setGraphic(btn);
//                    }
//                }
//            };
//            return cell;
//        };
//        Callback<TableColumn<MesEncheres, Void>, TableCell<MesEncheres, Void>> cellFactoryBouttonMain = (final TableColumn<MesEncheres, Void> param) -> {
//            final TableCell<MesEncheres, Void> cell = new TableCell<MesEncheres, Void>() {
//                
//                private  Button btn = new Button("Mains propre");
//                
//                {
//                    btn.setOnAction((ActionEvent event) -> {
//                        MesEncheres data = getTableView().getItems().get(getIndex());
//                        System.out.println("selectedData: " + data);
//                    });
//                }
//                
//                @Override
//                public void updateItem(Void item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty) {
//                        setGraphic(null);
//                    } else {
//                        setGraphic(btn);
//                    }
//                }
//            };
//            return cell;
//        };
        //this.choixMainpropreT.setCellFactory(cellFactoryBouttonMain);
        this.choixMainpropreT.setCellFactory(col -> {

            Button editButton = this.bMain;
            TableCell<MesEncheres, Void> cell = new TableCell<MesEncheres, Void>() {

                @Override
                public void updateItem(Void person, boolean empty) {
                    super.updateItem(person, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }
            };

            return cell;
        });
        this.choixExpeditionT.setCellFactory(col -> {

            Button editButton = this.bExp;
            TableCell<MesEncheres, Void> cell = new TableCell<MesEncheres, Void>() {

                @Override
                public void updateItem(Void person, boolean empty) {
                    super.updateItem(person, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }
            };

            return cell;
        });
//        this.bExp.setOnAction((t) -> {
//                System.out.println("bouton exp");
//            });
//        this.bMain.setOnAction((t) -> {
//                System.out.println("bouton main");
//            });

    }

    public static ArrayList<Integer> EncheresUtilisateurEnCours(int idUtil) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("select * from enchere where de = ? ")) {
            st.setInt(1, idUtil);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                int resultat = res.getInt("sur");
                if (ValiditeDateEnchere(resultat) == false) {
                    List.add(res.getInt("sur"));
                } else {
                }
            }
            return List;
        }
    }

    public static ArrayList<Integer> EncheresUtilisateurFini(int idUtil) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("select * from enchere where de = ? ")) {
            st.setInt(1, idUtil);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                int resultat = res.getInt("sur");
                System.out.println(ValiditeDateEnchere(resultat));
                if (ValiditeDateEnchere(resultat) == true) {
                    List.add(res.getInt("sur"));
                }
            }
            return List;
        }
    }

    public void ajoutEncheresUtilisateurEnCours() throws SQLException, ClassNotFoundException {
        if (this.lEnchereEnCours.size() > 0) {
            for (int i = 0; i < this.lEnchereEnCours.size(); i++) {
                this.tvEnCours.getItems().add(new MesEncheres(this.main, (Integer) lEnchereEnCours.get(i)));
            }
        }

    }

    private void ajoutEncheresUtilisateurFini() throws SQLException, ClassNotFoundException {
        if (this.lEnchereEnTerminee.size() > 0) {
            for (int i = 0; i < this.lEnchereEnTerminee.size(); i++) {
                this.tvTerminee.getItems().add(new MesEncheres(this.main, (Integer) lEnchereEnTerminee.get(i)));
            }
        }
    }

    public int UtilDernierEnchereSurObjet(int idObjet) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        int idDernierUtil = -1;
        try (PreparedStatement st = con.prepareStatement("select * from enchere where (select max(montant) from enchere where sur=?)")) {
            st.setInt(1, idObjet);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                idDernierUtil = res.getInt("de");

            }
            return idDernierUtil;
        }
    }

    public void setEtatLivraison(int valeur, int idObjet) throws SQLException {
        Connection con = this.main.getBDD();
        con.setAutoCommit(false);
        try (PreparedStatement pst = con.prepareStatement(
                "update objet set Etatlivraison = ? where id = ?", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, valeur);
            pst.setInt(2, idObjet);
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try (ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
            }
        } finally {
            con.setAutoCommit(true);
        }
    }
    
    public int getEtatLivraison(Connection con, Integer idObjet) throws SQLException {
        int valeur = 0;
        try ( PreparedStatement st = con.prepareCall("select etatlivraison from objet where id = ?")) {
            st.setInt(1, idObjet);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                valeur = res.getInt("etatlivraison");
            }

        }
        return valeur;
    }
    
   
}
