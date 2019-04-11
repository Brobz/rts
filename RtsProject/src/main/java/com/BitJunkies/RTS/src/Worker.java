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
    private Timer hitingResourceTimer;
    private boolean onMineCommand;
    private Resource targetMiningPatch;
    private int miningRange;
    public Worker(){
        super();
    }
    
    public Worker(Vector2 dimension, Vector2 position, Player owner){
       super(dimension, position, owner);
       texture = Assets.backgroundTexture;
       this.speed = 4;
       this.maxHealth = 10;
       this.health = this.maxHealth;
       this.damage = 100;
       this.attackSpeed = 0.5;
       this.range = 20;
       this.hitingResourceTimer = new Timer(Game.getFPS());
       hitingResourceTimer.setUp(1);
       this.miningRange = 100;
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
}
