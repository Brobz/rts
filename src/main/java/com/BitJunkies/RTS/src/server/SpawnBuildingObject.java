/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import java.util.ArrayList;

/**
 * Class to spawn buildings in the network context
 * @author brobz
 */
public class SpawnBuildingObject {
    public int playerID, buildingIndex;
    public double xPos, yPos;
    public String playerName;
    public ArrayList<Integer> workerIDs;
    
    /**
     * Empty Constructor
     */
    public SpawnBuildingObject() {
    }
    
    /** 
     * Constructor for the SpawnBuildingObject
     * @param playerID int player id spawning building
     * @param buildingIndex int for the idx of the building
     * @param xPos int for the x position of the building
     * @param yPos int for the y position of the building
     * @param workerIDs int ArrayList for the ids of the workers going to construct the building
     */
    public SpawnBuildingObject(int playerID, int buildingIndex, int xPos, int yPos, ArrayList<Integer> workerIDs, String playerName) {
        this.playerID = playerID;
        this.buildingIndex = buildingIndex;
        this.xPos = xPos;
        this.yPos = yPos;
        this.workerIDs = workerIDs;
        this.playerName = playerName;
    }
}
