/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author jules
 */
public class MainFX extends Application{
    @Override
    public void start(Stage stage) {
        Scene sc = new Scene(new VuePrincipale());
        
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setScene(sc);
        stage.setTitle("AmourFx");
          stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
}
