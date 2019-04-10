/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import mikera.vectorz.*;

/**
 *
 * @author brobz
 */
public abstract class Entity {
    private Vector2 dimension, position, velocity;
    
    public Entity(){
        dimension = Vector2.of(0, 0);
        position = Vector2.of(0, 0);
        velocity = Vector2.of(0, 0);
    }
    
    public Entity(Vector2 dimension, Vector2 position){
        this.dimension = dimension;
        this.position = position;
        this.velocity = Vector2.of(0, 0);
    }
    
    public void tick(){
        position.add(velocity);
    }
    
    public void render(GL2 gl, Camera cam){
        gl.glColor3f(0, 0, 1);
        gl.glBegin(GL2.GL_QUADS);
        Vector2 pos = cam.projectPosition(position);
        Vector2 dim = cam.projectDimension(dimension);
        gl.glVertex2d(pos.x, pos.y);
        gl.glVertex2d(pos.x, pos.y + dim.y);
        gl.glVertex2d(pos.x + dim.x, pos.y + dim.y);
        gl.glVertex2d(pos.x + dim.x, pos.y);
        gl.glEnd();
    }
}
