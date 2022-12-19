/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import javax.management.timer.Timer;

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
    private Integer prixDeBase;
    private Integer categorie;
    private Integer idVendeur;
    private Integer id;
    private Image image;
    private String stringImage;
    private GridPane grid;
    private ImageView imageV;
    public Annonce(FenetrePrincipale main, Integer id) {
        this.setId("annonce");
        this.id = id;
        this.main = main;
        this.grid = new GridPane();
        
        try {
            recupereObjet(this.id);
            try {
                this.image =texteEnImage(this.stringImage);
            } catch (IOException ex) {
                Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(this.stringImage);
        
        
//        System.out.println("joure fin "+this.fin.getDay());
//        System.out.println("heure fin "+this.fin.getHours());
//        System.out.println("minutes fin "+this.fin.getMinutes());
//        System.out.println("sec fin "+this.fin.getSeconds());
//        
        
        Timestamp cur = new Timestamp(System.currentTimeMillis());
        //LocalDateTime ldt = LocalDateTime.now();
        //LocalDateTime ldt2 = ldt.plusDays(7);
       // Timestamp conv = Timestamp.valueOf(ldt2);
        //LocalDateTime ldt3 = LocalDateTime.of(2022, Month.MARCH, 12, 0, 0);
        //long diff = ldt.until(ldt3, ChronoUnit.MINUTES);
        //this.restant = tempsRestant(debut, fin);
        //long time = this.restant.getTime();
        Timer ti = new Timer();

        Label timeLabel = new Label();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

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
        
        
        
        
        
        
        
        
        
        
        
        this.grid.add(new Text(this.titre), 0, 0);
        this.grid.add(new Text("Prix de base : "), 0, 2);
        this.grid.add(new Text(String.valueOf(this.prixDeBase)), 1, 2);
        this.grid.add(new Text("Catégorie : "), 0, 3);
        this.grid.add(new Text(String.valueOf(this.categorie)), 1, 3);
        this.grid.add(new Text("ID Vendeur : "), 0, 4);
        this.grid.add(new Text(String.valueOf(this.idVendeur)), 1, 4);
        //this.add(timeLabel, 0, 6);
        this.getChildren().addAll(this.grid);
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
    
    private Timestamp secRestant(Timestamp mtn, Timestamp fin) {
        long l1 = mtn.getSeconds();
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
    
    public static Image texteEnImage(String img) throws IOException {
        
        byte[] result = Base64.getUrlDecoder().decode(img);
        ByteArrayInputStream bis = new ByteArrayInputStream(result);
        BufferedImage bImage2 = ImageIO.read(bis);
        Image Final = SwingFXUtils.toFXImage(bImage2, null);
        return Final;
    }

}
