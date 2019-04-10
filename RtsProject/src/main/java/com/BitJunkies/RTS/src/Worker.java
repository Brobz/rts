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
    public Worker(){
        super();
    }
    
    public Worker(Vector2 dimension, Vector2 position, int owner){
       super(dimension, position, owner, Assets.backgroundTexture);
       this.speed = 10;
       this.maxHealth = 10;
       this.health = this.maxHealth;
       this.damage = 1;
       this.attackSpeed = 0.5;
       this.range = 2;
    }
    
    public void tick(){
        super.tick();
    }
    
    /*/
    public void mineAt(Resource resourcePatch){
    
    }
    /*/
    
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
    }
}
