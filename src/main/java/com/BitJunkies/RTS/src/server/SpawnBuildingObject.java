/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import java.util.ArrayList;

/**
 *
 * @author brobz
 */
public class SpawnBuildingObject {
    public int playerID, buildingIndex;
    public double xPos, yPos;
    public ArrayList<Integer> workerIDs;
    
    public SpawnBuildingObject() {
    } 
 
    public SpawnBuildingObject(int playerID, int buildingIndex, int xPos, int yPos, ArrayList<Integer> workerIDs) {
        this.playerID = playerID;
        this.buildingIndex = buildingIndex;
        this.xPos = xPos;
        this.yPos = yPos;
        this.workerIDs = workerIDs;
    }
}
