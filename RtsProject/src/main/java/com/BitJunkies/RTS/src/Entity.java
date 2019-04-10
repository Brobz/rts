/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import mikera.vectorz.*;

/**
 *
 * @author brobz
 */
public abstract class Entity {
    protected Vector2 dimension, position, velocity;
    protected Texture texture;
    
    public Entity(){
        dimension = Vector2.of(0, 0);
        position = Vector2.of(0, 0);
        velocity = Vector2.of(0, 0);
        texture = null;
    }
    
    public Entity(Vector2 dimension, Vector2 position, Texture texture){
        this.dimension = dimension;
        this.position = position;
        this.velocity = Vector2.of(0, 0);
        this.texture = texture;
    }
    
    public void tick(){
        position.add(velocity);
    }
    
    public void render(GL2 gl, Camera cam){
        texture = Assets.backgroundTexture;
        gl.glEnable(GL2.GL_TEXTURE_2D);
        
        Vector2 pos = cam.projectPosition(position);
        Vector2 dim = cam.projectDimension(dimension);
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
        gl.glTranslatef((float)pos.x,(float)pos.y, 0);
        
        gl.glColor4f(1, 1, 1, 1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(pos.x, pos.y);
        
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(pos.x, pos.y + dim.y);
        
        gl.glTexCoord2f(1, 1);        
        gl.glVertex2d(pos.x + dim.x, pos.y + dim.y);
        
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(pos.x + dim.x, pos.y);
        gl.glEnd();
        gl.glFlush();
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        gl.glTranslatef((float)-pos.x,(float)-pos.y, 0);
    }
}