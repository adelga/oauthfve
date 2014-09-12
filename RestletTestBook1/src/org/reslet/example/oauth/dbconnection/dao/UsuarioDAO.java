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
import org.reslet.example.oauth.dbconnection.dto.UsuarioDTO;

/**
 *
 * @author Usuario
 */
public class UsuarioDAO {
    
     
     
     
    public static String getRsName(Connection con, String userName, String nameRS){
        String rsName="";
        Statement stmt = null;
        String query =" SELECT RSUserName FROM usuariosresourceserver WHERE id_user IN(SELECT idUsuario FROM usuarios WHERE userName='"+userName +"') AND id_RS IN(SELECT id_RS FROM resourceserver WHERE name_RS='"+nameRS+"')";
        
        try {
            
            stmt = (Statement) con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
             while (rs.next()) {
           
                String supplierID = rs.getString("RSUserName");
                System.out.println( "\t" + supplierID +
                        "\t" );
                 System.out.println( "////////////RSNAME" + supplierID +
                        "\t" );
                 rsName=supplierID;
            }
            return rsName;
        }catch(Exception e ){
            
            e.printStackTrace();
            return "ERROR";
        }
        
        
    } 
     
   
    
    public static boolean correctLogin(String user,String password ){
    try{
        //TODO Check if users exists
        
        return true;
    }catch(Exception e){
    e.printStackTrace();
    return false;}
    }

   //  public static Map getUserByUserName(java.sql.Connection connection,java.lang.String tipo, java.lang.String username){
     public static UsuarioDTO getUserByUserName(java.sql.Connection connection, java.lang.String username){

    ResultSet resultadoConsulta=null;
    String respuesta="";
   // Map<String, String> map = new HashMap<String, String>();
    UsuarioDTO dto = new UsuarioDTO();
    try {

            String consulta = "";

            Statement st=null;
            st = connection.createStatement();
            
          //  switch (Integer.parseInt(tipo)) {

                // case 1:

                    //Recuperar el idEntrada
                    try{
                    //Consultar usuario
                    consulta = "SELECT * FROM usuarios WHERE username='"+username+"'";
                        System.out.println("consulta="+consulta);
                    resultadoConsulta = st.executeQuery(consulta);
                     while(resultadoConsulta.next()){
                         
             respuesta+="user="+resultadoConsulta.getString("username")+" password="+resultadoConsulta.getString("password");
          //   printResponse(respuesta);
            // map.put("username", resultadoConsulta.getString("username"));
            // map.put("password", resultadoConsulta.getString("password"));
             dto.setId_user(resultadoConsulta.getInt("idUsuario"));
             dto.setId(resultadoConsulta.getString("username"));
             dto.setPassword(resultadoConsulta.getString("password"));


         }
                        System.out.println("resp="+respuesta);
                     } catch (Exception e) {
                        e.printStackTrace();
                    }
                  //  break;


      //      }

        //connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            respuesta = null;
        }
        return dto;


    }

    private void printResponse(String response) {
        System.out.println(response);
    }

}
