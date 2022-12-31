/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.gestionBDD;

import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.strasbourg.zerr.projetEnchere.model.Utilisateur;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author jules
 */
public class BDD {

    public static Optional<Utilisateur> login(Connection con,
            String email, String pass) throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement(
                "select utilisateur.id as uid"
                + " from utilisateur "
                + " where "
                + "utilisateur.email = ? and pass = ?")) {

            pst.setString(1, email);
            pst.setString(2, pass);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                return Optional.of(new Utilisateur(res.getInt("uid"), email, pass));
            } else {
                return Optional.empty();
            }
        }
    }

    public static Connection connectGeneralPostGres(String host,
            int port, String database,
            String user, String pass)
            throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port
                + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }

    public static Connection defautConnect()
            throws ClassNotFoundException, SQLException {
        return connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
    }

    public static void creeSchema(Connection con)
            throws SQLException, ClassNotFoundException, FileNotFoundException {
        // je veux que le schema soit entierement créé ou pas du tout
        // je vais donc gérer explicitement une transaction
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) {
            // creation des tables
            st.executeUpdate( //utilisateur 
                    """
                    create table utilisateur (
                        id integer not null primary key
                        generated always as identity,
                        nom varchar(30) not null,
                        pass varchar(30) not null,
                        prenom varchar(50) not null,
                        email varchar(100) not null unique,
                        codepostal varchar(20),
                        image Text, 
                        lat real not null,
                        long real not null
                    )
                    """);
            System.out.println("1");
            st.executeUpdate( // categorie
                    """
                    create table categorie (
                        id integer not null primary key,
                        nom varchar(50) not null
                    ) 
                    """
            );
            System.out.println("2");
            st.executeUpdate(//enchere
                    """
                    create table enchere(
                            id integer not null primary key
                            generated always as identity,
                            sur integer not null,
                            de integer not null, 
                            montant integer not null
                    )
            """
            );
            st.executeUpdate(//objet 
                    """
                    create table objet(
                            id integer not null primary key
                            generated always as identity,
                            titre varchar(100) not null,
                            debut Timestamp not null,
                            fin Timestamp not null,
                            prixbase integer not null,
                            categorie integer not null,
                            proposerpar integer not null,
                            bio Text not null,
                            image Text, 
                            prixactuel integer not null,
                            lat real not null,
                            long real not null
                    )
                    """
            );
            //clé externes et liens
            st.executeUpdate(
                    """
                    alter table enchere
                         add constraint fk_enchere_objet
                         foreign key (sur) references objet(id)
                            ON UPDATE RESTRICT
                            ON DELETE RESTRICT
                    """
            );
            st.executeUpdate(
                    """
                    alter table enchere
                         add constraint fk_enchere_utilisateur
                         foreign key (de) references utilisateur(id)
                            ON UPDATE RESTRICT
                            ON DELETE RESTRICT
                    """
            );

            //clé externe objet
            st.executeUpdate(
                    """
                    alter table objet
                         add constraint fk_objet_categorie
                         foreign key (categorie) references categorie(id)
                            ON UPDATE RESTRICT
                            ON DELETE RESTRICT
                    """
            );
            st.executeUpdate(
                    """
                    alter table objet
                         add constraint fk_objet_utilisateur
                         foreign key (proposerpar) references utilisateur(id)
                            ON UPDATE RESTRICT
                            ON DELETE RESTRICT
                    """
            );

            con.commit();
            // je retourne dans le mode par défaut de gestion des transaction :
            // chaque ordre au SGBD sera considéré comme une transaction indépendante
            con.setAutoCommit(true);
            System.out.println("Ordre sql envoyé");
        } catch (SQLException ex) {
            // quelque chose s'est mal passé
            // j'annule la transaction
            con.rollback();
            // puis je renvoie l'exeption pour qu'elle puisse éventuellement
            // être gérée (message à l'utilisateur...)
            throw ex;
        } finally {
            // je reviens à la gestion par défaut : une transaction pour
            // chaque ordre SQL
            con.setAutoCommit(true);
        }

    }

    // vous serez bien contents, en phase de développement de pouvoir
    // "repartir de zero" : il est parfois plus facile de tout supprimer
    // et de tout recréer que d'essayer de modifier le schema et les données
    public static void deleteSchema(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            // pour être sûr de pouvoir supprimer, il faut d'abord supprimer les liens
            // puis les tables
            // suppression des liens
            try {
                st.executeUpdate(
                        """
                                    alter table enchere
                                        drop constraint fk_enchere_objet
                                             """);
                System.out.println("constraint fk_enchere_objet dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        """
                                    alter table enchere
                                        drop constraint fk_enchere_utilisateur
                                             """);
                System.out.println("constraint fk_enchere_utilisateur dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        """
                                    alter table objet
                                        drop constraint fk_objet_utilisateur
                                             """);
                System.out.println("constraint fk_objet_utilisateur dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        """
                                    alter table objet
                                        drop constraint fk_objet_categorie
                                    """);
                System.out.println("constraint fk_objet_categorie dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            // je peux maintenant supprimer les tables
            try {
                st.executeUpdate(
                        """
                    drop table objet
                    """);
                System.out.println("table objet dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate(
                        """
                    drop table utilisateur
                    """);
                System.out.println("table utilisateur dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate(
                        """
                    drop table categorie
                    """);
                System.out.println("table categorie dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate(
                        """
                    drop table enchere
                    """);
                System.out.println("table enchere dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
        }
    }

    public static void afficheTousLesUtilisateur(Connection con)
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

    public static void afficheUtilisateurParNom(Connection con,
            String nom)
            throws SQLException {
        try ( PreparedStatement st = con.prepareStatement(
                "select * from utilisateur where nom = ?")) {
            st.setString(1, nom);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                String lenom = res.getString("nom");
                String pass = res.getString(2);
                System.out.println("utilisateur " + lenom + " ("
                        + pass + ")");
            }
        }
    }

    public static int createUtilisateur(Connection con, String nom, String pass, String prenom, String email, Image image, double lat, double longitude)
            throws SQLException {
        // lors de la creation du PreparedStatement, il faut que je précise
        // que je veux qu'il conserve les clés générées
        con.setAutoCommit(false);
        try ( PreparedStatement pst2 = con.prepareStatement(
                "select id from utilisateur where email = ?")) {
            pst2.setString(1, email);
            ResultSet existe = pst2.executeQuery();
            if (!existe.next()) {
                try ( PreparedStatement pst = con.prepareStatement(
                        " insert into utilisateur (nom,pass,prenom,email,image,lat,long) values (?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pst.setString(1, nom);
                    pst.setString(2, pass);
                    pst.setString(3, prenom);
                    pst.setString(4, email);
                    pst.setDouble(6, lat);
                    pst.setDouble(7, longitude);

                    if (image == null) {
                        //Photo pour objet qui n'a pas de photo donnée

                        String imag2 = "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAN0UlEQVR4Xu2de3AV9RXHg1qrndFOZ9pq1T60-l_bfzpj26nt1Fdba3XaePcmJMLuJjFilVI77ZQ6Lb_dm_AKCEqiCL4hBAgQoD5AyPuBsbkhL0wgJIGQx819ihCDPJL0d5JIydnN-97dszf7nfmMwd27-_udc3b39_7FxESZGGNXxcrsDkFUH3bK6rNOSVnLyRUkpYLzsSCxTs4Z_vfgFVzg_y8kiEoH_7uJU8L_vc0hKy86JHWhY776kJDM7oRr4_vZMlmxKew27rAE7rAXBJkd4v_tQ84NH6JyllM2HBhKPL_XzTg9tiKshxau-zI8kQ6JZQnDT6vWUcbS5JTYOkFSHxQEdi1Or60w6FeMXeOU1EccIsvhBv9UxwlU-JQHQzYEKKQZ58PWFPWYlHaXU1ZWc8P26BibOl7-iciIE9O-j_Nla3zNcYjKr7kB3-P06xjWavTzssn7Tpk9gDNqa7Tm8NL6H_hT49YxYnQgKlW87BILecWZn9UaeeJrNAaLWiDI1QexHWad4pKVH3Bj7NcaaLbA9kObBbZL1EsQ1lzPS8sKN8J5rVFmG6xvyBazpQrpFJVf8oyf0Bpi1lMnzGd3Y3tFjaBezAtALh7xl3QybzNMP7RoQmMXtp-lJcxj34EmVJ0M2-hTLcxLux3b0ZIaaTM_rZNJm3FhAaes_gbb00qaM1LQ08mczSQZ4J_NFZbrhYQSLa_bb9bJkM30-A_UnLCdSerRpJU38ATv08mEzcwoSUxkN2J7k1J8ErtFmFUtekbD3HNTla9ju5PQiPNbtYm2CSsyaxTmp9-K7W-qICqFoeFWOgk2n_MJKa7jC_665sN_L3u9OGPdtsJ1G3cWbdz0TunW3YUVAPwN_w-OwTlw7tyUtBb4rc71KNAKI6GwH0wRfJeI9eB9lvKX1R-99Mbu4uqG5pZOn_-iJxAcnA7wW3fdsZbM13cXpyzKqIJr69zPLOpMLxM8kqp8hTu_VCdxhpOYurTxrW37Szt6fOewI8PFKY-v742cfWWJqelH8f1NQVQOpqZu-BL2iyGCuilPxLuaRBkMOGN_cdVh7KxI837Bfw8nPJHWjNNjNE5ZeTvGjPEFgqyoODHGwvpe2LCzcCav-JnS6fVfeP6l7cU8Pee06TMO6GPB_omohsbJmzhcy5mk-osra2uwQ8yisLzmiFNWgzidRuKQVAH7KSKKl9j3-A1Ny2x8kqu7rqm1HTvBbGoaW07y17EXp9c4WAg63bC_wipJYtfBuDbtzQ1CZGcPuT9uwManQoW74ahTUns16TYK6HEVcq_GfgubhidA6NzYIDblHijDRqfGm1v3leN0Gwn_FDDst7AoTmI_FUz87ictzKjCxqZItz8wmLxw5WGcfsMQlYtCErsH-29Ggrom_8bUa25mHAOVhxsbsbGpUlF15JhOHoykJayjihyy-k-dmxhGskWe_isx9S3AAZ9hP05LMC0a6tz4BkayJa-gHBuYOptyP4Dp6Jq8GMhnYakV8At9oHNxIzl_sstzFhuYOq2dnjND32NtfgwDJqpif05JMCYNX9Ro5i1YRrbaNxGJTy41u89gAIbhY79OVnMEGJ2qvaih_H3J-mJsWKvw7HNZplYJhxCVKvAldu6EcorqY5qLmUBG1vZCbFirsHxtDvQTaPJkOKL6MPbvhDK1xe8KMl_bU4QNaxVe3LCrBOfHHFgl9u-4GlruRHMRc3h187sl2LBW4ZVN75AYKzGErNyH_TymBEKjejNf3V2EDWsV6LwBFAiAvdjPuoIlTQQTm3wxa9fvKMKGtQpr1-fSCQDu00ktVzOyJg_-sWnYARA-YJYR9vcowQxegdiCTHYAhJWucbuLnRL7nc6PTMUOgPAy7oRTfsIW_AOzsQMgvDgk5S3s9yFB96GgXUPXdOwACDuf6C5HQ6HdXw87AMKP7pqFgqhk4hMpYAdA-IEV1LH_4ftv9igWXewAiASsfpTzYx9f-i3tSTSwAyAiDIDPLwfA8Do-mpNIYAdAZBg1kQQ2QMAnUMEOgMjgEJVVlwOAfxPMH7igQ3yyq_1o2ykPNqxVaGo91cPzcArniwasdNj5Qu7V_B_mzWYZm4HC8sNk5v9Nl_yyahhOP6CTP3MRlbMxMFIINmXQHCSA_MwKw6d8RwrpqRW1OH8kgFHDFNv_Af79LMKGtCrPv5xLY2gYYqhfwCkqf8YHKJCzu6ACG9Kq5OTlH8L5owBsiQcFwBfwAQrseqfkQ2xIq7Lz3dJKnD8KOGVlJVQBt-MDFLADwBC2QBMwybqqHQBGwIohAChsvqjBDgAjYLUQAF3aA-ZjB4AhnIAA-ETngOnYAWAELAQBQHI5VDsAjID1QjWQ5B4-dgAYwjmyb4DsXflR0xC0La_wQ5w_IpyHN4Cpq3-Mxaqs6GkKXrN-B8mmYPA9BAC5kcCA-NTyOmxIqyI_TbQzSGI-CIBO7QEaFJRXWz4IRrqDNXmjAWuDACCbwLgkV-fRtg4vNqpVaGo56Y1PdnXgfNGB1UMAEP0-DROXpHbVNraexMalzoFSdy2kHeeHGPkwH2CHzgFSpK_ZXIQNTJUuX-BS0jPmrhE4BTbFwOBAnQOkmP_ksiPY0FR572Alpe1zJmI5LAb1tM4BavQ3t3f6sbEpsnBxJskBtnqA72NgBSl8gCKvZb9Hfq2gE12eXqIDbHVxiOr9MbEyuwMfoIi4YHk9Njg1Ml_LIzm2Yixgr0cYGT5HsMju3rANHDY6FaDwF59EdQ6AHuz0FRNDaI4Kwix2bSS7auiOvSVUO3z0kZXCKwKA5sBQLay3ub0jgI1vNt3-QD-BdYGnxKgFo6gsCzsZ0tdkk1s6dvMO05eGnzI8AGIvB8Af5ynfFChOX9JDVs80n-gk0zzc0eM_z7_9ZPtTxmBgbsrSmy4HAEigu_mzhsUqnbLAiswcS5SfEDWjnA-CZUN0TqTKhcrqxqPYGUbjrm9u42n5XCd9pBmaEILlnK_ei0-kzOOpSz_u8PkvYKcYBRT8xD8tJ9uTOi56C0ePrBJq2o6g0wH27cWOMQp-byu--gdhEAj4Gvt_SLATtfYHlGF97rpjrdg5keZAsbvG7P2Apgsv_W_Efr8sWD8O_4A6CU-kH4fXMXZSpGhr7_rEKas-nA7roD6I_X5ZjLGr-FNloebMYYwsC1TXN7fi-1sGUekYd7FokFNkyzQ_JI4dAJNEVlTsb41ik9O_KxCdLDIWdgBMin7wLfa3rvjJu3QuQBY7ACaBzPKwn8cUbDaouQBh7ACYGKfo-jn287jinwE6O15NgB0AE_IR9u-EorR13ETYATA-sBIc9u-kJBBdQRTR3-0PXMKOihTuhmPQ_o_TQBdRKcN-nbQckusXmgsSI05WO7GTIgnM9sFpoAz4EPt1SnJKSi6-KCWSF62qwk6KJPxtM-CU1BBOB0mmUvIfS_Fy-rf5xT7TXJwIW_MKDmEnRZrF6qsW6AhifcK8tNuxP6clQWRLtDcwn_iUtLZOr3EFwC843NhyknpnkENSGfbjtAW7TPEMN-CbmAl_DfdW1R47jp1jFFmv76H8FmiSJHYd9uOM9Jjs-gmVJmKYNl5UUWv6fEElY1MRTpv5sEvgK-y_sMj0jiL-2v3bkvWl0CWLnWEWez-oqEpISSfTNjDh_sAz0cioIVOGQCc9k1FdWdPUjB1AARiPsH1P0SHTA0FUqnQ3hQynoGRp5FSyeQuWNeSXuy2xc0iXL9CfvbOg0pxAYKdhnif2V0TkkNnv-U37tYkIH3OTXSfhqYJ6NzY0dUwIhIFREz2MkFNiz-kkZMY4ZSUIgz07vL7PsWGthmGfBl5Nx_4xQnOckrJVk5hpw3pdq9_Ob-vq-RQb0upE8o0A-z2AL7BzDNHwjuNKPk7UlOAl-2f_9XIRbLGGDRdthD8QWKkgrLke-8VQPZq08gaekGmti0O5ZB9JwvNpYPUJCcu_hv1hioYml06hpdBKJftIMv03Amvm3Iz9YKoEedk3eOLqtIn9P_HJrhNWLdlHkqm9EdhRYX76rdj-JDQUBDLTrJHnlNXA-jf3lpjRcWMlYJmZ7J0HyueOGQisXjOtm5oSE9mN_HNw8ItEL3ouq-x4e1cQZ9ZmbDp9_ouw4BR_cK5ocGOlZL75EwmaI-Nk19tv5Owrw5mzmTxHjrV1wSKZMCgn7L17kdbg4OBVnmBwCc-IYfP2opCBUx6vC6bsYftaRh6__16ekaiv40eAgMcXmt5oXmrqDAZv4xkyfOiWhXH3nD4dnuFcVHRkcPBaTyCwgmfuok6GbYa5xD-bq8BW2H5RI28o9COeUUNH8loCf7COPyB3Y3tFpXgB8ZqeYHARz3ivxhCzj3Pc-UpUP_Vjyefz3cUNsIMzG1sGB3oCwTxugzuxXWadekKhH3KD5OoYKSrp8QfLu32hmc3YiUb1BAL3cQNFzY6hOhyCajHOty2kbr__x7yMsIEbrE_HiFbjPCeXB_cDOJ-2JpDX673JEwj9gxuwXcew1PFAtRfaQHC-bE1RvNZw9fDnIZTFDdulY2wq8LQFXuJpvR9qOjgftsIg6GPwBoM_8wSDq7nBYRsZM_sa4N4NPC0ZkCZIG06vrQgrFAp9tTsY_C2vS6vcGfmcszqOChdnugPBg3AvuCfcG6fHFgH5_f5bukKhe_jrWARn8VdyNndeAcfNaeLATqQhDgxQAeBvKGfAMX5OqJD_ZvNwUAVEqLLBNfF9okH_AzM7OhApx3TaAAAAAElFTkSuQmCC";
                        pst.setString(5, imag2);
                    } else {
                        String conv = ImageEnTexte(image);
                        System.out.println("Ici ?????");
                        pst.setString(5, conv);
                    }
                    pst.executeUpdate();
                    con.commit();

                    // je peux alors récupérer les clés créées comme un result set :
                    try ( ResultSet rid = pst.getGeneratedKeys()) {
                        // et comme ici je suis sur qu'il y a une et une seule clé, je
                        // fait un simple next 
                        rid.next();
                        // puis je récupère la valeur de la clé créé qui est dans la
                        // première colonne du ResultSet
                        int id = rid.getInt(1);
                        return id;
                    }
                }
            } else {
                return -1;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static void demandeNouvelUtilisateur(Connection con)
            throws SQLException {
        String nom = ConsoleFdB.entreeString("nom ?");
        String prenom = ConsoleFdB.entreeString("prenom?");
        String email = ConsoleFdB.entreeString("email?");
        String pass = ConsoleFdB.entreeString("pass ?");
        int id = createUtilisateur(con, nom, pass, prenom, email, null, 48.69060664200385, 7.579645983737555);
        if (id == -1) {
            System.out.println("utilisateur existe déjà");
        } else {
            System.out.println("utilisateur N° " + id + "créé");
        }
    }

    public static int createObjet(Connection con, String titre, Timestamp debut, Timestamp fin, int prixBase, int categorie, int proposerpar, String bio, Image image) throws SQLException, FileNotFoundException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into objet (titre,debut,fin,prixbase,categorie,proposerpar,bio,image, prixactuel,lat,long) values (?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, titre);
            pst.setTimestamp(2, debut);
            pst.setTimestamp(3, fin);
            pst.setInt(4, prixBase);
            pst.setInt(5, categorie);
            pst.setInt(6, proposerpar);
            pst.setString(7, bio);
            double lat = 0;
            double longitude = 0;
            try ( PreparedStatement st = con.prepareStatement("select long,lat from utilisateur where id=?")) {
                st.setInt(1, proposerpar);
                ResultSet res2 = st.executeQuery();
                while (res2.next()) {
                    lat = res2.getInt("long");
                    longitude = res2.getInt("lat");
                }
            }
            pst.setDouble(10, lat);
            pst.setDouble(11, longitude);
            if (image == null) {
                //Photo pour objet qui n'a pas de photo donnée
                String imag2 = "iVBORw0KGgoAAAANSUhEUgAAAgAAAAIACAYAAAD0eNT6AAA5M0lEQVR4Xu3dB7gtZXm38T_Sq4J0lSZYEKkq3QOiookhlqDGCGLBFg0ajURROaImqARFE3tBwBqNGkVpgoqKCCoWuggCglQR6e373ifjkcP7rL3PLms988zM_buu-7r03Ye995pZs2btNTPvSP21VukJpReVDikdVTqudGbpt6Xr_9z_IyKiwXWP7t0PXKJm32D7CNtX2D7D9h27l9YUUluttGfpbWpW4BXyK5uIiGgu2T7lW6WFpSeXVhVac7_S9mpWxumlu-RXGBER0SSyfc5ppbeWHqdmn4QJe1Tp0NLv5FcIERFRG11eOqK0S2kpYWzWKR1Q-pX8QiciIsrUBaUD1ey7MEfblT5TukN-ARMREWXu9tIxpW2EGdujdLL8wiQiIupi31ZzRQGmsHPpFPkFR0RE1IfsjcBOwl88RM11l3ZNZr2wiIiI-tbXSxtrwFYovaN0q_zCISIi6nO3qJm7ZnkNjF0qca78AiEiIhpSF6qZtbb3Viy9X3zcT0REtCjbJ75PzSfjvWST-Jwl_8CJiIhIOru0tXrm-aWb5R8sERER3ZudF_di9cBypQ_JP0AiIiKauv9Ssw_tpFVKx8s_KCIiIlpyNime3e22U2wO5J_IPxgiIiKaeWeW1lZHbFQ6X_5BEBER0ey7qLSpkttCzW0R61-eiIiI5t6VSnyFwFalP8j_0kRERDT_bB-7rZLZpHSF_C9LRERE4-saNfPqpGAn_NlUhvUvSUREROPP_uDeSC1bVZztT0REFN05pdXVEpugwO5tXP9SRERENPlsH7ysWvBh-V-GiIiI4vqAgu0j_0sQERFRfC9UkC3V3Kyg_gWIiIgoPrvZ3sSvDLB7Ff9c_ocTERFRe_1KzT56Yt4v_0OJiIio_Q7XhCwo3SP_A4mIiKj97i7tojFbXs01h_UPIyIiojydpzEfCniH_A8hIiKifB2sMbF5_rt-1v9NpeNL7yntX3q8msdl0xivIgDA0Nhrv-0DbF-wQM2-4bDSCWr2GfV-pEvdUtpAY_Al-W_ehc4vLSztrJZmSgIAdJLtM-xY-ttKF8jvX7rQ5zVPtgDqb5o5e9djMxTuKAAAxmOn0kfVrU_D7aT9HTQPJ8t_04z9qXREaX0BADAZa6v5ZPkG-f1QxuxwxpzsIf_NsmXvcI5Ss1IAAIjwwNJH1I1L43fTHJwi_40yZZcl7ioAANqxoHSu_P4pUydqlraT_yaZsr_6VxYAAO1aUc35AfV-KlPbaBY-I_8NMnRbaV8BAJCL3ZHP9lH1fitDR2uG7LrIO-S_QdvZiX5PEgAAOe1W-qP8_qvtbtcMz5U7UP4_brurStsKAIDcbF91tfx-rO1eryVYqnSh_H_YZvZuip0_AKAr7Dy6G-X3Z21m9wiwffyUbLKD-j9qM_vYgo_9AQBds7vynRPwWE3jffL_QZtxwh8AoKteJL9fazO718FI9yv9Tv4_aKtPCQCAbrPL1uv9W1tdqikOA2wv_4_byib5WUkAAHSb3XXQjr_X-7m2svMTnIXy_7CNbGrF3QQAQD_YrLVZpg0-SCOcLv8P2-iTAgCgX46R39-10fdVWa10l_w_jM4um5jRZAUAAHTIuqWb5Pd70d2p5rDEX-w54h-10bsEAEA__Yf8fq-N7G6_f3HIiH8Q3a2l9QQAQD_ZpwC3yO__ojtYizluxD-I7sMCAKDfPia__4vuWC3myhH_ILodBABAv-0sv_-L7jL92VojvhjdBZpicgIAAHrmfPn9YHQPLP3ffMX1F6I7WAAADMPb5feD0dncBHrxiC9EZzchAgBgCBbI7weje0Gp9Xcidl3kcgIAYBhsn9f2nAALS63fqOB4AQAwLCfK7w8jO7KkE0Z8IbL3CACAYTlcfn8Y2bdK-smIL0T2EgEAMCwvk98fRvbjki4Z8YXIHi9gfO5X2lTN9NYvLx2q5jCXTXzxo9KFpYtK15au_3M2D4aNnavmRhlfK31CzTGyF6g5Ycdm8AKAcdlNfn8Y2W9Kum7EFyKzF2tgrjZTs5O2mSRPK_1J_jk2rq5Wc9zu3aWnixtXAZi7h8m_xkR2TWmiL5gzib-sMBs2cdXzS58pXSX_fIruvNL7S08trSgAmBm79039ehKZ3X1Xt4_4QmQrC5iebSivKp1aulv-OZQlu8nHl0rPLq0kAJia3ZK3fg2JzPb9bjA6pgDGKMuWnqXmRlV3yT9vsmfX-H6ytKMAwLN9X_26Edk9GjEYHbC4ddTcnjrDDarG1S9L-5dWEADcq36tiM4NRAcYO5nPbpN5q_xzpC_9vvSW0gMEAP41Ijo3EB2GbYPSR0p3yj83-pqdfGOXJ95fAIasfm2Izg1Eh2Gyv4JtJqy2T0JtM7us0CYDWVoAhqh-TYjODUSHYbGJemynZzu_-rkw1H6uZrIhAMNSvxZE5waiw3DYpE8nyz8HqDkj12YsfKAADEX9OhCdG4gO_WcfcR9Uuk1-_dN9s6sf9hKAIai3_-jcQHTotw1L35Vf7zR99mkAk2QB_VZv99G5gejQX39X-qP8OqeZdU5pcwHoq3qbj84NRIf-sY_87TI3O65dr2-aXXavDptaGED_1Nt7dG4gOvSLXdt-gvx6prlnb6T-TUybDfRNva1H5waiQ388qPQz-XVM48luNMQdB4H-qLfx6NxAdOiHrdSv-fuz9n0xlTDQF_X2HZ0biA7dt13pWvl1S5Ppp6W1BKDr6m07OjcQHbptFzVz29frlSbb2aV1BaDL6u06OjcQHbprm9If5NcpxWS3GWbmQKC76m06OjcQHbrJjvlfL78-KbYzxV0Fga6qt-fo3EB06B472_8y-XVJ7fSd0vIC0DX1thydG4gO3bKamrvX1euR2u2zYp4AoGvq7Tg6NxAdusNm-Dtefh1Sjt4kAF1Sb8PRuYHo0B3vkl9_lKe7S08VgK6ot-Ho3EB06IZnibn9u9B1pY0EoAvq7Tc6NxAd8nuIOOO_S51RWlYAsqu33ejcQHTI7X6lU-TXG-XuEAHIrt5uo3MD0SG3N8qvM8rfXaWdBCCzeruNzg1Eh7weVrpVfp1RNzqvtIIAZFVvs9G5geiQk330_z359UXdaqEAZFVvr9G5geiQ00vk11UXu6D0xdLbS_9Q2rG0ZWmT0hqltf_8v7ct7VZ6aemw0jdKV8t_v651e-nhApBRvb1G5waiQz42t_xV8uuqC_2pdExpXzVXL8yHzaxnbxYOKJ2g5rh6_fO6kL2ZAZBPva1G5waiQz7vkV9P2fu2mp3-ypocuwfCG0rnyv_87D1FALKpt9Po3EB0yGWD0m3y6yljNvPd_5S2Uyw7P-KZpZ_I_05Z-4Wa3xtAHvV2Gp0biA65fFR-HWXs5NIWat9epYvkf7-MPUcAMqm30ejcQHTI46GlO-TXUaauVHMyXyYrqpl4J_snJ3bowm7oBCCHehuNzg1Ehzw-LL9-MvX10gOV1yNLv5T_vTO1twBkUW-f0bmB6JDDWqVb5NdPhu5Ucz17F45h26cBH5N_DFn6sQBkUW-f0bmB6JDDW-XXTYZuLD1R3fOvynv3xF0FIIN624zODUSH9i1Tulx-3bSd3dp2B3WXXZaY8ZyKzwlABvW2GZ0biA7t-1v59dJ2NgvfI9R9dsw92wRCdrLimgLQtnrbjM4NRIf2HSu_XtrMPvZ_jPrj5fKPse1eKwBtq7fL6NxAdGiXzYVvJ9nV66Wt7CPzLh7zX5K3yT_WNrNJjAC0q94uo3MD0aFdr5BfJ232evWT3Vfgq_KPt802E4A21dtkdG4gOrTrO_LrpK3sUITtKPtq9dIl8o-7rQ4SgDbV22R0biA6tMduh5vlBLXfK_ckP-OyQHkuDzxDANpUb5PRuYHo0J7nyq-PttpHw_Ep-cffRnYzpXUEoC31NhmdG4gO7fm0_Ppoo--p3x_91-yTjmvll0MbDemNF5BNvT1G5waiQ3syTP5jH4dvpeF5jfyyaKOjBKAt9fYYnRuIDu3YWH5dtJGdGT9EK5SukF8e0dmtjAG0o94eo3MD0aEdz5dfF220o4bL7hdQL482Wl8A2lBvi9G5gejQjg_Ir4vovqthW610k_xyie6ZAtCGeluMzg1Eh3acKr8uonuhcLT8coluoQC0od4Wo3MD0SGenXH_B_l1EdktpfsLT5ZfNtH9jwC0od4Wo3MD0SHeBvLrIbrPC2ZpNZMg1csnsgsFoA31thidG4gO8faQXw_RvUhY5DPyyycyuxnUMgIQrd4Wo3MD0SHei-XXQ3R2GSIaL5FfPtFtJADR6u0wOjcQHeIdIr8eIrtYWFyGORl2F4Bo9XYYnRuIDvE-Ib8eIuOkM6_tkzJtXggAsertMDo3EB3ifU1-PUT270LtdPnlFNkBAhCt3g6jcwPRId735ddDZPsJNZuTv15OkdlhIQCx6u0wOjcQHeKdLb8eIttNqL1NfjlF9p8CEK3eDqNzA9Eh3q_l10Nk2wq118kvp8g-JgDR6u0wOjcQHeJdKr8eIttMqL1UfjlFxm2BgXj1dhidG4gO8dqeeY67z3l_L7-cIvuCAESrt8Po3EB0iHel_HqI7MFCbR_55RTZ5wQgWr0dRucGokO8S-TXQ2SPFGqvlF9OkR0pANHq7TA6NxAd4l0gvx4ie5xQO1B-OUX2EQGIVm-H0bmB6BDv5_LrIbKnCLVD5ZdTZO8TgGj1dhidG4gO8U6WXw-RvUqo2fTI9XKK7M0CEK3eDqNzA9Eh3hfl10NkHxBqbU_O9AoBiFZvh9G5gegQ74Py6yGyE4XFLV26TX45Rba3AESrt8Po3EB0iPdG-fUQmd35znZ6aDxWfhlFt72QwTql55QOU3NYyN4sn1b6Rumjaj6p2fwv_xpdV2-H0bmB6BDvufLrITrb6aHR9hUAlu140I6lSk9Xs7O_S37djMqu5Hl96QFCl9XrNTo3EB3i7SC_HqKznR4ax8svn8huUrMTQrwdSz-VXycz7Vo1J9XeT-iien1G5waiQ7w15NdDdN8VzGqlW-SXT2S2A0Is22G_vXS3_PqYS3Zlz7pC19TrMTo3EB3a0fYNge4pbSy8SH7ZRHekEGk5Teayz4tLjxC6pF6H0bmB6NAOO6moXhfRvUU4RX65RGe3IkYM-8v_K_LrYFxdVXq00BX1-ovODUSHdtjHj_W6iO4iDftqgE01vo-A59MThCj_Jr_8x901pW2ELqjXXXRuIDq046ny66KNnqfh-rj88ojuztIqQgTb5uzQV70OJpFdasulnfnV6y06NxAd2mGXD2X46_McDfMM5oeUbpdfHtH9WIhgt8C2v8zr5T_JblBzlQHyqtdZdG4gOrSn7ZsCLcomPhkau_tevRza6HBh0pYpnSq_7CO6sbSrkFW9vqJzA9GhPe-RXx9tdJmG9TH0dpr5hC-Tbk9h0v5dfrlHdnPpiUJG9bqKzg1Eh_bYyV_1-mird2sY7HDHj-QffxvZ_AMrCpNk21iGQ222rp8sZFOvp-jcQHRoj12P_Ef5ddJGd2gY0wP_s_xjb6tjhUlaX81lefVyb6tbS38lZFKvo-jcQHRo1zHy66StfqtmlsK-sjc4GU78W9R-wqTYJz0nyS_ztrPn3zOELOr1E50biA7t2kt-nbTZ_6qf89LbVRe_kX-8bWU7gj6_2WrbIfLLPEt26eeQL7_NpF430bmB6NCu5dVcM1yvlzazF88-sWX8bfnH2WZfFyZlD-U47j9d9ibA7gqKdtXrJTo3EB3a90H59dJ2r1E_2EfBX5B_fG33TGES1i5dIb-8M2ZXouwntKleJ9G5gejQPps2tF4vbWd_Qe2rbrOdf5br_Rfv96VlhXHLetx_umw7sxtSoR31-ojODUSHHGxGuHrdtJ1Nm2pnzXeRfez_RfnHlKF3CZNgN7eql3UXsu3s5UIb6nURnRuIDjk8X37dZMkmLOrSTYNWV75j_ouyY78bCuO2QHkmd5pL9ibg1UK0ej1E5waiQw72kbBdhlevnyydoua66uweo-Yuh_XvnyW77BPjtVbpcvll3cUOEiLVyz86NxAd8ni9_PrJlB27zjp1rX1C8Trlus5_VNsK42TH_Y-TX85d7o1ClHrZR-cGokMeK6vZydbrKFt2CZvdTS8L26lmmd53ur4mjJvtLOvl3IcOFSLUyz06NxAdcsk0Ve102RTG_1paVe3ZqPQx5b_m27JjvFsL47SLmnMq6mXdl94pTFq9zKNzA9EhF7s5TJeOZ15XOlixs9o9rPQpNfcvqH-frNkVCRifB5YulV_OfWsoN-lqS728o3MD0SEfu_6-Xk_Zu03NoYG9NZlr3O-vZrmcqOav6frnZ87OS9hMGBebqvqr8su5r31I_ZyeO4N6WUfnBqJDPraxny6_rrqSfSrw36VXlB6hub142ZuIHdScFW2X9Nmd1Oqf05XsfvQYn-wny04imy10LtsRplcv5-jcQHTIaXt149j2TLq59NPS59Wc3HRg6ZVq_qK3WdD-qfSm0uFqTpQ7X936eH-67HBOm-dJ9I29KezLc2O22fkudtUDxqdextG5geiQ1_vl1xd1K279Oj7Z7ujYRp8tLSOMS718o3MD0SEvuyxw6C94Xe5LwrjYx9__I7-Mh5jd3Io3AeNRL9vo3EB0yO1J6t5JbyRdXVpXGJcD5JfxkLOrSiZxsu3Q1Ms1OjcQHfKzY-P1eqO82Ru2vYRxsemds8_w2EbfKK0gzEe9TKNzA9EhP7uz3Vny645y9gFhXOzyz8z3dmg7exNgrw-Ym3p5RucGokM3bFS6Vn79Ua7s8k1ekMfHTnqrlzHdt-PVTCCG2auXZXRuIDp0x9PUn0sD-5i9QdtIGJd_lF_GNLrvllYRZqtejtG5gejQLW-WX4fUfnaMejdhXLZRM7tkvZxp6uxNAHNOzE69DKNzA9Ghez4lvx6pveykvxcI42J_yZ4nv5xpyZ1RWl2YqXr5RecGokP32OU_J8mvS2qnhcI4fU5-GdPMs_NQeBMwM_Wyi84NRIduWqn0Pfn1SbFxxv94vUx-GdPs-1lpTWFJ6uUWnRuIDt1ll0idKb9OKaYjxdzs47RF6Rb55Uxz65zSesJ06mUWnRuIDt1m90XnTUB8nygtLYyLHfc_V345Z8lmdvzJiPHs_UrMSDmdenlF5waiQ_fZJwHfl1-3NJns1qz85T9en5Zfzlmykzz_Rs129sMRX8-e3V3zQcIo9bKKzg1Eh36wGwcdK79-aXzZjuBgYdzsltD1ss7Uv-teq6mbb7YvLD1EqNXLKTo3EB36wz6StpPS6nVM88-u899XGLdHlW6WX95Z-pH8TXfsBNwuXoVzSemhwuLqZRSdG4gO_fOa0p3y65rm1lWlBcK42Y70bPnlnSWb2XGqv5rtdz9R_r_J3m_Fm4DF1csnOjcQHfpp19IV8uubZpedYLmRMAmfkl_eWbLDPX-r6dk9H74m_99m78rS5oKpl010biA69Jed-PMd-XVOS852AO8XN_aZlH3kl3mm3qOZsefH_8r_99njTUCjXi7RuYHo0G9LlQ4Q91OfTXbJl914CZOxWelG-eWeJZtJbznNnJ0j8GX575M9O7S1pYatXibRuYHoMAzblc6SX_90375YWkeYFLtt7S_kl3uWri9tqNmzNwFfkP9-2btGzY2XhqpeHtG5gegwHPYi9cbSrfLPg6F3eWkvYdI-Ir_ss2SHfZ6uubOrcI6R_77Zszc9j9Uw1csiOjcQHYbHzmw-Ss0LXv18GFp2aOQINdd3Y7KeLb_8M_VezZ9NEPVJ-e-dvRtKO2p46uUQnRuIDsO1m5rbh9bPiSF0t5qPbDcWImQ_7m_bwWyO-0_H3gRk_qRjqmz92NVDQ1Ivg-jcQHTAE9XNec7nml2_va0Qxc6Uz_z8sr9-N9F42cm375P_WdmzSZns9WAo6scfnRuIDjD2V4vNd_5d-edIH7qjdHRpayHaf8mvj0z9nSbD3gQcLv_zsmdvAp6kYagfe3RuIDqgZicEHancU7TONJsMyeZyn2pGN0yW7VzrdZIpe3MyaYfK_9zs2YnCf6X-qx93dG4gOmAqdvezV6q5A1qXThi8rfRVNWd0LyO0xT5Wt4_X6_WTpZ-WVlCMt8v__OzZdtT3K2PqxxydG4gOmIkNS_9S-kHpLvnnUdvdpGZaVrthj71xQbvshLrMJ5jaCW92YmKkt8j_HtmzQ2fPUn_Vjzc6NxAdMFtrlJ5T-njpXLXz6YC9MJ2mZspWO2mJKXtzyX4C3HPVjgPlf5fs2Y3F2lpek1Y_1ujcQHTAfK2pZurcN6m5tM7u8GYfH9bPtbn2x9KP1bzhsGmNH69mRjnkZIde2nhTONPsEr02vVa5l8-o7FM_u39D39SPMzo3EB0wCXYGtN2MaBc1nxbYuQRvVTPZir0AW59Tc2b-ov9vf83bTIUvUbMTsemL7dMGdMdGamaWq19nsvRz5Xjz-I_q3psAmzvjxeqX-jFG5waiA4BxsBMu7RyR-jUmS3aeyCOVh73RtZ1q_Xtmzt60vFr9UT--6NxAdAAwDofJv75kyk4QzeZ5ao6x179r5uxNwGvUD_Vji84NRAcA82XXjGf-SPsTystOsOvamwDrIHVf_ZiicwPRAcB8PFjNbWXr15Ys2UmpKym3vdVc2VL_7tmz83q6rH480bmB6ABgruy4_6nyrytZshnttlI32JU047x6Jiqb6bCr6scSnRuIDgDmKvs0ty9UtzxVzZuW-nFk713qpvpxROcGogOAuXiKcp_FbpeZdtGepVvkH0_27CTQrqkfQ3RuIDoAmK11SlfKv55k6YLSququBaU_yT-u7H1IzRwgXVH__tG5gegAYDbs1tEnyb-WZMk-Qu_DbZ93VXPPgvrxZc8m9bLnSBfUv3t0biA6AJiN7He221_9sbOaqbDrx5g9m7a7C28C6t87OjcQHQDM1O7KeTfIRdm9KPrGpsS-Tv6xZu-zyn877vp3js4NRAcAM2HH_a-Qfw3J0oWl1dRP25SulX_M2bM3ZJnfBNS_b3RuIDoAWBL7OPcE-dePLNn189uq3-y8hqvlH3v2_ru0rHKqf9fo3EB0ALAkB8u_dmTK7jY5BHYzo9_JP_7sfaO0gvKpf8_o3EB0ADCdBcp93N_-whySh5cul18O2fum8r0JqH_H6NxAdAAwlbWU-y_OS0praHg2K10mvzyyd3xpReVR_37RuYHoAGAUO-5_nPxrRpbs5jk7aLg2LF0kv1yy993SKsqh_t2icwPRAcAob5J_vcjUAcIGaq5-qJdN9uwGUhlmaqx_r-jcQHQAULO_rDPfnvbr6taUs5O0rppbHtfLKHtnlFZXu-rfKTo3EB0ALM6OqV8i_1qRpUtLDxQWZ3M0_FJ-WWXvTLW7LuvfJzo3EB0ALGJ_VX9N_nUiS3eWdhJGWbv0C_lllr2fldZUO-rfJTo3EB0ALPIG-deITL1OmI59pG4frdfLLXvnlNZTvPr3iM4NRAcA5nGl2-VfI7J0rDjuPxMPKJ0uv_yyd25pfcWqf4fo3EB0AGB_OV4s__qQJbvmva2Pibvo_qXT5Jdj9s4vPUhx6p8fnRuIDsCw2V_VX5F_bciSHfffRZitlUsnyy_P7F1c2lgx6p8dnRuIDsCwvVb-dSFT_yrM1Uqlk-SXafYuKT1Uk1f_3OjcQHQAhuuxyn3c_1tqZiTE3NmbgMx3cpwqu9xzU01W_TOjcwPRARgmO1nsN_KvCVn6vZpJbjB_yyv35Z1TdWVpc01O_fOicwPRARimL8u_HmTp7tIewjgtp9znekyVvRHcQpNR_6zo3EB0AIbn1fKvBZl6szAJyyr3G7-puqq0pcav_jnRuYHoAAyLvZDeIv9akKVTSksLk2LL9mj55Z6969WcszJO9c-Izg1EB2A47Das58m_DmTJ_tJrY0a4obE3AUfKL__s_aG0vcan_v7RuYHoAAzHMfKvAVmy4_5PEqLY1RWflF8P2buhtKPGo_7e0bmB6AAMw8vlt_9MvU2IZpNA_af8usjeTaXdNX_1943ODUQHoP8erdzH_b8jjvu3xd4EHCG_TrJ3c-mJmp_6e0bnBqID0G923N9utFJv-1m6WvE3gcF92ZuAw-XXTfbsTe2TNXf194vODUQHoN-Okt_us2TH_fcUsjhEfh1lz2ay3EtzU3-v6NxAdAD668Xy23ym3ilkc6D8esqevQl4hmav_j7RuYHoAPTTo9QcJ623-SzZ7WptYhrk08U3AXbXyOdpdurvEZ0biA5A_9itYM-W396zZJO6bChk9nr59Za9u0r7aObq_z46NxAdgP45Un5bz9I9pb8VuuAVatZXvQ4zZ28C9tPM1P9tdG4gOgD98vfy23mm3i10ycvUnKxZr8fM2ZuWV2rJ6v8uOjcQHYD-eFjpRvntPEunq7krHbrlJermmwC76dV06v8mOjcQHYB-WKF0lvw2niU77r-R0FV2gp2daFev18zZm4DXaGr1v4_ODUQHoB8-Jr99Z8leiJ8udN1z1b03AdZBGq3-d9G5gegAdN-z5bftTL1X6Iu9S3fIr-PsvVVe_W-icwPRAei2TUt_lN-2s3SGOO7fN08r3Sa_rrN3qO6r_np0biA6zJ_No712afU_t8x9vwxMjB33_5n8dp0lu3_7JkIf_Y26-SZg8dkn669F5waiw-wtr-bJf6Sak67qu6zZ_7dZzuw2m3a3KrvvNjAJH5TfpjP1LKHPnlq6VX69Z2_Rpaj1eHRuIDrM3Gqld5RukF-O0_Wb0hvUvHEAxsWOxdbPtUzZG2D0n_2Rk3nK6al634ix6NxAdJgZu6nKNfLLbzadX9pDwPw9VLN_IxrZT8Qb3iHZrfQn-ecBTZ8biA7TsxexcV9eZX8Z8eKIubLnzpnyz6ss2QmJdmIihmVn5T4ZNWNuIDpM7QGl78svs3H009JmAmbv_fLPp0w9Rxiq7dWc-Fk_J2h0biA6jGZn89u0pfXyGmc2Zetsb1-JYbPLrzLfnOXDwtBtU7pW_rlBPjcQHTy7pO_n8stqUh1VWknA9DYoXSf__MnSL0srCpC2Ll0t_xyh--YGosN9rVv6lfxymnR27_YtBIy2bOmH8s-bLN1UeqSAe9nz4Qr55wrdmxuIDveyv7AulF9GUdn8AXbXLaB2uPzzJVP7CPAeXrpc_vlCTW4gOjQ2VnO9fr182sgOCawsoPFXyn3c_-MCpmYnO18m_7yhEQPRQXqE8r1LPbf0aGHoHqz5zz8xyexwGeevYEk2LF0k__wZem4guqHLfLKKTbF5gDBUdk-JSV2GOo7suP_mAmam7UOsGXMD0Q3ZdurG5Sp2SGAVYWjeJf9cyNR-AmbHTrK2E57r59JQcwPRDVXXZq06r7SVMBR2k5W75Z8HWfqcgLlZR80lo_Vzaoi5geiGaIGaSXjqZZE9DgkMw4OU-7j_BaVVBcydzbXyC_nn1tByA9ENzVPkb9_btb6sZppi9M_SpW_Lr_Ms2ZtQO28GmC-bbfUM-efYkHID0Q2JTaPaxXtXj-piNfNuo1_sdtP1us7U_gLGx_6QmfSU65lzA9ENxbNLd8g__i53mzgk0Ce7l-6SX89Z-oKA8bt_6TT559sQcgPRDYHdcOdO-cfel76i5uM0dJedGJV52lS7fGs1AZNhE5-dIv-863tuILq-e6lyn009ri4p7Sh00f1KJ8qv0yzZJ03bCpgsexNwkvzzr8-5gej67JXKPYXquLNPOQ4sLSV0yUL5dZmpVwiIYbNKniD_HOxrbiC6vrIdYf1Yh9LXSmsIXbBAuY_7_7eAWMureQ2rn4t9zA1E10dD3vkv6lI1kx0hL7sW-nfy6y5Lv1ZzghYQbTk15zbVz8m-5Qai6xP76Psw-cc41OyQwEI1x5iRi62T4-XXWZbsipkdBLRnWTVzntTPzT7lBqLrC9v5v0_-8VFzgpmdZY48DpJfT5n6JwHts4mxjpZ_fvYlNxBdH9iT5BPyj43uze7HvYuQwa7KfVnq18WJpMjDXt-PlH-e9iE3EF3X2ZPj0_KPi3wcEmifnZz5W_l1kyU7d-SBAnKx16xPyj9fu54biK7L7ESRL8k_Jpo-u9bWbsuJWPZXdeazm-24_04CcrLt5z_ln7ddzg1E11V2qchX5R8PzayrSk8SImW_OuWfBeRmbwKOkH_udjU3EF0XDW2yiEll158vVHMYBZNlN27KfC-KY8Vxf3SDPU8Pl38OdzE3EF3X2HSRmW-X2sVOLq0nTIrdp-Fi-eWeJTtBdE0B3XKI_HO5a7mB6LrEbh35Q_nHQPPv6tKewrjZXyuZJzSxE0O5OgRdlf2w2pJyA9F1hf0V9WP535_Gl903wY6v2QQcGA87rl4v50zZCyjQZV1-E-AGousCm8TmF_K_O02m75TWF-brsaXb5Zdvlr4lLglFP7xe_vndhdxAdNnZsemz5X9vmmzXlJ4qzJUdrvqN_HLN0pXiUlD0i921smt3f3UD0WW2oZobktS_M8XEIYG5yzyH-d2lJwjon5epeX7Xz_msuYHosnqYmlnJ6t83ezeNGOt6p5YeLMzUq-WXYabeLKC_XqruvAlwA9Fl9Ajlvk3qVNkbls1K-5b-NOLrXe7a0l8LS7Jl6Vb55Zclu-STeR_Qd89T7vttLMoNRJfN1mouSat_z-xdXHqo7vXw0s9H_Lsut-iQgE3BDG-V0nnyyy1LvxfzPWA4nqv8bwLcQHSZbKfmL836d8yeveiP-oh8BfVr2spF2eWYmwi1z8gvqyzZR6JM_Yyh2Vu5Z-B0A9FlYZOR_FH-98veOVryJXPPKv1B_r_tcjeo2bjQsDOQ62WUqYUChulppdvkt4kMuYHoMligbh4z_6lmPoWqndT4M_nv0fU-Ig4JPLp0i_yyydJ3xHF_DJtd0pzx3Bw3EF3bbMVkfvGcqjPU3Nt9NuwOhn08JHCm7nv-w5DYcf9z5ZdJluyuj0v6hAoYApvqPNu-xg1E16bMH81M1_dKq2nunlG6Xv77djk7fGMn3QzN0fLLIkt23P_JArDIAuX6tNkNRNeW5yj3yRlTdYqav_rma8PSafLfv-sdVVpRw7C__OPP1DsFoLZr6Ub57aWN3EB0bfgH5b88Y1R2z3Q7s39cbIa9Q9W96SuXlJ0bYfMh9NmjSjfLP_Ys2ZtLZnAERttZOU46dwPRRevifM3WlzS5k904JNAtK6u5-qN-zFmy-ziMuiwVwL22l992onMD0UX6R3Vz5_-50jKarA1KP5D_2V3PDgmspH45Uv5xZsm2r70EYCbq7Sc6NxBdlK7es_ljirtlqr3JWKjuzGM90-xujluoH_aTf3yZepcAzFS9_UTnBqKLsFD-53ahDylu57-4v1E3Z0ScLrv85iXqNpvLIcvJQ6M6XZM7TAX0Ub0NRecGopukpUr_If8zu9B71C47hmt34at_r65nhwTsGHrX2MmfZ8k_nizZOSQbCcBs1NtRdG4gukmxnX9XJ72xM_Mz6OshAZs4x2bP65KPyz-OLNlx_6cLwGzV21J0biC6SbBpRz8h_7O60FuVzx6lK-V_1y5n03IeoG6wOSvq3z9ThwvAXNTbUnRuILpxs53_p-V_Tvbsr6jXKq91SifK_95dzw4JjGNipUnZVDmuF54quzMjx_2Buam3p-jcQHTjZC9EX5b_Gdmznf-rlJ-9uVpYukv-MXQ5u53yVsrHjvvbpEb175slu8PkxgIwV_U2FZ0biG5c7EY3X5X__tmznekL1S27l66QfyxdLuMhgQ_L_56ZeqYAzEe9TUXnBqIbB5vo5QT575092_k_X920duk4-cfU9ewTpAeofXvL_26Zer8AzFe9XUXnBqKbLzt-e7L8983e7er-X1B2pYVNsNS3QwIXq5mmsy12a-Mb5H-vLP1c470nBTBU9bYVnRuIbj7sL7Uu3tHObkHcp-lSF5R-J_84u5ytozYOCdihrJ_I_z5ZsluZPlwAxqHevqJzA9HN1RpqzkCuv1_2bio9Uf2zVumb8o-3632ltLrifED-d8iU3UkTwHjU21d0biC6ubBL0n4h_72yZx_r7qT-skMC9lfzHfKPvctdUtpRk2dTMGe-WZVNTQ1gfOptLDo3EN1sPaR0gfz3yZ5dMtXmceVI9jgvll8GXe5ONec72JucSbC7MV4n_3OzZG-4VxSAcaq3s-jcQHSzsWHp1_LfI3tXKed15pO0Zukb8sui631NzeGncVq29EP5n5UlO-7_CAEYt3pbi84NRDdTdie0y-T_--zZFLp9uRXtbC06JGBXPNTLpctdWtpZ4_Ne-Z-RqX0EYBLqbS06NxDdTDxS3TzL_LelzYTHln4jv3y6nB0SWKj53675r5X7uL_dhAjAZNTbW3RuILol2aZ0jfx_l72LS5sIi9y_9EX55dT17P4IdlLqXNj5LNfKf88s_UrNJFsAJqPe5qJzA9FN5zHK_QI5VTa3_IOFWl8PCdihqV00O3ar5e_Lf68s2eWqmwvAJNXbXXRuILqp2Atq5rugTdU5pfWF6Wynbp7MOV2zPSTwbvnvkakXCMCk1dtddG4gulF2U3Pmcf1vs2czuNnZ71iy1Uqfl1-GXe_bpXU1vacq93H_zwpAhHrbi84NRFezF0e7M1v977J3hsZ_edgQ7Fu6RX55djm77PNJGs0ODWU-p-X80qoCEKHe_qJzA9EtzmZCsznY63-Tve-JF8352LZ0ofxy7XJ2g6SFpaV1Lzvub8-V-t9myd54D22-CqBN9TYYnRuIbpHnqjmOWn89e6eouSMh5sfeQNlHz_Xy7Xp2p8r11Pi3EV_P1EsEIFK9DUbnBqIzL1I3byn7v2ru3obxebm6eQhoumwyqINKd4_4WpY-IwDR6u0wOjcQ3cuU-4VxquyadpvCFeNnl5_ZNej1MqfJZIdf7KRMALHqbTE6NxBd5rOhp-po3ffYLsbPDqscI7_sabzZpy1bC0Ab6u0xOjdA0_cxzfxab8yfXSVgk9LU64HGkx1yAdCOenuMzg3Q1Nn90Nn5x7M70dntaOv1QfPLDmMBaE-9TUbnBmh0NnMb2mP3oj9Cfr3Q3LKZGO3-DADaU2-X0bkB8h0qZGG3pu3iLJGZuqO0gwC0rd42o3MDdN_eImTz8NJZ8uuKZtarBSCDetuMzg1Qk12d8BohqxXEIYG59HU1d2UE0L56-4zODVCz83-V0AXPKv1Bfh2S77fifhVAJvU2Gp0bGHo2I-F-Qpc8rPQz-XVJ92bH_XcUgEzq7TQ6NzDkbOf_fKGLbEpmDglM3WsFIJt6O43ODQy120vPELrO1uH18ut3yB0rjvsDGdXbanRuYIjZ_ej3FPpiw9Jp8ut5iF1WWlMAMqq31-jcwNCyaWafKPSN3ajJ5m_o4r0mxpXdXntnAciq3majcwND6obSTkKf7VW6Tn7dD6E3CEBm9TYbnRsYSnaceHthCDYo_UD-OdDnvinuWwFkV2-30bmBIXRVaSthSJYpLSzdLf986FuXi-P-QBfU2250bqDvXVnaQhiqp5WulX9e9CW7lPUJAtAF9fYbnRvoczYT2qbC0D24dKr886MPHSQAXVFvv9G5gb52cWkTAY0-HhI4ubS0AHRFvQ1H5wb62HmlBwnw9lBzWKh-znSt35fWE4Auqbfj6NxA3zpbvDBieuuUTpB_7nQl-xSDuSyA7qm35ejcQJ_6iTgbGjNjH50vVHMSXf08yt7BAtBF9bYcnRvoSz8Wtz7F7O1eukL--ZS174jj_kBX1dtzdG6gD323tKqAuVm7dJz88ypbNp_F-gLQVfU2HZ0b6Hp2JvQqAubH7p53oPIeErDj_k8WgC6rt-vo3ECX-0ZpBQHjs6D0O_nnWtu9QwC6rt6uo3MDXe2Lau4AB4zbWmrm1q-fc231PTXzGADotnrbjs4NdLHPihdETJYdEjigdIf88y8yu7Oh3dwIQPfV23d0bqBrfVTc9QxxHqdmVsn6eRjRPWpubwygH-ptPDo30KU-KHb-iGdzS9j5JvXzcdIdKgB9Um_j0bmBrvQuAe2xQwL_orhDAt8X57gAfVNv59G5gS7EX0LI4rGli-Sfo-PMrkLgXhZA_9TbenRuIHtvEZDL_dVchVI_V8eR3ahocwHoo3p7j84NZM1OgHq1gJzskMBrSrfLP3fn2lmljQWgr-ptPjo3kDGb9Wx_AfltrWbHXT-HZ5PNPvje0koC0Gf1th-dG8iWvRjuI6A77OY8LyydL_98nq5bSx8pbSYAQ1C_BkTnBjJlZ1jvLaCb7LDAgtLhpTNLt8k_xy8vHaPmE651mv8MwEDUrwfRuYEs2Yslk56gT-wNge3kN1FzbH_l-34ZwMDU-73o3ECGbhZ3OgMA9Fu974vODbTdTaU9BABAv9X7v-jcQJvdUNpJAAD0X70PjM4NtNX1am60AgDAENT7wejcQBtdVdpSAAAMR70vjM4NRGdTnT5KAAAMS70_jM4NRPdQAQAwPPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EN1SAgBgWGzfV-8PI7unpDtGfCGylQQAwLCsIr8_jOz2km4a8YXI1hEAAMOynvz-MLIbS7puxBci21QAAAzLw-T3h5FdU9IlI74Q2a4CAGBYdpPfH0Z2UUlnjvhCZPsLAIBhebn8_jCy00s6bsQXIjtMAAAMy3vl94eRHVvSUSO-ENnxAgBgWE6S3x9GdmRJh4z4QmQ3l5YTAADDYPs82_fV-8PIDi7pxSO-EN0uAgBgGHaT3w9Gt29Ju4_4QnRvEwAAw_BO-f1gdP93Bd6aI74Q3YViSmAAQP_Zvs4uwav3g9GtoT-7YsQXo9tRAAD0m_3lXe__ortUi_nWiH8Q3UcFAEC_fVx-_xfd_10CuMjCEf8guttK6wsAgH6y-f9vld__RfcWLeZJI_5BG71HAAD0U9uT_yzKTv7_i9VKd434R9H9SdwdEADQP_YJd9vX_lt3llZW5Ufy_7CNPi0AAPrlc_L7uzY6VSMcLP8P2-geVR9PAADQYY9Xs2-r93dt9CaN8Dj5f9hW52rERxQAAHTMKqXz5fdzbbWtRrhf6XL5f9xWnxUAAN3W9g33Fs-u_59y0r0sZygu6oUCAKCb9pffr7XZtFfa7SD_H7TZ7aUnCwCAbnmCmvlt6v1am22nadhHAxfI_0dt9kdNccwCAICEHlO6UX5_1mbnaAbeIP8ftt3VWsI7FwAAErCdv-2z6v1Y271OM2B3B8z2sYVlkwTtKQAAcrKP_e1T63r_1XZ2OH0tzdAx8t8gQ_bG5EUCACAXO-HPdrT1fitDdiXCjG2pPJMWjOpoNddWAgDQJtsXZf2jeVFba5a-Lf9NMnVeaTcBANAO-8g_0yQ_ozpec2DT8dbfKFv2KYW981pXAADEsBv72GR19T4pY7tqjrJ_CrCom0pHlB4kAAAmY-3SocpxV7-ZdJzmYSf5b5i5W0sfLe2saaY7BABghmxfskvp48p5hdxU2Sfkdo-fefmi_DfuQr8uvV3NXZiWEwAAM7N8aUHpnaWL5PcvXegzGoONSrfIf_MuZR_XnFT6j9LL1Jw8uFlpvdKqAgAMjb322z7A9gV2zpvtGw5Xs6_oykf8U2W__0M0Jm-T_wFERESUrzdrjOzjkLPlfwgRERHl6Vw1--yx2rF0t_wPIyIiovazfbSdBD8R75P_gURERNR-h2mC7GOFs-R_KBEREbXXL0sraMK2UPevCiAiIupLNhneIxXkH-R_ASIiIorvBQr2X_K_BBEREcVl5-aFs9n1TpT_ZYiIiGjy2T54WbVkpdIP5X8pIiIimlw2N88D1DK7O9IF8r8cERERjb_LSxsoCZtz-FL5X5KIiIjG19WlzZXMlqU_yP-yRERENP-uL22tpLZR8-6k_qWJiIho7l2lxDv_RR6h5vhE_csTERHR7LtSzSR8nfAgMWUwERHRfPtVaUN1zKqlb8k_GCIiIlpyJynBpX5zZRMUfED-QREREdHU2Qx_rU3yM07PK90s_wCJiIjo3m4t7aeesTsV_VT-wRIREVFzS99Hq6eWLx1eulv-gRMREQ0x2ycepmYf2Xs7qZnHuF4IREREQ8qm0t9NA2PvdBaWbpFfIERERH3Ozot7s5o76w6WzRlwVOke-QVERETUt75e2kj4ix3U3Nu4XlBERER96PjS44QpLRBvBIiIqD_Zjn9XYcbspgdHl26XX5hERESZs32XHd7eSpiz1UsvLf1CfgETERFl6rzSgaW1hbF6lJorB34tv9CJiIja6NLSEaVdSksJE2ULeDs1l1D8oHSn_AohIiKaRLbPObX0ptK2YqffqlVKe5QOLh1bukx-hREREc0l-wvf9i1vLT2htLKQ2hqlx5deoOawwadL3yydXvpN6fo_x7TERETD6y7dux-wfYLtG2wfcaSafca-as7ct_PQeun_A4fQT7pKk6rlAAAAAElFTkSuQmCC";
                pst.setString(8, imag2);
            } else {
                String conv = ImageEnTexte(image);
                pst.setString(8, conv);
            }
            pst.setInt(9, prixBase);
            pst.executeUpdate();
            con.commit();
            System.out.println("objet créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static void demandeNouvelObjet(Connection con) throws SQLException, FileNotFoundException {
        String titre = ConsoleFdB.entreeString("titre ?");
        Timestamp debut = new Timestamp(System.currentTimeMillis());
        Timestamp fin = new Timestamp(2022, 12, 25, 0, 0, 0, 0);
        int prixbase = ConsoleFdB.entreeInt("prix?");
        int categorie = ConsoleFdB.entreeInt("categorie ?");
        int proposerpar = ConsoleFdB.entreeInt("proposer par id de qui?");
        String bio = ConsoleFdB.entreeString("bio?");
        double lat = ConsoleFdB.entreeDouble("lat");
        double longitude = ConsoleFdB.entreeDouble("longitude");
        createObjet(con, titre, debut, fin, prixbase, categorie, proposerpar, bio, null);
        System.out.println("objet est bien entrée");
    }

    public static void recreeTout(Connection con) throws SQLException, ClassNotFoundException, FileNotFoundException {
        deleteSchema(con);
        creeSchema(con);
    }

    public static void menu(Connection con) throws FileNotFoundException, IOException, ClassNotFoundException {
        int rep = -1;
        while (rep != 0) {
            System.out.println("Menu BdD Aime");
            System.out.println("=============");
            System.out.println("1) créer/recréer la BdD initiale");
            System.out.println("2) liste des utilisateurs");
            System.out.println("4) ajouter un utilisateur");
//            System.out.println("5) ajouter un lien 'Aime'");
            System.out.println("6) ajouter objet");
            System.out.println("0) quitter");
            System.out.println("5) Supprimer table");
            System.out.println("9) Création base");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                if (rep == 1) {
                    recreeTout(con);
                    creationBase(con);
                } else if (rep == 2) {
                    afficheTousLesUtilisateur(con);
                } else if (rep == 3) {
                    String lenom = ConsoleFdB.entreeString("donnez un nom");
                    afficheUtilisateurParNom(con, lenom);
                } else if (rep == 4) {
                    demandeNouvelUtilisateur(con);
                } else if (rep == 5) {
                    deleteSchema(con);
                } else if (rep == 6) {
                    demandeNouvelObjet(con);
                } else if (rep == 7) {
                    creerCategorie(con);
                } else if (rep == 8) {
                    demandeEnchere(con);
                    IdencheresSurObjet(1);
                } else if (rep == 9) {
                    try {
                        creationBase(con);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(BDD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (rep == 10) {
                    System.out.println(CalculDistance(-73.984, 40.76, -77.032, 38.89));
                } else if (rep == 11) {
                    
                    TreeMap map =new TreeMap();
                    map.put(Double.valueOf(48.39),3);
                    map.put(Double.valueOf(28.39),5);
                    map.put(Double.valueOf(23.9),1);
                    map.put(Double.valueOf(8.39),4);
                    map.put(Double.valueOf(18.39),2);
                    Set set = map.entrySet();
                    Iterator it = set.iterator();
                    ArrayList<Integer> List = new ArrayList<>();
                    while (it.hasNext()) {
                        Map.Entry me = (Map.Entry) it.next();
                        List.add((Integer) me.getValue());
                        
                    }
                    for (int i =0; i<5;i++){
                            System.out.println(List.get(i));
                        }
                    
                }
//                else if (rep == 5) {
//                    demandeNouvelAime(con);
//                } else if (rep == 6) {
//                    System.out.println("création d'utilisateurs 'aléatoires'");
//                    int combien = ConsoleFdB.entreeEntier("combien d'utilisateur : ");
//                    for (int i = 0; i < combien; i++) {
//                        boolean exist = true;
//                        while (exist) {
//                            String nom = "U" + ((int) (Math.random() * 10000));
//                            try{
//                                createUtilisateur(con, nom, "P" + ((int) (Math.random() * 10000)));
//                                exist = false;
//                            }
//                            catch (NomExisteDejaException ex) {
//                            }
//                        }
//
//                    }
//                }
            } catch (SQLException exe) {
                throw new Error(exe);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        try ( Connection con = defautConnect()) {

            System.out.println("Connection ok!");
            menu(con);
            System.out.println("user créé");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BDD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int creerCategorie(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 1);
            pst.setString(2, "meuble");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int createEnchere(Connection con, int sur, int de, int montant) throws SQLException, ClassNotFoundException {
        con.setAutoCommit(false);
        int prixObjetActuel = 0;
        try ( PreparedStatement pst2 = con.prepareStatement("select prixactuel from objet where id = ?")) {
            pst2.setInt(1, sur);
            ResultSet res2 = pst2.executeQuery();

            while (res2.next()) {
                prixObjetActuel = res2.getInt("prixactuel");
            }
        }
        System.out.println(prixObjetActuel);
        if (montant > prixObjetActuel) {
            try ( PreparedStatement pst = con.prepareStatement(
                    "insert into enchere(sur,de,montant) values (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setInt(1, sur);
                pst.setInt(2, de);
                pst.setInt(3, montant);
                pst.executeUpdate();
                con.commit();
                System.out.println("enchere créé");
                miseAjourPrix(montant, sur);
                try ( ResultSet rid = pst.getGeneratedKeys()) {
                    rid.next();
                    int id = rid.getInt(1);

                    return id;
                } finally {
                    con.setAutoCommit(true);
                }
            }
        } else {
            return -1;
        }

    }

    public static void demandeEnchere(Connection con) throws SQLException, ClassNotFoundException {
        int sur = ConsoleFdB.entreeInt("quel id objet ?");
        int de = ConsoleFdB.entreeInt("De QUI ?");
        int montant = ConsoleFdB.entreeInt("montannt");
        createEnchere(con, sur, de, montant);
        System.out.println("demande enchere faite");
    }

    public static String ImageEnTexte(Image img) {

        BufferedImage bufi = SwingFXUtils.fromFXImage(img, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufi, "png", baos);
        } catch (IOException ex) {
            throw new Error("pb conv image ; ne devrait pas arriver");
        }
        byte[] bytes = baos.toByteArray();
        String ImageTexte = Base64.getUrlEncoder().encodeToString(bytes);
        return ImageTexte;
    }

    public static Image texteEnImage(String img) throws IOException {
        byte[] result = Base64.getUrlDecoder().decode(img);
        ByteArrayInputStream bis = new ByteArrayInputStream(result);
        BufferedImage bImage2 = ImageIO.read(bis);
        Image Final = SwingFXUtils.toFXImage(bImage2, null);
        return Final;
    }

    public static int creerCategorie1Voitures(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 1);
            pst.setString(2, "Voitures");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie2Motos(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 2);
            pst.setString(2, "Motos");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie3Caravaing(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 3);
            pst.setString(2, "Caravaing");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie4Appartement(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 4);
            pst.setString(2, "Appartement");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie5Maison(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 5);
            pst.setString(2, "Maison");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie6Terrain(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 6);
            pst.setString(2, "Terrain");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie7Vetements(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 7);
            pst.setString(2, "Vetements");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie8Chaussures(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 8);
            pst.setString(2, "Chaussures");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie9MontreEtBijou(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 9);
            pst.setString(2, "Montre & Bijoux");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie10Accesoires(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 10);
            pst.setString(2, "Accessoires");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie11Ameublement(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 11);
            pst.setString(2, "Ameublement");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie12Electromenager(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 12);
            pst.setString(2, "Electroménager");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie13Decorations(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 13);
            pst.setString(2, "Décoration");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie14Bricolage(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 14);
            pst.setString(2, "Bricolage");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie15Jardinage(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 15);
            pst.setString(2, "Jardinage");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie16Info(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 16);
            pst.setString(2, "Informatique");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie17ConsoleEtJeux(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 17);
            pst.setString(2, "Consoles & jeux vidéo");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie18Image(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 18);
            pst.setString(2, "Image & Son");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCtaegorie19Telephonie(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 19);
            pst.setString(2, "Téléphonie");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie20DVDCD(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 20);
            pst.setString(2, "DVD & CD");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie21Livres(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 21);
            pst.setString(2, "Livres");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie22Velos(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 22);
            pst.setString(2, "Vélos");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie23SportsHobbies(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 23);
            pst.setString(2, "Sports & Hobbies");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int creerCategorie24Jeux(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, 24);
            pst.setString(2, "Jeux & jouets");
            pst.executeUpdate();
            con.commit();
            System.out.println("categorie créé");
            try ( ResultSet rid = pst.getGeneratedKeys()) {
                // et comme ici je suis sur qu'il y a une et une seule clé, je
                // fait un simple next 
                rid.next();
                // puis je récupère la valeur de la clé créé qui est dans la
                // première colonne du ResultSet
                int id = rid.getInt(1);
                return id;
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static void creationBase(Connection con) throws ClassNotFoundException, SQLException, FileNotFoundException {
        //Connection con = defautConnect(); 
        creerCategorie1Voitures(con);
        creerCategorie2Motos(con);
        creerCategorie3Caravaing(con);
        creerCategorie4Appartement(con);
        creerCategorie5Maison(con);
        creerCategorie6Terrain(con);
        creerCategorie7Vetements(con);
        creerCategorie8Chaussures(con);
        creerCategorie9MontreEtBijou(con);
        creerCategorie10Accesoires(con);
        creerCategorie11Ameublement(con);
        creerCategorie12Electromenager(con);
        creerCategorie13Decorations(con);
        creerCategorie14Bricolage(con);
        creerCategorie15Jardinage(con);
        creerCategorie16Info(con);
        creerCategorie17ConsoleEtJeux(con);
        creerCategorie18Image(con);
        creerCtaegorie19Telephonie(con);
        creerCategorie20DVDCD(con);
        creerCategorie21Livres(con);
        creerCategorie22Velos(con);
        creerCategorie23SportsHobbies(con);
        creerCategorie24Jeux(con);
        createUtilisateur(con, "Auvray", "jojo", "Nicolas", "nicolas.auvray@gmail.com", null, 48.69060664200385, 7.579645983737555);
        createUtilisateur(con, "test", "test", "test", "test", null, 48.69060664200385, 7.579645983737555);
        createUtilisateur(con, "Beauquis", "jojo", "Dorian", "dorian.beauquis@insa-strasbourg.fr", null, 48.69060664200385, 7.579645983737555);
        createUtilisateur(con, "Lenglart", "jojo", "Louis", "louislebg@gmail.com", null, 48.69060664200385, 7.579645983737555);
        createUtilisateur(con, "Mariannie", "jojo", "Alexandra", "alex.mariannie@hotmail.com", null, 48.69060664200385, 7.579645983737555);
        createUtilisateur(con, "Lareyre", "jojo", "Jean-Laurent", "jlaurent.lareyre@gmail.com", null, 48.69060664200385, 7.579645983737555);
        createUtilisateur(con, "Espinola", "jojo", "Sophia", "sophia.espi@outlook.fr", null, 48.69060664200385, 7.579645983737555);
        createUtilisateur(con, "Doucet", "jojo", "Baptiste", "ladoucette@gmail.com", null, 48.69060664200385, 7.579645983737555);
        createUtilisateur(con, "Zerr", "jojo", "Jules", "jules.zerr@gmail.com", null, 48.69060664200385, 7.579645983737555);
//        Timestamp cur = new Timestamp(System.currentTimeMillis());
//        LocalDateTime ldt = LocalDateTime.now();
//        LocalDateTime ldt2 = ldt.plusDays(7);
//        Timestamp conv = Timestamp.valueOf(ldt2);
//        LocalDateTime ldt3 = LocalDateTime.of(2022, Month.MARCH, 12, 0, 0);
//        long diff = ldt.until(ldt3, ChronoUnit.MINUTES);
        Timestamp conv = Timestamp.valueOf(LocalDateTime.of(2022, 12, 19, 16, 8));
        Timestamp conv1 = Timestamp.valueOf(LocalDateTime.of(2022, 12, 19, 16, 8).plusDays(45));
        Timestamp conv2 = Timestamp.valueOf(LocalDateTime.of(2022, 12, 19, 16, 8).plusDays(9));
        createObjet(con, "Babouches", conv, conv1, 10, 24, 1, "S'est facile à utliser", null);
        createObjet(con, "Vélo Tout Terrain", conv, conv1, 100, 23, 1, "Vélo tout neuf", null);
        createObjet(con, "Collier plaqué or", conv, conv1, 1000, 9, 1, "Bijou de ma grand-mère décédée", null);
        createObjet(con, "Princesse de Clèves", conv, conv1, 3, 21, 1, "Livre utilisé au lycée. Livre très interessant", null);
        createObjet(con, "Micro ondes", conv, conv1, 70, 12, 1, "Marque Samsung - état neuf", null);
        createObjet(con, "AIRFORCE 1 Blanc", conv, conv1, 110, 2, 1, "Taille 43 - propre - etat neuf", null);
        createObjet(con, "Grande Pelle", conv, conv2, 15, 15, 1, "Bien utile dans cette période pour votre jardin", null);

    }

    public static long getHeuresRestantes(Timestamp fin) {
        LocalDateTime debut1 = LocalDateTime.now();
        LocalDateTime fin1 = fin.toLocalDateTime();
        long diffHeures = fin1.until(debut1, ChronoUnit.HOURS);
        while (diffHeures >= 8760) {
            diffHeures = diffHeures - 8760;
        }
        while (diffHeures >= 730) {
            diffHeures = diffHeures - 730;
        }
        while (diffHeures >= 24) {
            diffHeures = diffHeures - 24;
        }
        return diffHeures;
    }

    public static long getMinutesRestantes(Timestamp fin) {
        LocalDateTime debut1 = LocalDateTime.now();
        LocalDateTime fin1 = fin.toLocalDateTime();
        long diffMinutes = fin1.until(debut1, ChronoUnit.MINUTES);
        while (diffMinutes >= 525600) {
            diffMinutes = diffMinutes - 525600;
        }
        while (diffMinutes >= 43800) {
            diffMinutes = diffMinutes - 43800;
        }
        while (diffMinutes >= 1440) {
            diffMinutes = diffMinutes - 1440;
        }
        while (diffMinutes >= 60) {
            diffMinutes = diffMinutes - 60;
        }
        return diffMinutes;
    }

    public static long getJoursRestants(Timestamp fin) {
        LocalDateTime debut1 = LocalDateTime.now();
        LocalDateTime fin1 = fin.toLocalDateTime();
        long diffJours = fin1.until(debut1, ChronoUnit.MINUTES);
        while (diffJours >= 365) {
            diffJours = diffJours - 365;
        }
        while (diffJours >= 30) {
            diffJours = diffJours - 30;
        }
        return diffJours;
    }

    public static long getSecondesRestantes(Timestamp fin) {
        LocalDateTime debut1 = LocalDateTime.now();
        LocalDateTime fin1 = fin.toLocalDateTime();
        long diffSecondes = fin1.until(debut1, ChronoUnit.SECONDS);
        while (diffSecondes >= 31536008) {
            diffSecondes = diffSecondes - 31536008;
        }
        while (diffSecondes >= 2628003) {
            diffSecondes = diffSecondes - 2628003;
        }
        while (diffSecondes >= 86400) {
            diffSecondes = diffSecondes - 86400;
        }
        while (diffSecondes >= 3600) {
            diffSecondes = diffSecondes - 3600;
        }
        while (diffSecondes >= 60) {
            diffSecondes = diffSecondes - 60;
        }
        return diffSecondes;
    }
//    FileInputStream input = new FileInputStream("resources/images/iconmonstr-home-6-48.png");
    //InputStream is = this.getClass().getResourceAsStream("ressources/addImage.png"); --> ca marche mieux ca
//Image image = new Image(input);
//ImageView imageView = new ImageView(image);

    public static void miseAjourPrix(int Prix, int idObjet) throws ClassNotFoundException, SQLException {
        Connection con = defautConnect();
        con.setAutoCommit(false);
        try ( PreparedStatement pst2 = con.prepareStatement("update objet set prixactuel = ? where id = ?")) {
            pst2.setInt(1, Prix);
            pst2.setInt(2, idObjet);
            pst2.executeUpdate();
            con.commit();
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int idUtilDerniereEnchere(int idObjet) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        try ( PreparedStatement st = con.prepareStatement("select de from enchere where montant=(select max(montant) from enchere where sur = ?)")) {
            st.setInt(1, idObjet);
            ResultSet res = st.executeQuery();
            int IdUtil = 0;
            while (res.next()) {
                IdUtil = res.getInt("de");
            }
            System.out.println(IdUtil);
            return IdUtil;
        }
    }

    public static ArrayList<Integer> IdencheresSurObjet(int idObjet) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select id from enchere where sur = 1 order by montant desc")) {
            st.setInt(1, idObjet);
            ResultSet res = st.executeQuery();
            int IdUtil = 0;
            while (res.next()) {
                List.add(res.getInt("id"));
                System.out.println("Valeurs" + res.getInt("id"));
            }
            System.out.println(IdUtil);
            return List;
        }
    }
//retourne la liste des id de enchere fait sur un objet

    public static ArrayList<Integer> EnchereParUtilisateur(int idUtil) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select sur from enchere where de = ? ")) {
            st.setInt(1, idUtil);
            ResultSet res = st.executeQuery();
            int IdUtil = 0;
            while (res.next()) {
                List.add(res.getInt("sur"));
                System.out.println("Valeurs" + res.getInt("sur"));
            }
            System.out.println(IdUtil);
            return List;
        }
    }

    public static boolean ValiditeDateEnchere(int idObjet) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        try ( PreparedStatement st = con.prepareStatement("select fin from objet where id = ? ")) {
            st.setInt(1, idObjet);
            ResultSet res = st.executeQuery();
            Timestamp fin2 = null;
            while (res.next()) {
                fin2 = res.getTimestamp("fin");
            }
            LocalDateTime fin3 = fin2.toLocalDateTime();
            return LocalDateTime.now().isAfter(fin3);
        }
    }
    //revoit vrai si la date n'est pas passé et renvoit faux si l'enchere n'est plus valide: 

    public static ArrayList<Integer> EnchereParUtilisateurPlusHautEnCours(int idUtil) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select sur,id from enchere where de = ? ")) {
            st.setInt(1, idUtil);
            ResultSet res = st.executeQuery();
            int IdUtil = 0;
            while (res.next()) {
                int resultat = res.getInt("sur");
                if (ValiditeDateEnchere(resultat) == true) {
                    List.add(res.getInt("id"));
                    System.out.println("Valeurs" + res.getInt("sur"));
                } else {
                }
            }
            System.out.println(IdUtil);
            return List;
        }
    }

    public static ArrayList<Integer> EnchereParUtilisateurPlusHautFini(int idUtil) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select sur,id from enchere where de = ? ")) {
            st.setInt(1, idUtil);
            ResultSet res = st.executeQuery();
            int IdUtil = 0;
            while (res.next()) {
                int resultat = res.getInt("sur");
                if (ValiditeDateEnchere(resultat) == false) {
                    List.add(res.getInt("id"));
                    System.out.println("Valeurs" + res.getInt("sur"));
                } else {
                }
            }
            System.out.println(IdUtil);
            return List;
        }
    }

    public static double DistanceObjetFromUtiilisateur(int idUtilisateur, int IdObjet) throws SQLException, ClassNotFoundException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        double distance;
        double Utlong = 0;
        double Utlat = 0;
        double Objlong = 0;
        double Objlat = 0;
        try ( PreparedStatement st = con.prepareStatement("select long,lat from utilisateur where id=? ")) {
            st.setInt(1, idUtilisateur);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                Utlong = res.getInt("long");
                Utlat = res.getInt("lat");
            }
        }
        try ( PreparedStatement st = con.prepareStatement("select long,lat from objet where id=? ")) {
            st.setInt(1, idUtilisateur);
            ResultSet res2 = st.executeQuery();
            while (res2.next()) {
                Objlat = res2.getInt("long");
                Objlat = res2.getInt("lat");
            }
        }
        return CalculDistance(Utlong, Utlat, Objlong, Objlat);

    }

    public static int CalculDistance(double Utlong, double Utlat, double Objlong, double Objlat) {
        double value = Math.sin(Math.toRadians(Utlat)) * Math.sin(Math.toRadians(Objlat));
        double value2 = Math.cos(Math.toRadians(Objlat)) * Math.cos(Math.toRadians(Utlat)) * Math.cos(Math.toRadians(Utlong - Objlong));
        double arccos = Math.acos(value + value2);
        double distance = 6371 * arccos;
        int distancerenvoye = (int) Math.round(distance);
        return distancerenvoye;
    }//renvoit une distance en kilometres

    public static ArrayList<Integer> TriDistanceObjet(int idUtil) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        TreeMap<Double, Integer> map = new TreeMap();

        try ( PreparedStatement st = con.prepareStatement("select id from objet")) {
            ResultSet res = st.executeQuery();

            while (res.next()) {
                map.put(DistanceObjetFromUtiilisateur(idUtil, res.getInt("id")), res.getInt("id"));
            }
        }

        Set set = map.entrySet();
        Iterator it = set.iterator();
        ArrayList<Integer> List = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            List.add((Integer) me.getValue());
        }

        return List;
    //tri du plus proche au plus éloigné 
    }
    
    public static ArrayList<Integer> EncheresUtilisateurFini(int idUtil) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select id,sur from enchere where de = ? ")) {
            st.setInt(1, idUtil);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                int resultat = res.getInt("sur");
                if (ValiditeDateEnchere(resultat) == false) {
                    List.add(res.getInt("id"));
                    System.out.println("Valeurs" + res.getInt("sur"));
                } 
            }
            return List;
        }
    } // renvoit l'id des encheres que l'utilsateurs a fait qui sont fini
    
    public static ArrayList<Integer> EncheresUtilisateurEnCours(int idUtil) throws ClassNotFoundException, SQLException {
        Connection con = connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
        ArrayList<Integer> List = new ArrayList<>();
        try ( PreparedStatement st = con.prepareStatement("select id,sur from enchere where de = ? ")) {
            st.setInt(1, idUtil);
            ResultSet res = st.executeQuery();
            int IdUtil = 0;
            while (res.next()) {
                int resultat = res.getInt("sur");
                if (ValiditeDateEnchere(resultat) == true) {
                    List.add(res.getInt("id"));
                    System.out.println("Valeurs" + res.getInt("sur"));
                } 
            }
            System.out.println(IdUtil);
            return List;
        }
    }
    
     //retourne la liste des objets par rapport à la recherche des objets en cours de validité 
    
}
