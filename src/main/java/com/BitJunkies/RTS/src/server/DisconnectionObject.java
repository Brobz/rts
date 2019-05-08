/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import com.esotericsoftware.kryonet.Connection;

/**
 * DisconnectionObjectClass to setup disconnection in the networkd
 * @author brobz
 */
public class DisconnectionObject {
    public int connectionID;
    public String connectionName;
    /**
     * Empty Constructor
     */
    public DisconnectionObject() {
    } 
 
    /**
     * DisconnectionObject constructor
     * @param connectionID int id for the connection id
     */
    public DisconnectionObject(String connectionName) {
        this.connectionName = connectionName;
    }
}
