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
 * Class involving the PlayerInGame and its queries
 * @author Gibran Gonzalez
 */
public class CreateJugadorEnPartida {
    public static ArrayList<createJugadorEnPartidaQuery> arrCreateJugadorEnPartida = new ArrayList<createJugadorEnPartidaQuery>();
    public static HashMap<Integer, Integer> mapAcciones = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> mapRecAd = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> mapRecGas = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> mapEd = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> mapUn = new HashMap<Integer, Integer>();
    
    /**
     * Class to store the values of the table PlayerInGame
     */
    public static class createJugadorEnPartidaQuery {
        public static String jugadorId;
        public static int partidaId;
        public static int acciones;
        public static int recursosAdquiridos;
        public static int recursosConsumidos;
        public static int edificiosConstruidos;
        public static int unidadesConstruidas;
        public static boolean host;
    
        /**
         * Method to set the values of the table
         * @param jId player id
         * @param pId game id
         * @param acc actions
         * @param rA adquiredResources
         * @param rC consumed resources
         * @param ed built buildings
         * @param un built units
         * @param host player is host of game
         */
        public createJugadorEnPartidaQuery(String jId, int pId, int acc, int rA, int rC, int ed, int un, boolean host) {
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.jugadorId = jId;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.partidaId = pId;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.acciones = acc;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.recursosAdquiridos = rA;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.unidadesConstruidas = un;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.edificiosConstruidos = ed;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.recursosConsumidos = rC;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.host = host;
        }
    }   
    
    /**
     * Method to insert a row to the PlayerInGame table
     */
    public void insertJugadorEnPartida() {
        for(Player p : Game.getPlayers().values()) { 
            int acumAcc = CreateJugadorEnPartida.mapAcciones.get(p.getID());
            int acumRecAd = CreateJugadorEnPartida.mapRecAd.get(p.getID());
            int acumRecGas = CreateJugadorEnPartida.mapRecGas.get(p.getID());
            int acumEd = CreateJugadorEnPartida.mapEd.get(p.getID());
            int acumUn = CreateJugadorEnPartida.mapUn.get(p.getID());
            boolean hostea = (p.getID() == 1) ? true : false;
            CreateJugadorEnPartida.arrCreateJugadorEnPartida.add(new createJugadorEnPartidaQuery(p.getUsername(), Game.partidaId, acumAcc, acumRecAd, acumRecGas, acumEd, acumUn, hostea));
        }
    }
    
    /**
     * Method get the AcumActions in the match
     * @param i integer of the id
     * @return
     */
    public static Integer getAcumAcciones(Integer i) {
        if (mapAcciones.containsKey(i))
            return mapAcciones.get(i);
        return 0;
    }
    
    /**
     * Method to get the acquired resources of the match
     * @param i integer of the id
     * @return
     */
    public static Integer getAcumRecAd(Integer i) {
        if (mapRecAd.containsKey(i))
            return mapRecAd.get(i);
        return 0;
    }
    
    /**
     * Method to get the acumResources spent in the match
     * @param i integer of the id 
     * @return
     */
    public static Integer getAcumRecGas(Integer i) {
        if (mapRecGas.containsKey(i))
            return mapRecGas.get(i);
        return 0;
    }
    
    /**
     * MEthod to get the acumBuildings built in the match
     * @param i integer of the id
     * @return
     */
    public static Integer getAcumEd(Integer i) {
        if (mapEd.containsKey(i))
            return mapEd.get(i);
        return 0;
    }
    
    /**
     * Method to get the acumUnits in the match
     * @param i integer of the id
     * @return
     */
    public static Integer getAcumUn(Integer i) {
        if (mapUn.containsKey(i))
            return mapUn.get(i);
        return 0;
    }
}
