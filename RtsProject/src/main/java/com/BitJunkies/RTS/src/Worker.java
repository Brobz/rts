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
    public Worker(){
        super();
    }
    
    public Worker(Vector2 dimension, Vector2 position, int owner){
       super(dimension, position, owner, Assets.backgroundTexture);
       this.speed = 5;
       this.maxHealth = 10;
       this.health = this.maxHealth;
       this.damage = 100;
       this.attackSpeed = 0.5;
       this.range = 70;
       this.hitingResourceTimer = new Timer(Game.getFPS());
       hitingResourceTimer.setUp(.5);
    }
    
    public void tick(){
        super.tick();
        if(targetMiningPatch == null || !targetMiningPatch.isUsable()){
            stopMining();
        }
        if(onMineCommand){
            if(targetMiningPatch.position.distance(this.position) > range){
                moveTo(targetMiningPatch.position);
            }
            else{
                if(hitingResourceTimer.doneWaiting()){
                    targetMiningPatch.singleAttack((int)damage);
                    hitingResourceTimer.setUp(.5);
                }
            }
        }
    }
    
    public void mineAt(Resource resourcePatch){
        targetMiningPatch = resourcePatch;
        onMineCommand = true;
    }
    
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
    }
    
    public void stopMining(){
        onMineCommand = false;
        targetMiningPatch = null;
    }
}
