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
 *
 * @author brobz
 */
//Basic unit class, Entity
public abstract class Entity {
    //Entity variables used to draw an entity itself
    private static int curr_id = -1;
    protected Vector2 dimension, position, velocity;
    protected float opacity;
    protected Texture texture;
    protected Rectangle hitBox;
    protected int id;
    
    public Entity(){
        dimension = Vector2.of(0, 0);
        position = Vector2.of(0, 0);
        velocity = Vector2.of(0, 0);
        updateHitBox();
        texture = null;
        opacity = 1;
    }
    
    public Entity(Vector2 dimension, Vector2 position, int id){
        this.dimension = dimension;
        this.position = position;
        this.velocity = Vector2.of(0, 0);
        this.opacity = (float) 1;
        this.id = id;
        updateHitBox();
    }
    
    public void tick(GridMap map){
        //changing the place of the Entity in the screen
        map.deleteMap(this);
        position.add(velocity);
        updateHitBox();
        map.updateMap(this);
    }
    
    public void tickRand(GridMap map){
        //changing the place of the Entity in the screen
        map.deleteMap(this);
        Vector2 randVel = Vector2.of((RandomGenerator.generate(0, 2) - 1) * dimension.x/2, (RandomGenerator.generate(0, 2) - 1 * dimension.y/2));
        position.add(randVel);
        updateHitBox();
        map.updateMap(this);
    }
    
    public void render(GL2 gl, Camera cam){
        if(texture == null) return;
        Display.drawImageCentered(gl, cam, texture, position.x, position.y, dimension.x, dimension.y, (float)opacity);
    }
    
    //method to update the rectangle hitbox
    public void updateHitBox(){
        hitBox = new Rectangle((int)(position.x - dimension.x / 2), (int)(position.y - dimension.y / 2), (int)dimension.x, (int)dimension.y);
    }
    
    public Rectangle getHitBox(){
        return hitBox;
    }
    
    public void setOpacity(float opacity){
        this.opacity = opacity;
    }
        
    public static Integer getId(){
        curr_id++;
        return curr_id;
    }
}