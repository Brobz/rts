/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import java.awt.Rectangle;
import mikera.vectorz.Vector2;

/**
 * Menu parent class, the base of every menu in the game
 * @author rober
 */
public class Menu {
    //Basic menu variables to draw and interact with it
    protected Vector2 dimension, position;
    protected float opacity;
    protected Texture texture;
    protected Rectangle hitBox;
    
    /**
     * Constructor for the class
     */
    public Menu(){
        dimension = Vector2.of(0, 0);
        position = Vector2.of(0, 0);
        updateHitBox();
        texture = null;
        opacity = 1;
    }
    
    /**
     * Constructor for the class
     * @param dimension vector2 with width and height
     * @param position  vector2 with x and y
     */
    public Menu(Vector2 dimension, Vector2 position){
        this.dimension = dimension;
        this.position = position;
        updateHitBox();
    }
    
    /**
     * ticking for the game
     */
    public void tick(){
        updateHitBox();
    }
    
    /**
     * Draw elements of the objects in the display
     * @param gl
     * @param cam 
     */
    public void render(GL2 gl, Camera cam){
        //draw the menu itseld
        //Display.drawRectangleStatic(gl, cam, position.x, position.y, dimension.x, dimension.y, 0, 0, 0, (float)1);
    }
    
    /**
     * simple method to update the rectangle
     */
    public void updateHitBox(){
        hitBox = new Rectangle((int)(position.x - dimension.x / 2), (int)(position.y - dimension.y / 2), (int)dimension.x, (int)dimension.y);
    }
    
    /**
     * Getter of the hit box
     * @return Rectangle for click
     */
    public Rectangle getHitBox(){
        return hitBox;
    }
}