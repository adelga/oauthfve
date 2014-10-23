
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

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.reslet.example.oauth.dbconnection.dao.GenericDAO;
import org.reslet.example.oauth.dbconnection.dao.UsuarioDAO;
import org.restlet.data.Form;
import org.restlet.example.ext.oauth.util.Network;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * Defines the an example resource that needs authentication.
 * 
 * @author Bret K. Ikehara
 */
public class RestrictedResource extends ServerResource {

  @Get
  public String getRep() throws ResourceException {
      
      ClientResource cr = new ClientResource("https://"+Network.getLocalIP()+":5052/oauth/token_auth");
      System.out.println("Get Restricted");
      Form form = new Form();
      form.add("access_token", getQueryValue("access_token"));
      form.add("token_type", getQueryValue("token_type"));
      
      
      
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
         // String username = "mycallback("+json.getString("username")+")";
           String username = json.getString("username");
          String rsName=getQueryValue("nameRS");
          
          String  rsUser;
          if(rsName!=null){
                        //TODO Checkear en la tabla User-RS que para el id_user del userName dado y para el id_RS del NameRS dado existe una entrada y devolver el RSUserName
              System.out.print("RSNAME " + rsName);
               rsUser=checkUserRS(username,rsName);
          }else{
              rsUser=username;
          }
          
          
          
          
          return "mycallback({\"username\": \"" + rsUser + "\"});";
          
          
      }
      catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
       catch (JSONException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
         // if(getCookieSettings().getFirst("_cid").getValue().equalsIgnoreCase("null")){
         // getCookieSettings().getFirst("_cid").setValue("null");
         // }
         // System.out.println("value cid:"+getCookieSettings().getFirst("_cid").getValue());
          return "({\"error\": \"error\"});";
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
