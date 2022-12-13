/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.gestionBDD;

import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.strasbourg.zerr.projetEnchere.model.Utilisateur;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            throws SQLException {
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
                        codepostal varchar(20)
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
                            de integer not null
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
                            proposerpar integer not null
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
//            st.executeUpdate(
//                    """
//                    create table aime1 (
//                        u1 integer not null,
//                        u2 integer not null
//                    )
//                    """);
            // je defini les liens entre les clés externes et les clés primaires
            // correspondantes
//            st.executeUpdate(
//                    """
//                    alter table aime
//                        add constraint fk_aime_u1
//                        foreign key (u1) references utilisateur(id)
//                    """);
//            st.executeUpdate(
//                    """
//                    alter table aime
//                        add constraint fk_aime_u2
//                        foreign key (u2) references utilisateur(id)
//                    """);
            // si j'arrive jusqu'ici, c'est que tout s'est bien passé
            // je confirme (commit) la transaction
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
                String mail  = res.getString("email");
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

    public static int createUtilisateur(Connection con, String nom, String pass, String prenom, String email)
            throws SQLException {
        // lors de la creation du PreparedStatement, il faut que je précise
        // que je veux qu'il conserve les clés générées
        con.setAutoCommit(false);
        try ( PreparedStatement pst2 = con.prepareStatement(
                "select id from utilisateur where nom = ?")) {
            pst2.setString(1, nom);
            ResultSet existe = pst2.executeQuery();
            if (!existe.next()) {
                try ( PreparedStatement pst = con.prepareStatement(
                        " insert into utilisateur (nom,pass,prenom,email) values (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pst.setString(1, nom);
                    pst.setString(2, pass);
                    pst.setString(3, prenom);
                    pst.setString(4, email);
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
        int id = createUtilisateur(con, nom, pass,prenom,email);
        if (id == -1) {
            System.out.println("utilisateur existe déjà");
        } else {
            System.out.println("utilisateur N° " + id + "créé");
        }
    }
    
    public static int createObjet(Connection con, String titre, Timestamp debut, Timestamp fin, int prixBase, int categorie, int proposerpar) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                            "insert into objet (titre,debut,fin,prixbase,categorie,proposerpar) values (?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)){
            pst.setString(1, titre);
            pst.setTimestamp(2, debut);
            pst.setTimestamp(3, fin);
            pst.setInt(4, prixBase);
            pst.setInt(5, categorie);
            pst.setInt(6, proposerpar);
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
        }
        finally {
            con.setAutoCommit(true);
        }
    }
    public static void demandeNouvelObjet(Connection con) throws SQLException{
        String titre = ConsoleFdB.entreeString("titre ?");
        Timestamp debut = new Timestamp(System.currentTimeMillis());
        Timestamp fin= new Timestamp(2022, 12, 25, 0, 0, 0, 0);
        int prixbase = ConsoleFdB.entreeInt("prix?");
        int categorie = ConsoleFdB.entreeInt("categorie ?");
        int proposerpar = ConsoleFdB.entreeInt("proposer par id de qui?");
        createObjet(con, titre, debut, fin, prixbase, categorie, proposerpar);
        System.out.println("objet est bien entrée");
    }

    public static void recreeTout(Connection con) throws SQLException {
        deleteSchema(con);
        creeSchema(con);
    }

    public static void menu(Connection con) {
        int rep = -1;
        while (rep != 0) {
            System.out.println("Menu BdD Aime");
            System.out.println("=============");
            System.out.println("1) créer/recréer la BdD initiale");
            System.out.println("2) liste des utilisateurs");
//            System.out.println("3) liste des liens 'Aime'");
            System.out.println("4) ajouter un utilisateur");
//            System.out.println("5) ajouter un lien 'Aime'");
            System.out.println("6) ajouter objet");
            System.out.println("0) quitter");
            System.out.println("5) Supprimer table");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                if (rep == 1) {
                    recreeTout(con);
                } else if (rep == 2) {
                    afficheTousLesUtilisateur(con);
                } else if (rep == 3) {
                    String lenom = ConsoleFdB.entreeString("donnez un nom");
                    afficheUtilisateurParNom(con, lenom);
                } else if (rep == 4) {
                    demandeNouvelUtilisateur(con);
                } else if (rep == 5) {
                    deleteSchema(con);
                }
                else if (rep == 6){
                    demandeNouvelObjet(con);
                }
                else if (rep==7){
                    creerCategorie(con); 
                }
                else if(rep==8){
                    demandeEnchere(con);
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

    public static void main(String[] args) {
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
                            "insert into categorie (id,nom) values (?,? )", PreparedStatement.RETURN_GENERATED_KEYS)){
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
        }
        finally {
            con.setAutoCommit(true);
        }
    }
    
    public static int createEnchere(Connection con, int sur, int de) throws SQLException{
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                            "insert into enchere(sur,de) values (?,?)", PreparedStatement.RETURN_GENERATED_KEYS)){
            pst.setInt(1, sur);
            pst.setInt(2, de);
            pst.executeUpdate();
            con.commit();
            System.out.println("enchere créé");
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
        finally {
            con.setAutoCommit(true);
        }
    }
    
    public static void demandeEnchere(Connection con) throws SQLException{
        int sur = ConsoleFdB.entreeInt("quel id objet ?");
        int de = ConsoleFdB.entreeInt("De QUI ?");
            createEnchere(con, sur, de);
            System.out.println("demande enchere faite");
    }

}
