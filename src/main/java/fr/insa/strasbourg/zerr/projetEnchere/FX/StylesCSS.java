/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX;

import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.Annonce;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author jules
 */
public class StylesCSS {

    public static void DarkTheme(GridPane pane) {
        pane.setStyle(""
                + "-fx-background-color:#252525;"
                + "-fx-font-size : 22 pt;"
                + "");
    }
    
    public static void DarkTheme(Pane pane) {
        pane.setStyle(""
                + "-fx-background-color:#252525;"
                + "-fx-font-size : 30 pt;"
                + "");
    }

    public static void DefautTheme(Pane pain) {
        pain.setStyle(""
                + "-fx-font-size : 22 pt;"
                + "");
    }

    public static void StyleBoutonVert(Button b) {
        b.setStyle("-fx-content-display : center;"
                + "-fx-alignment : center;"
                + "-fx-background-color: #32cd32; "
                + "-fx-text-fill: white;"
                + "-fx-effect: dropshadow( one-pass-box , black , 8 , 0.0 , 2 , 0 );"
                + "-fx-padding: 3 6 6 6;");
    }
    
    public static void StyleBoutonBleu(Button b) {
        b.setStyle("-fx-content-display : center;"
                + "-fx-alignment : center;"
                + "-fx-background-color: #00bfff; "
                + "-fx-text-fill: white;"
                + "-fx-effect: dropshadow( one-pass-box , black , 8 , 0.0 , 2 , 0 );"
                + "-fx-padding: 3 6 6 6;");
    }
    
    public static void StyleBoutonRouge(Button b) {
        b.setStyle("-fx-content-display : center;"
                + "-fx-alignment : center;"
                + "-fx-background-color: #ff0000; "
                + "-fx-text-fill: white;"
                + "-fx-effect: dropshadow( one-pass-box , black , 8 , 0.0 , 2 , 0 );"
                + "-fx-padding: 3 6 6 6;");
    }
    
    public static void StyleGrandTitre(Label titre){
        titre.setStyle( "-fx-font: normal bold 50px 'serif';" 
                +"-fx-text-fill: white;"        );
    }
    
    public static void StyleText(Label text){
        text.setStyle( "-fx-text-fill: white;");
    }
    
    
    public static void StyleEntete(Pane entete){
        entete.setStyle( "-fx-spacing:20;"
                + "-fx-background-color:#e2e2e2;"
                           
              
                );
    }
    
    public static void StyleBoutonNouvelleAnnonce (Button b){
        b.setStyle("-fx-content-display : center;"
                + "-fx-alignment : center;"
                + "-fx-background-color: #297b40; "
                + "-fx-text-fill: white;"
                + "-fx-effect: dropshadow( one-pass-box , black , 8 , 0.0 , 2 , 0 );"
                + "-fx-padding: 3 6 6 6;");
    }

    public static void ThemeAnnonce(GridPane pane) {
pane.setStyle(""
                + "-fx-background-color:#c1d2ff;"
                + "-fx-font-size : 22 pt;"
                + "");
    }
    
}
