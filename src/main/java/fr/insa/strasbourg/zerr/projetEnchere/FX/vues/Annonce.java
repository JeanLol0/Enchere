/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javax.imageio.ImageIO;

/**
 *
 * @author jules
 */
public class Annonce extends HBox {

    private FenetrePrincipale main;
    private String titre;
    private Timestamp debut;
    private Timestamp fin;
    private Timestamp restant;
    private Label prixActuel;
    private Label categorie;
    private Integer idVendeur;
    private Integer id;
    private Image image;
    private String stringImage;
    private GridPane grid;
    private ImageView imageV;

    private Label npVendeur;
    private Label tTime;

    private Label tTitre;
    private Label tPrix;
    private Label tCategorie;
    private Label tVendeur;
    private Label tTempsR;

    public Annonce(FenetrePrincipale main, Integer id) throws SQLException, ClassNotFoundException, IOException {
        //this.getStylesheets().add(getClass().getResource("StyleAnnonce.css").toExternalForm());
        this.id = id;
        this.main = main;
        recupereObjet(this.id);
        this.grid = new GridPane();
        this.grid.setHgap(20);
        this.setId("annonce");
        this.image = texteEnImage(this.stringImage);
        this.imageV = new ImageView(this.image);
        this.imageV.setFitHeight(200);
        this.imageV.setFitWidth(200);

        this.tTime = new Label("Temps restant :");
        this.tPrix = new Label("Prix :");
        this.tCategorie = new Label("Catégorie :");
        this.tVendeur = new Label("Nom du vendeur :");
        this.tTitre = new Label(this.titre);
        this.tTempsR = new Label("Temps restant :");
        
        this.tTitre.setId("grand-text-annonce");
        
        this.grid.add(this.tTitre, 0, 0,2,1);
        this.grid.add(tPrix, 0, 2);
        this.grid.add(prixActuel, 1, 2);
        this.grid.add(tCategorie, 0, 3);
        this.grid.add(categorie, 1, 3);
        this.grid.add(tVendeur, 0, 4);
        this.grid.add(npVendeur, 1, 4);
        this.grid.add(tTempsR, 0, 5);
        this.grid.add(tTime, 1, 5);
        this.getChildren().addAll(this.imageV, this.grid);

        Timeline tempsRestant = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), (t) -> {
            Timestamp mtn = new Timestamp(System.currentTimeMillis());
            long secR = secRestant(this.fin);
            long minR = minRestant(this.fin);
            long heureR = hRestant(this.fin);
            long jourR = jourRestant(this.fin);
//            LocalDateTime ldt = LocalDateTime.now();
//            LocalDateTime ldt3 = this.fin.toLocalDateTime();
//            long diffH = ldt.until(ldt3, ChronoUnit.HOURS);
                if(secR<0){
                    tTime.setText("Enchere terminée");
                }else{
                    tTime.setText(jourR + " j " + heureR + " h " + minR + " m " + secR + " s");
                }
        }));
        tempsRestant.setCycleCount(Animation.INDEFINITE);
        tempsRestant.play();
        
        this.tTitre.setOnMouseClicked((t) -> {
            //this.main.setCenter(new VueAnnonceDetaille(this.main, this.id, tTitre, tTempsR, tPrix, categorie, tVendeur, tTempsR, imageV));
            this.main.setCenter(new VueAnnonceDetaille(main, grid, this.id,this.imageV));
        });
        
    }

    private void recupereObjet(int id)
            throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");

        try (PreparedStatement st = con.prepareStatement("select * from objet where id = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                this.titre = res.getString("titre");
                this.debut = res.getTimestamp("debut");
                this.fin = res.getTimestamp("fin");
                int prix = res.getInt("prixactuel");
                this.prixActuel = new Label(String.valueOf(prix));
                int idcat = res.getInt("categorie");
                this.categorie = new Label(getStringCategorie(idcat));
                String nom = getNom(res.getInt("proposerpar"));
                String prenom = getPrenom(res.getInt("proposerpar"));
                this.npVendeur = new Label(prenom + " " + nom);
                this.idVendeur = res.getInt("proposerpar");
                this.stringImage = res.getString("image");

            }
        }

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

    public static long getHeuresRestantes(Timestamp fin) {
        LocalDateTime debut1 = LocalDateTime.now();
        LocalDateTime fin1 = fin.toLocalDateTime();
        long diffHeures = fin1.until(debut1, ChronoUnit.HOURS);
        while (diffHeures >= 8760) {
            diffHeures = diffHeures - 8760;
        }
        while (diffHeures >= 730) {
            diffHeures = diffHeures - 730;
        }
        while (diffHeures >= 24) {
            diffHeures = diffHeures - 24;
        }
        return diffHeures;
    }

    public static long getMinutesRestantes(Timestamp fin) {
        LocalDateTime debut1 = LocalDateTime.now();
        LocalDateTime fin1 = fin.toLocalDateTime();
        long diffMinutes = fin1.until(debut1, ChronoUnit.MINUTES);
        while (diffMinutes >= 525600) {
            diffMinutes = diffMinutes - 525600;
        }
        while (diffMinutes >= 43800) {
            diffMinutes = diffMinutes - 43800;
        }
        while (diffMinutes >= 1440) {
            diffMinutes = diffMinutes - 1440;
        }
        while (diffMinutes >= 60) {
            diffMinutes = diffMinutes - 60;
        }
        return diffMinutes;
    }

    public static long getJoursRestants(Timestamp fin) {
        LocalDateTime debut1 = LocalDateTime.now();
        LocalDateTime fin1 = fin.toLocalDateTime();
        long diffJours = fin1.until(debut1, ChronoUnit.MINUTES);
        while (diffJours >= 365) {
            diffJours = diffJours - 365;
        }
        while (diffJours >= 30) {
            diffJours = diffJours - 30;
        }
        return diffJours;
    }

    public static long getSecondesRestantes(Timestamp fin) {
        LocalDateTime debut1 = LocalDateTime.now();
        LocalDateTime fin1 = fin.toLocalDateTime();
        long diffSecondes = fin1.until(debut1, ChronoUnit.SECONDS);
        while (diffSecondes >= 31536008) {
            diffSecondes = diffSecondes - 31536008;
        }
        while (diffSecondes >= 2628003) {
            diffSecondes = diffSecondes - 2628003;
        }
        while (diffSecondes >= 86400) {
            diffSecondes = diffSecondes - 86400;
        }
        while (diffSecondes >= 3600) {
            diffSecondes = diffSecondes - 3600;
        }
        while (diffSecondes >= 60) {
            diffSecondes = diffSecondes - 60;
        }
        return diffSecondes;
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
        try (PreparedStatement st = con.prepareCall("select * from categorie where id = ?")) {
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
        try (PreparedStatement st = con.prepareCall("select nom from utilisateur where id = ?")) {
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
        try (PreparedStatement st = con.prepareCall("select prenom from utilisateur where id = ?")) {
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

}
//        Timeline tempsRestant = new Timeline(new KeyFrame(Duration.seconds(1), (t) -> {
//            Timestamp mtn = new Timestamp(System.currentTimeMillis());
//
//            Timestamp sec = secRestant(mtn, fin);
//            Timestamp min = minRestant(mtn, fin);
//            Timestamp heure = hRestant(mtn, fin);
//            Timestamp jour = jourRestant(mtn, fin);
//            long secR = sec.getTime();
//            long minR = min.getTime() ;
//            long heureR = heure.getTime() ;
//            long jourR = jour.getTime() ;
//            
//            LocalDateTime ldt = LocalDateTime.now();
//            LocalDateTime ldt3 = this.fin.toLocalDateTime();
//            long diffH = ldt.until(ldt3, ChronoUnit.HOURS);
//           timeLabel.setText("temps restant: " +jourR+" jours, "+heureR+" heures, "+minR+" minutes, "+ secR + " sec");
//            //timeLabel.setText("temps restant: " );
//            System.out.println();
//        }));
//        
//        
//        tempsRestant.setCycleCount(Animation.INDEFINITE);
//        tempsRestant.play();
