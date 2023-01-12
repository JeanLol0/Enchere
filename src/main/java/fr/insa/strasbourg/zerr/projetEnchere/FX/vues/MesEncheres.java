/*
 * Copyright 2022 jules.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Label;

/**
 *
 * @author jules
 */
public class MesEncheres {
    private String titre;
    private String prixActuel; 
    private String prixDepart; 
    private String nbEnchere;
    private String tempRestant;
    private String Encherisseur;
    private Integer idEncherisseur;
    private FenetrePrincipale main;
    

    public MesEncheres(FenetrePrincipale main, Integer idAnn) throws SQLException, ClassNotFoundException {
        this.main = main;
        recupereAnnonce(idAnn);
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getPrixActuel() {
        return prixActuel;
    }

    public void setPrixActuel(String prixActuel) {
        this.prixActuel = prixActuel;
    }

    public String getPrixDepart() {
        return prixDepart;
    }

    public void setPrixDepart(String prixDepart) {
        this.prixDepart = prixDepart;
    }

    public String getNbEnchere() {
        return nbEnchere;
    }

    public void setNbEnchere(String nbEnchere) {
        this.nbEnchere = nbEnchere;
    }

    public String getTempRestant() {
        return tempRestant;
    }

    public void setTempRestant(String tempRestant) {
        this.tempRestant = tempRestant;
    }

    public String getEncherisseur() {
        return Encherisseur;
    }

    public void setEncherisseur(String Encherisseur) {
        this.Encherisseur = Encherisseur;
    }


    private void recupereAnnonce(Integer idAnn) throws SQLException, ClassNotFoundException {
        Connection con = this.main.getBDD();

        try (PreparedStatement st = con.prepareStatement("select * from objet where id = ?")) {
            st.setInt(1, idAnn);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                this.titre = res.getString("titre");
                //this.idEncherisseur = res.getInt(titre)
                this.nbEnchere = String.valueOf(IdencheresSurObjet(idAnn).size());
                this.prixActuel = res.getString("prixactuel");
                this.prixDepart = res.getString("prixbase");

            }
        }
    }
    
    public static ArrayList<Integer> IdencheresSurObjet(int idObjet) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select id from enchere where sur = ? order by montant desc")) {
            st.setInt(1, idObjet);
            ResultSet res = st.executeQuery();
            int IdUtil = 0;
            while (res.next()) {
                List.add(res.getInt("id"));
            }
            return List;
        }
    }

//    private void recupereNomCategorie(Integer id) throws SQLException, ClassNotFoundException {
//        Connection con = this.main.getBDD();
//
//        try (PreparedStatement st = con.prepareStatement("select * from ca where id = ?")) {
//            st.setInt(1, idAnn);
//            ResultSet res = st.executeQuery();
//            while (res.next()) {
//                this.titre = res.getString("titre");
//                //this.idEncherisseur = res.getInt(titre)
//                this.nbEnchere = String.valueOf(IdencheresSurObjet(idAnn).size());
//                this.prixActuel = res.getString("prixactuel");
//                this.prixDepart = res.getString("prixbase");
//
//            }
//        }
//    }
    
    
}
