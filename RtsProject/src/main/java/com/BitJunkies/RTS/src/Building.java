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
    //Building unique variables
    public static final int BUILDING_WIDTH = 100, BUILDING_HEIGHT = 100;
    private int maxHealth, health, cost;
    private boolean created, usable;
    float creatingLife;
    
    public Building(Vector2 dimension, Vector2 position, Player owner){
        super(dimension, position);
        this.created = false;
        this.maxHealth = 1000;
        this.health = 1000;
        this.cost = 10;
        this.usable = true;
        this.texture = Assets.casttleTexture;
        this.creatingLife = 0;
    }
    
    @Override
    public void tick() {
        super.tick();
        //check if buiding is usable
        if(usable && created){
            if(health <= 0) usable = false;
            else{
                //here goes health changes
            }
        }
        //check if it wasnt created yet
        else if(!created){
            if(creatingLife >= 1000){
                stopOnCreateMode();
            }
            else setOpacity((float) ((float)(creatingLife * .9 / 1000) + .1));
        }
    }
    
    public void render(GL2 gl, Camera cam) {
        if(usable){
            super.render(gl, cam);
        }
    }
    
   //method to convert a building to a building itself getting it out of the creating mode
   public void stopOnCreateMode(){
       created = true;
       usable = true;
       setOpacity((float)1);
   }
   
   public void setPosition(Vector2 newPosition){
       position = newPosition;
   }
   
   //method to set damage to building
   public void singleAtack(int damage){
       health -= damage;
   }
   
   public boolean isCreated(){
       return created;
   }
   
   //method for workers to increase the creation level of the building
   public void singleCreation(int creationImpact){
       creatingLife += creationImpact;
   }
}
