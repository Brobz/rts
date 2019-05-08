/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import java.awt.Rectangle;
import java.util.ArrayList;
import mikera.vectorz.Vector2;

/**
 *
 * @author Gibran Gonzalez
 */
public class Building extends Entity{
    public final static float MAX_MASK_B_RADIUS = 700;
    public float currMaskRad;
    
    //Building unique variables
    protected int maxHealth, health;
    protected boolean created, usable;
    protected Rectangle healthBar; //GUI health representation
    protected Player owner;
    public static int currDbId=0;
    
    public Building(Vector2 dimension, Vector2 position, int id, Player owner){
        super(dimension, position, id);
        this.healthBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 15), (int) dimension.x, 8);
        this.created = false;
        this.usable = true;
        this.health = 1;
        this.owner = owner;
        this.currMaskRad = (float)dimension.x + 100f;
        
        if(owner.getID() == Game.currPlayer.getID()) Assets.otherExplosionSound.play();
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
   public Vector2 getSpawningPosition(){
       return Vector2.of(position.x + dimension.x / 2 + Warrior.WARRIOR_WIDTH + 10, position.y);
   }
   
   
    @Override
    public void renderMask(GL2 gl, Camera cam){
        if(texture == null) return;
        if(created)
            Display.drawImageCentered(gl, cam, Assets.circleTexture, position.x, position.y, dimension.x + MAX_MASK_B_RADIUS, dimension.y + MAX_MASK_B_RADIUS, 1f);
        else{
            currMaskRad = (float)((dimension.x + 100f) + (MAX_MASK_B_RADIUS - (dimension.x + 100f)) * ((float)health / maxHealth));
            Display.drawImage(gl, cam, Assets.circleTexture, position.x - dimension.x / 2 - currMaskRad / 2, position.y - dimension.y / 2 - currMaskRad / 2, dimension.x + currMaskRad, dimension.y + currMaskRad, 1f);
        }
        
        //Display.drawRectangle(gl, cam, position.x - dimension.x/2 - 150, position.y - dimension.y / 2 - 150, dimension.x+300, dimension.y+300, 1f, 1f, 1f, 1f);
    }

    void updateInfo(ArrayList<Double> info) {
        // 0 -> Health
        // 1 -> Created
        // 2 -> Usable
        // 3 -> Type (0 - Castle, 1 - Barracks)
        // 4 -> xPos
        // 5 -> yPos
        // 6 -> Entity.curr_id
        
        if(cleanedUp) return;
        
        this.health = (int) Math.floor(info.get(0));
        if((int) Math.floor(info.get(1)) == 1)
            this.created = true;
        else
            this.created = false;
        if((int) Math.floor(info.get(2)) == 1)
            this.usable = true;
        else
            this.usable = false;
        
        if(!isAlive() && !cleanedUp){
            Game.map.deleteMap(this);
            this.hitBox = new Rectangle(0, 0, 0, 0);
            cleanedUp = true;
        }
        
        updateHitBox();
        healthBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 15), (int) dimension.x, 8);
    }
    
    public static int getCurrBuildingDbId(){
        currDbId++;
        return currDbId;
    }
    
    public int getHealth() {
        return health;
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public void setHealth(int h) {
        health = h;
    }
}