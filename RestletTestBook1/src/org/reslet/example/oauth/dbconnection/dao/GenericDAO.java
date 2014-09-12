/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.reslet.example.oauth.dbconnection.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author fve
 */
public class GenericDAO {
 private static String dbms="mysql";
    private static String serverName="localhost";
    private static String portNumber="3306";
    private static String dbName="admin";
    private static String usersTab = "usuarios";
    private static final String PASS="";

    private static String rstable= "resourcetable";

     public static Connection getConnection() {
         System.out.println("Connecting to database...");

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password",PASS);
           try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                e.printStackTrace();
            }
        if(conn == null){
            try{
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
        if(conn != null){
         System.out.println("Connected to database");
        }
            }catch(SQLException ex){
                System.out.println("Hubo un problema al intentar conectarse con la base de datos");
            } 
        }
        return conn;
    }
}
