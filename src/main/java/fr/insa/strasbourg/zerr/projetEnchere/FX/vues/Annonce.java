/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javax.management.timer.Timer;

/**
 *
 * @author jules
 */
public class Annonce extends GridPane {

    private FenetrePrincipale main;
    private String titre;
    private Timestamp debut;
    private Timestamp fin;
    private Timestamp restant;
    private Integer prixDeBase;
    private Integer categorie;
    private Integer idVendeur;
    private Integer id;

    public Annonce(FenetrePrincipale main, Integer id) {
        this.setId("grille-annonce");
        this.id = id;
        this.main = main;
        try {
            recupereObjet(this.id);
        } catch (SQLException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.restant = tempsRestant(debut, fin);
        long time = this.restant.getTime();
        Timer ti = new Timer();

        Label timeLabel = new Label();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        Timeline tempsRestant = new Timeline(new KeyFrame(Duration.seconds(1), (t) -> {
            Timestamp mtn = new Timestamp(System.currentTimeMillis());

            Timestamp sec = secRestant(debut, fin);
            Timestamp min = minRestant(debut, fin);
            Timestamp heure = hRestant(debut, fin);
            Timestamp jour = jourRestant(debut, fin);
            long secR = sec.getTime() - mtn.getSeconds();
            long minR = min.getTime() - mtn.getMinutes();
            long heureR = heure.getTime() - mtn.getHours();
            long jourR = jour.getTime() - mtn.getDay();
            timeLabel.setText("temps restant: " +jourR+" jours, "+heureR+" heures, "+minR+" minutes, "+ secR + " sec");
        }));
        
        
        tempsRestant.setCycleCount(Animation.INDEFINITE);
        tempsRestant.play();
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
        this.add(timeLabel, 0, 6);

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
                System.out.println("debut recuperé" + debut);
            }
        }
    }

    private Timestamp tempsRestant(Timestamp debut, Timestamp fin) {
        long l1 = debut.getTime();
        long l2 = fin.getTime();
        long l3 = l2 - l1;

        return new Timestamp(l3);

    }

    private Timestamp secRestant(Timestamp debut, Timestamp fin) {
        long l1 = debut.getSeconds();
        long l2 = fin.getSeconds();
        long l3 = l2 - l1;
        return new Timestamp(l3);

    }

    private Timestamp minRestant(Timestamp debut, Timestamp fin) {
        long l1 = debut.getMinutes();
        long l2 = fin.getMinutes();
        long l3 = l2 - l1;
        return new Timestamp(l3);

    }

    private Timestamp hRestant(Timestamp debut, Timestamp fin) {
        long l1 = debut.getHours();
        long l2 = fin.getHours();
        long l3 = l2 - l1;
        return new Timestamp(l3);

    }

    private Timestamp jourRestant(Timestamp debut, Timestamp fin) {
        long l1 = debut.getDay();
        long l2 = fin.getDay();
        long l3 = l2 - l1;
        return new Timestamp(l3);

    }

}
