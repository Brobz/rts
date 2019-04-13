/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import mikera.vectorz.Vector2;

/**
 *
 * @author brobz
 */
public class Worker extends Unit{
    public static final int WORKER_WIDTH = 40, WORKER_HEIGHT = 40;
    private Timer hitingResourceTimer,buildingCasttleTimer;
    private boolean onMineCommand;
    private boolean onBuildCommad;
    private Resource targetMiningPatch;
    private Building targetBuilding;
    private int miningRange;
    private int creationImpact;
    public Worker(){
        super();
    }
    
    public Worker(Vector2 dimension, Vector2 position, Player owner){
       super(dimension, position, owner);
       this.speed = 4;
       this.maxHealth = 10;
       this.health = this.maxHealth;
       this.damage = 200;
       this.attackSpeed = 0.5;
       this.range = regularRange;
       this.hitingResourceTimer = new Timer(Game.getFPS());
       hitingResourceTimer.setUp(1);
       this.buildingCasttleTimer = new Timer(Game.getFPS());
       buildingCasttleTimer.setUp(1);
       this.miningRange = 40;
       this.creationImpact = 70;
       this.texture = Assets.workerTexture;
    }
    
    public void tick(){
        super.tick();
        
        if(onMineCommand){
            if(!targetMiningPatch.isUsable())
                stopMining();    
            else if(!onMoveCommand){
                if(hitingResourceTimer.doneWaiting()){
                    targetMiningPatch.singleAttack((int)damage);
                    hitingResourceTimer.setUp(1);
                }
            }
        }
        else if(onBuildCommad){
            if(targetBuilding.isCreated())
                stopBuilding();
            if(onBuildCommad){
                if(buildingCasttleTimer.doneWaiting()){
                    targetBuilding.singleCreation(creationImpact);
                    buildingCasttleTimer.setUp(1);
                }
            }
        }
    }
    
    //method to deretmine where to mine
    public void mineAt(Resource resourcePatch){
        onMineCommand = true;
        targetMiningPatch = resourcePatch;
        moveTo(resourcePatch.position);
        range = miningRange;
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
    
    public void buildAt(Building building){
        onBuildCommad = true;
        targetBuilding = building;
        moveTo(building.position);
        range = miningRange;
    }
    public void stopBuilding(){
        onBuildCommad = false;
        targetBuilding = null;
        stopMoving();
        range = regularRange;
    }
}
