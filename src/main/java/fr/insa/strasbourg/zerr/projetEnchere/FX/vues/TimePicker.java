/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

/**
 *
 * @author jules
 */
public class TimePicker extends HBox {
    private ComboBox<Integer> Heure;

    private ComboBox<Integer> Minute;

    public TimePicker() {

       
      
        ObservableList<Integer> heures = FXCollections.observableArrayList(1, 2, 3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,00);
        this.Heure = new ComboBox<Integer>(heures);
        ObservableList<Integer> minutes = FXCollections.observableArrayList(00,05,10,15,20,25,30,35,40,45,50,55);
        this.Minute = new ComboBox<Integer>(minutes);
        
        this.Heure.setValue(java.time.LocalTime.now().getHour());
        this.Minute.setValue(java.time.LocalTime.now().getMinute());
       this.getChildren().addAll(this.Heure, this.Minute);

       

    }

    public Integer getHeure() {
        return this.Heure.getValue();
    }

    public Integer getMinute() {
        return this.Minute.getValue();
    }

}
