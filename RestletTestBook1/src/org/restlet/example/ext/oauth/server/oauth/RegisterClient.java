package org.restlet.example.ext.oauth.server.oauth;

import freemarker.template.Configuration;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.reslet.example.oauth.dbconnection.dao.ClientDAO;
import org.reslet.example.oauth.dbconnection.dao.GenericDAO;
import org.reslet.example.oauth.dbconnection.dao.UsuarioDAO;
import org.reslet.example.oauth.dbconnection.dto.UsuarioDTO;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.example.ext.oauth.server.OAuth2Sample;
import org.restlet.example.ext.oauth.util.Network;
import org.restlet.ext.freemarker.ContextTemplateLoader;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.ext.oauth.internal.AbstractClientManager;
import org.restlet.ext.oauth.internal.AuthSession;
import org.restlet.ext.oauth.internal.Client;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.security.SecretVerifier;

/**
 * Defines the an example resource that needs authentication.
 * 
 * @author Bret K. Ikehara
 */
public class RegisterClient extends ServerResource {
    @Get("html")
    @Post("html")
    public Representation getPage() throws OAuthException, SQLException {
        try{
            
            //TODO dar la opción de a que RS se le deja redirigir el logueo
            
        System.out.println("Register");
        String clientName = getQueryValue("client_name");
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        if (clientName != null && !clientName.isEmpty()) {
            
            Client cl=OAuth2Sample.getClientManager().createClient(Client.ClientType.CONFIDENTIAL, ClientDAO.getRedirectsURIsByIdClient(GenericDAO.getConnection(), "b8d1cfda-0a13-4072-ab8f-ae3494b2e028"), null);
            
            if (cl == null) {
                data.put("error", "Not Possible register Client");
                data.put("error_description", "ID is invalid.");
            } else {
                System.out.println("Llamada a createClient " + cl.getClientId() + " - "+ clientName + "-"+cl.getClientSecret());
                ClientDAO.insertClient(GenericDAO.getConnection(), cl.getClientId(), cl.getClientSecret().toString(), clientName);
                System.out.println("Fin llamada findByUserId");
                
                boolean result = true;
                if (result) {
                    
                    return new EmptyRepresentation();
                } else {
                    data.put("error", "Authentication failed.");
                    data.put("error_description", "Password is invalid.");
                }
            }
        }
        
        
        TemplateRepresentation response = getRegisterPage("register.html");
        
        response.setDataModel(data);
        
        return response;
        }catch(Exception e){
            e.printStackTrace();
        }
        TemplateRepresentation response = getRegisterPage("register.html");
        
    
        
        return response;
    }
    
    
    protected TemplateRepresentation getRegisterPage(String registerPage) {
        Configuration config = new Configuration();
        config.setTemplateLoader(new ContextTemplateLoader(getContext(), "clap://system/resources"));
        getLogger().fine("loading: " + registerPage);
        return new TemplateRepresentation(registerPage, config, MediaType.TEXT_HTML);
    }
}
