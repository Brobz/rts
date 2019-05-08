/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

/**
 * BuildObject class to build objects in the network context
 * @author brobz
 */
public class BuildObject {
    public int playerID, workerID, targetID;
    public String playerName;
    
    /**
     * Empty Constructor
     */
    public BuildObject() {
    } 
    /**
     * Constructor for the BuildObject
     * @param playerID int for the id of the player constructing
     * @param workerID int for the if of the worker constructing
     * @param targetID int for the target id 
     */
    public BuildObject(int playerID, int workerID, int targetID, String playerName) {
        this.playerID = playerID;
        this.workerID = workerID;
        this.targetID = targetID;
        this.playerName = playerName;
    }
}
