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

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.example.ext.oauth.server.external.ExternalApplication;
import org.restlet.example.ext.oauth.server.oauth.OAuth2ServerApplication;
import org.restlet.example.ext.oauth.server.oauth.UsuarioManager;
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

  //  userManager.addUser("Alberto").setPassword("123456".toCharArray());
   // userManager.addUser("bob").setPassword("123456".toCharArray());

    clientManager = new MemoryClientManager();
    Client client =clientManager.createClient("b8d1cfda-0a13-4072-ab8f-ae3494b2e028", "4RI419iBLqXTEiItKgZUcwm3F/Q=".toCharArray(), ClientType.PUBLIC, new String[] { "http://localhost:5050/sample/popup" }, null);
 



    

        System.out.println("SampleClient: client_id=" + client.getClientId() + ", client_secret="
        + String.copyValueOf(client.getClientSecret()));

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
    component.getServers().add(Protocol.HTTP, 5052);

    component.getDefaultHost().attach("/sample", new ExternalApplication());

    OAuth2ServerApplication app = new OAuth2ServerApplication();
    component.getDefaultHost().attach("/oauth", app);
    component.getInternalRouter().attach("/oauth", app);

    component.start();
  }
}
