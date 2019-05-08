/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BuildingInfo Class to build in the network context
 * @author brobz
 */
public class BuildingInfoObject {
    public int playerID;
    public String playerName;
    public ConcurrentHashMap<Integer, ArrayList<Double>> buildingInfo;
    
    /**
     * Empty Constructor
     */
    public BuildingInfoObject() {
    }
    /**
     * Constructor for BuildingInfoObject
     * @param playerID int for the id of the player constructing
     * @param buildingInfo ArrayList for the information itself
     */
    public BuildingInfoObject(int playerID, ConcurrentHashMap<Integer, ArrayList<Double>> buildingInfo, String playerName) {
        this.playerID = playerID;
        this.buildingInfo = buildingInfo;
        this.playerName = playerName;
    }
}
