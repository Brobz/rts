/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseQueries;

import com.BitJunkies.RTS.src.Game;
import com.BitJunkies.RTS.src.Player;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Gibran Gonzale
 */
public class CreateJugadorEnPartida {
    public static ArrayList<createJugadorEnPartidaQuery> arrCreateJugadorEnPartida = new ArrayList<>();
    public static HashMap<Integer, Integer> mapAcciones = new HashMap<>();
    public static HashMap<Integer, Integer> mapRecAd = new HashMap<>();
    public static HashMap<Integer, Integer> mapRecGas = new HashMap<>();
    public static HashMap<Integer, Integer> mapEd = new HashMap<>();
    public static HashMap<Integer, Integer> mapUn = new HashMap<>();
    
    public static class createJugadorEnPartidaQuery {
        public static String jugadorId;
        public static Integer partidaId;
        public static int acciones;
        public static int recursosAdquiridos;
        public static int recursosConsumidos;
        public static int edificiosConstruidos;
        public static int unidadesConstruidas;
        public static boolean host;
    
    
        public createJugadorEnPartidaQuery(String jId, int pId, int acc, int rA, int rC, int ed, int un, boolean host) {
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.jugadorId = jId;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.partidaId = pId;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.acciones = acc;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.recursosAdquiridos = rA;
            CreateJugadorEnPartida.createJugadorEnPartidaQuery.recursosConsumidos = rC;
        }
    }   
    
    public static void insertJugadoresEnPartida() throws SQLException, URISyntaxException{
        int i=0;
        int idPart = InsertToDB.getCurrGameId();
        //for(Player p : Game.getPlayers().values()) { 
        while(i < Game.server.connectedPlayers.size()) {
            if (Game.server.connectedPlayers.get(i) != null) {
                Player p = Game.getPlayers().get(i+1);
                int acumAcc = p.actions;
                int acumRecAd = p.recAd;
                int acumRecGas = p.recGas;
                int acumEd = p.edCon;
                int acumUn = p.uniCon;
                boolean hostea = (i+1 == 1);
                InsertToDB.insertUnJugadorEnPartida(Game.server.connectedPlayers.get(i).toString(), idPart, acumAcc, acumRecAd, acumRecGas, acumEd, acumUn, hostea);
                i++;
            }
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
    
    public static Integer getAcumEd(Integer i) {
        if (mapEd.containsKey(i))
            return mapEd.get(i);
        return 0;
    }
    
    public static Integer getAcumUn(Integer i) {
        if (mapUn.containsKey(i))
            return mapUn.get(i);
        return 0;
    }
}
