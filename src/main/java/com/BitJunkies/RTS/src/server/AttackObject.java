/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

/**
 * AttackObject class for doing attacks in the network conext
 * @author brobz
 */
public class AttackObject {
    public int playerID, unitID, targetPlayerID, targetUnitID, targetBuildingID;
    public String playerName, targetPlayerName;
    
    /**
     * Empty Constructor
     */
    public AttackObject() {
    }
    /**
     * Constructor for the AttackObject
     * @param playerID int for the id of the player doing the attack
     * @param unitID int for the id of the unit being used in the attack
     * @param targetPlayerID int for the id of the targetPlayer
     * @param targetUnitID int for the id of the target unit
     * @param targetBuildingID int for the id of the target building
     */
    public AttackObject(int playerID, int unitID, int targetPlayerID, int targetUnitID, int targetBuildingID, String playerName, String taretPlayerName) {
        this.playerID = playerID;
        this.unitID = unitID;
        this.targetPlayerID = targetPlayerID;
        this.targetUnitID = targetUnitID;
        this.targetBuildingID = targetBuildingID;
        this.playerName = playerName;
        this.targetPlayerName = targetPlayerName;
    }
}