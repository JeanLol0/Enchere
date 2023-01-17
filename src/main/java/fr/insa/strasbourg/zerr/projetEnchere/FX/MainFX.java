/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX;

import fr.insa.strasbourg.beuvron.utils.SceneManager;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.FenetrePrincipale;
import java.io.InputStream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author jules
 */
public class MainFX extends Application {

    public static Pane getMainVue;

    private Pane mainVue;
    public static Scene scene;

    @Override
    public void start(Stage stage) {
        stage.setTitle("INS'Enchere");
        InputStream complet = this.getClass().getResourceAsStream("logo.png");
        if (complet != null) {
            stage.getIcons().add(new Image(complet));
        }
        
        
        this.mainVue = new FenetrePrincipale(stage);
         
        this.scene = new Scene(mainVue);
        
        
        SceneManager.getInstance().setScene(this.scene);
        JavaFXUtils.grandeFenetre(stage);
        //JavaFXUtils.FullScreen(stage);
        //this.scene.getStylesheets().add("FX/DarkTheme.css");
        
        
        Cursor cur = Cursor.OPEN_HAND;
        Image curI = getImage("le-curseur.png");
        this.scene.setCursor(new ImageCursor(curI));
        this.scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        stage.setScene(scene);

        stage.show();
    }
    

    public Scene getScene() {
        return scene;
    }

    public Pane getMainVue() {
        return mainVue;
    }

    public void setMainVue(Pane mainVue) {
        this.mainVue = mainVue;
    }

    public  Image getImage(String resourcePath) {
        InputStream input //
                = this.getClass().getResourceAsStream(resourcePath);
        Image image = new Image(input);
        return image;
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
