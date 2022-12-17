/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.composants;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.FenetrePrincipale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 *
 * @author jules
 */
public class BarReccherche extends HBox {
    private Button bRecherche;
    private HBox center;
    private ComboBox trieCombo;
    private ComboBox categorieCombo;
    private TextField tfRecherche;

    public BarReccherche(FenetrePrincipale main) {
        String c1 = "Prix croissant";
        String c2 = "Prix décroissant";
        String c3 = "Dates croissante";
        String c4 = "Dates décroissante";
        String c5 = "Catégorie";
        //MenuItem c1 = new MenuItem("Prix croissant");
        //Choic c1 = new MenuItem("Prix croissant");
        this.trieCombo = new ComboBox();
        
        this.categorieCombo = new ComboBox();
        ObservableList<String> listTrie = FXCollections.observableArrayList(c1,c2,c3,c4,c5);
        this.trieCombo.setItems(listTrie);
        ObservableList<String> listCategorie = getCategorieList();
        this.categorieCombo.setItems(listCategorie);
        
        this.trieCombo.setOnAction((t) -> {
            System.out.println(this.trieCombo.getValue());
        });
        
        this.bRecherche =new Button("Rechercher");
        this.setAlignment(Pos.CENTER);
        this.prefWidthProperty().bind(main.widthProperty());
        
        this.tfRecherche = new TextField("Effectuer une recherche");
        this.tfRecherche.setPrefWidth(200);
        
        this.center = new HBox();
        this.center.getChildren().addAll(new Text("Catégorie"),this.categorieCombo,this.tfRecherche,this.bRecherche,new Label("Trier par :"),this.trieCombo);
        
        this.getChildren().addAll(this.center);
        JavaFXUtils.addSimpleBorder(this);
        
        
    }

    public ComboBox getTrieCombo() {
        return trieCombo;
    }

    public ComboBox getCategorieCombo() {
        return categorieCombo;
    }
    
    
    public static ObservableList<String> getCategorieList() {
        String c1 = new String("Voitures");
        String c2 = new String("Motos");
        String c3 = new String("Caravaning");
        String c4 = new String("Mainson");
        String c5 = new String("Appartement");
        String c6 = new String("Terrain");
        String c7 = new String("Vetements");
        String c8 = new String("Chaussures");
        String c9 = new String("Montre & Bijoux");
        String c10 = new String("Accessoires");
        String c11= new String("Ameublement");
        String c12 = new String("Electromenager");
        String c13 = new String("Décoration");
        String c14 = new String("Bricolage");
        String c15 = new String("Jardinage");
        String c16 = new String("Informatique");
        String c17 = new String("Consoles & jeux vidéo");
        String c18 = new String("Image & Son");
        String c19 = new String("Téléphonie");
        String c20= new String("DVD & CD");
        String c21= new String("Livres");
        String c22= new String("Vélos");
        String c23= new String("Sports & Hobbies");
        String c24= new String("Jeux & jouets");
        

        ObservableList<String> list 
                = FXCollections.observableArrayList(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,c21,c22,c23,c24);

        return list;
    }
}
