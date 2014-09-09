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

    private static String rstable= "resourcetable";

     public static Connection getConnection() throws SQLException {
         System.out.println("Connecting to database...");
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password","fve");

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
}
