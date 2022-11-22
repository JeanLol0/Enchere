/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX;

import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.BDD;
import static fr.insa.strasbourg.zerr.enchereprojet.projetenchere.BDD.defautConnect;
import static fr.insa.strasbourg.zerr.enchereprojet.projetenchere.BDD.deleteSchema;
import static fr.insa.strasbourg.zerr.enchereprojet.projetenchere.BDD.menu;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        Scene sc = new Scene(new VuePrincipale(stage));
        
        
        stage.setScene(sc);
        stage.setTitle("Enchère");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
//        try ( Connection con = defautConnect()) {
//
//            System.out.println("Connection ok!");
//            deleteSchema(con);
//            menu(con);
//            //createUtilisateur(con, "nom"," pass", "prenom"," email");
//            System.out.println("user créé");
//            
//        } catch (ClassNotFoundException | SQLException ex) {
//            Logger.getLogger(BDD.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }
    
}
