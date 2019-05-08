/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author brobz
 */
public class BuildingInfoObject {
    public int playerID;
    public String playerName;
    public ConcurrentHashMap<Integer, ArrayList<Double>> buildingInfo;
 
    public BuildingInfoObject() {
    } 
 
    public BuildingInfoObject(int playerID, ConcurrentHashMap<Integer, ArrayList<Double>> buildingInfo, String playerName) {
        this.playerID = playerID;
        this.buildingInfo = buildingInfo;
        this.playerName = playerName;
    }
}
