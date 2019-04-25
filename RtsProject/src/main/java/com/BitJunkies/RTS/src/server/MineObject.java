/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

/**
 *
 * @author brobz
 */
public class MineObject {
    public int workerID, resourceID;
    
    public MineObject() {
    } 
 
    public MineObject(int workerID, int resourceID) {
        this.workerID = workerID;
        this.resourceID = resourceID;
    }
    
}
