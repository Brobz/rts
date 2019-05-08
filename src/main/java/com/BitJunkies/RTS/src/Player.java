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
    private String username;
    private String password;
    private boolean hasLost;
    private boolean killedUnits;
    
    public ConcurrentHashMap<Integer, Unit> units;
    public ConcurrentHashMap<Integer, Building> buildings;
    //public ConcurrentHashMap<Integer, Building> 
    
    public Player(int id){
        this.id = id;
        this.rubys = 150;
        this.units = new ConcurrentHashMap<>();
        this.buildings = new ConcurrentHashMap<>();
        hasLost = false;
        killedUnits = false;
    }
    
    public void renderMasks(GL2 gl, Camera cam){
        for(Unit u : units.values()){
            u.renderMask(gl, cam);
        }
        for(Building b : buildings.values()){
            b.renderMask(gl, cam);
        }
    }
    
    public int getID(){
        return id;
    }
    
    public int getRubys(){
        return this.rubys;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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
        boolean checkLost = true;
        for (Building b : buildings.values()) {
            b.tick(map);
            
            if (b.getHealth() > 0)
                checkLost = false;
        }
        hasLost = checkLost;
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
    
    public void killUnits() {
        for (Unit u : units.values()) {
            u.setHealth(0);
        }
        killedUnits = true;
    }
    
    public static Integer getId(){
        curr_id++;
        return curr_id;
    }
    
    public ConcurrentHashMap<Integer, Unit> getPlayerUnits() {
        return units;
    }
    
    public ConcurrentHashMap<Integer, Building> getPlayerBuildings() {
        return buildings;
    }

    void updateInfo(PlayerInfoObject playerInfoObject) {
        this.rubys = playerInfoObject.rubys;
    }
    
    public boolean hasLost() {
        return hasLost;
    }
    
    public boolean hasKilledUnits() {
        return killedUnits;
    }
}