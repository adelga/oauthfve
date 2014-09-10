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

import freemarker.template.Configuration;
import java.sql.SQLException;
import java.util.HashMap;
import org.reslet.example.oauth.dbconnection.dto.UsuarioDTO;
import org.restlet.data.MediaType;
import org.restlet.example.ext.oauth.server.OAuth2Sample;
import org.restlet.ext.freemarker.ContextTemplateLoader;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.oauth.AuthorizationBaseServerResource;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.ext.oauth.internal.AuthSession;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.security.SecretVerifier;

/**
 * Simple login authentication resource.
 * 
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class LoginPageServerResource extends AuthorizationBaseServerResource {
    
    @Get("html")
    @Post("html")
    public Representation getPage() throws OAuthException, SQLException {
       System.out.println("Get Login");
        String userId = getQueryValue("user_id");
        System.out.println("Get Login 2"+userId);
        HashMap<String, Object> data = new HashMap<String, Object>();
        if (userId != null && !userId.isEmpty()) {
            String password = getQueryValue("password");
            getLogger().info("User=" + userId + ", Pass=" + password);
            
            //TODO Chekear en la base de datos de Users (UssuarioConnection) si es correcto la tupla userid/password
            // Podemos utilizar UsuarioConnection dentro de SampleUser y que se gestione internamente desde esta clase, p.e
            System.out.println("Llamada a findByUserById");
            UsuarioDTO user = OAuth2Sample.getSampleUserManager().findUserById(userId);
            System.out.println("Fin llamada findByUserId");
            if (user == null) {
                data.put("error", "Authentication failed.");
                data.put("error_description", "ID is invalid.");
            } else {
                System.out.println("Obj created");
                System.out.println("password en Login page:"+ user.getPassword());
                boolean result = SecretVerifier.compare(password.toCharArray(), user.getPassword().toCharArray());
                if (result) {
                    AuthSession session = getAuthSession();
                    session.setScopeOwner(userId);
                    String uri = getQueryValue("continue");
                    getLogger().info("URI: " + uri);
                    redirectTemporary(uri);
                    return new EmptyRepresentation();
                } else {
                    data.put("error", "Authentication failed.");
                    data.put("error_description", "Password is invalid.");
                }
            }
        }
        
        String continueURI = getQueryValue("continue");
        TemplateRepresentation response = getLoginPage("login.html");
        data.put("continue", continueURI);
        response.setDataModel(data);
        
        return response;
    }
    
    
    protected TemplateRepresentation getLoginPage(String loginPage) {
        Configuration config = new Configuration();
        config.setTemplateLoader(new ContextTemplateLoader(getContext(), "clap://system/resources"));
        getLogger().fine("loading: " + loginPage);
        return new TemplateRepresentation(loginPage, config, MediaType.TEXT_HTML);
    }
}
