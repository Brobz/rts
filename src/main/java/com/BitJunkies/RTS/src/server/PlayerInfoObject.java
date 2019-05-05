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
public class PlayerInfoObject {
    public int playerID, rubys;
 
    public PlayerInfoObject() {
    } 
 
    public PlayerInfoObject(int playerID, int rubys) {
        this.playerID = playerID;
        this.rubys = rubys;
    }
}
