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
public class MoveObject {
    public int playerID, entityID;
    public float xPosition, yPosition;
    public String playerName;
 
    public MoveObject() {
    } 
 
    public MoveObject(int playerID, int entityID, float xPosition, float yPosition, String playerName) {
        this.playerID = playerID;
        this.entityID = entityID;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.playerName = playerName;
    }
}
