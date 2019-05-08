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
public class MineObject {
    public int playerID, workerID, resourceID;
    public String playerName;
    
    public MineObject() {
    } 
 
    public MineObject(int playerID, int workerID, int resourceID, String playerName) {
        this.playerID = playerID;
        this.workerID = workerID;
        this.resourceID = resourceID;
        this.playerName = playerName;
    }
    
}
