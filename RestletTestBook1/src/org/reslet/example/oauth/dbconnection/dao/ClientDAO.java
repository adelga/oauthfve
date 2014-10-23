
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

package org.reslet.example.oauth.dbconnection.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.reslet.example.oauth.dbconnection.dto.ClientDTO;
import org.restlet.ext.oauth.internal.Client;

/**
 *
 * @author adelga
 */
public class ClientDAO {


    public static void registerClientUser(Connection connection, java.lang.String client_id, int user_id)
    {
    ResultSet resultadoConsulta=null;
    String respuesta="";
    // Map<String, String> map = new HashMap<String, String>();
    try{
        String consulta = "";

        Statement st=null;
        st = connection.createStatement();
        consulta = "INSERT INTO `admin`.`clientsusers` (`id_user`, `client_id`) VALUES ('"+user_id+"', '"+client_id+"')";
        System.out.println("result to instert " + consulta);

         boolean b = st.execute(consulta);
          System.out.println("result to instert " + b);
    }catch(Exception e){
        e.printStackTrace();
    }

}

    public static void insertClient(java.sql.Connection connection, java.lang.String client_id, String client_secret, String name){
        
        ResultSet resultadoConsulta=null;
        String respuesta="";
        // Map<String, String> map = new HashMap<String, String>();
        ClientDTO dto = new ClientDTO();
        try{
            String consulta = "";

            Statement st=null;
            st = connection.createStatement();
            consulta = "INSERT INTO `clients` (`client_id`, `client_secret`, `name`) VALUES ('"+client_id+"', '"+client_secret+"', '"+name+"')";
            System.out.println("result to instert " + consulta);
boolean b = st.execute(consulta);
          System.out.println("result to instert " + b);
        }catch(Exception e){
            e.printStackTrace();
        }


    }
    public static ClientDTO getClientbyClientID(java.sql.Connection connection, java.lang.String client_id){

        ResultSet resultadoConsulta=null;
        String respuesta="";
        ClientDTO dto = new ClientDTO();
        try {

            String consulta = "";

            Statement st=null;
            st = connection.createStatement();

            try{
                consulta = "SELECT * FROM clients WHERE client_id='"+client_id+"'";
                System.out.println("consulta="+consulta);
                resultadoConsulta = st.executeQuery(consulta);
                while(resultadoConsulta.next()){

                    dto.setClient_id(resultadoConsulta.getString("client_id"));
                    dto.setClient_secret(resultadoConsulta.getString("client_secret"));
                    dto.setName(resultadoConsulta.getString("name"));


                }
                System.out.println("resp="+respuesta);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            respuesta = null;
        }
        return dto;


    }
    
     public static ClientDTO getClientbyName(java.sql.Connection connection, java.lang.String name){

        ResultSet resultadoConsulta=null;
        String respuesta="";
        ClientDTO dto = new ClientDTO();
        try {

            String consulta = "";

            Statement st=null;
            st = connection.createStatement();

            try{
                consulta = "SELECT * FROM clients WHERE name='"+name+"'";
                System.out.println("consulta="+consulta);
                resultadoConsulta = st.executeQuery(consulta);
                while(resultadoConsulta.next()){
                    dto.setClient_id(resultadoConsulta.getString("client_id"));
                    dto.setClient_secret(resultadoConsulta.getString("client_secret"));
                    dto.setName(resultadoConsulta.getString("name"));


                }
                System.out.println("resp="+respuesta);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            respuesta = null;
        }
        return dto;


    }

    public static boolean checkClientUserAuthorization(Connection connection,String client, String userName) {
        ResultSet resultadoConsulta=null;
        String respuesta="";
        try {

            String consulta = "";

            Statement st=null;
            st = connection.createStatement();

            try{
                consulta = "SELECT * FROM clientsusers WHERE id_user IN(SELECT idUsuario FROM usuarios WHERE userName='"+userName +"') AND client_id= '"+client+"'";
                System.out.println("consulta="+consulta);
                resultadoConsulta = st.executeQuery(consulta);
               System.out.println(resultadoConsulta.toString());
                System.out.println("resp="+respuesta);
                while(resultadoConsulta.next()){
                     System.out.println(resultadoConsulta.getString("id_user"));
                return true;}
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            respuesta = null;
        }
        return false;
    }
    
    public static String[] getRedirectsURIsByIdClient(Connection connection,String client_id) {
        ResultSet resultadoConsulta=null;
        String respuesta="";
        
        try {
            if(connection==null){
                return null;
            }
            String consulta = "";
            ArrayList<String> redirectsUris;
            String[] uris = new String[100];
            redirectsUris = new ArrayList<String>();
            Statement st=null;
            st = connection.createStatement();
            
            try{
                consulta = "SELECT redirect_uri FROM resourceserver WHERE id_RS IN(SELECT id_RS FROM clientsrs WHERE client_id='"+client_id+"')";
                System.out.println("consulta="+consulta);
                resultadoConsulta = st.executeQuery(consulta);
//               System.out.println(resultadoConsulta.first());
                while(resultadoConsulta.next()){
                    redirectsUris.add(resultadoConsulta.getString("redirect_uri"));
                }
                
                for(int i=0; i<redirectsUris.size(); i++){
                    System.out.println("client: " + client_id + ", uri " +  redirectsUris.get(i));
                    uris[i]= redirectsUris.get(i);
                }
                return uris;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            
        }
        return null;
    }
    
     public static ArrayList<ClientDTO> getAllClients(Connection connection){
           
     try{
     String consulta = "";
            ArrayList<String> redirectsUris;
            ArrayList<ClientDTO> clientsDTO = new ArrayList<ClientDTO>();
            Statement st=null; 
            st = connection.createStatement();
      consulta = "SELECT * FROM clients";
                System.out.println("consulta="+consulta);
         ResultSet resultadoConsulta = st.executeQuery(consulta);
         ClientDTO dto;
//               System.out.println(resultadoConsulta.first());
                while(resultadoConsulta.next()){
                    dto= new ClientDTO(resultadoConsulta.getString("client_id"), resultadoConsulta.getString("client_secret"), resultadoConsulta.getString("name"));
                clientsDTO.add(dto);
                }
            return clientsDTO;
            
     }catch(Exception e){
         e.printStackTrace();
     return null;}
     
}
}
