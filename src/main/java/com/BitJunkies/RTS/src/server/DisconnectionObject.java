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
public class DisconnectionObject {
    public String connectionName;
 
    public DisconnectionObject() {
    } 
 
    public DisconnectionObject(String connectionName) {
        this.connectionName = connectionName;
    }
}
