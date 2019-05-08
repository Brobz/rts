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
public class SpendRubysObject {
    public int playerID, amount;
    public String playerName;
 
    public SpendRubysObject() {
    } 
 
    public SpendRubysObject(int playerID, int amount, String playerName) {
        this.playerID = playerID;
        this.amount = amount;
        this.playerName = playerName;
    }
}
