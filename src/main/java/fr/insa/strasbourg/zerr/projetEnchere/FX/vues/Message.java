/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;

/**
 *
 * @author jules
 */
public class Message extends HBox {

    private FenetrePrincipale main;
    private String titre;
    private Label TitreMessage;
    private Label TexteContenu;
    private Label textedate;
    private Label Envoyeur;

    private Image image;
    private String stringImage;
    private GridPane grid;

    private int idAcheteur;
    private int idVendeur;
    private int idMessage;
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

    public Message(FenetrePrincipale main, Integer id) throws SQLException, ClassNotFoundException {
        //this.getStylesheets().add(getClass().getResource("StyleAnnonce.css").toExternalForm());
        this.idMessage = id;
        this.main = main;
        int idUtil = this.main.getSessionInfo().getUserID();
        recupereMessage(idUtil);
        this.grid = new GridPane();
        this.grid.setHgap(20);
        this.setId("message");

        this.tTime = new Label(this.titre);
        this.TexteContenu = new Label(this.ContenuMessage);
        this.Envoyeur = new Label(getNom(idAcheteur));
        //this.textedate = new Label(this.date.toString());
        

//        this.tTitre.setId("grand-text-annonce");
        this.grid.add(this.tTime, 0, 0, 2, 1);
        this.grid.add(TexteContenu, 3, 0);
        this.grid.add(Envoyeur, 4, 8);
        //this.grid.add(textedate, 5, 8);

        this.getChildren().addAll(this.grid);

    }

    private void recupereMessage(int id)
            throws SQLException, ClassNotFoundException {
        Connection con = this.main.getBDD();

        try ( PreparedStatement st = con.prepareStatement("select * from message where vendeur = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                idAcheteur = res.getInt("acheteur");
                idVendeur = res.getInt("vendeur");
                idMessage = res.getInt(ContenuMessage);
                ContenuMessage = res.getString("texte");
                this.date = res.getTimestamp("date");
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
