/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author jules
 */
public class JavaFXUtils {
    public static void addSimpleBorder(Region c) {
        c.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

//    public static WebView preparedStatementInWebView(PreparedStatement pst) {
//        WebView view = new WebView();
//
//        try ( ResultSet rs = pst.executeQuery()) {
//            view.getEngine().loadContent(SQLUtils.formatResultSetAsHTMLTable(rs));
//        } catch (SQLException ex) {
//            view.getEngine().loadContent("<b> problem bdd : " + ex.getLocalizedMessage() + " </b>");
//        }
//        return view;
//    }

    public static void showErrorInAlert(String titre, String message, String detail) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(message);
        alert.setContentText(detail);
        alert.showAndWait();

    }
    
    public static void redimentionnerFenetre(Stage fenetre, int width, int height){
        fenetre.setWidth(width);
        fenetre.setHeight(height);
    }
    
    public static void grandeFenetre(Stage fenetre){
        fenetre.setWidth(1400);
        fenetre.setHeight(800);
    }
    
    public static void maxFenetre(Stage fenetre){
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
int width = gd.getDisplayMode().getWidth();
int height = gd.getDisplayMode().getHeight();
        fenetre.setWidth(width);
        fenetre.setHeight(height);
    }
    
    public static void FullScreen(Stage fenetre){
        fenetre.setFullScreen(true);
    }

    public static void petiteFenetre(Stage fenetre) {
        fenetre.setWidth(400);
        fenetre.setHeight(300);
    }
    
    public static void DesactiveAutoFocus(TextField tf){
        tf.styleProperty().bind(
                Bindings
                        .when(tf.focusedProperty())
                        .then("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);")
                        .otherwise("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);"));
    }
    
    public static void DesactiveAutoFocus(PasswordField pf){
        pf.styleProperty().bind(
                Bindings
                        .when(pf.focusedProperty())
                        .then("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);")
                        .otherwise("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);"));
    }
    
}
