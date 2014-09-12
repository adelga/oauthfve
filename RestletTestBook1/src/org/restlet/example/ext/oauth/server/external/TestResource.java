package org.restlet.example.ext.oauth.server.external;

import java.util.HashMap;
import java.util.Map;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.ContextTemplateLoader;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import freemarker.template.Configuration;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.data.Cookie;
import org.restlet.util.Series;

public class TestResource extends ServerResource {
    
    public static  String REDIRECT_URL = "";
    
    @Get("text/html")
    public Representation get() {
        try {
            
            REDIRECT_URL="http://"+InetAddress.getLocalHost().getHostAddress()+":5050/sample/popup";
            Series<Cookie> cs = getRequest().getCookies();
             System.out.println("cs size "+cs.size());
            for(int i=0; i<cs.size();i++){
                System.out.println("cs "+cs.get(i));
                
                    }
            Configuration config = new Configuration();
            config.setTemplateLoader(new ContextTemplateLoader(getContext(), "clap://system/resources/external/"));
            
            Map<String, String> map = new HashMap<String, String>();
       
            TemplateRepresentation rep =
                    new TemplateRepresentation("index.html", config, MediaType.TEXT_HTML);
            
            map.put("url", "kkkkk");
            map.put("authenticated", "no");
            rep.setDataModel((Object) map);
            return rep;
        } catch (UnknownHostException ex) {
            Logger.getLogger(TestResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
}