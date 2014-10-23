
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
 * @author Manuel Valls
 */
package org.restlet.example.ext.oauth.server.oauth;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.example.ext.oauth.server.OAuth2Sample;
import org.restlet.ext.oauth.AccessTokenServerResource;
import org.restlet.ext.oauth.AuthPageServerResource;
import org.restlet.ext.oauth.AuthorizationServerResource;
import org.restlet.ext.oauth.ClientVerifier;
import org.restlet.ext.oauth.HttpOAuthHelper;
import org.restlet.ext.oauth.TokenAuthServerResource;
import org.restlet.ext.oauth.internal.ClientManager;
import org.restlet.ext.oauth.internal.TokenManager;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;

public class OAuth2ServerApplication extends Application {
    
    @Override
    public synchronized Restlet createInboundRoot(){
        try{
            Router router = new Router(getContext());
            System.out.println("Client Manager to verify:"+OAuth2Sample.getClientManager().toString());
            System.out.println( getContext().getAttributes().keySet().size());
            

            getContext().getAttributes().put(ClientManager.class.getName(), OAuth2Sample.getClientManager());
            getContext().getAttributes().put(TokenManager.class.getName(), OAuth2Sample.getTokenManager());
            
             router.attach("/registerClient", RegisterClient.class);
              router.attach("/registerUser", RegisterUser.class);
            
            // Setup Authorize Endpoint
            router.attach("/authorize", AuthorizationServerResource.class);
            router.attach("/restricted-resource", RestrictedResource.class);

            //router.attach("/check-cookies",CheckCookies.class);
            router.attach(HttpOAuthHelper.getAuthPage(getContext()), AuthPageServerResource.class);
            HttpOAuthHelper.setAuthPageTemplate("resources/authorize.html", getContext());
            HttpOAuthHelper.setAuthSkipApproved(true, getContext());
            HttpOAuthHelper.setErrorPageTemplate("resources/error.html", getContext());
            router.attach(HttpOAuthHelper.getLoginPage(getContext()), LoginPageServerResource.class);
            router.attach("/logout",LogoutAction.class);
            // Setup Token Endpoint
            ChallengeAuthenticator clientAuthenticator =
                    new ChallengeAuthenticator(getContext(),
                            ChallengeScheme.HTTP_BASIC, "OAuth2Sample");
            ClientVerifier clientVerifier = new ClientVerifier(getContext());
            clientVerifier.setAcceptBodyMethod(true);
            clientAuthenticator.setVerifier(clientVerifier);
            clientAuthenticator.setNext(AccessTokenServerResource.class);
            router.attach("/token", clientAuthenticator);
            
            // Setup Token Auth for Resources Server
            router.attach("/token_auth", TokenAuthServerResource.class);
            
            final Directory resources = new Directory(getContext(), "clap://system/resources");
            router.attach("", resources);
             return router;
        }catch(Exception e){
            e.printStackTrace();
           }
        return null;
       
    }
}