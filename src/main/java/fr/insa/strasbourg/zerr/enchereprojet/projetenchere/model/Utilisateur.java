/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.enchereprojet.projetenchere.model;

/**
 *
 * @author jules
 */
public class Utilisateur {
     private final int id;
    private String nom;
    private String pass;
    private String nomRole;

    public Utilisateur(int id, String nom, String pass, String nomRole) {
        this.id = id;
        this.nom = nom;
        this.pass = pass;
        this.nomRole = nomRole;
    }

    @Override
    public String toString() {
        return "Utilisateur{" + "id=" + id + ", nom=" + nom 
                + ", nomRole=" + nomRole + '}';
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPass() {
        return pass;
    }

    public String getNomRole() {
        return nomRole;
    }
}
