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
package org.restlet.example.ext.oauth.server.oauth;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.example.ext.oauth.server.OAuth2Sample;
import org.restlet.example.ext.oauth.server.oauth.LogoutAction;
import org.restlet.example.ext.oauth.server.services.SessionLogout;
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

/**
 * Simple OAuth 2.0 draft30 server-side application.
 * 
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class OAuth2ServerApplication extends Application {
    
    @Override
    public synchronized Restlet createInboundRoot(){
        try{
            Router router = new Router(getContext());
            System.out.println("Client Manager to verify:"+OAuth2Sample.getClientManager().toString());
            System.out.println( getContext().getAttributes().keySet().size());
            
//            System.out.println("Before client" + mn.toString());
//             System.out.println("before " + mn.findById("http://localhost:5050/sample/popup").toString());
            getContext().getAttributes().put(ClientManager.class.getName(), OAuth2Sample.getClientManager());
            getContext().getAttributes().put(TokenManager.class.getName(), OAuth2Sample.getTokenManager());
            
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