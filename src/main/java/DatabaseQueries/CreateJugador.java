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
 *
 * @author Gibran Gonzalez
 */
public class CreateJugador {
    public static ArrayList<createJugadorQuery> arrCreateJugador = new ArrayList<createJugadorQuery>();
    
    public static class createJugadorQuery {
        public static String username;
        public static String password;
    
    
        public createJugadorQuery(String u, String p) {
            CreateJugador.createJugadorQuery.username = u;
            CreateJugador.createJugadorQuery.password = p;
        }
    }    
    
    public void insertPlayers() {
        for (Player p : Game.getPlayers().values()) {
            CreateJugador.arrCreateJugador.add(new createJugadorQuery(p.getUsername(), p.getPassword()));
        }
    } 
}
