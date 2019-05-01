/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

/**
 *
 * @author brobz
 */
public class SpawnUnitObject {
    public int playerID, buildingID, unitIndex;
    
    public SpawnUnitObject() {
    } 
 
    public SpawnUnitObject(int playerID, int buildingID, int unitIndex) {
        this.playerID = playerID;
        this.buildingID = buildingID;
        this.unitIndex = unitIndex;
    }
}
