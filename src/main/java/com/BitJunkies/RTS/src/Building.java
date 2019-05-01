/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import java.awt.Rectangle;
import mikera.vectorz.Vector2;

/**
 *
 * @author Gibran Gonzalez
 */
public class Building extends Entity{
    //Building unique variables
    protected int maxHealth, health;
    protected boolean created, usable;
    protected Rectangle healthBar; //GUI health representation
    
    public Building(Vector2 dimension, Vector2 position, int id){
        super(dimension, position, id);
        this.healthBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 15), (int) dimension.x, 8);
        this.created = false;
        this.usable = true;
        this.health = 1;
    }
    
    @Override
    public void tick(GridMap map) {
        if(!isAlive() && !cleanedUp){
            map.deleteMap(this);
            this.hitBox = new Rectangle(0, 0, 0, 0);
            cleanedUp = true;
        }
        if(cleanedUp) return;
        super.tick(map);
        if(health <= 0 && created) usable = false;
        //check if it wasnt created yet
        healthBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 15), (int) dimension.x, 8);
        if(!created){
            if(health >= maxHealth){
                stopOnCreateMode();
            }
            else setOpacity((float) ((float)(health * .9 / maxHealth) + .1));
        }
    }
    
    public void render(GL2 gl, Camera cam) {
        if(isAlive()){
            super.render(gl, cam);
            if(health < maxHealth){
                drawHealthBar(gl, cam);
            }
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
   public void singleAttack(double damage){
       health -= damage;
   }
   
   public boolean isCreated(){
       return created;
   }
   
   //method for workers to increase the creation level of the building
   public void singleCreation(int creationImpact){
       health += creationImpact;
   }
   
   public boolean isUsable(){
       return usable;
   }
   
   private void drawHealthBar(GL2 gl, Camera camera){
        gl.glColor4f(0.85f, 0, 0, 1f);
             gl.glBegin(GL2.GL_QUADS);
             gl.glVertex2d(healthBar.x - camera.position.x, healthBar.y - camera.position.y);
             gl.glVertex2d(healthBar.x - camera.position.x, healthBar.y + healthBar.height - camera.position.y);       
             gl.glVertex2d(healthBar.x + healthBar.width - camera.position.x, healthBar.y + healthBar.height - camera.position.y);
             gl.glVertex2d(healthBar.x + healthBar.width - camera.position.x, healthBar.y - camera.position.y);
        gl.glEnd();
        
        gl.glColor4f(0, 0.85f, 0, 1f);
             gl.glBegin(GL2.GL_QUADS);
             gl.glVertex2d(healthBar.x - camera.position.x, healthBar.y - camera.position.y);
             gl.glVertex2d(healthBar.x - camera.position.x, healthBar.y + healthBar.height - camera.position.y);       
             gl.glVertex2d(healthBar.x + (healthBar.width * health / maxHealth) - camera.position.x, healthBar.y + healthBar.height - camera.position.y);
             gl.glVertex2d(healthBar.x + (healthBar.width * health / maxHealth) - camera.position.x, healthBar.y - camera.position.y);
        gl.glEnd();
    }
   
   public boolean isAlive(){
        return health > 0;
    }
}