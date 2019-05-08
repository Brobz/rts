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
 * Parent class for every building entity
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
    
    /**
     *
     * @param dimension vector2 with width and height
     * @param position vector2 with x and y
     * @param id integer identifying the building
     * @param owner Player that owns the building
     */
    public Building(Vector2 dimension, Vector2 position, int id, Player owner){
        super(dimension, position, id);
        this.healthBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 15), (int) dimension.x, 8);
        this.created = false;
        this.usable = true;
        this.health = 1;
        this.owner = owner;
        this.currMaskRad = (float)dimension.x + 100f;
        
        if(Game.currPlayer != null && owner.getID() == Game.currPlayer.getID()) Assets.otherExplosionSound.play();
    }
    
    /**
     * ticking method
     * @param map
     */
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
    
    /**
     * Draw the objects
     * @param gl
     * @param cam
     */
    @Override
    public void render(GL2 gl, Camera cam) {
        if(isAlive()){
            super.render(gl, cam);
            if(health < maxHealth){
                drawHealthBar(gl, cam);
            }
        }
    }
    

    /**
     * method to convert a building to a building itself getting it out of the creating mode
     */
   public void stopOnCreateMode(){
       created = true;
       usable = true;
       setOpacity((float)1);
   }
   
    /**
     * Method the set the position of the building
     * @param newPosition vector2 with x and y
     */
    public void setPosition(Vector2 newPosition){
       position = newPosition;
   }
   

    /**
     * method to set damage to building
     * @param damage
     */
   public void singleAttack(double damage){
       health -= damage;
   }
   
    /**
     * Getter of the attribute created
     * @return boolean if the attribute is already created
     */
    public boolean isCreated(){
       return created;
   }
   

    /**
     * method for workers to increase the creation level of the building
     * @param creationImpact
     */
   public void singleCreation(int creationImpact){
       health += creationImpact;
   }
   
    /**
     * Getter of the attribute usable
     * @return boolean if the building is usable for build
     */
    public boolean isUsable(){
       return usable;
   }
   
    /**
     * Method to draw the health bar of the building
     * @param gl
     * @param camera 
     */
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
   
    /**
     * Get if the building is alive
     * @return boolean
     */
    public boolean isAlive(){
        return health > 0;
   }

    /**
     * Getter for the spawning position of the building
     * @return vector2 with x and y
     */
    public Vector2 getSpawningPosition(){
       return Vector2.of(position.x + dimension.x / 2 + Warrior.WARRIOR_WIDTH + 10, position.y);
   }
   
    /**
     * Method to remove the fog of the map
     * @param gl
     * @param cam
     */
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

    /**
     * Method to update all attributes of the building
     * @param info 
     */
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
    
    /**
     * Getter for the id of the building
     * @return integer with the id
     */
    public static int getCurrBuildingDbId(){
        currDbId++;
        return currDbId;
    }
    /**
     * Getter for the health of the building
     * @return integer with the health
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * Getter for the owner of the building
     * @return Player that built the building
     */
    public Player getOwner() {
        return owner;
    }
    
    /**
     * Getter for the maxHealth of the building 
     * @return integer of the maxHealth
     */
    public int getMaxHealth() {
        return maxHealth;
    }
    
    /**
     * Setter for the health of the building
     * @param h integer to the current head
     */
    public void setHealth(int h) {
        health = h;
    }
}