
/*
 * Copyright (c) 2014 , Fundacion Vodafone Espa�a
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. Neither the name of copyright holders nor the names of its
   contributors may be used to endorse or promote products derived
   from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
''AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDERS OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE. BESIDES THIS, IN NO EVENT FUNDACION VODAFONE ESPA�A
SHALL BE LIABLE FOR THE REDISTRIBUTION AND USE IN SOURCE AND BINARY FORMS, WITH OR 
WITHOUT MODIFICATION, DEVELOPMENTS OR ANY OTHER ACTIONS DONE BY THIRD PARTIES, AND NEITHER 
SHALL BE LIABLE FOR ANY DAMAGES (ANY CATEGORY) DERIVED FROM THESE ACTIONS.
Copyright (c) 2013, Fundaci�n Vodafone Espa�a
Todos los derechos reservados.

La redistribuci�n y el uso en las formas de c�digo fuente y binario, con o sin
modificaciones, est�n permitidos siempre que se cumplan las siguientes condiciones:
1. Las redistribuciones del c�digo fuente deben conservar el aviso de copyright
anterior, esta lista de condiciones y el siguiente descargo de responsabilidad.
2. Las redistribuciones en formato binario deben reproducir el aviso de copyright anterior, esta lista de condiciones y la siguiente renuncia en la documentaci�n y/u otros materiales suministrados con la distribuci�n.
3. Ni el nombre de los titulares de derechos de autor ni los nombres de sus colaboradores pueden usarse para apoyar o promocionar productos derivados de este software sin permiso previo y por escrito.

ESTE SOFTWARE SE SUMINISTRA POR FUNDACI�N VODAFONE ESPA�A ''COMO EST�'' Y 
CUALQUIER GARANT�A EXPRESAS O IMPL�CITAS, INCLUYENDO, CON CAR�CTER ENUNCIATIVO 
PERO NO LIMITATIVO A, LAS GARANT�AS IMPL�CITAS DE COMERCIALIZACI�N Y APTITUD 
PARA UN PROP�SITO DETERMINADO, SON TODAS RECHAZADAS. EN NING�N CASO FUNDACI�N VODAFONE ESPA�A 
SER� RESPONSABLE DE DA�OS DIRECTOS, INDIRECTOS, INCIDENTALES, ESPECIALES, EJEMPLARES O CONSECUENTES 
(INCLUYENDO, CON CAR�CTER ENUNCIATIVO PERO LIMITADO A: LA ADQUISICI�N DE BIENES O SERVICIOS,LA P�RDIDA DE USO, 
DE DATOS O DE BENEFICIOS O, LA INTERRUPCI�N DE LA ACTIVIDAD EMPRESARIAL) O POR CUALQUIER OTRO TIPO DE 
RESPONSABILIDAD LEGALMENTE ESTABLECIDA, YA SEA POR CONTRATO, RESPONSABILIDAD ESTRICTA O AGRAVIO 
(INCLUYENDO NEGLIGENCIA O CUALQUIER OTRA CAUSA) QUE SURJA DE CUALQUIER MANERA DEL USO DE ESTE SOFTWARE, 
INCLUSO SI SE HA ADVERTIDO DE LA POSIBILIDAD DE TALES DA�OS. EN ESTE MISMO SENTIDO,FUNDACION VODAFONE 
ESPA�A NO SER� RESPONSABLE BAJO NINGUNA CIRCUNSTANCIA DE LA REDISTRIBUCI�N Y DEL USO EN LAS FORMAS DE 
C�DIGO FUENTE Y BINARIO QUE TERCEROS HAGAN, DE SUS POSIBLES DESARROLLOS, MODIFICACIONES Y DEM�S ACCIONES, 
NI TAMPOCO DE LOS POSIBLES DA�OS (DE CUALQUIER CATEGOR�A) QUE PUDIERAN GENERARSE A PARTIR DE DICHAS ACCIONES.
 */

/**
 * @author Alberto Delgado
 */
package org.restlet.example.ext.oauth.server;

import java.util.ArrayList;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.reslet.example.oauth.dbconnection.dao.ClientDAO;
import org.reslet.example.oauth.dbconnection.dao.GenericDAO;
import org.reslet.example.oauth.dbconnection.dto.ClientDTO;
import org.reslet.example.oauth.dbconnection.model.UsuarioManager;
import org.restlet.Component;
import org.restlet.data.Parameter;
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
import org.restlet.util.Series;

/**
 * 
 * @author Alberto Delgado 
 */
public class OAuth2Sample {

  private static UsuarioManager userManager;
  private static MemoryClientManager clientManager;
  private static TokenManager tokenManager;
    private static String PASSWORD="fundacionVF";

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
//    component.getServers().add(Protocol.HTTP, 5050);
//    component.getServers().add(Protocol.HTTPS, 4040);
//
//    component.getServers().add(Protocol.HTTP, 5052);

    Series<Parameter> parameters = component.getServers().add(Protocol.HTTPS,5050).getContext().getParameters();
    parameters.add("keystorePath", "C:\\seg\\mfacilita.jks");
        parameters.add("keystorePassword", PASSWORD);
       // parameters.add("keyPassword", PASSWORD);
        parameters.add("keystoreType", "JKS");
    Series<Parameter> parameters2 = component.getServers().add(Protocol.HTTPS, 5052).getContext().getParameters();
       // parameters2.add("JsslutilsSslContextFactory","com.noelios.restlet.ext.ssl.PkixSslContextFactory");
        parameters2.add("keystorePath", "C:\\seg\\mfacilita.jks");
        parameters2.add("keystorePassword", PASSWORD);
      //  parameters2.add("keyPassword", PASSWORD );
        parameters2.add("keystoreType", "JKS");
    
    
    component.getDefaultHost().attach("/sample", new ExternalApplication());

    OAuth2ServerApplication app = new OAuth2ServerApplication();
    component.getDefaultHost().attach("/oauth", app);
    component.getInternalRouter().attach("/oauth", app);

    component.start();
  }
  static {
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
        {
            public boolean verify(String hostname, SSLSession session)
            {
                // ip address of the service URL(like.23.28.244.244)
                if (hostname.equals(Network.getLocalIP()))
                    return true;
                return false;
            }
        });
}
  
}
