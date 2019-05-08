/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseQueries;

import com.BitJunkies.RTS.src.Game;
import com.BitJunkies.RTS.src.Player;
import java.util.ArrayList;

/**
 * Class involving the Player table and its queries
 * @author Gibran Gonzalez
 */
public class CreateJugador {
    public static ArrayList<createJugadorQuery> arrCreateJugador = new ArrayList<createJugadorQuery>();
    
    /**
     * Subclass to store the values of the table 
     */
    public static class createJugadorQuery {
        public static String username;
        public static String password;
    
        /**
         * Set the values for the player
         * @param u is the username
         * @param p is the password
         */
        public createJugadorQuery(String u, String p) {
            CreateJugador.createJugadorQuery.username = u;
            CreateJugador.createJugadorQuery.password = p;
        }
    }    
    
    /**
     * Method to add a player to the database with their attributes (username,password)
     */
    public static void insertPlayers() {
        for (Player p : Game.getPlayers().values()) {
            CreateJugador.arrCreateJugador.add(new createJugadorQuery(p.getUsername(), p.getPassword()));
        }
    } 
}
