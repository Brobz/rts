/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseQueries;

import static DatabaseQueries.InsertToDB.DB_URL;
import com.BitJunkies.RTS.src.Game;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to perform the selections of the data base
 * @author Gibran Gonzalez
 */
public class SelectFromDB {
    // JDBC driver name and database URL
   static final String JDBC_DRIVER = "org.postgresql.Driver";  
   static final String DB_URL = "jdbc:postgresql://ec2-54-225-95-183.compute-1.amazonaws.com:5432/dah1sh2i3uomfn?user=seaynizasqgwhc&password=015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020&sslmode=require";

   //  Database credentials
   static final String USER = "seaynizasqgwhc";
   static final String PASS = "015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020";
   
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
    
    /**
     * Method containing the SQL to get the actions per minute
     * @param username string that identifies the player
     * @return actions per minute Float
     */
    public static float getActionsPerMin(String username) {
        String SQL = "select (Select avg(accionespormin/(EXTRACT(epoch FROM (termino-inicio))/60)) from jugadorenpartida a, partida b where a.partidaid = b.id) as acc from jugadorenpartida where jugadorid = '" + username + "' group by jugadorid";
        float acc=0;
 
        try {
            Statement stmt = Game.conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if(rs.next())
                acc = rs.getFloat(1);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return acc;
    }
    
    /**
     * Method containing the SQL to get the buildings per game
     * @param username string that identifies the player
     * @return buildings per game Float
     */
    public static float getBuildingPerGame(String username) {
        String SQL = "select (Select avg(edificiosConstruidos) from jugadorenpartida a, partida b, jugador c where a.partidaid = b.id and a.jugadorid = c.username and c.username = '" + username + "') as acc from jugadorenpartida where jugadorid = 'currPlayer.getUsername()' group by jugadorid";
        float ed=0;
 
        try (
                Statement stmt = Game.conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            
            if(rs.next())
                ed = rs.getFloat(1);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return ed;
    }
    
    /**
     * Method containing the SQL to get the units built per game
     * @param username string that identifies the player
     * @return units built per game Float
     */
    public static float getUnitsPerGame(String username) {
        String SQL = "select (Select avg(unidadesConstruidas) from jugadorenpartida a, partida b, jugador c where a.partidaid = b.id and a.jugadorid = c.username and c.username = '" + username + "') as acc from jugadorenpartida where jugadorid = 'currPlayer.getUsername()' group by jugadorid";
        float un=0;
 
        try (
                Statement stmt = Game.conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            
            if(rs.next())
                un = rs.getFloat(1);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return un;
    }
    
    /**
     * Method containing the SQL to get the win rate
     * @param username string that identifies the player
     * @return win rate Float
     */
    public static float getWinRate(String username) {
        String SQL = "Select (select count(*) from jugadorenpartida a, partida b where a.partidaId = b.id and a.jugadorid = '" + username + "' and b.ganador = '" + username + "')::float/(select count(*) from jugadorenpartida a, partida b where a.partidaId = b.id and a.jugadorid = '" + username + "')::float * 100 as winRate";
        float rate=0;
 
        try (
                Statement stmt = Game.conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            
            if(rs.next())
                rate = rs.getFloat(1);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return rate;
    }
    
    /**
     * Method containing the SQL to get the floating resources
     * @param username string that identifies the player
     * @return floating resources Float
     */
    public static float getFloatingResources(String username) {
        String SQL = "select 100::float - (((select avg(recursosconsumidos) from jugadorenpartida where jugadorid = '" + username + "')::float) / (select avg(recusosadquiridos) from jugadorenpartida where jugadorid = '" + username + "')::float)";
        float rate=0;
 
        try (
                Statement stmt = Game.conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            
            if(rs.next())
                rate = rs.getFloat(1);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        System.out.println("RATE----------" + rate);
        return rate;
    }
    
    /**
     * Method containing the SQL to get if a username exists in the table 
     * @param username string that identifies the player
     * @return boolean if player exists
     */
    public static boolean existsUsername(String username) {
        String SQL = "select exists(select * from jugador where username = '" + username + "')";
        boolean exists = false;
 
        try (
                Statement stmt = Game.conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            
            if(rs.next())
                exists = rs.getBoolean(1);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return exists;
    }
    
    /**
     * Method containing the SQL that validates an existing username with a password
     * @param username string that identifies the player
     * @param password password of the input
     * @return boolean if password matches the username
     */
    public static boolean validatePassword(String username, String password) {
        if(!existsUsername(username)) return false;
        String SQL = "select password from jugador where username = '" + username + "'";
        String passw = "";
 
        try (
                Statement stmt = Game.conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            
            if(rs.next())
                passw = rs.getString(1);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return passw.equals(password);
    }
    
}



//
