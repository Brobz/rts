/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import com.esotericsoftware.kryonet.Connection;

/**
 * ConnectionObject class to monitor connections to the host
 * @author brobz
 */
public class ConnectionObject {
    public int connectionID;
    public String connectionName, connectionIP;
    public boolean self;
    
    /**
     * Empty Constructor
     */
    public ConnectionObject() {
    } 
 
    
    /**
     * ConnectionObject Constructor
     * @param connectionID int for the id of the connection
     * @param connectionName String for the name of the connection
     * @param connectionIP String for the ip of the connection
     * @param self boolean for setting up the connection
     */
    public ConnectionObject(int connectionID, String connectionName, String connectionIP, boolean self) {
        this.connectionID = connectionID;
        this.connectionName = connectionName;
        this.connectionIP = connectionIP;
        this.self = self;
    }
}
