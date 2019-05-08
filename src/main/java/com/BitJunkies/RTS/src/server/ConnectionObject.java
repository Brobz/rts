/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import com.esotericsoftware.kryonet.Connection;

/**
 *
 * @author brobz
 */
public class ConnectionObject {
    public int connectionID;
    public String connectionName, connectionIP;
    public boolean self;
    
    public ConnectionObject() {
    } 
 
    public ConnectionObject(int connectionID, String connectionName, String connectionIP, boolean self) {
        this.connectionID = connectionID;
        this.connectionName = connectionName;
        this.connectionIP = connectionIP;
        this.self = self;
    }
}
