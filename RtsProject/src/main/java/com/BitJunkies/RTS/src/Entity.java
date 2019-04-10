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
        this.velocity = Vector2.of(0, 0);;
    }
    
    public void tick(){
        position.add(velocity);
    }
    
    public void render(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glColor3f(0, 0, 1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(position.x, position.y);
        gl.glVertex2d(position.x, position.y + dimension.y);
        gl.glVertex2d(position.x + dimension.x, position.y + dimension.y);
        gl.glVertex2d(position.x + dimension.x, position.y);
        gl.glEnd();
    }
}
