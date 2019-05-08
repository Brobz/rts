/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

/**
 * Class to know if a player spawned a unit in the network context
 * @author brobz
 */
public class SpawnUnitObject {
    public int playerID, unitId, unitIndex, type;
    public String playerName;
    /**
     * Empty Constructor
     */
    public SpawnUnitObject() {
    }

    /**
     * Constructor or the SpawnUnitObject
     * @param playerID int for the id of the player spawning the unit
     * @param unitId int for the id of the unit being spawned
     * @param unitIndex itn for the idx of the unit being spawned
     * @param type int for designating the type of unit being spawned
     */
    public SpawnUnitObject(int playerID, int unitId, int unitIndex, int type, String playerName) {
        this.playerID = playerID;
        this.unitId = unitId;
        this.unitIndex = unitIndex;
        this.type = type;
        this.playerName = playerName;
    }
}