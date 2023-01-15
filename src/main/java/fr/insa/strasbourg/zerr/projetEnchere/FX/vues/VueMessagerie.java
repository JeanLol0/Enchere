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
    private Connection con; 

    public VueMessagerie(FenetrePrincipale main) throws SQLException, ClassNotFoundException {
        this.main = main;
        this.grid= new GridPane();
        this.grid.add(new Label("MESSAGERIE"), 0, 4);
        this.con = this.main.getBDD();
        this.idMessages = new ArrayList <Integer>();
        recupereIdMessages();
        afficheMessages();
        this.setContent(this.grid);
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
            this.grid.add(new Message(this.main, this.idMessages.get(j)), 0, j+2);
        }
    }
    public void recupereIdMessages()
            throws SQLException, ClassNotFoundException {
        try ( PreparedStatement st = this.con.prepareStatement("select distinct id from message where vendeur = ?")) {
            st.setInt(1, this.main.getSessionInfo().getUserID());
            ResultSet res = st.executeQuery();
            while (res.next()) {
                    this.idMessages.add(res.getInt("id"));
            }
        }
    }
    
}
