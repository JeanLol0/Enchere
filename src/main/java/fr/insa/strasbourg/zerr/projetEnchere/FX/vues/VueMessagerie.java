/*
 * Copyright 2023 jules.
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

import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.ValiditeDateEnchere;
import static fr.insa.strasbourg.zerr.projetEnchere.gestionBDD.BDD.connectGeneralPostGres;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author jules
 */
public class VueMessagerie extends ScrollPane {

    private FenetrePrincipale main;
    private GridPane grid;
    private ArrayList<Integer> idMessages;
    private ArrayList<Integer> idMessagesInter;
    private Connection con;

    public VueMessagerie(FenetrePrincipale main) throws SQLException, ClassNotFoundException, IOException {
        this.main = main;
        this.setStyle("-fx-color: #148ca7;");
        this.grid = new GridPane();
        this.grid.add(new Label("MESSAGERIE"), 0, 4);
        this.con = this.main.getBDD();
        this.idMessages = new ArrayList<Integer>();
        this.idMessagesInter = new ArrayList<Integer>();
        recupereTOUT();
        recupereIdAnnonce();
        recupereIdMessages();
        afficheMessages();
        this.setContent(this.grid);
        this.setFitToWidth(true);
    }

    public void afficheMessages() throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        this.grid.getChildren().clear();
        int nbMessage = this.idMessages.size();
//        try ( PreparedStatement st = con.prepareStatement("select id from message where vendeur = ?")) {
//            st.setInt(1, this.main.getSessionInfo().getUserID());
//            ResultSet res = st.executeQuery();
//            while (res.next()) {
//                int resultat = res.getInt("id");
//                for (int i = 0; i < nbMessage; i++) {
//                    if (this.idMessages.get(i) == resultat) {
//                        this.idMessages.remove(i);
//                        nbMessage = nbMessage - 1;
//                    }
//                }
//            }
//
//        }
        nbMessage = this.idMessages.size();
        for (int j = 0; j < nbMessage; j++) {
            this.grid.add(new Message(this.main, this.idMessages.get(j)), 0, nbMessage-j + 2);
        }
    }

    public void recupereIdMessages()
            throws SQLException, ClassNotFoundException {
        int nbr = this.idMessagesInter.size();
        try ( PreparedStatement st = this.con.prepareStatement("select distinct id from message where vendeur = ? ")) {
            st.setInt(1, this.main.getSessionInfo().getUserID());
            ResultSet res = st.executeQuery();
            while (res.next()) {

                int vrai = 0;
                for (int i = 0; i < nbr; i++) {
                    System.out.println("Coucou" + gettexte(con, i));
                    System.out.println("texte comparaison: " + gettexte(con, res.getInt("id")));
                    if (gettexte(con, i) == gettexte(con, res.getInt("id"))) {

                        vrai = 1;
                    }
                }
                System.out.println();
                if (vrai == 0) {
                    this.idMessages.add(res.getInt("id"));
                }

            }
        }
    }

    public void recupereIdAnnonce()
            throws SQLException, ClassNotFoundException, IOException {
        try ( PreparedStatement st = this.con.prepareStatement("select id from objet")) {
            ResultSet res = st.executeQuery();
            while (res.next()) {
                if (ValiditeDateEnchere(res.getInt("id")) == false) {
                } else {
                    Annonce annonce = new Annonce(this.main, res.getInt("id"));
                    if (getEtatLivraison(con, res.getInt("id")) == 0) {
                        annonce.messageFin();
                        Annonce.setEtatLivraison(con, 1, res.getInt("id"));
                    }
                }

//                System.out.println("ids recupéré nb:" + this.idAnnonce.size());
//            if (ValiditeDateEnchere(res.getInt("id")) == false) {
//                int size = this.idAnnonce.size();
//                this.idAnnonce.remove(size-1);
//            }
            }
        }
    }

    public int getEtatLivraison(Connection con, Integer idObjet) throws SQLException {
        int valeur = 0;
        try ( PreparedStatement st = con.prepareCall("select etatlivraison from objet where id = ?")) {
            st.setInt(1, idObjet);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                valeur = res.getInt("etatlivraison");
            }
        }

        return valeur;
    }

    public String gettexte(Connection con, Integer idMessage) throws SQLException {
        String valeur = "";
        try ( PreparedStatement st = con.prepareCall("select texte from message where id = ?")) {
            st.setInt(1, idMessage);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                valeur = res.getString("texte");
            }
        }

        return valeur;
    }

    private void recupereTOUT() throws SQLException {
        try ( PreparedStatement st = this.con.prepareStatement("select distinct id from message where vendeur = ? ")) {
            st.setInt(1, this.main.getSessionInfo().getUserID());
            ResultSet res = st.executeQuery();
            while (res.next()) {
                    this.idMessagesInter.add(res.getInt("id"));
                }

            }
        }
    }


