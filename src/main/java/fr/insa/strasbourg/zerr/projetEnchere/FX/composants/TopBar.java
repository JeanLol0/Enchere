/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.composants;

import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueMessagerie;
import com.ctc.wstx.util.StringUtil;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.FenetrePrincipale;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueAcceuil;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueLogin;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueMesAnnonces;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueMesEnchere;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueNouvelleAnnonce;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author jules
 */
public class TopBar extends HBox {

    private FenetrePrincipale main;
    private Button bNouvelleAnnonce;

    private ImageView logo;

    private MenuItem milogout;
    private MenuItem miEnchere;
    private MenuItem miAnnonce;
    private MenuItem miMessagerie;

    private MenuButton menuUser;
    
    private Label lNomPrenom;
    
    

    public TopBar(FenetrePrincipale main) {
        this.main = main;
        ImageView imageView = this.getIcon("ressources/user.png",25,25);
        this.logo = getIcon("ressources/logo.png",50,50);
        this.logo.setOnMouseClicked((t) -> {
            this.main.setCenter(new VueAcceuil(this.main));
            this.main.setLeft(null);
        });
        this.milogout = new MenuItem("Déconnection");
        this.miAnnonce = new MenuItem("Mes Annonces");
        this.miEnchere = new MenuItem("Mes Encheres");
        this.miMessagerie = new MenuItem("Messagerie");
        

        ContextMenu cm = new ContextMenu();

        this.menuUser = new MenuButton();
        this.menuUser.setGraphic(imageView);
        this.menuUser.getItems().addAll(this.miAnnonce, this.miEnchere, this.milogout);
        this.menuUser.setPopupSide(Side.TOP);
        this.menuUser.setGraphicTextGap(200);
        this.menuUser.setPrefSize(50, 50);
        StackPane sp = new StackPane();
        this.menuUser.setOpacity(0);
        this.menuUser.setStyle("-fx-background-color: #ff0000; ");
        Circle cr = new Circle(30, 30, 30);
        cr.setFill(new ImagePattern(getImage("ressources/user.png", 35, 35)));
        cm.getItems().addAll(miAnnonce, miEnchere, miMessagerie,milogout);
        
        cr.setOnMouseClicked((t) -> {
            System.out.println("bib bib");
            cm.show(cr, t.getScreenX(), t.getScreenY());
        });

        cr.setOnContextMenuRequested((ContextMenuEvent event) -> {
            cm.show(cr, event.getScreenX(), event.getScreenY());
        });
        sp.getChildren().addAll(cr, menuUser);
        
        this.setId("topbar");
        this.bNouvelleAnnonce = new Button("Déposer une annonce");
        this.bNouvelleAnnonce.setId("bouton-nouvelle-annonce");
        ImageView plus = this.getIcon("ressources/bouton-ajouter.png",35,35);
        this.bNouvelleAnnonce.setGraphic(plus);
        this.bNouvelleAnnonce.setMaxWidth(Integer.MAX_VALUE);
        TopBar.setHgrow(this.bNouvelleAnnonce, Priority.SOMETIMES);
        

        Region region = new Region();
        TopBar.setHgrow(region, Priority.ALWAYS);
        
        this.lNomPrenom = new Label(WordUtils.capitalize(main.getSessionInfo().getUserNomPrenom()));
        this.lNomPrenom.setId("nom-prenom");


        this.setSpacing(10);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.getChildren().addAll(logo,bNouvelleAnnonce, region,lNomPrenom, sp);

        this.bNouvelleAnnonce.setOnAction((t) -> {
            try {
                this.main.setCenter(new VueNouvelleAnnonce(this.main));
                this.main.setLeft(null);
            } catch (IOException ex) {
                Logger.getLogger(TopBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        this.milogout.setOnAction((t) -> {
            t.consume();
            Alert confirmer = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType oui = new ButtonType("Oui");
            ButtonType non = new ButtonType("Non");
            confirmer.setTitle("Attention");
            confirmer.setHeaderText("Etes-vous sur de vous déconnecter ? ");
            confirmer.getButtonTypes().clear();
            confirmer.getButtonTypes().addAll(oui, non);
            Optional<ButtonType> select = confirmer.showAndWait();
            if (select.get() == oui) {
                doLogout();
            } else if (select.get() == non) {
            }
        });
        
        this.miAnnonce.setOnAction((t) -> {
            try {
                this.main.setCenter(new VueMesAnnonces(this.main));
                this.main.setLeft(null);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TopBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        this.miEnchere.setOnAction((t) -> {
            try {
                this.main.setCenter(new VueMesEnchere(this.main));
                this.main.setLeft(null);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TopBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.miMessagerie.setOnAction((t) -> {
            this.main.setCenter(new VueMessagerie(this.main));
            this.main.setLeft(null);
        });

    }

    private void doLogout() {

        this.main.getSessionInfo().setCurUser(Optional.empty());
        this.main.setCenter(new VueLogin(this.main));
        this.main.setTop(null);
        this.main.setLeft(null);
    }

    private ImageView getIcon(String resourcePath,int w,int h) {
        InputStream input //
                = this.getClass().getResourceAsStream(resourcePath);
        Image image = new Image(input);
        ImageView IV = new ImageView(image);
        IV.setFitHeight(h);
        IV.setFitWidth(w);
        return IV;
    }

    public  Image getImage(String resourcePath, int par, int par1) {
        InputStream input //
                = this.getClass().getResourceAsStream(resourcePath);
        Image image = new Image(input);
        return image;
    }
}
