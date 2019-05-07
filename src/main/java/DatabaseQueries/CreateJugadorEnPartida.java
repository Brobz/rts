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
public class CreateJugadorEnPartida {
    public static ArrayList<createJugadorEnPartidaQuery> arrCreateJugadorEnPartida;
    
    public static class createJugadorEnPartidaQuery {
        public static String jugadorId;
        public static String partidaId;
        public static int acciones;
        public static int recursosAdquiridos;
        public static int recursosConsumidos;
    
    
        public createJugadorEnPartidaQuery(String jId, String pId, int acc, int rA, int rC) {
            CreateJugadorEnPartida.arrCreateJugadorEnPartida = new ArrayList<createJugadorEnPartidaQuery>();
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.jugadorId = jId;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.partidaId = pId;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.acciones = acc;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.recursosAdquiridos = rA;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.recursosConsumidos = rC;
        }
    }   
}
