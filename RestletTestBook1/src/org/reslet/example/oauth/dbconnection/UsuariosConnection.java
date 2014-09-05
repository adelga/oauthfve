/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.reslet.example.oauth.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author Usuario
 */
public class UsuariosConnection {
    
      private static String dbms="mysql";
    private static String serverName="localhost";
    private static String portNumber="3306";
    private static String dbName="oauthfve";
    private static String usersTab = "usuarios";
    
    private static String rstable= "resourcetable";
    
    public static void viewTable(Connection con)
            throws SQLException {
        
               Statement stmt = null;

        String query = "SELECT idUsuario, userName, password FROM usuarios";
        try {
            stmt = (Statement) con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String coffeeName = rs.getString("userName");
                String pass = rs.getString("password");
                int supplierID = rs.getInt("idUsuario");
                System.out.println(coffeeName + "\t" + supplierID +
                        "\t" );
            }
        } catch (SQLException e ) {
            e.printStackTrace();;
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }
     
     
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
     
    public static Connection getConnection() throws SQLException {
        
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password","");
        
        if (dbms.equals("mysql")) {
            conn = DriverManager.getConnection(
                    "jdbc:" + dbms + "://" +
                            serverName +
                            ":" + portNumber + "/"+dbName,
                    connectionProps);
        } else if (dbms.equals("derby")) {
            conn = DriverManager.getConnection(
                    "jdbc:" + dbms + ":" +
                            dbName +
                            ";create=true",
                    connectionProps);
        }
        System.out.println("Connected to database");
        return conn;
    }
    
    public static boolean correctLogin(String user,String password ){
    try{
        //TODO Check if users exists
        
        return true;
    }catch(Exception e){
    e.printStackTrace();
    return false;}
    }
}
