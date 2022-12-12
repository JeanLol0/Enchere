/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;

import fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.SessionInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 *
 * @author jules
 */
public class VueNouvelleAnnonce extends GridPane {

    private Label label;
    private FenetrePrincipale main;
    private Text titre;
    private TextField tfTitre;
    
    private DatePicker dDebut;
    private DatePicker dFin;
    
    private TimePicker tDebut;
    private TimePicker tFin;
    
    private TextField prixBase;
    private TextField categorie; //TODO
    private Integer proposerPar;
    private Button bCreerAnnonce;
    
    private DateTimePicker dtPicker;
    
    private SessionInfo sessionInfo;

    public VueNouvelleAnnonce(FenetrePrincipale main) {
                this.main = main;

        this.sessionInfo = this.main.getSessionInfo();
        this.label = new Label("page nouvelle annonce ");
        this.tfTitre = new TextField("Titre de l'annonce");
        
        this.dtPicker = new DateTimePicker("dt");
        
        
        this.dDebut = new DatePicker(LocalDate.MIN);
         this.dFin = new DatePicker(LocalDate.MIN);
         this.tDebut = new TimePicker();
         this.tFin = new TimePicker();
         
         this.dDebut.setValue(LocalDate.now());
         
        final Callback<DatePicker, DateCell> dayCellFactory = 
                (final DatePicker datePicker) -> new DateCell(){
                    
            public void updateItem(LocalDate item, boolean empty){
                super.updateItem(item, empty);
                if(item.isBefore(dDebut.getValue().plusDays(1))){
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
                long p = ChronoUnit.DAYS.between(dDebut.getValue(), item);//temps entre les deux dates 
                //todo 
                
            }
        };
        
       this.dFin.setDayCellFactory(dayCellFactory);
       this.dFin.setValue(dDebut.getValue().plusDays(1));
        
        this.prixBase = new TextField("indiquer le prix");
        this.prixBase.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (newValue.matches("\\d*")) {
                    int value = Integer.parseInt(newValue);
                } else {
                    prixBase.setText(oldValue);
                }
            }
        });
        this.bCreerAnnonce = new Button("Mettre en ligne");
        this.categorie = new TextField("n° catégorie --> TODO");
        this.add(this.label, 0, 1);
        this.add(this.tfTitre, 0, 2);
        this.add(this.dDebut, 0, 3);
        this.add(this.tDebut, 1, 3);
        this.add(this.dtPicker, 1, 7);
        this.add(this.dFin, 0, 4);
        this.add(this.tFin, 1, 4);
        this.add(this.prixBase, 0, 5);
        this.add(this.categorie, 0, 6);
        this.add(this.bCreerAnnonce, 0, 7);
        DurationPicker dur = new DurationPicker();
        this.add(dur, 5, 10);

        this.bCreerAnnonce.setOnAction((t) -> {
            doMiseEnLigne();
        });

    }

//    private void doMiseEnLigne() {
//
//        Connection con = this.main.getBDD();
//        try {
//            if (this.tfTitre.getText().isEmpty()) {
//                JavaFXUtils.showErrorInAlert("Erreur", "Competez les infos necessaires", "");
//
//            } else {
//                String titre = this.tfTitre.getText();
//                int yearD = dDebut.getValue().getYear();
//                int monthD = dDebut.getValue().getMonthValue();
//                int dayD = dDebut.getValue().getDayOfMonth();
//                int heureD = tDebut.getHeure();
//                int minuteD = tDebut.getMinute();
//                
//                int yearF = dFin.getValue().getYear();
//                int monthF = dFin.getValue().getMonthValue();
//                int dayF = dFin.getValue().getDayOfMonth();
//                int heureF = tFin.getHeure();
//                int minuteF = tFin.getMinute();
//                int categorie = Integer.parseInt(this.categorie.getText());
//                
//                              
//                BDD.createObjet(con, titre, 
//                        new Timestamp(yearD, monthD, dayD, heureD, minuteD, 0, 0), 
//                        new Timestamp(yearF, monthF, dayF, heureF, minuteF, 0, 0), 
//                        Integer.parseInt(this.prixBase.getText()), categorie, this.sessionInfo.getUserID());
//                System.out.println("annonce créé");
//                this.main.setCenter(new VueAcceuil(this.main));
//            }
//        } 
//        catch (SQLException ex) {
//            Logger.getLogger(VueInscription.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }

    private void doMiseEnLigne() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
