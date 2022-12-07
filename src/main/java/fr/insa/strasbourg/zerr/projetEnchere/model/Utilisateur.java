/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.model;

/**
 *
 * @author jules
 */
public class Utilisateur {
    private final int id;
    private String email;
    private String pass;
   // private String nomRole;

    public Utilisateur(int id, String email, String pass) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        //this.nomRole = nomRole;
    }

    @Override
    public String toString() {
        return "Utilisateur{" + "id=" + id + ", nom=" + email +"}";
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
