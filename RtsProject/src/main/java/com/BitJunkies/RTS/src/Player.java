/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

/**
 *
 * @author brobz
 */
//Player class, still not used but useful when setting up connections to host
public class Player {
    private int playerID, rubys;
    
    public Player(int playerID){
        this.playerID = playerID;
        this.rubys = 0;
    }
    
    public int getPlayerID(){
        return playerID;
    }
    
    public void getRubys(int rubys){
        this.rubys += rubys;
    }
    
    public boolean hasRubys(int rubys){
        return this.rubys >= rubys;
    }
    
    public void spendRubys(int rubys){
        this.rubys -= rubys;
    }
}
