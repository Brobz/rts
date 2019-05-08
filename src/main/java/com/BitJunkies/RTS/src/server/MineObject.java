/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

/**
 * Class used for mining in the network context
 * @author brobz
 */
public class MineObject {
    public int playerID, workerID, resourceID;
    
    /**
     * Empty Constructor
     */
    public MineObject() {
    } 
 /**
  * Constructor for the MineObject
  * @param playerID int for the id of the player mining
  * @param workerID int for the id of the worker mining
  * @param resourceID int for the resource id being mined
  */
    public MineObject(int playerID, int workerID, int resourceID) {
        this.playerID = playerID;
        this.workerID = workerID;
        this.resourceID = resourceID;
    }
    
}
