/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.vues;

import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.enchereprojet.projetenchere.FX.VuePrincipale;
import java.util.Optional;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 *
 * @author jules
 */
public class EnteteConnecte extends HBox {
    private Text curentUser; 
    
    private Button bLogout;
    private VuePrincipale main;

    public EnteteConnecte(VuePrincipale main) {
        this.main = main ;
        this.curentUser = new Text(this.main.getSessionInfo().getUserName());
        this.bLogout = new Button("Deconnection");
        this.bLogout.setOnAction((t) -> {
            doLogout();
        });
        this.getChildren().addAll(this.curentUser, this.bLogout);
        
        
        
    }

    public void doLogout(){
        this.main.getSessionInfo().setCurUser(Optional.empty());
        this.main.setCenter(new EnteteInitial(main));
        JavaFXUtils.petiteFenetre(this.main.getFenetre());
        this.main.setEntete(new EnteteBienvenue(main));

    }
    
}
