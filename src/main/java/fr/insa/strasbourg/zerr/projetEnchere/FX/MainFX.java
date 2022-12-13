/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX;

import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueInscription;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueLogin;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.FenetrePrincipale;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author jules
 */
public class MainFX extends Application {

    public static Pane getMainVue;

    private Pane mainVue;
    private Scene scene;

    @Override
    public void start(Stage stage) {

        this.mainVue = new FenetrePrincipale(stage);
         
        this.scene = new Scene(mainVue);
        JavaFXUtils.maxFenetre(stage);
        //this.scene.getStylesheets().add("FX/DarkTheme.css");
        this.scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Enchère");

        stage.show();
    }

    public Pane getMainVue() {
        return mainVue;
    }

    public void setMainVue(Pane mainVue) {
        this.mainVue = mainVue;
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
