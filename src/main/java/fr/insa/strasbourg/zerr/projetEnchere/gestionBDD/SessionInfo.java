/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.gestionBDD;

import fr.insa.strasbourg.zerr.projetEnchere.model.Utilisateur;
import java.sql.Connection;
import java.util.Optional;

/**
 *
 * @author jules
 */
public class SessionInfo {
    private Optional<Utilisateur> curUser;
    private Connection conBdD;

    public SessionInfo() {
        this.curUser = Optional.empty();
        this.conBdD = null;
    }

    public boolean userConnected() {
        return this.curUser.isPresent();
    }

    public Optional<Utilisateur> getCurUser() {
        return this.curUser;
    }

    public void setCurUser(Optional<Utilisateur> curUser) {
        this.curUser = curUser;
    }
    
//    @Override
//    public String toString(){
//      return this.get;  
//    }

    public int getUserID() {
        return this.curUser.orElseThrow().getId();
    }

    /**
     * @return the conBdD
     */
    public Connection getConBdD() {
        return conBdD;
    }

    /**
     * @param conBdD the conBdD to set
     */
    public void setConBdD(Connection conBdD) {
        this.conBdD = conBdD;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return this.curUser.orElseThrow().getEmail();
    }
    public String getUserNomPrenom() {
        return this.curUser.orElseThrow().getNomPrenom();
    }
}
