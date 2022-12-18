/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.composants.Categories;
import fr.insa.strasbourg.zerr.projetEnchere.FX.composants.VueImage;
import fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD;

import fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.SessionInfo;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 *
 * @author jules
 */
public class VueNouvelleAnnonce extends ScrollPane {

    private FenetrePrincipale main;
    private GridPane gridMain;
    private Text titre;
    private TextField tfTitre;
    
    private VueImage image;
    
    private RadioButton rbDuree;
    private RadioButton rbProgramme;

    private DatePicker dDebut;
    private DatePicker dFin;

    private TimePicker tDebut;
    private TimePicker tFin;

    private TextField prixBase;
    private TextField categorie; //TODO
    private Categories categories;

    private TextArea taDescription;
    private Integer proposerPar;
    private Button bCreerAnnonce;

    // private DateTimePicker dtPicker;
    private SessionInfo sessionInfo;

    public VueNouvelleAnnonce(FenetrePrincipale main) {
        this.main = main;
        this.gridMain =new GridPane();
        this.gridMain.setAlignment(Pos.CENTER);
        this.setFitToWidth(true);
        this.rbDuree = new RadioButton("Définir la durée de l'enchère");
        this.rbProgramme = new RadioButton("Programmer le début et la fin de la vente");
        this.bCreerAnnonce = new Button("Mettre en ligne");
        this.rbProgramme.setOnAction((t) -> {
            insererLigne(9,2);
            this.gridMain.add(this.dDebut, 0, 9);
            this.gridMain.add(this.tDebut, 1, 9);
            this.gridMain.add(this.dFin, 0, 10);
            this.gridMain.add(this.tFin, 1, 10);
            this.rbProgramme.setDisable(true);
        });
        this.rbDuree.setOnAction((t) -> {
            if(this.rbProgramme.isSelected()){
            insererLigne(12,1);
            this.gridMain.add(new Label("TODO"), 0, 12);
            
            this.rbDuree.setDisable(true);
            }
        });

        this.sessionInfo = this.main.getSessionInfo();
        this.tfTitre = new TextField("Titre de l'annonce");
        this.taDescription = new TextArea("Dercription de l'objet");
//        this.taDescription.setMaxHeight(120);
//        this.taDescription.setPrefWidth(300);
        this.taDescription.setWrapText(true);
//this.dtPicker = new DateTimePicker();

        this.dDebut = new DatePicker(LocalDate.MIN);
        this.dFin = new DatePicker(LocalDate.MIN);
        this.tDebut = new TimePicker();
        this.tFin = new TimePicker();
        

        this.dDebut.setValue(LocalDate.now());

        final Callback<DatePicker, DateCell> dayCellFactory
                = (final DatePicker datePicker) -> new DateCell() {

            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(dDebut.getValue().plusDays(0))) {
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
        this.prixBase.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.matches("\\d*")) {
                int value = Integer.parseInt(newValue);
            } else {
                prixBase.setText(oldValue);
            }
        });
        this.bCreerAnnonce = new Button("Mettre en ligne");
        this.categorie = new TextField("n° catégorie --> TODO");
        this.categories = new Categories(this.main);
        this.image = new VueImage();
        
        Pane re = new Pane();
        re.getChildren().add(this.categories);
        re.setPrefHeight(200);

        gridMain.setVgap(20);
        gridMain.setHgap(20);
        gridMain.setAlignment(Pos.TOP_CENTER);

        
        
          this.gridMain.add(new Label("Titre"), 0, 0, 2, 1);
          this.gridMain.add(this.tfTitre, 0, 1, 2, 1);
          this.gridMain.add(new Label("Ajouter une photo"), 0,2, 2, 1);
          this.gridMain.add(this.image, 0, 3);
          this.gridMain.add(new Label("Ajouter une déscription"), 0, 4, 2, 1);
          this.gridMain.add(this.taDescription, 0, 5, 2, 1);
          this.gridMain.add(new Label("Définir le prix de départ"), 0, 6, 2, 1);
          this.gridMain.add(this.prixBase, 0, 7, 2, 1);
          this.gridMain.add(this.rbProgramme, 0, 8, 2, 1);
          this.gridMain.add(this.rbDuree, 0, 9, 2, 1);
          this.gridMain.add(new Label("Définir une catégorie"), 0, 10, 2, 1);
          this.gridMain.add(this.categories, 0, 11, 2, 1);
          this.gridMain.add(this.bCreerAnnonce, 0, 12);
//        gridMain.add(this.tfTitre, 0, 0);
//        gridMain.add(new Text("Début de l'enchère"), 0, 1);
//        gridMain.add(this.dDebut, 1, 1);
//        gridMain.add(this.tDebut, 1, 2);
//        gridMain.add(new Text("Fin de l'enchère"), 0, 2); 
//        gridMain.add(this.dFin, 1, 2); 
//        gridMain.add(this.tFin, 2, 2);
//        gridMain.add(new Text("Définir une durée ?"), 0, 3); 
//        gridMain.add(new CheckBox(), 1, 3);
//        gridMain.add(new Text("duration picker todo"), 0, 4);
//        gridMain.add(this.prixBase, 0, 5);
//        gridMain.add(this.taDescription, 0, 6);
//        
//        gridMain.add(this.categorie, 0, 10);
//        gridMain.add(new Text("Définir une catégorie"), 3, 0);
//        gridMain.add(re, 3  , 1);
//        gridMain.add(this.bCreerAnnonce, 3, 6);
        
        this.setContent(this.gridMain);

        this.bCreerAnnonce.setOnAction((t) -> {
            try {
                doMiseEnLigne();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(VueNouvelleAnnonce.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        this.image.setOnDragOver((final DragEvent event) -> {
            this.image.mouseDragOver(event);
        });
        
        this.image.setOnDragDropped((final DragEvent event) -> {
            this.image.mouseDragDropped(event);
        });

         this.image.setOnDragExited((final DragEvent event) -> {
             this.image.getContentPane().setStyle("-fx-border-color: #C6C6C6;");
        });
        

    }

    private void doMiseEnLigne() throws FileNotFoundException {

        Connection con = this.main.getBDD();
        try {
            if (this.tfTitre.getText().isEmpty()) {
                JavaFXUtils.showErrorInAlert("Erreur", "Competez les infos necessaires", "");

            } else {
                String titre = this.tfTitre.getText();
                int yearD = dDebut.getValue().getYear();
                int monthD = dDebut.getValue().getMonthValue();
                int dayD = dDebut.getValue().getDayOfMonth();
                int heureD = tDebut.getHeure();
                int minuteD = tDebut.getMinute();

                int yearF = dFin.getValue().getYear();
                int monthF = dFin.getValue().getMonthValue();
                int dayF = dFin.getValue().getDayOfMonth();
                int heureF = tFin.getHeure();
                int minuteF = tFin.getMinute();
                int categorie = getIdCategorie(this.categories.getTextCategorieSelected());
                BDD.createObjet(con, titre,
                        new Timestamp(yearD, monthD, dayD, heureD, minuteD, 0, 0),
                        new Timestamp(yearF, monthF, dayF, heureF, minuteF, 0, 0),
                        Integer.parseInt(this.prixBase.getText()), categorie, this.sessionInfo.getUserID(),"Texte",null);
                //ici à la place cu null il faut mettre l'image en format image
                System.out.println("annonce créé");
                this.main.setCenter(new VuePrincipale(this.main));
            }
        } catch (SQLException ex) {
            Logger.getLogger(VueInscription.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public int getIdCategorie(String textCat) throws SQLException{
        Connection con =this.main.getBDD();
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

    private void insererLigne( int pos,int count) {
        for (Node child : this.gridMain.getChildren()) {
          if(GridPane.getRowIndex(child) >=pos){ 
        Integer rowIndex = GridPane.getRowIndex(child);
        GridPane.setRowIndex(child, rowIndex  == null ? count : count + rowIndex );
    }
        }

    }

    
    
    
   
}

