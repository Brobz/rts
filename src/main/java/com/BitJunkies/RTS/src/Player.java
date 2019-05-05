/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.src.server.PlayerInfoObject;
import com.jogamp.opengl.GL2;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author brobz
 */
//Player class, still not used but useful when setting up connections to host
public class Player {
    private static int curr_id = -1;
    private int id, rubys;
    
    public ConcurrentHashMap<Integer, Unit> units;
    public ConcurrentHashMap<Integer, Building> buildings;
    //public ConcurrentHashMap<Integer, Building> 
    
    public Player(int id){
        this.id = id;
        this.rubys = 150;
        this.units = new ConcurrentHashMap<>();
        this.buildings = new ConcurrentHashMap<>();
    }
    
    public int getID(){
        return id;
    }
    
    public int getRubys(){
        return this.rubys;
    }
    
    public void giveRubys(int rubys){
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
    
    public static Integer getId(){
        curr_id++;
        return curr_id;
    }

    void updateInfo(PlayerInfoObject playerInfoObject) {
        this.rubys = playerInfoObject.rubys;
    }
}