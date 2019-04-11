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
 * @author Gibran Gonzalez
 */
public class Building extends Entity {
    private int maxHealth, health, cost;
    private boolean onCreateMode, usable;
    
    public Building(Vector2 dimension, Vector2 position, Player owner){
        super(dimension, position);
        onCreateMode = true;
        this.maxHealth = 1000;
        this.health = 1000;
        this.cost = 10;
        setOpacity((float).5);
        this.texture = Assets.casttleTexture;
    }
    
    public Building(Vector2 dimension, Vector2 position) {
        super(dimension, position);
    }
    
    @Override
    public void tick() {
        super.tick();
        if(usable && !onCreateMode){
            if(health <= 0) usable = false;
            else{
                //aqui van los cambios de textura con la health
            }
        }
    }
    
    public void render(GL2 gl, Camera cam) {
        if(usable){
            super.render(gl, cam);
        }
    }
    
   public void stopOnCreateMode(){
       onCreateMode = false;
       usable = true;
       setOpacity((float)1);
   }
   
   public void setPosition(Vector2 newPosition){
       position = newPosition;
   }
   
   public void singleAtack(int damage){
       health -= damage;
   }
   
   public int getCost(){
       return cost;
   }
}
