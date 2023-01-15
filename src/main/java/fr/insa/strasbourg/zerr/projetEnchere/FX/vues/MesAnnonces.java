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

import static fr.insa.strasbourg.zerr.projetEnchere.FX.vues.Annonce.setEtatLivraison;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;

/**
 *
 * @author jules
 */
public class MesAnnonces {

    private int idAnn;

    private String titre;
    private String prixActuel;
    private String prixDepart;
    private String nbEnchere;
    private String tempRestant;
    private String Encherisseur;

    private Integer idEncherisseur;
    private FenetrePrincipale main;
    private Timestamp fin;

    public MesAnnonces(FenetrePrincipale main, Integer idAnn) throws SQLException, ClassNotFoundException {
        this.idAnn = idAnn;
        this.main = main;
        recupereAnnonce(idAnn);
        ActualisationTempsRestant();

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
        recupereFin(this.idAnn);
        recupereTempsRestant();
        setTempRestant(tempRestant);

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
        int idDerEN = UtilDernierEnchereSurObjet(this.idAnn);
        String NomDerEn = recupereNomUTil(con, idDerEN);
        this.Encherisseur = NomDerEn;
    }

    public static ArrayList<Integer> IdencheresSurObjet(int idObjet) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("select id from enchere where sur = ? order by montant desc")) {
            st.setInt(1, idObjet);
            ResultSet res = st.executeQuery();
            int IdUtil = 0;
            while (res.next()) {
                List.add(res.getInt("id"));
            }
            return List;
        }
    }

    public int UtilDernierEnchereSurObjet(int idObjet) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        int idDernierUtil = -1;
        try (PreparedStatement st = con.prepareStatement("select * from enchere where montant = (select max(montant) from enchere where sur = ?)")) {
            st.setInt(1, idObjet);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                idDernierUtil = res.getInt("de");
            }
            return idDernierUtil;
        }
    }

    public static String recupereNomUTil(Connection con, int id)
            throws SQLException, ClassNotFoundException {
        String NOM = "";
        try (PreparedStatement st = con.prepareStatement("select * from utilisateur where id = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                String nom = res.getString("nom");
                String prenom = res.getString("prenom");
                NOM = nom.toUpperCase() + " " + prenom.toUpperCase();
            }
        }
        return NOM;
    }

    private void ActualisationTempsRestant() {
        Timeline tempsRestant = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), (t) -> {
            Timestamp mtn = new Timestamp(System.currentTimeMillis());
            long secR = secRestant(this.fin);
            long minR = minRestant(this.fin);
            long heureR = hRestant(this.fin);
            long jourR = jourRestant(this.fin);
//            LocalDateTime ldt = LocalDateTime.now();
//            LocalDateTime ldt3 = this.fin.toLocalDateTime();
//            long diffH = ldt.until(ldt3, ChronoUnit.HOURS);
            if (secR < 0) {
                this.tempRestant = "Enchere terminée";

            } else {
                this.tempRestant = jourR + " j " + heureR + " h " + minR + " m " + secR + " s";
            }
        }));
        tempsRestant.setCycleCount(Animation.INDEFINITE);
        tempsRestant.play();

        long secR = secRestant(this.fin);
        if (secR < 0) {
            tempsRestant.stop();;
        }
    }

    private void recupereFin(Integer idAnn) throws SQLException, ClassNotFoundException {
        Connection con = this.main.getBDD();

        try (PreparedStatement st = con.prepareStatement("select fin from objet where id = ?")) {
            st.setInt(1, idAnn);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                this.fin = res.getTimestamp("fin");

            }
        }
    }

    private long secRestant(Timestamp fin) {
        LocalDateTime l1 = LocalDateTime.now();
        LocalDateTime l2 = this.fin.toLocalDateTime();
        long diffS = l1.until(l2, ChronoUnit.SECONDS);
        return diffS % 60;
    }

    private long minRestant(Timestamp fin) {
        LocalDateTime l1 = LocalDateTime.now();
        LocalDateTime l2 = this.fin.toLocalDateTime();
        long diffM = l1.until(l2, ChronoUnit.MINUTES);
        return diffM % 60;

    }

    private long hRestant(Timestamp fin) {
        LocalDateTime l1 = LocalDateTime.now();
        LocalDateTime l2 = this.fin.toLocalDateTime();
        long diffH = l1.until(l2, ChronoUnit.HOURS);
        return diffH % 24;

    }

    private long jourRestant(Timestamp fin) {
        LocalDateTime l1 = LocalDateTime.now();
//        System.out.println("now"+LocalDateTime.now());
//        System.out.println("fin"+fin.toLocalDateTime());
        LocalDateTime l2 = fin.toLocalDateTime();
        long diffJ = l1.until(l2, ChronoUnit.DAYS);

        return diffJ;

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
    private void recupereTempsRestant() {
        Timestamp mtn = new Timestamp(System.currentTimeMillis());
        long secR = secRestant(this.fin);
        long minR = minRestant(this.fin);
        long heureR = hRestant(this.fin);
        long jourR = jourRestant(this.fin);
        if (secR < 0) {
            this.tempRestant = "Enchere terminée";

        } else {
            this.tempRestant = jourR + " j " + heureR + " h " + minR + " m " + secR + " s";
        }
        System.out.println(this.tempRestant);
    }

}
