/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseQueries;

import java.util.ArrayList;
import java.sql.Timestamp;

/**
 *
 * @author Gibran Gonzalez
 */
public class CreateGame {
    
    /**
     *  Method to add to the database the match info (beginning,end,winner,id)
     */
    public static class createGameQuery {
        public static int idPartida;
        public static Timestamp fechaIni;
        public static Timestamp fechaFin;
        public static String ganador;
    
    
        public createGameQuery(long fIni, long fFin, String ganador) {
            CreateGame.createGameQuery.fechaIni = new Timestamp(fIni);
            CreateGame.createGameQuery.fechaFin = new Timestamp(fFin);
            CreateGame.createGameQuery.ganador = ganador;
        }
    }
}
