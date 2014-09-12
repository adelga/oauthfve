package org.restlet.example.ext.oauth.server.oauth;

import org.restlet.example.ext.oauth.server.external.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.json.JSONException;
import org.json.JSONObject;
import org.reslet.example.oauth.dbconnection.dao.GenericDAO;
import org.reslet.example.oauth.dbconnection.dao.UsuarioDAO;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * Defines the an example resource that needs authentication.
 * 
 * @author Alberto Delgado
 */
public class RestrictedResource extends ServerResource {

  @Get
  public String getRep() throws ResourceException {
        ClientResource cr = null;
      try{
     cr= new ClientResource("http://"+InetAddress.getLocalHost().getHostAddress()+":5052/oauth/token_auth");
      System.out.println("Get Restricted");
      Form form = new Form();
      form.add("access_token", getQueryValue("access_token"));
      form.add("token_type", getQueryValue("token_type"));
      }  catch (Exception e2) {
          // TODO Auto-generated catch block
          e2.printStackTrace();
      }
      
      
      JSONObject resp = new JSONObject();
      try {
          resp.put("access_token", getQueryValue("access_token"));
          resp.put("token_type", getQueryValue("token_type"));
      }
      catch (Exception e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
      }
      
      Representation rep = cr.post(new JsonRepresentation(resp));
      
      try {
          JSONObject json = new JSONObject(rep.getText());
          System.out.println("json" + json);
          String result ="OK";
          String username="none";
          // String username = "mycallback("+json.getString("username")+")";
          try{
               username = json.getString("username");}
          catch(JSONException je){
              je.printStackTrace();
               result = json.getString("error");
          }
          String rsName=getQueryValue("nameRS");
          String  rsUser;
          if(rsName!=null&& !username.equalsIgnoreCase("none")){
              //TODO Checkear en la tabla User-RS que para el id_user del userName dado y para el id_RS del NameRS dado existe una entrada y devolver el RSUserName
              System.out.print("RSNAME " + rsName);
              rsUser=checkUserRS(username,rsName);
          }else{
              rsUser=username;
          }
          
          
          
          System.err.println("");
          return "mycallback({\"result\": \"" + result + "\",\"username\": \"" + rsUser + "\"});";
          
          
      }
      catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
       catch (JSONException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          return "Not Login";
      }
      catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      
      return "";
  }

  private String checkUserRS(String username, String RSName) {
      try{
          
          return UsuarioDAO.getRsName(GenericDAO.getConnection(),username,RSName);
      }catch(Exception e){
          e.printStackTrace();
          return "ERROR";
      }
  }
}