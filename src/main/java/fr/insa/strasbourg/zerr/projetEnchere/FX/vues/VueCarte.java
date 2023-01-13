/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import java.io.IOException;
import java.util.logging.Level;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javax.swing.text.Position;

/**
 *
 * @author jules
 */
public class VueCarte extends BorderPane {

    private FenetrePrincipale main;
    private Button bRetour;
    private Button Confirmer;
    private MapPoint mapPoint;
    private Circle circle;
    public MapLayer maplayer;
    private int zoom = 10;
    private double longitude;
    private double latitude;

    public VueCarte(FenetrePrincipale main) throws IOException {
        this.main = main;
        this.bRetour = new Button("Retour à l'inscription");
        this.Confirmer = new Button("Confirmer");
        this.setPrefSize(500, 400);
        this.setStyle("-fx-background-color:white");
        this.bRetour.setOnAction((t) -> {
            this.main.setCenter(new VueInscription(this.main));
        });
        this.Confirmer.setOnAction((t) -> {
//            if((this.longitude!=0)&&(this.latitude!=0)){
            if ((this.longitude != 0)&&(this.latitude != 0)) {
                    //this.main.setCenter(new VueInscription(this.main, this.longitude, this.latitude));
            }
            else{
                JavaFXUtils.showErrorInAlert("Erreur", "Vous n'avez pas entré de position", "Veuillez saisir une localisation");
            }
        });
        this.setTop(this.bRetour);
        this.setTop(this.Confirmer);
        System.setProperty("javafx.platform", "desktop");

        /*
   * Définit l'user agent pour éviter l'exception
   * "Server returned HTTP response code: 403"
         */
        System.setProperty("http.agent", "Gluon Mobile/1.0.3");
//
//        final WebView browser = new WebView();
//        final WebEngine webEngine = browser.getEngine();
//
//        buttonURL.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                // Load a page from remote url.
//                webEngine.load(this.getClass().getResource("carte.html").toString());
//            }
//        });

        VBox root = new VBox();

        MapView view = new MapView();
        MapPoint mappoint = new MapPoint(48.852969, 2.349903);
//        this.maplayer = new CustomCircleMarker(mapPoint);
        view.setOnMouseClicked((t) -> {
            this.mapPoint = view.getMapPosition(t.getX(), t.getY());
            view.removeLayer(maplayer);
            this.maplayer = new CustomCircleMarker(this.mapPoint);
            view.addLayer(maplayer);
            this.longitude=mappoint.getLongitude();
            this.latitude=mappoint.getLatitude();

        });
        view.setZoom(this.zoom);
        view.flyTo(0, mappoint, 0.1);
        root.setPadding(new Insets(5));
        root.setSpacing(5);
        root.getChildren().addAll(view);
        this.setCenter(root);
    }

    public VueCarte(MapPoint mapPoint) {
        this.mapPoint = mapPoint;

        /* Cercle rouge de taille 5 */
        this.circle = new Circle(5, Color.RED);
        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }

    public double getLongitude() {
        return this.mapPoint.getLongitude();
    }

    public double getLatitude() {
        return this.mapPoint.getLatitude();
    }

    /* La fonction est appelée à chaque rafraichissement de la carte */
}
