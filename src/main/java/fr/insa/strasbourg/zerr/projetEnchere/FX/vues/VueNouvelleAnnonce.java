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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
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

    private Text tTitre;
    private Text tPrix;
    private Text tCategorie;
    private Text tPhoto;
    private Text tDescr;

    private SessionInfo sessionInfo;

    public VueNouvelleAnnonce(FenetrePrincipale main) throws IOException {
        this.main = main;
        this.gridMain = new GridPane();
        this.gridMain.setId("vue-nouvelle-annonce");
        this.gridMain.setAlignment(Pos.CENTER);
        this.setFitToWidth(true);
        this.rbDuree = new RadioButton("D??finir la dur??e de l'ench??re");
        this.rbProgramme = new RadioButton("Programmer le d??but et la fin de la vente");
        this.bCreerAnnonce = new Button("Mettre en ligne");
        this.rbProgramme.setOnAction((t) -> {
            insererLigne(9, 2);
            this.gridMain.add(this.dDebut, 0, 9);
            this.gridMain.add(this.tDebut, 1, 9);
            this.gridMain.add(this.dFin, 0, 10);
            this.gridMain.add(this.tFin, 1, 10);
            this.rbProgramme.setDisable(true);
        });
        this.rbDuree.setOnAction((t) -> {
            if (this.rbProgramme.isSelected()) {
                insererLigne(12, 1);
                this.gridMain.add(new Label("TODO"), 0, 12);

                this.rbDuree.setDisable(true);
            }
        });

        this.sessionInfo = this.main.getSessionInfo();
        this.tfTitre = new TextField("Titre de l'annonce");
        this.taDescription = new TextArea("Dercription de l'objet");
        this.taDescription.setWrapText(true);
        this.dDebut = new DatePicker(LocalDate.now());
        this.dFin = new DatePicker(LocalDate.now());
        this.tDebut = new TimePicker();
        this.tFin = new TimePicker();

        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {

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
        
        this.tTitre = new Text("D??finir le titre");
        this.tCategorie = new Text("Ajouter une photo");
        this.tDescr = new Text("Ajouter une d??scription");
        this.tPrix = new Text("D??finir le prix de d??part");
        this.tPhoto = new Text("D??finir une cat??gorie");
        this.tTitre.setId("grand-text-annonce");
        this.tCategorie.setId("grand-text-annonce");
        this.tDescr.setId("grand-text-annonce");
        this.tPrix.setId("grand-text-annonce");
        this.tPhoto.setId("grand-text-annonce");
        
        this.bCreerAnnonce = new Button("Mettre en ligne");
        this.categories = new Categories(this.main);
        this.image = new VueImage();

        gridMain.setVgap(20);
        gridMain.setHgap(20);
        gridMain.setAlignment(Pos.TOP_CENTER);

        this.gridMain.add(this.tTitre, 0, 0, 2, 1);
        this.gridMain.add(this.tfTitre, 0, 1, 2, 1);
        this.gridMain.add(this.tPhoto, 0, 2, 2, 1);
        this.gridMain.add(this.image, 0, 3, 2, 1);
        this.gridMain.add(this.tDescr, 0, 4, 2, 1);
        this.gridMain.add(this.taDescription, 0, 5, 2, 1);
        this.gridMain.add(this.tPrix, 0, 6, 2, 1);
        this.gridMain.add(this.prixBase, 0, 7, 2, 1);
        this.gridMain.add(this.rbProgramme, 0, 8, 2, 1);
        this.gridMain.add(this.rbDuree, 0, 9, 2, 1);
        this.gridMain.add(this.tCategorie, 0, 10, 2, 1);
        this.gridMain.add(this.categories, 0, 11, 2, 1);
        this.gridMain.add(this.bCreerAnnonce, 0, 12);
//        gridMain.add(this.tfTitre, 0, 0);
//        gridMain.add(new Text("D??but de l'ench??re"), 0, 1);
//        gridMain.add(this.dDebut, 1, 1);
//        gridMain.add(this.tDebut, 1, 2);
//        gridMain.add(new Text("Fin de l'ench??re"), 0, 2); 
//        gridMain.add(this.dFin, 1, 2); 
//        gridMain.add(this.tFin, 2, 2);
//        gridMain.add(new Text("D??finir une dur??e ?"), 0, 3); 
//        gridMain.add(new CheckBox(), 1, 3);
//        gridMain.add(new Text("duration picker todo"), 0, 4);
//        gridMain.add(this.prixBase, 0, 5);
//        gridMain.add(this.taDescription, 0, 6);
//        
//        gridMain.add(this.categorie, 0, 10);
//        gridMain.add(new Text("D??finir une cat??gorie"), 3, 0);
//        gridMain.add(re, 3  , 1);
//        gridMain.add(this.bCreerAnnonce, 3, 6);

        this.setContent(this.gridMain);

        this.bCreerAnnonce.setOnAction((t) -> {
            LocalDateTime ldt = LocalDateTime.now();
            LocalDate d1 = LocalDate.of(2014, Month.JULY, 4);
            LocalDate d2 = LocalDate.of(2014, Month.DECEMBER, 25);
            System.out.println(ChronoUnit.DAYS.between(d1, d2));//temps entre les deux dates);
            System.out.println(this.dFin.getValue().getYear());//temps entre les deux dates);
            try {
                doMiseEnLigne();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(VueNouvelleAnnonce.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VueNouvelleAnnonce.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
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
        this.image.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(getContextMenu());
            this.image.mouseClicked(file);
        });

    }

    private void doMiseEnLigne() throws FileNotFoundException, ClassNotFoundException, IOException {

        Connection con = this.main.getBDD();
        try {
            if (this.tfTitre.getText().isEmpty()) {
                JavaFXUtils.showErrorInAlert("Erreur", "Completez les infos necessaires", "");

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
                int idcategorie = getIdCategorie(this.categories.getTextCategorieSelected());
                Image img = image.getImage();
                Timestamp t1 = Timestamp.valueOf(LocalDateTime.of(yearD, monthD, dayD, heureD, minuteD));
                Timestamp t2 = Timestamp.valueOf(LocalDateTime.of(yearF, monthF, dayF, heureF, minuteF));
                BDD.createObjet(con, titre, t1, t2, Integer.parseInt(this.prixBase.getText()), idcategorie, this.sessionInfo.getUserID(), "Texte", img);
                this.main.setCenter(new VuePrincipale(this.main));
            }
        } catch (SQLException ex) {
            Logger.getLogger(VueInscription.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getIdCategorie(String textCat) throws SQLException {
        Connection con = this.main.getBDD();
        try (PreparedStatement st = con.prepareCall("select id from categorie where nom = ?")) {
            st.setString(1, textCat);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                return res.getInt("id");
            } else {
                JavaFXUtils.showErrorInAlert("Erreur", "Selectionner une cat??gorie", "blabla");
                return -1;
            }

        }

    }

    private void insererLigne(int pos, int count) {
        for (Node child : this.gridMain.getChildren()) {
            if (GridPane.getRowIndex(child) >= pos) {
                Integer rowIndex = GridPane.getRowIndex(child);
                GridPane.setRowIndex(child, rowIndex == null ? count : count + rowIndex);
            }
        }

    }

}
