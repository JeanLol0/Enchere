/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.createMessage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javax.imageio.ImageIO;

/**
 *
 * @author jules
 */
public class Message extends GridPane {

    private FenetrePrincipale main;
    private Label TitreMessage;
    private Label TexteContenu;
    private Label textedate;
    private Label Envoyeur;

    private Image image;
    private String stringImage;
    //private GridPane grid;

    private int idAcheteur;
    private int idVendeur;
    private int idMessage;
    private String titre;
    private int type;
    private String ContenuMessage;
    private Timestamp date;

    private Label npVendeur;
    private Label tTime;
    private Label tTitre;
    private Label tPrix;
    private Label tCategorie;
    private Label tVendeur;
    private Label tTempsR;
    private Label tDistance;
    private Label lChoixRemise;
    private RadioButton bExp;
    private RadioButton bMain;

    public Message(FenetrePrincipale main, Integer id) throws SQLException, ClassNotFoundException {
        //this.getStylesheets().add(getClass().getResource("StyleAnnonce.css").toExternalForm());
        this.idMessage = id;
        this.main = main;
        int idUtil = this.main.getSessionInfo().getUserID();
        recupereMessage(id);
        this.setVgap(20);
        this.setId("message");
        this.tTime = new Label(this.titre);
        this.TexteContenu = new Label(this.ContenuMessage);
        this.Envoyeur = new Label(getNom(idAcheteur));
        this.textedate = new Label(this.date.toString());

        this.bExp = new RadioButton("Expédition");
        this.bMain = new RadioButton("Main propre");
        this.lChoixRemise = new Label("Choisissez le moyen de remise de l'objet pour en informer le vendeur (votre choix sera définitif)");

        this.tTime.setId("grand-text-annonce");
        this.add(this.tTime, 0, 0, 2, 1);
        this.add(TexteContenu, 0, 1, 3, 1);
        this.add(this.textedate, 0, 2);
        this.add(Envoyeur, 0, 3);

        if (this.titre == "Vous avez remporté une enchère!") {

            this.add(this.lChoixRemise, 0, 4);
            this.add(this.bExp, 0, 5);
            this.add(this.bMain, 1, 5);

        }
        int message = getEtatLivraison(this.main.getBDD(), recupereidObjet(this.main.getBDD(), this.idMessage));
        System.out.println(message);
        if (getEtatLivraison(this.main.getBDD(), recupereidObjet(this.main.getBDD(), this.idMessage)) != 1) {
            this.bExp.setDisable(true);
            this.bMain.setDisable(true);
        }

        this.bExp.setOnAction((t) -> {
//            setEtatLivraison(3, 2);
            this.bExp.setDisable(true);
            this.bMain.setDisable(true);
            try {
                messageMainP();
            } catch (SQLException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                messageConfirmation();
            } catch (SQLException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.bMain.setOnAction((t) -> {
            System.out.println("bouton main cell");
            this.bExp.setDisable(true);
            this.bMain.setDisable(true);
            try {
                messageExp();
            } catch (SQLException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                messageConfirmation();
            } catch (SQLException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //this.grid.add(textedate, 5, 8);
        //this.setGridLinesVisible(true);
        //this.setAlignment(Pos.TOP_CENTER);
    }

    private void recupereMessage(int id)
            throws SQLException, ClassNotFoundException {
        Connection con = this.main.getBDD();

        try ( PreparedStatement st = con.prepareStatement("select distinct * from message where id = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                idAcheteur = res.getInt("acheteur");
                idVendeur = res.getInt("vendeur");
                ContenuMessage = res.getString("texte");
                this.type = res.getInt("titre");
                this.date = res.getTimestamp("date");
                if (type == 1) {
                    titre = "Nouvelle Enchere sur l'un de vos objet! ";
                } else if (type == 2) {
                    titre = "Vous avez remporté une enchère!";
                } else if (type == 3) {
                    titre = "Annonce terminée! Préparez la remise/livraison !";
                } else if (type == 4) {
                    titre = "Annonce terminée...Mauvaise nouvelle";
                } else if (type == 5) {
                    titre = "Objet: " + BDD.recupereTitreObjet(this.main.getBDD(), recupereidObjet(this.main.getBDD(), this.idMessage)) + " L'Acheteur a choisi le mode de livraison";
                } else if (type == 6) {
                    titre = "Objet: " + BDD.recupereTitreObjet(this.main.getBDD(), recupereidObjet(this.main.getBDD(), this.idMessage)) + " L'Acheteur a choisi le mode de livraison";
                } else if (type == 7) {
                    titre = "Confirmation mode de livraison";
                }

            }
        }

    }

    public static int CalculDistance(double Utlong, double Utlat, double Objlong, double Objlat) {
        double value = Math.sin(Math.toRadians(Utlat)) * Math.sin(Math.toRadians(Objlat));
        double value2 = Math.cos(Math.toRadians(Objlat)) * Math.cos(Math.toRadians(Utlat)) * Math.cos(Math.toRadians(Utlong - Objlong));
        double arccos = Math.acos(value + value2);
        double distance = 6371 * arccos;
        int distancerenvoye = (int) Math.round(distance);
        return distancerenvoye;
    }//renvoit une distance en kilometres

    private Timestamp tempsRestant(Timestamp debut, Timestamp fin) {
        long l1 = debut.getTime();
        long l2 = fin.getTime();
        long l3 = l2 - l1;

        return new Timestamp(l3);

    }

    public static Image texteEnImage(String img) throws IOException {

        byte[] result = Base64.getUrlDecoder().decode(img);
        ByteArrayInputStream bis = new ByteArrayInputStream(result);
        BufferedImage bImage2 = ImageIO.read(bis);
        Image Final = SwingFXUtils.toFXImage(bImage2, null);
        return Final;
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

    public String getNom(Integer idU) throws SQLException {
        Connection con = this.main.getBDD();
        String nom = "";
        String prenom = "";
        try ( PreparedStatement st = con.prepareCall("select nom,prenom from utilisateur where id = ?")) {
            st.setInt(1, idU);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                nom = res.getString("nom");
                prenom = res.getString("prenom");
            }
        }
        String NomEntier = nom.toUpperCase() + " " + prenom.toUpperCase();
        return NomEntier;
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

    public int UtilDernierEnchereSurObjet(int idObjet) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        int idDernierUtil = -1;
        try ( PreparedStatement st = con.prepareStatement("select * from enchere where montant = (select max(montant) from enchere where sur = ?)")) {
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

    public static int recupereidObjet(Connection con, int id)
            throws SQLException, ClassNotFoundException {
        int idObj = 0;
        try ( PreparedStatement st = con.prepareStatement("select objet from message where id = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                idObj = res.getInt("objet");
            }
        }
        return idObj;
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

    public void setEtatLivraison(int valeur, int idObjet) throws SQLException {
        Connection con = this.main.getBDD();
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "update objet set Etatlivraison = ? where id = ?", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, valeur);
            pst.setInt(2, idObjet);
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

    
    public void messageExp() throws SQLException, ClassNotFoundException {
        String texte = getNom(idVendeur).toUpperCase() + " vous propose d'envoyer l'objet: " + BDD.recupereTitreObjet(this.main.getBDD(), recupereidObjet(this.main.getBDD(), this.idMessage)) + " par voie postal. \nVous disposez de 15 jours pour envoyer le colis.";
        BDD.createMessage(this.main.getBDD(), texte, idVendeur, idAcheteur, 5, recupereidObjet(this.main.getBDD(), this.idMessage));
        setEtatLivraison(2, recupereidObjet(this.main.getBDD(), this.idMessage));
    }

    public void messageMainP() throws SQLException, ClassNotFoundException {
        String texte = getNom(idVendeur).toUpperCase() + " vous propose de vous retrouver afin de donner en main propre l'objet: " + BDD.recupereTitreObjet(this.main.getBDD(), recupereidObjet(this.main.getBDD(), this.idMessage)) + "\nVous disposez de 15 jours pour envoyer le colis.";
        BDD.createMessage(this.main.getBDD(), texte, idVendeur, idAcheteur, 6, recupereidObjet(this.main.getBDD(), this.idMessage));
        setEtatLivraison(2, recupereidObjet(this.main.getBDD(), this.idMessage));
    }

    public void messageConfirmation() throws SQLException, ClassNotFoundException {
        String texte = "Le mode de livraison de l'objet: " + BDD.recupereTitreObjet(this.main.getBDD(), recupereidObjet(this.main.getBDD(), type)).toUpperCase() + " a bien été choisi";
        BDD.createMessage(this.main.getBDD(), texte, idVendeur, idAcheteur, 7, recupereidObjet(this.main.getBDD(), this.idMessage));
        BDD.createMessage(this.main.getBDD(), texte, idAcheteur, idVendeur, 7, recupereidObjet(this.main.getBDD(), this.idMessage));
    }

}
