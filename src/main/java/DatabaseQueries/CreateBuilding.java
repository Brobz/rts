/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseQueries;

import com.BitJunkies.RTS.src.Building;
import com.BitJunkies.RTS.src.Castle;
import com.BitJunkies.RTS.src.Game;
import com.BitJunkies.RTS.src.Player;
import com.BitJunkies.RTS.src.Worker;
import java.util.ArrayList;

/**
 *
 * @author Gibran Gonzalez
 */
public class CreateBuilding {
    public static ArrayList<CreateBuilding.createBuildingQuery> arrCreateBuilding = new ArrayList<CreateBuilding.createBuildingQuery>();
    
    public static class createBuildingQuery {
        public static int idEdificio;
        public static int partidaId;
        public static int jugadorId;
        public static String tipoE;
        
        public createBuildingQuery(int eId, int pId, int jId, String tipo) {
            CreateBuilding.createBuildingQuery.idEdificio = eId;
            CreateBuilding.createBuildingQuery.partidaId = pId;
            CreateBuilding.createBuildingQuery.jugadorId = jId;
            CreateBuilding.createBuildingQuery.tipoE = tipo;
        }
        
    }
    
    public void insertBuildings() {
        for(Player p : Game.getPlayers().values()) {
            for (Building b : p.getPlayerBuildings().values()) {
                String tipo = new String();
                if (b instanceof Castle)
                    tipo = "Castle";
                else
                    tipo = "Barrack";
                CreateBuilding.arrCreateBuilding.add(new createBuildingQuery(b.getDbId(), Game.partidaId, b.getOwner().getID(), tipo));
            }
        }
    }
}
