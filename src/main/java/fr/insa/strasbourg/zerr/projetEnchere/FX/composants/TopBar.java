/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.composants;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.FenetrePrincipale;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueLogin;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueNouvelleAnnonce;
import java.io.InputStream;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

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

    private MenuItem milogout;
    private MenuItem miEnchere;
    private MenuItem miAnnonce;

    private MenuButton menuUser;

    public TopBar(FenetrePrincipale main) {
        ImageView imageView = this.getIcon("user.png");
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        this.milogout = new MenuItem("Déconnection");
        this.miAnnonce = new MenuItem("Mes Annonces");
        this.miEnchere = new MenuItem("Mes Encheres");
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
                if (select.get() == oui){
                               doLogout();
                } else if (select.get() == non){
                }
        });

        ContextMenu cm = new ContextMenu();

        this.menuUser = new MenuButton();
        this.menuUser.setGraphic(imageView);
        this.menuUser.getItems().addAll(this.miAnnonce, this.miEnchere, this.milogout);
        this.menuUser.setPopupSide(Side.TOP);
        this.menuUser.setGraphicTextGap(200);
        this.menuUser.setPrefSize(50, 50);
//        this.menuUser.setVisible(false);
//        this.miAnnonce.setVisible(true);
        StackPane sp = new StackPane();
        this.menuUser.setOpacity(0);
        this.menuUser.setStyle("-fx-background-color: #ff0000; ");
        Circle cr = new Circle(30, 30, 30);
        cr.setFill(new ImagePattern(getImage("user.png")));
        cm.getItems().addAll(miAnnonce, miEnchere, milogout);
        cr.setOnMouseClicked((t) -> {
            System.out.println("bib bib");
            cm.show(cr, t.getScreenX(), t.getScreenY());
        });

        cr.setOnContextMenuRequested((ContextMenuEvent event) -> {
            cm.show(cr, event.getScreenX(), event.getScreenY());
        });

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

        //JavaFXUtils.addSimpleBorder(region);

        this.setSpacing(10);
        this.setPadding(new Insets(10, 10, 10, 10));

        sp.getChildren().addAll(cr,menuUser);
        this.getChildren().addAll(bNouvelleAnnonce, this.tfRecherche, this.bRecherche, region, sp);

        

        this.bNouvelleAnnonce.setOnAction((t) -> {
            this.main.setCenter(new VueNouvelleAnnonce(this.main));
        });

    }

    private void doLogout() {

        this.main.getSessionInfo().setCurUser(Optional.empty());
        this.main.setCenter(new VueLogin(this.main));
        this.main.setTop(null);
    }

    private ImageView getIcon(String resourcePath) {
        InputStream input //
                = this.getClass().getResourceAsStream(resourcePath);
        Image image = new Image(input);
        return new ImageView(image);
    }

    private Image getImage(String resourcePath) {
        InputStream input //
                = this.getClass().getResourceAsStream(resourcePath);
        Image image = new Image(input);
        return image;
    }
}
