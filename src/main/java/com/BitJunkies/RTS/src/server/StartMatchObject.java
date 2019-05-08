/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

/**
 * StartMatchObject to know in a network context if the match has started yet
 * @author brobz
 */
public class StartMatchObject {
    public int playerID;
    /**
     * Empty constructor
     */
    public StartMatchObject() {
    } 
    /**
     * Constructor for StartMatchObject
     * @param playerID int for the id of the player starting the match
     */
    public StartMatchObject(int playerID) {
        this.playerID = playerID;
    }
}
