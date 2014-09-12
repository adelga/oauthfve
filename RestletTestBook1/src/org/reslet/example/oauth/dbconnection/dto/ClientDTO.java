/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.reslet.example.oauth.dbconnection.dto;

/**
 *
 * @author adelga
 */
public class ClientDTO {
    private String client_id;
    private String client_secret;
    private String name;

    public ClientDTO(String client_id, String client_secret, String name) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.name = name;
    }

    public ClientDTO() {
       
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
