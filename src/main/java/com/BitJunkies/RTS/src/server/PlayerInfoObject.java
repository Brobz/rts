/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class PlayerInfoObject for using player information int the network context
 * @author brobz
 */
public class PlayerInfoObject {
    public int playerID, rubys;
    public boolean hasFallen;
 
    /**
     * Empty Constructor
     */
    public PlayerInfoObject() {
    } 
 
    /**
     * Constructor for the Player Info Object
     * @param playerID int for the player id
     * @param rubys int for the rubies of the player
     * @param hasFallen boolean for determining if the player has lost
     */
    public PlayerInfoObject(int playerID, int rubys, boolean hasFallen) {
        this.playerID = playerID;
        this.rubys = rubys;
        this.hasFallen = hasFallen;
    }
}
