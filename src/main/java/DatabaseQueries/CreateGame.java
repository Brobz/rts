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
public class CreateGame {
    
    public static class createGameQuery {
        public static String idPartida;
        public static String fechaIni;
        public static String fechaFin;
    
    
        public createGameQuery(String idP, String fIni, String fFin) {
            CreateGame.createGameQuery.idPartida = idP;
            CreateGame.createGameQuery.fechaIni = fIni;
            CreateGame.createGameQuery.fechaFin = fFin;
        }
    }
}
