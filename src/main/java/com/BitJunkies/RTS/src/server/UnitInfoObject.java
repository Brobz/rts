/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that holds the information of a unit networkwise
 * @author brobz
 */
public class UnitInfoObject {
    public int playerID;
    public String playerName;
    public ConcurrentHashMap<Integer, ArrayList<Double>> unitInfo;
 
    /**
     * Empty constructor
     */
    public UnitInfoObject() {
    } 
 
    /**
     * Constructor for the UnitInfoObject
     * @param playerID int for the id of the player owner of the unit
     * @param unitInfo ConcurrentHashMap for the unit information itself
     */
    public UnitInfoObject(int playerID, ConcurrentHashMap<Integer, ArrayList<Double>> unitInfo, String playerName) {
        this.playerID = playerID;
        this.unitInfo = unitInfo;
        this.playerName = playerName;
    }
}
