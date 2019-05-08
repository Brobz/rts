/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

/**
 * Class to move an object in the network context
 * @author brobz
 */
public class MoveObject {
    public int playerID, entityID;
    public float xPosition, yPosition;
 
    /**
     * Empty Constructor
     */
    public MoveObject() {
    } 
 
    /**
     * Constructor for the MoveObject
     * @param playerID int for the id of the player moving the object
     * @param entityID int for the id of the entity being moved
     * @param xPosition int for the new x position
     * @param yPosition int for the new y position
     */
    public MoveObject(int playerID, int entityID, float xPosition, float yPosition) {
        this.playerID = playerID;
        this.entityID = entityID;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
}
