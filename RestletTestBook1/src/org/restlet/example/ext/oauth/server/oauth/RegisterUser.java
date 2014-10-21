package org.restlet.example.ext.oauth.server.oauth;

import freemarker.template.Configuration;
import java.sql.SQLException;
import java.util.HashMap;
import org.reslet.example.oauth.dbconnection.dao.ClientDAO;
import org.reslet.example.oauth.dbconnection.dao.GenericDAO;
import org.reslet.example.oauth.dbconnection.dao.UsuarioDAO;
import org.reslet.example.oauth.dbconnection.dto.UsuarioDTO;
import org.restlet.data.MediaType;
import org.restlet.example.ext.oauth.server.OAuth2Sample;
import org.restlet.ext.freemarker.ContextTemplateLoader;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.ext.oauth.internal.Client;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Defines the Rest Api to register an user
 * 
 * @author Alberto Delgado
 */
public class RegisterUser extends ServerResource {
    @Get("html")
    @Post("html")
    /*
    * Usage example : http://localhost:5050/oauth/registerUser?username=Usuario001&password=inredis
    */
    public Representation getPage() throws OAuthException, SQLException {
        try{
    
        System.out.println("Register User");
        String userName = getQueryValue("username");
        String pass = getQueryValue("password");
         System.out.println("insert user " + userName + " pass " + pass  );
        HashMap<String, Object> data = new HashMap<String, Object>();
        if (userName != null &&pass!=null) if(!pass.isEmpty() && !userName.isEmpty()) {
            
            UsuarioDTO user = new UsuarioDTO(userName);
            user.setPassword(pass);
            
           
               boolean result= UsuarioDAO.insertUsuario(GenericDAO.getConnection(), user);
                System.out.println("Fin llamada insert " + result);
                
                if (!result) {
                    
                 
                    data.put("error", "Registration failed.");
                    data.put("error_description", "not possible");
                }
            
        }
        
        
        TemplateRepresentation response = getRegisterPage("useradded.html");
        
        response.setDataModel(data);
        
        return response;
        }catch(Exception e){
            e.printStackTrace();
        }
     
        
    
        
        return new EmptyRepresentation();
    }
    
    
    protected TemplateRepresentation getRegisterPage(String registerPage) {
        Configuration config = new Configuration();
        config.setTemplateLoader(new ContextTemplateLoader(getContext(), "clap://system/resources"));
        getLogger().fine("loading: " + registerPage);
        return new TemplateRepresentation(registerPage, config, MediaType.TEXT_HTML);
    }
}
