/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseQueries;

import com.BitJunkies.RTS.src.Game;
import com.BitJunkies.RTS.src.Player;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Gibran Gonzalez
 */
public class CreateJugadorEnPartida {
    public static ArrayList<createJugadorEnPartidaQuery> arrCreateJugadorEnPartida = new ArrayList<createJugadorEnPartidaQuery>();
    public static HashMap<Integer, Integer> mapAcciones = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> mapRecAd = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> mapRecGas = new HashMap<Integer, Integer>();
    
    public static class createJugadorEnPartidaQuery {
        public static String jugadorId;
        public static int partidaId;
        public static int acciones;
        public static int recursosAdquiridos;
        public static int recursosConsumidos;
    
    
        public createJugadorEnPartidaQuery(String jId, int pId, int acc, int rA, int rC) {
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.jugadorId = jId;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.partidaId = pId;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.acciones = acc;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.recursosAdquiridos = rA;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.recursosConsumidos = rC;
        }
    }   
    
    public void insertJugadorEnPartida() {
        for(Player p : Game.getPlayers().values()) { 
            int acumAcc = CreateJugadorEnPartida.mapAcciones.get(p.getID());
            int acumRecAd = CreateJugadorEnPartida.mapRecAd.get(p.getID());
            int acumRecGas = CreateJugadorEnPartida.mapRecGas.get(p.getID());
            CreateJugadorEnPartida.arrCreateJugadorEnPartida.add(new createJugadorEnPartidaQuery(p.getUsername(), Game.partidaId, acumAcc, acumRecAd, acumRecGas));
        }
    }
    
    public static Integer getAcumAcciones(Integer i) {
        if (mapAcciones.containsKey(i))
            return mapAcciones.get(i);
        return 0;
    }
    
    public static Integer getAcumRecAd(Integer i) {
        if (mapRecAd.containsKey(i))
            return mapRecAd.get(i);
        return 0;
    }
    
    public static Integer getAcumRecGas(Integer i) {
        if (mapRecGas.containsKey(i))
            return mapRecGas.get(i);
        return 0;
    }
}
