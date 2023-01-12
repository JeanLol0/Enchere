/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.composants;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.FenetrePrincipale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author jules
 */
public class BarRecherche extends VBox {
    private FenetrePrincipale main;
    private Button bRecherche;
    private Button bCategorie;
    private VBox center;
    private ComboBox trieCombo;
    private TextField tfRecherche;
    private Categories categorie;
    
    private Label lTrie;
    private Label lCategorie;

    public BarRecherche(FenetrePrincipale main) {
        this.main = main;
        this.setPrefWidth(350);
        this.setId("bar-recherche");
        String c1 = "Prix croissant";
        String c2 = "Prix décroissant";
        String c3 = "Dates croissante";
        String c4 = "Dates décroissante";
        //MenuItem c1 = new MenuItem("Prix croissant");
        //Choic c1 = new MenuItem("Prix croissant");
        this.trieCombo = new ComboBox();
        this.categorie=new Categories(this.main);
        
        ObservableList<String> listTrie = FXCollections.observableArrayList(c1,c2,c3,c4);
        this.trieCombo.setItems(listTrie);
        ObservableList<String> listCategorie = getCategorieList();
        
        this.trieCombo.setOnAction((t) -> {
            System.out.println(this.trieCombo.getValue());
            
            
        });
        
        this.lCategorie=new Label("Rechercher par catégorie");
        this.lTrie = new Label("Trier par");
        this.lTrie.setId("text-recherche");
        this.lCategorie.setId("text-recherche");

        
        this.bRecherche =new Button("Rechercher");
        this.bCategorie =new Button("Rechercher par catégorie");
        this.bCategorie.setOnAction((t) -> {
            System.out.println(this.categorie.getTextCategorieSelected());
        });
        this.setAlignment(Pos.TOP_CENTER);
        
        
        this.tfRecherche = new TextField();
        this.tfRecherche.setPromptText("Effectuer une recherche");
        this.center = new VBox();
        this.bRecherche.prefWidthProperty().bind(this.widthProperty());
        this.tfRecherche.prefWidthProperty().bind(this.widthProperty());
        this.trieCombo.prefWidthProperty().bind(this.widthProperty());
        this.center.getChildren().addAll(this.tfRecherche,this.bRecherche,this.lTrie,this.trieCombo,
                this.lCategorie,this.categorie,this.bCategorie);
        this.center.setSpacing(10);
        this.center.setPadding(new Insets(10));
        
        this.getChildren().addAll(this.center);
        
        
        
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
    
    public TextField getTextField(){
        return tfRecherche;
    }

    
    
    public static ObservableList<String> getCategorieList() {
        String c1 = new String("Voitures");
        String c2 = new String("Motos");
        String c3 = new String("Caravaning");
        String c4 = new String("Maison");
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
