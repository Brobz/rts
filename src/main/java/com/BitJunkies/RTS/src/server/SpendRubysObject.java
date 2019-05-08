/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

/**
 * Class to know if a player spent its rubies in the network context
 * @author brobz
 */
public class SpendRubysObject {
    public int playerID, amount;
    public String playerName;
 
    /**
     * Empty Constructor
     */
    public SpendRubysObject() {
    }
    
    /**
     * Constructor
     * @param playerID int for the player spending rubies
     * @param amount int for the amount of rubies spent
     */
    public SpendRubysObject(int playerID, int amount, String playerName) {
        this.playerID = playerID;
        this.amount = amount;
        this.playerName = playerName;
    }
}