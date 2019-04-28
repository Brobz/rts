/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import static com.BitJunkies.RTS.src.Game.currPlayer;
import static com.BitJunkies.RTS.src.Game.selectedUnits;
import com.BitJunkies.RTS.src.server.GameClient;
import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.HashMap;
import mikera.vectorz.Vector2;

/**
 *
 * @author brobz
 */

//Simple Worker class
public class Worker extends Unit{
    private static final int MINING_TOP = 3;
    //Worker unique variables
    public static final int WORKER_WIDTH = 40, WORKER_HEIGHT = 40;
    private Timer hitingResourceTimer,buildingCasttleTimer;
    private boolean onMineCommand;
    private boolean onBuildCommad;
    private Resource targetMiningPatch;
    private Building targetBuilding;
    private int miningRange;
    private int creationImpact;
    
    //mining stuff
    private int currMining;
    private boolean onBringResourcesBackCommand;
    private Building nearestMiningBuilding;
    
    public Worker(){
        super();
    }
    
    public Worker(Vector2 dimension, Vector2 position, int id){
       super(dimension, position, id);
       this.speed = 4;
       this.maxHealth = 10;
       this.health = this.maxHealth;
       this.damage = 2;
       this.attackSpeed = 0.5;
       this.range = regularRange;
       this.hitingResourceTimer = new Timer(Game.getFPS());
       hitingResourceTimer.setUp(1);
       this.buildingCasttleTimer = new Timer(Game.getFPS());
       buildingCasttleTimer.setUp(1);
       this.miningRange = 40;
       this.creationImpact = 70;
       this.texture = Assets.workerTexture;
       this.currMining = 0;
       this.onBringResourcesBackCommand = false;
    }
    
    public void tick(GridMap map){
        super.tick(map);
        
        //If the worker is designated to mine then...
        if(onMineCommand){
            if(currMining == MINING_TOP){
                onMineCommand = false;
                onBringResourcesBackCommand = true;
                System.out.println("go bring resources back");
                if(nearestMiningBuilding == null)
                    findNearesMiningBuilding();
                moveTo(nearestMiningBuilding.position);
            }
            
            //checking if the mining resource is still usable
            else if(!targetMiningPatch.isUsable()){
                stopMining();
                if(currMining != 0){
                    onBringResourcesBackCommand = true;
                    System.out.println("go bring resources back");
                    if(nearestMiningBuilding == null)
                        findNearesMiningBuilding();
                    moveTo(nearestMiningBuilding.position);
                }
            }
            //otherwise check its already mining
            else if(!onMoveCommand){
                if(hitingResourceTimer.doneWaiting()){
                    targetMiningPatch.singleAttack((int)damage);
                    hitingResourceTimer.setUp(1);
                    currMining ++;
                }
            }
        }
        //has to bring resources back
        else if(onBringResourcesBackCommand){
            //we are moving to the nearest building
            //if we've reached the building
            if(!onMoveCommand){
                Game.currPlayer.getRubys(currMining);
                currMining = 0;
                onBringResourcesBackCommand = false;
                if(targetMiningPatch != null){
                   onMineCommand = true;
                   moveTo(targetMiningPatch.position);
                   range = miningRange;
                }
            }
        }
        //If the worker is designated to build...
        else if(onBuildCommad){
            //check if the building is not built yet
            if(targetBuilding.isCreated())
                stopBuilding();
            //check if the worker is designated to build
            if(onBuildCommad){
                if(buildingCasttleTimer.doneWaiting()){
                    targetBuilding.singleCreation(creationImpact);
                    buildingCasttleTimer.setUp(1);
                }
            }
        }
    }
    
    //method to deretmine where to mine
    public void mineAt(int playerID, GameClient client, Resource resourcePatch){
        client.sendMineCommand(currPlayer.getID(), id, resourcePatch.id);
    }
    
    public void mineAt(Resource resourcePatch){
        onMineCommand = true;
        targetMiningPatch = resourcePatch;
        moveTo(resourcePatch.position);
        range = miningRange;
        nearestMiningBuilding = null;
    }
    
    //simple render method
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
    }
    
    //method to stop minning
    public void stopMining(){
        onMineCommand = false;
        targetMiningPatch = null;
        stopMoving();
        range = regularRange;
    }
    
    //method to tell worker where to go build
    public void buildAt(Building building){
        onBuildCommad = true;
        targetBuilding = building;
        moveTo(building.position);
        range = miningRange;
    }
    
    
    //method to tell worker to stop building
    public void stopBuilding(){
        onBuildCommad = false;
        targetBuilding = null;
        stopMoving();
        range = regularRange;
    }
    
    public void findNearesMiningBuilding(){
        //BFS
        //AQUI deberia ir una bfs
        HashMap<Integer, Building> currBuildings = Game.currPlayer.buildings;
        double distance = 10000000;
        for(Building build : currBuildings.values()){
            double currDist = position.distance(build.position);
            if(currDist < distance){
                nearestMiningBuilding = build;
                distance = currDist;
            }
        }
    }
}