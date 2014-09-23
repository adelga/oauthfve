/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package org.reslet.example.oauth.dbconnection.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}
