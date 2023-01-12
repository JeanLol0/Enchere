/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.composants.BarRecherche;
import static fr.insa.strasbourg.zerr.projetEnchere.FX.vues.Annonce.CalculDistance;
import static fr.insa.strasbourg.zerr.projetEnchere.FX.vues.Annonce.getNbr;
import static fr.insa.strasbourg.zerr.projetEnchere.FX.vues.Annonce.texteEnImage;
import fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.createEnchere;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 *
 * @author jules
 */
public class VueAnnonceDetaille extends BorderPane {

    private FenetrePrincipale main;
    private String titre;
    private GridPane gridPane;
    private Label desc;
    private Text tDesc;
    private Label lEnchere;
    private TextField tfEnchere;
    private Button bEnchere;

    private int idObj;
    private Label prix;

    private Timestamp debut;
    private Timestamp fin;
    private Timestamp restant;
    private double Olongitude;
    private double Olatitude;
    private double Utillatitude;
    private double Utillongitude;
    private Label distance;
    private Label prixActuel;
    private Label categorie;
    private Integer idVendeur;
    private Image image;
    private String stringImage;
    private ImageView imageV;

    private Label npVendeur;
    private Label tTime;
    private Label tTitre;
    private Label tPrix;
    private Label tCategorie;
    private Label tVendeur;
    private Label tTempsR;
    private Label tDistance;
    
    private ScrollPane scroll;
    private HBox HBox;
    private Region region;
    
    private GridPane gri;
    
    private Button bGoEnchere;
    private Button bRetour;
    

    public VueAnnonceDetaille(FenetrePrincipale main, int idO) throws SQLException, ClassNotFoundException, IOException {
        this.main = main;
        this.gridPane = new GridPane();
        this.main.setLeft(null);
        this.idObj = idO;
        recupereObjet(idO);
        this.desc = new Label("Description");
        this.gridPane = new GridPane();
        this.gridPane.setId("annonce");
        this.gridPane.setHgap(20);
        this.image = texteEnImage(this.stringImage);
        this.imageV = new ImageView(this.image);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth() / 2;
//        this.imageV.setFitHeight(width*0.8);
//        this.imageV.setFitWidth(width*0.8);
        this.gridPane.setPrefWidth(width);
        
        this.gridPane.setVgap(10);

        this.tTime = new Label("Temps restant :");
        this.tPrix = new Label("Prix actuel :");
        this.tCategorie = new Label("Catégorie :");
        this.tVendeur = new Label("Nom du vendeur :");
        this.tTitre = new Label(this.titre);
        this.tTempsR = new Label("Temps restant :");
        this.tDistance = new Label("Distance");
        this.bEnchere = new Button("Encherir");

        int idUtil = this.main.getSessionInfo().getUserID();
        RecupCoordUtil(idUtil);
        double distance = CalculDistance(this.Utillongitude, Utillatitude, Olongitude, Olatitude);
        if (distance < 1) {
            this.distance = new Label("Moins d'un kilomètre ");
        } else {
            this.distance = new Label(Double.toString(distance) + " km");
        }
        this.tfEnchere = new TextField();
        this.tfEnchere.setPromptText("Indiquer le nouveau montant");
        this.tfEnchere.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.matches("\\d*")) {
                int value = Integer.parseInt(newValue);
            } else {
                tfEnchere.setText(oldValue);
            }
        });
        this.tTitre.setId("grand-text-annonce");

        this.gridPane.add(this.tTitre, 0, 0, 2, 1);
        this.gridPane.add(imageV, 0, 1, 2, 1);
        this.gridPane.add(this.desc, 0, 2, 1, 1);
        this.gridPane.add(this.tDesc, 0, 3, 2, 1);
        this.gridPane.add(tCategorie, 0, 4);
        this.gridPane.add(categorie, 1, 4);
        this.gridPane.add(tVendeur, 0, 5);
        this.gridPane.add(npVendeur, 1, 5);
        this.gridPane.add(tDistance, 0, 6);
        this.gridPane.add(this.distance, 1, 6);
        this.gridPane.add(tTempsR, 0, 7);
        this.gridPane.add(tTime, 1, 7);
        this.gridPane.add(tPrix, 0, 8);
        this.gridPane.add(prixActuel, 1, 8);
        this.gridPane.add(tfEnchere, 0, 9,2,1);
        this.gridPane.add(bEnchere, 1, 10,2,1);

        
        this.gridPane.setAlignment(Pos.TOP_CENTER);
        this.gridPane.setHalignment(this.tTitre, HPos.CENTER);
        this.gridPane.setHalignment(this.bEnchere, HPos.CENTER);
        this.gridPane.setHalignment(this.imageV, HPos.CENTER);
        
//        //this.gridPane.setGridLinesVisible(true);
//        //this.content.setPrefWidth(this.getWidth());
//        
//        this.region = new Region();
//        this.region.setPrefWidth(this.main.getWidth()/4);
//        this.HBox=new HBox();
//        this.gri=new GridPane();
//        this.gri.add(this.gridPane, 0, 0);
//        this.gri.setHalignment(this.gridPane,HPos.CENTER);
////        this.HBox.setHgrow(this.region, Priority.SOMETIMES);
////        this.HBox.setHgrow(this.gridPane, Priority.ALWAYS);
//        this.HBox.getChildren().addAll(this.gri);
        
//        this.content.getChildren().addAll(this.gridPane);
//        this.content.setAlignment(Pos.CENTER);
        this.scroll = new ScrollPane();
        this.scroll.setContent(this.gridPane);
        
        this.setCenter(this.scroll);
        //this.BorderPane.setId("scroll-annonce");
        ActualisationTempsRestant();
        ActualisePrix();
        this.bEnchere.setOnAction((t) -> {
            faireEnchere();
        });
        JavaFXUtils.addSimpleBorder(this);
    }
    public VueAnnonceDetaille(FenetrePrincipale main, int idO,int i) throws SQLException, ClassNotFoundException, IOException {
        this.main = main;
        this.gridPane = new GridPane();
        this.main.setLeft(null);
        this.idObj = idO;
        recupereObjet(idO);
        this.desc = new Label("Description");
        this.gridPane = new GridPane();
        this.gridPane.setId("annonce");
        this.gridPane.setHgap(20);
        this.image = texteEnImage(this.stringImage);
        this.imageV = new ImageView(this.image);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth() / 2;
//        this.imageV.setFitHeight(width*0.8);
//        this.imageV.setFitWidth(width*0.8);
        this.gridPane.setPrefWidth(width);
        
        this.gridPane.setVgap(10);

        this.tTime = new Label("Temps restant :");
        this.tPrix = new Label("Prix actuel :");
        this.tCategorie = new Label("Catégorie :");
        this.tVendeur = new Label("Nom du vendeur :");
        this.tTitre = new Label(this.titre);
        this.tTempsR = new Label("Temps restant :");
        this.tDistance = new Label("Distance");
        this.bEnchere = new Button("Encherir");

        int idUtil = this.main.getSessionInfo().getUserID();
        RecupCoordUtil(idUtil);
        double distance = CalculDistance(this.Utillongitude, Utillatitude, Olongitude, Olatitude);
        if (distance < 1) {
            this.distance = new Label("Moins d'un kilomètre ");
        } else {
            this.distance = new Label(Double.toString(distance) + " km");
        }
        this.tfEnchere = new TextField();
        this.tfEnchere.setPromptText("Indiquer le nouveau montant");
        this.tfEnchere.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.matches("\\d*")) {
                int value = Integer.parseInt(newValue);
            } else {
                tfEnchere.setText(oldValue);
            }
        });
        this.tTitre.setId("grand-text-annonce");
        
        this.bGoEnchere = new Button("Plus de détails");
        this.bRetour = new Button("Fermer");

        this.gridPane.add(this.tTitre, 0, 0, 2, 1);
        this.gridPane.add(imageV, 0, 1, 2, 1);
        this.gridPane.add(this.desc, 0, 2, 1, 1);
        this.gridPane.add(this.tDesc, 0, 3, 2, 1);
        this.gridPane.add(tCategorie, 0, 4);
        this.gridPane.add(categorie, 1, 4);
        this.gridPane.add(tVendeur, 0, 5);
        this.gridPane.add(npVendeur, 1, 5);
        this.gridPane.add(tDistance, 0, 6);
        this.gridPane.add(this.distance, 1, 6);
        this.gridPane.add(tTempsR, 0, 7);
        this.gridPane.add(tTime, 1, 7);
        this.gridPane.add(tPrix, 0, 8);
        this.gridPane.add(prixActuel, 1, 8);
        this.gridPane.add(bGoEnchere, 0, 9);
        this.gridPane.add(bRetour,1 , 9);

        
        this.gridPane.setAlignment(Pos.TOP_CENTER);
        this.gridPane.setHalignment(this.tTitre, HPos.CENTER);
        this.gridPane.setHalignment(this.bEnchere, HPos.CENTER);
        this.gridPane.setHalignment(this.imageV, HPos.CENTER);
        this.gridPane.setHalignment(this.bGoEnchere, HPos.CENTER);
        this.gridPane.setHalignment(this.bRetour, HPos.CENTER);
        
//        //this.gridPane.setGridLinesVisible(true);
//        //this.content.setPrefWidth(this.getWidth());
//        
//        this.region = new Region();
//        this.region.setPrefWidth(this.main.getWidth()/4);
//        this.HBox=new HBox();
//        this.gri=new GridPane();
//        this.gri.add(this.gridPane, 0, 0);
//        this.gri.setHalignment(this.gridPane,HPos.CENTER);
////        this.HBox.setHgrow(this.region, Priority.SOMETIMES);
////        this.HBox.setHgrow(this.gridPane, Priority.ALWAYS);
//        this.HBox.getChildren().addAll(this.gri);
        
        this.scroll = new ScrollPane();
        this.scroll.setContent(this.gridPane);
        
        this.setCenter(this.scroll);
        //this.BorderPane.setId("scroll-annonce");
        ActualisationTempsRestant();
        ActualisePrix();
        this.bEnchere.setOnAction((t) -> {
            faireEnchere();
        });
        JavaFXUtils.addSimpleBorder(this);
        this.bGoEnchere.setOnAction((t) -> {
            try {
                this.main.setCenter(new VueAnnonceDetaille(main, idObj));
                this.main.setRight(null);
            } catch (SQLException ex) {
                Logger.getLogger(VueAnnonceDetaille.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VueAnnonceDetaille.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VueAnnonceDetaille.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.bRetour.setOnAction((t) -> {
            this.main.setRight(null);
            this.main.setLeft(new BarRecherche(main));
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

                this.titre = res.getString("titre");
                this.debut = res.getTimestamp("debut");
                this.fin = res.getTimestamp("fin");
                int prix = res.getInt("prixactuel");
                this.prixActuel = new Label(String.valueOf(prix)+" €");
                int idcat = res.getInt("categorie");
                this.categorie = new Label(getStringCategorie(idcat));
                String nom = getNom(res.getInt("proposerpar"));
                String prenom = getPrenom(res.getInt("proposerpar"));
                this.npVendeur = new Label(prenom + " " + nom);
                this.idVendeur = res.getInt("proposerpar");
                this.stringImage = res.getString("image");
                this.Olongitude = res.getDouble("lat");
                this.Olatitude = res.getDouble("long");

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
public String getNom(Integer idU) throws SQLException {
        Connection con = this.main.getBDD();
        try ( PreparedStatement st = con.prepareCall("select nom from utilisateur where id = ?")) {
            st.setInt(1, idU);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                return res.getString("nom");
            } else {
                JavaFXUtils.showErrorInAlert("Erreur", "Selectionner une catégorie", "blabla");
                return null;
            }

        }

    }

    public String getPrenom(Integer idU) throws SQLException {
        Connection con = this.main.getBDD();
        try ( PreparedStatement st = con.prepareCall("select prenom from utilisateur where id = ?")) {
            st.setInt(1, idU);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                return res.getString("prenom");
            } else {
                JavaFXUtils.showErrorInAlert("Erreur", "Selectionner une catégorie", "blabla");
                return null;
            }

        }

    }

 public String getStringCategorie(Integer id) throws SQLException {
        Connection con = this.main.getBDD();
        try ( PreparedStatement st = con.prepareCall("select * from categorie where id = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                return res.getString("nom");
            } else {
                JavaFXUtils.showErrorInAlert("Erreur", "Selectionner une catégorie", "blabla");
                return null;
            }

        }

    }
 
 private void ActualisationTempsRestant() {
        Timeline tempsRestant = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), (t) -> {
            Timestamp mtn = new Timestamp(System.currentTimeMillis());
            long secR = secRestant(this.fin);
            long minR = minRestant(this.fin);
            long heureR = hRestant(this.fin);
            long jourR = jourRestant(this.fin);
//            LocalDateTime ldt = LocalDateTime.now();
//            LocalDateTime ldt3 = this.fin.toLocalDateTime();
//            long diffH = ldt.until(ldt3, ChronoUnit.HOURS);
            if (secR < 0) {
                tTime.setText("Enchere terminée");

                try {
                    if ((recupereEtatLivraison(this.main.getBDD(), this.idObj) != 2) || (recupereEtatLivraison(this.main.getBDD(), this.idObj) != 3)) {
                        setEtatLivraison(this.main.getBDD(), 1); //ca veut dire que l'objet n'est plus en vente mais que mode de livraison n'est pas déterminé
                        messageFin();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                tTime.setText(jourR + " j " + heureR + " h " + minR + " m " + secR + " s");
            }
        }));
        tempsRestant.setCycleCount(Animation.INDEFINITE);
        tempsRestant.play();
        
        
        long secR = secRestant(this.fin);
        if(secR < 0) {
                tempsRestant.stop();;
                tTime.setText("Stop timeline");
                
                
            } 
    }
 
  private static int recupereEtatLivraison(Connection con, int id)
            throws SQLException, ClassNotFoundException {
        int etat = 0;
        try ( PreparedStatement st = con.prepareStatement("select * from objet where id = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                etat = res.getInt("Etatlivraison");
            }
        }
        return etat;
    }

    public static void setEtatLivraison(Connection con, int valeur) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "update objet set Etatlivraison = ?", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, valeur);
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
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

    private void ActualisePrix() {
        Timeline Prix = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), (t) -> {
            int prixActu = Integer.parseInt(getNbr(this.prixActuel.getText()));
            Connection con = this.main.getBDD();

            try ( PreparedStatement st = con.prepareStatement("select * from objet where id = ?")) {
                st.setInt(1, this.idObj);
                ResultSet res = st.executeQuery();
                while (res.next()) {
                    int prix = res.getInt("prixactuel");
                    if (prixActu != prix) {
                        this.prixActuel.setText(String.valueOf(prix) + " €");
                        System.out.println("Prix actualisé");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));
        Prix.setCycleCount(Animation.INDEFINITE);
        Prix.play();
    }
     private void messageFin() throws SQLException, ClassNotFoundException {
        String TextePourVendeurSiPasEnchere = "Votre objet '" + BDD.recupereTitreObjet(this.main.getBDD(), this.idObj) + "' n'est plus en vente mais il n'y a pas eut d'offres! \nVous pouvez le remettre en ligne en suivant les conseils du guide";
        int idVendeur = this.idVendeur;
        int IdDernierMec = UtilDernierEnchereSurObjet(this.idObj);
        if (UtilDernierEnchereSurObjet(this.idObj) != -1) {
            String TextePourAcheteurSiEnchere = "Vous avez gagné l'enchere sur l'objet: " + BDD.recupereTitreObjet(this.main.getBDD(), this.idObj) + "\nVous pouvez à présent choisir le mode de réception sur la rubrique 'Mes Encheres Finies'";
            String TextePourVendeurSiEnchere = "Votre objet " + BDD.recupereTitreObjet(this.main.getBDD(), this.idObj) + " n'est plus en vente! L'utilisateur ";
            TextePourVendeurSiEnchere = TextePourVendeurSiEnchere + recupereNomUTil(this.main.getBDD(), UtilDernierEnchereSurObjet(this.idObj)) + " vous propose de vous l'acheter";
            BDD.createMessage(this.main.getBDD(), TextePourAcheteurSiEnchere, UtilDernierEnchereSurObjet(this.idObj), this.idVendeur);
            BDD.createMessage(this.main.getBDD(), TextePourVendeurSiEnchere, this.idVendeur, UtilDernierEnchereSurObjet(this.idObj));
        }
        else{
            //createMessage(this.main.getBDD(), TextePourVendeurSiPasEnchere, )
        }

    }
     public int UtilDernierEnchereSurObjet(int idObjet) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        int idDernierUtil = -1;
        try ( PreparedStatement st = con.prepareStatement("select * from enchere where (select max(montant) from enchere where sur=?)")) {
            st.setInt(1, idObjet);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                idDernierUtil = res.getInt("de");

            }
            return idDernierUtil;
        }
    }

    public static String recupereNomUTil(Connection con, int id)
            throws SQLException, ClassNotFoundException {
        String NOM = "";
        try ( PreparedStatement st = con.prepareStatement("select * from utilisateur where id = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                String nom = res.getString("nom");
                String prenom = res.getString("prenom");
                NOM = nom.toUpperCase() + " " + prenom.toUpperCase();
            }
        }
        return NOM;

    }
    private Timestamp tempsRestant(Timestamp debut, Timestamp fin) {
        long l1 = debut.getTime();
        long l2 = fin.getTime();
        long l3 = l2 - l1;

        return new Timestamp(l3);

    }

    private long secRestant(Timestamp fin) {
        LocalDateTime l1 = LocalDateTime.now();
        LocalDateTime l2 = this.fin.toLocalDateTime();
        long diffS = l1.until(l2, ChronoUnit.SECONDS);
        return diffS % 60;
    }

    private long minRestant(Timestamp fin) {
        LocalDateTime l1 = LocalDateTime.now();
        LocalDateTime l2 = this.fin.toLocalDateTime();
        long diffM = l1.until(l2, ChronoUnit.MINUTES);
        return diffM % 60;

    }

    private long hRestant(Timestamp fin) {
        LocalDateTime l1 = LocalDateTime.now();
        LocalDateTime l2 = this.fin.toLocalDateTime();
        long diffH = l1.until(l2, ChronoUnit.HOURS);
        return diffH % 24;

    }

    private long jourRestant(Timestamp fin) {
        LocalDateTime l1 = LocalDateTime.now();
//        System.out.println("now"+LocalDateTime.now());
//        System.out.println("fin"+fin.toLocalDateTime());
        LocalDateTime l2 = fin.toLocalDateTime();
        long diffJ = l1.until(l2, ChronoUnit.DAYS);

        return diffJ;

    }
    private void RecupCoordUtil(int id)
            throws SQLException, ClassNotFoundException {
        Connection con = this.main.getBDD();
        try ( PreparedStatement st = con.prepareStatement("select * from utilisateur where id = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                this.Utillatitude = res.getDouble("lat");
                this.Utillongitude = res.getDouble("long");
            }
        }

    }

    
}
