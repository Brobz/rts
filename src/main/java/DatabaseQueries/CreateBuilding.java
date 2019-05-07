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
public class CreateBuilding {
    public static ArrayList<CreateBuilding.createBuildingQuery> arrCreateBuilding;
    
    public static class createBuildingQuery {
        public static int idEdificio;
        public static String partidaId;
        public static int jugadorId;
        public static String tipoE;
        
        public createBuildingQuery(int eId, String pId, int jId, String tipo) {
            CreateBuilding.arrCreateBuilding = new ArrayList<CreateBuilding.createBuildingQuery>();
            CreateBuilding.createBuildingQuery.idEdificio = eId;
            CreateBuilding.createBuildingQuery.partidaId = pId;
            CreateBuilding.createBuildingQuery.jugadorId = jId;
            CreateBuilding.createBuildingQuery.tipoE = tipo;
        }
        
    }
}
