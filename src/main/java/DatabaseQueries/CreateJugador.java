/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseQueries;

import java.util.ArrayList;

/**
 *
 * @author Gibran Gonzalez
 */
public class CreateJugador {
    public static ArrayList<createJugadorQuery> arrCreateJugador;
    
    public static class createJugadorQuery {
        public static String username;
        public static String name;
    
    
        public createJugadorQuery(String u, String n) {
            CreateJugador.arrCreateJugador = new ArrayList<createJugadorQuery>();
            CreateJugador.createJugadorQuery.username = u;
            CreateJugador.createJugadorQuery.name = n;
        }
    }    
}
