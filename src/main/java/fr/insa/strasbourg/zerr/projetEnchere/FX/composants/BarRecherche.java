/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.composants;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.FenetrePrincipale;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 *
 * @author jules
 */
public class BarRecherche extends ScrollPane {

    private FenetrePrincipale main;
    private Button bRecherche;
    private Button bCategorie;
    private Button bDistance;
    private Button bReinitialise;
    private VBox center;
    private ComboBox trieCombo;
    private TextField tfRecherche;
    private Categories categorie;

    private VBox content;

    private Label lTrie;
    private Label lCategorie;
    private Label lDistance;
    private Label lValeur;

    private Slider sliderDistance;

    public BarRecherche(FenetrePrincipale main) {
        this.main = main;
        this.setPrefWidth(400);

        String c1 = "Prix croissant";
        String c2 = "Prix décroissant";
        String c3 = "Temps croissant";
        String c4 = "Temps décroissant";
        //MenuItem c1 = new MenuItem("Prix croissant");
        //Choic c1 = new MenuItem("Prix croissant");
        this.trieCombo = new ComboBox();
        this.categorie = new Categories(this.main);

        ObservableList<String> listTrie = FXCollections.observableArrayList(c1, c2, c3, c4);
        this.trieCombo.setItems(listTrie);
        ObservableList<String> listCategorie = getCategorieList();

        this.trieCombo.setOnAction((t) -> {
            System.out.println(this.trieCombo.getValue());

        });

        this.sliderDistance = new Slider();

        this.sliderDistance.setMin(0);
        this.sliderDistance.setMax(500);
        this.sliderDistance.setValue(100);

        this.sliderDistance.setShowTickLabels(true);
        this.sliderDistance.setShowTickMarks(true);
        this.lDistance = new Label("Trier par distance");
        this.lDistance.setId("text-recherche");
        this.lValeur = new Label("Distance: " + this.sliderDistance.getValue() + " km");
        this.lValeur.setId("text-recherche");
        this.sliderDistance.valueProperty().addListener(
                new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int entier = newValue.intValue();
                lValeur.setText("Distance: " + entier + " km");
            }
        });
        this.bDistance = new Button("Rechercher par distance");
        this.bReinitialise = new Button("Réinitialiser la recherche");
        
        this.sliderDistance.setBlockIncrement(50);

        this.lCategorie = new Label("Rechercher par catégorie");
        this.lTrie = new Label("Trier par");
        this.lTrie.setId("text-recherche");
        this.lCategorie.setId("text-recherche");

        this.bRecherche = new Button("Rechercher");
        this.bCategorie = new Button("Rechercher par catégorie");
        this.tfRecherche = new TextField();
        this.tfRecherche.setPromptText("Effectuer une recherche");
        this.center = new VBox();
        //JavaFXUtils.addSimpleBorder(this.center);
        this.center.setId("bar-recherche");
        this.setId("scroll-recherche");
        this.bRecherche.prefWidthProperty().bind(this.widthProperty());
        this.tfRecherche.prefWidthProperty().bind(this.widthProperty());
        this.trieCombo.prefWidthProperty().bind(this.widthProperty());
        this.bCategorie.prefWidthProperty().bind(this.widthProperty());
        this.bDistance.prefWidthProperty().bind(this.widthProperty());
        this.bReinitialise.prefWidthProperty().bind(this.widthProperty());
        Region region = new Region();
        region.setPrefHeight(200);
        this.center.setVgrow(region, Priority.SOMETIMES);
        this.center.getChildren().addAll(this.bReinitialise,this.tfRecherche, this.bRecherche,
                this.lTrie, this.trieCombo,
                this.lDistance, this.sliderDistance, this.lValeur, this.bDistance,
                this.lCategorie, this.categorie,this.bCategorie,region);
        this.center.setSpacing(10);
        this.center.setPadding(new Insets(10));

        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setFitToWidth(true);
        this.setContent(this.center);

    }

    public Button getbReinitialise() {
        return bReinitialise;
    }

    public Button getbCategorie() {
        return bCategorie;
    }

    public Button getbRecherche() {
        return bRecherche;
    }

    public Categories getCategorie() {
        return categorie;
    }

    public ComboBox getTrieCombo() {
        return trieCombo;
    }

    public Button getButtonRecherche() {
        return bRecherche;
    }

    public TextField getTextField() {
        return tfRecherche;
    }

    public static ObservableList<String> getCategorieList() {
        String c1 = "Voitures";
        String c2 = "Motos";
        String c3 = "Caravaning";
        String c4 = "Maison";
        String c5 = "Appartement";
        String c6 = "Terrain";
        String c7 = "Vetements";
        String c8 = "Chaussures";
        String c9 = "Montre & Bijoux";
        String c10 = "Accessoires";
        String c11 = "Ameublement";
        String c12 = "Electromenager";
        String c13 = "Décoration";
        String c14 = "Bricolage";
        String c15 = "Jardinage";
        String c16 = "Informatique";
        String c17 = "Consoles & jeux vidéo";
        String c18 = "Image & Son";
        String c19 = "Téléphonie";
        String c20 = "DVD & CD";
        String c21 = "Livres";
        String c22 = "Vélos";
        String c23 = new String("Sports & Hobbies");
        String c24 = "Jeux & jouets";

        ObservableList<String> list
                = FXCollections.observableArrayList(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24);

        return list;
    }
    public String getValeurDist(){
        return lValeur.getText();
    }

    public Button getbDistance() {
        return bDistance;
    }

}
