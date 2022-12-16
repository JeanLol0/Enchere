/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

/**
 *
 * @author jules
 */
public class VueAnnonces extends GridPane {

    private FenetrePrincipale main;
    private Annonce anonce;
    private ArrayList<Integer> idAnnonce;
    private Connection con;

    public VueAnnonces(FenetrePrincipale main) {
        this.main = main;
        this.idAnnonce = new ArrayList<Integer>();
        this.con = this.main.getBDD();
        try {
            recupereIdAnnnce();
            int nbAnnonce = this.idAnnonce.size();
            for (int i = 1; i <= nbAnnonce; i++) {
                this.add(new Annonce(this.main, i), 0, i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VueAnnonces.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setAlignment(Pos.CENTER);
        this.setHgap(30);

    }

    public void afficheTousLesUtilisateur(Connection con)
            throws SQLException {
        try ( Statement st = con.createStatement()) {
            ResultSet res = st.executeQuery("select * from utilisateur");
            while (res.next()) {
                String lenom = res.getString("nom");
                String pass = res.getString("pass");
                String prenom = res.getString("prenom");
                String mail = res.getString("email");
                System.out.println("utilisateur " + lenom + " " + prenom + " ("
                        + pass + ")" + mail);
            }
        }
    }

    private void recupereIdAnnnce()
            throws SQLException {
        try ( Statement st = this.con.createStatement()) {
            ResultSet res = st.executeQuery("select id from objet");
            while (res.next()) {
                this.idAnnonce.add(res.getInt("id"));
                //System.out.println("ids recupéré nb:" + this.idAnnonce.size());
            }
        }
    }

}
                                                                                    
                                                                                    
                                                                                    
                                                                                    
                                                                                    