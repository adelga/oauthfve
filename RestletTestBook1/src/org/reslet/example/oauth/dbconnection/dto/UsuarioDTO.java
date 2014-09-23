/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.reslet.example.oauth.dbconnection.dto;

/**
 *
 * @author fve
 */
public class UsuarioDTO {
    
    private int id_user;

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
    private String id;
    private String password;
    private String status;

    public UsuarioDTO(){
    this.id = "";
    this.password = "";
    this.status = "";
    };


    public UsuarioDTO(String id) {
        this.id = id;
    }
     public UsuarioDTO(String id,String password, String status) {
        this.id = id;
        this.password = password;
        this.status = status;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isEmpty(){
    if(id.isEmpty() || id == null || password.isEmpty() || password == null){
        return true;}
    else{return false;}
    }

       @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UsuarioDTO other = (UsuarioDTO) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
