/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import java.util.HashMap;

/**
 *
 * @author brobz
 */
//Player class, still not used but useful when setting up connections to host
public class Player {
    private int playerID, rubys;
    
    //public ArrayList<Unit> units;
    //public ArrayList<Building> buildings;
    
    public HashMap<Integer, Unit> units;
    public HashMap<Integer, Building> buildings;
    
    public Player(int playerID){
        this.playerID = playerID;
        this.rubys = 0;
        this.units = new HashMap<>();
        this.buildings = new HashMap<>();
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
    
    public void tickUnits(GridMap map){
        //unit tick
        for (Unit u : units.values()) {
            u.tick(map);
        }
    }
    
    public void tickBuildings(GridMap map){
        //buildings tick
        for (Building b : buildings.values()) {
            b.tick(map);
        }
    }
    
    public void renderBuildings(GL2 gl, Camera cam){
        //buildings render
        for (Building b : buildings.values()) {
            b.render(gl, cam);
        }
    }
    
    public void renderUnits(GL2 gl, Camera cam){
        //units render
        for (Unit u : units.values()) {
            u.render(gl, cam);
        }
    }
}
