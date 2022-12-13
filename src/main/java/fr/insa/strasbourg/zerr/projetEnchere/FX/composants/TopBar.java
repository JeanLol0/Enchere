/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.composants;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.StylesCSS;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.FenetrePrincipale;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueLogin;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueNouvelleAnnonce;
import fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 *
 * @author jules
 */
public class TopBar extends HBox {
    private FenetrePrincipale main;
    private TextField tfRecherche;
    private BoutonIcon bRecherche;
    private Button bLogout;
    private Button bNouvelleAnnonce;

    private GridPane gpBar;


    public TopBar(FenetrePrincipale main) {
           
        this.main = main;
        this.setId("topbar");
        this.bNouvelleAnnonce = new Button("Déposer une annonce");
        this.bNouvelleAnnonce.setMaxWidth(Integer.MAX_VALUE);
        TopBar.setHgrow(this.bNouvelleAnnonce, Priority.SOMETIMES);
        
        this.tfRecherche = new TextField("Recherche");
        this.tfRecherche.setMaxWidth(Integer.MAX_VALUE);
        TopBar.setHgrow(this.tfRecherche, Priority.SOMETIMES);
       
        
        
        this.bRecherche = new BoutonIcon("icones/recherche.png", 20, 20);
        this.bRecherche.setText("RE");
        
                this.bRecherche.setStyle("-fx-content-display: top;");

        //this.bRecherche.setText("Rechercher");
        this.bRecherche.setMaxWidth(Integer.MAX_VALUE);
        TopBar.setHgrow(this.bRecherche, Priority.SOMETIMES);
        
        Region region = new Region();
        TopBar.setHgrow(region, Priority.ALWAYS);
        
        this.bLogout = new Button("Déconnction");
        this.bLogout.setMaxWidth(Integer.MAX_VALUE);
        this.bLogout.setId("bouton-rouge");
        TopBar.setHgrow(this.bLogout, Priority.SOMETIMES);

        JavaFXUtils.addSimpleBorder(region);
        
        
        this.setSpacing(10);
        this.setPadding(new Insets(10, 10, 10, 10));
        
        
        this.getChildren().addAll(bNouvelleAnnonce, this.tfRecherche, this.bRecherche,region, this.bLogout);

     
        this.bLogout.setOnAction((t) -> {
            doLogout();
        });
        
        this.bNouvelleAnnonce.setOnAction((t) -> {
            this.main.setCenter(new VueNouvelleAnnonce(this.main));
        });
        
    }

    private void doLogout() {

        this.main.getSessionInfo().setCurUser(Optional.empty());
        this.main.setCenter(new VueLogin(this.main));
        this.main.setTop(null);
    }    
}
