/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

/**
 *
 * @author Gibran Gonzalez org.postgresql.Driver
 */

import java.sql.*;


public class saveDB {
    // JDBC driver name and database URL
   static final String JDBC_DRIVER = "org.postgresql.Driver";  
   static final String DB_URL = "jdbc:postgresql://ec2-54-225-95-183.compute-1.amazonaws.com:5432/dah1sh2i3uomfn?user=seaynizasqgwhc&password=015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020&sslmode=require";
   
   //  Database credentials
   static final String USER = "seaynizasqgwhc";
   static final String PASS = "015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020";
   
   public Connection connect(String query) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL, USER, PASS);
           
           stmt = conn.prepareStatement(query);
           
           rs = stmt.executeQuery(query);
           
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return conn;
   }
}
