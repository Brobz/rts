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
 *
 * @author rober
 */
public class Menu {
    protected Vector2 dimension, position;
    protected float opacity;
    protected Texture texture;
    protected Rectangle hitBox;
    
    public Menu(){
        dimension = Vector2.of(0, 0);
        position = Vector2.of(0, 0);
        updateHitBox();
        texture = null;
        opacity = 1;
    }
    
    public Menu(Vector2 dimension, Vector2 position){
        this.dimension = dimension;
        this.position = position;
        updateHitBox();
    }
    
    public void tick(){
        updateHitBox();
    }
    
    public void render(GL2 gl, Camera cam){
        //dibujamos el menu como tal
        Display.drawRectangleStatic(gl, cam, position.x, position.y, dimension.x, dimension.y, 0, 0, 0, (float)1);
    }
    
    public void updateHitBox(){
        hitBox = new Rectangle((int)(position.x - dimension.x / 2), (int)(position.y - dimension.y / 2), (int)dimension.x, (int)dimension.y);
    }
    
    public Rectangle getHitBox(){
        return hitBox;
    }
}