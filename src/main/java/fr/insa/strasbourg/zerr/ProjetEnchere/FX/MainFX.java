/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.ProjetEnchere.FX;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author jules
 */
public class MainFX extends Application {

    private VuePrincipale main;

    @Override
    public void start(Stage stage) {

        this.main = new VuePrincipale(stage);
        Scene sc = new Scene(main, Double.MAX_EXPONENT, Double.MAX_EXPONENT);

        main.setStyle("{    -fx-background-color:aquamarine;    -fx-font-size : 17 pt;}");
        System.out.println(sc.getStylesheets().size());
        sc.getStylesheets().add("DarkTheme.css");
        System.out.println(sc.getStylesheets().size());
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
