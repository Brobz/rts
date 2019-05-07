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
}
