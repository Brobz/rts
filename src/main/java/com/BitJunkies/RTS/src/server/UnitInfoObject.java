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
public class UnitInfoObject {
    public int playerID;
    public ConcurrentHashMap<Integer, ArrayList<Double>> unitInfo;
 
    public UnitInfoObject() {
    } 
 
    public UnitInfoObject(int playerID, ConcurrentHashMap<Integer, ArrayList<Double>> unitInfo) {
        this.playerID = playerID;
        this.unitInfo = unitInfo;
    }
}
