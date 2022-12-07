/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.composants;


import java.io.InputStream;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author IEUser
 */
public class BoutonIcon extends Button {
    public BoutonIcon(String relPathOfImageFile, double sizeX, double sizeY) {        
        InputStream is = this.getClass().getResourceAsStream(relPathOfImageFile);
        if (is == null) {
            this.setText("?? " + relPathOfImageFile);
        } else {
            Image img = new Image(is, sizeX, sizeY, false, true);
            this.setGraphic(new ImageView(img));
            //this.setStyle("-fx-background-color: #fdfbf3; ");
            //<div>Icônes conçues par <a href="https://www.flaticon.com/fr/auteurs/vaadin" title="Vaadin">Vaadin</a> from <a href="https://www.flaticon.com/fr/" title="Flaticon">www.flaticon.com</a></div><div>Icônes conçues par <a href="https://www.flaticon.com/fr/auteurs/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/fr/" title="Flaticon">www.flaticon.com</a></div><div>Icônes conçues par <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/fr/" title="Flaticon">www.flaticon.com</a></div><div>Icônes conçues par <a href="https://www.flaticon.com/fr/auteurs/debi-alpa-nugraha" title="Debi Alpa Nugraha">Debi Alpa Nugraha</a> from <a href="https://www.flaticon.com/fr/" title="Flaticon">www.flaticon.com</a></div>
        }

    }
    
}
