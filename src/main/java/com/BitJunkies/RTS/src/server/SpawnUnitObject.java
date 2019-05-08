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
    public int playerID, unitId, unitIndex, type;
    public String playerName;
    
    public SpawnUnitObject() {
    } 
 
    public SpawnUnitObject(int playerID, int unitId, int unitIndex, int type, String playerName) {
        this.playerID = playerID;
        this.unitId = unitId;
        this.unitIndex = unitIndex;
        this.type = type;
        this.playerName = playerName;
    }
}