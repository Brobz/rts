/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import java.awt.Rectangle;
import mikera.vectorz.*;

/**
 * Basic unit class for all objects
 * @author brobz
 */
public abstract class Entity {
    public final static float MAX_MASK_RADIUS = 500;
    //Entity variables used to draw an entity itself
    public static int curr_id = -1;
    protected Vector2 dimension, position, velocity;
    protected float opacity;
    protected Texture texture;
    protected Rectangle hitBox;
    protected boolean cleanedUp;
    protected int id;
    
    /**
     * Constructor for the class
     */
    public Entity(){
        dimension = Vector2.of(0, 0);
        position = Vector2.of(0, 0);
        velocity = Vector2.of(0, 0);
        updateHitBox();
        texture = null;
        opacity = 1;
    }
    
    /**
     * Constructor for the class
     * @param dimension vector2 with width and height
     * @param position vector2 with x and y
     * @param id integer that identifies the object
     */
    public Entity(Vector2 dimension, Vector2 position, int id){
        this.dimension = dimension;
        this.position = position;
        this.velocity = Vector2.of(0, 0);
        this.opacity = (float) 1;
        this.id = id;
        this.cleanedUp = false;
        updateHitBox();
    }
    
    /**
     * Ticking of the object
     * @param map GridMap to update it
     */
    public void tick(GridMap map){
        //changing the place of the Entity in the screen
        map.deleteMap(this);
        position.add(velocity);
        updateHitBox();
        Game.map.updateMap(this);
    }
    
    /**
     * Method to change the place of the Entity in the screed
     * @param map GridMap to update it
     */
    public void tickRand(GridMap map){
        //changing the place of the Entity in the screen
        map.deleteMap(this);
        Vector2 randVel = Vector2.of((RandomGenerator.generate(0, 2) - 1) * dimension.x/2, (RandomGenerator.generate(0, 2) - 1 * dimension.y/2));
        position.add(randVel);
        updateHitBox();
        map.updateMap(this);
    }
    
    /**
     * Draw the objects of the entity
     * @param gl GL2 for opengl
     * @param cam Camera for the display
     */
    public void render(GL2 gl, Camera cam){
        if(texture == null) return;
        if(cam.hitBox.intersects(hitBox))
            Display.drawImageCentered(gl, cam, texture, position.x, position.y, dimension.x, dimension.y, (float)opacity);
        MiniMap.addToMap(this);
    }
    
    /**
     * Method to paint the fog in the display 
     * @param gl
     * @param cam 
     */
    public void renderMask(GL2 gl, Camera cam){
        if(texture == null) return;
        Display.drawImageCentered(gl, cam, Assets.circleTexture, position.x, position.y, dimension.x + 500, dimension.y + 500, 1f);
        //Display.drawRectangle(gl, cam, position.x - dimension.x/2 - 150, position.y - dimension.y / 2 - 150, dimension.x+300, dimension.y+300, 1f, 1f, 1f, 1f);
    }
    
    /**
     * Method to render animation of objects
     * @param gl
     * @param cam
     * @param contFrame currentFrame of the animation
     * @param direction 
     */
    public void renderAnimation(GL2 gl, Camera cam, int contFrame, int direction) {
        if(texture == null) return;
        Display.drawAnimation(gl, cam, texture, position.x, position.y, dimension.x, dimension.y, (float)opacity, contFrame, direction);
        MiniMap.addToMap(this);
    }
    
    /**
     * method to update the rectangle hit box for clicking it
     */
    public void updateHitBox(){
        hitBox = new Rectangle((int)(position.x - dimension.x / 2), (int)(position.y - dimension.y / 2), (int)dimension.x, (int)dimension.y);
    }
    
    /**
     * Getter for the hit box 
     * @return Rectangle
     */
    public Rectangle getHitBox(){
        return hitBox;
    }
    
    /**
     * Setter for the opacity
     * @param opacity Float
     */
    public void setOpacity(float opacity){
        this.opacity = opacity;
    }
        
    /**
     * Getter for the id of the object that gets a new one
     * @return Integer
     */
    public static Integer getId(){
        curr_id++;
        return curr_id;
    }
    
    /**
     * Getter of the entity id
     * @return 
     */
    public Integer getEntityId() {
        return id;
    }
}