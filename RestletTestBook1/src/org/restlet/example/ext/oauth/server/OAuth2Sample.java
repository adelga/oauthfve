/**
 * Copyright 2005-2013 Restlet S.A.S.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.restlet.com/products/restlet-framework
 * 
 * Restlet is a registered trademark of Restlet S.A.S.
 */
package org.restlet.example.ext.oauth.server;

import java.util.ArrayList;
import org.reslet.example.oauth.dbconnection.dao.ClientDAO;
import org.reslet.example.oauth.dbconnection.dao.GenericDAO;
import org.reslet.example.oauth.dbconnection.dao.UsuarioDAO;
import org.reslet.example.oauth.dbconnection.dto.ClientDTO;
import org.reslet.example.oauth.dbconnection.dto.UsuarioDTO;
import org.reslet.example.oauth.dbconnection.model.UsuarioManager;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.example.ext.oauth.server.external.ExternalApplication;
import org.restlet.example.ext.oauth.server.oauth.OAuth2ServerApplication;
import org.restlet.example.ext.oauth.util.Network;
import org.restlet.ext.oauth.internal.Client;
import org.restlet.ext.oauth.internal.Client.ClientType;
import org.restlet.ext.oauth.internal.ClientManager;
import org.restlet.ext.oauth.internal.TokenManager;
import org.restlet.ext.oauth.internal.memory.MemoryClientManager;
import org.restlet.ext.oauth.internal.memory.MemoryTokenManager;

/**
 * 
 * @author Alberto Delgado 
 */
public class OAuth2Sample {

  private static UsuarioManager userManager;
  private static MemoryClientManager clientManager;
  private static TokenManager tokenManager;

  public static UsuarioManager getSampleUserManager() {
    return userManager;
  }

  public static ClientManager getClientManager() {
    return clientManager;
  }

  public static TokenManager getTokenManager() {
    return tokenManager;
  }

  public static void main(String[] args) throws Exception {
    userManager = new UsuarioManager();

    clientManager = new MemoryClientManager();
//    UsuarioDTO user = new UsuarioDTO("alberto");
//    user.setPassword("123456");
//     UsuarioDAO.insertUsuario(GenericDAO.getConnection(),user);
      String[] uris= ClientDAO.getRedirectsURIsByIdClient(GenericDAO.getConnection(), "b8d1cfda-0a13-4072-ab8f-ae3494b2e028");
     
     
    //Create the client for example web app
      ClientDTO clientdto= ClientDAO.getClientbyName(GenericDAO.getConnection(),"Client SAMPLE");
      
      ArrayList<ClientDTO> clientes= ClientDAO.getAllClients(GenericDAO.getConnection());
      Client client = null;
      for(int i=0; i<clientes.size(); i++){
          clientdto=clientes.get(i);
     client =clientManager.createClient(clientdto.getClient_id(), clientdto.getClient_secret().toCharArray(), ClientType.PUBLIC,ClientDAO.getRedirectsURIsByIdClient(GenericDAO.getConnection(), clientdto.getClient_id()) , null);
      }



    

//        System.out.println("SampleClient: client_id=" + client.getClientId() + ", client_secret="
//        + String.copyValueOf(client.getClientSecret()));

    ExternalApplication.clientID = client.getClientId();
    ExternalApplication.clientSecret = String.valueOf(client.getClientSecret());

    tokenManager = new MemoryTokenManager();

    // Setup Restlet
    Component component = new Component();
    component.getClients().add(Protocol.HTTP);
    component.getClients().add(Protocol.HTTPS);
    component.getClients().add(Protocol.RIAP);
    component.getClients().add(Protocol.CLAP);
    component.getServers().add(Protocol.HTTP, 5050);
    component.getServers().add(Protocol.HTTPS, 4040);

    component.getServers().add(Protocol.HTTP, 5052);

    component.getDefaultHost().attach("/sample", new ExternalApplication());

    OAuth2ServerApplication app = new OAuth2ServerApplication();
    component.getDefaultHost().attach("/oauth", app);
    component.getInternalRouter().attach("/oauth", app);

    component.start();
  }
}
