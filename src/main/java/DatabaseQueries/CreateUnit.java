/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseQueries;

import com.BitJunkies.RTS.src.Game;
import com.BitJunkies.RTS.src.Player;
import com.BitJunkies.RTS.src.Unit;
import com.BitJunkies.RTS.src.Worker;
import java.util.ArrayList;

/**
 *
 * @author Gibran Gonzalez
 */
public class CreateUnit {
    
    public static ArrayList<createUnitQuery> arrCreateUnit;
    
    public static class createUnitQuery {
        public static int idUnidad;
        public static int edificioId;
        public static String tipoU;
    
    
        public createUnitQuery(int idU, int eId, String tipo) {
            CreateUnit.arrCreateUnit = new ArrayList<createUnitQuery>();
            CreateUnit.createUnitQuery.idUnidad = idU;
            CreateUnit.createUnitQuery.edificioId = eId;
            CreateUnit.createUnitQuery.tipoU = tipo;
        }
    }
    
    public void insertUnits() {
        for (Player p : Game.getPlayers().values()) {
            for (Unit u : p.getPlayerUnits().values()) {
                String tipo = new String();
                if (u instanceof Worker)
                    tipo = "Worker";
                else
                    tipo = "Warrior";
                CreateUnit.arrCreateUnit.add(new createUnitQuery(u.getEntityId(), u.getBuildingId(), tipo));
            }
        }
    }
}
