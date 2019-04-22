/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import java.util.ArrayList;

/**
 *
 * @author brobz
 */
//Player class, still not used but useful when setting up connections to host
public class Player {
    private int playerID, rubys;
    
    public ArrayList<Unit> units;
    public ArrayList<Building> buildings;
    
    public Player(int playerID){
        this.playerID = playerID;
        this.rubys = 0;
        this.units = new ArrayList<Unit>();
        this.buildings = new ArrayList<Building>();
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
    
    public void tickUnits(){
        //unit tick
        for(int i = 0; i < units.size(); i++){
            units.get(i).tick();
        }
    }
    
    public void tickBuildings(){
        //buildings tick
        for(int i = 0; i < buildings.size(); i++){
           buildings.get(i).tick();
        }
    }
    
    public void renderBuildings(GL2 gl, Camera cam){
        //buildings render
        for(int i = 0; i < buildings.size(); i++){
           buildings.get(i).render(gl, cam);   
        }
    }
    
    public void renderUnits(GL2 gl, Camera cam){
        //units render
        for(int i = 0; i < units.size(); i++){
            units.get(i).render(gl, cam);   
        }
    }
}
