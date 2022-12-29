/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author jules
 */
public class VueCarte extends BorderPane{
    private FenetrePrincipale main;
    private Button bRetour;
    


    public VueCarte(FenetrePrincipale main) {
        this.main = main ;
        this.bRetour = new Button("Retour à l'inscription");
        this.setPrefSize(500, 400);
        this.setStyle("-fx-background-color:white");
        this.bRetour.setOnAction((t) -> {
            this.main.setCenter(new VueInscription(this.main));
        });
        this.setTop(this.bRetour);
        Button buttonURL = new Button("Load Page google map");

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        buttonURL.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // Load a page from remote url.
                webEngine.load(this.getClass().getResource("carte.html").toString());
            }
        });

        VBox root = new VBox();
        root.setPadding(new Insets(5));
        root.setSpacing(5);
        root.getChildren().addAll(buttonURL, browser);
        this.setCenter(root);
    }
    
}
