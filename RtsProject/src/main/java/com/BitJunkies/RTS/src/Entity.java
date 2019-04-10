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
    private Vector2 dimension, position, velocity;
    private Texture texture;
    
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
    
    public void render(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_TEXTURE_2D);
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
        gl.glTranslatef((float)position.x,(float)position.y, 0);
        
        gl.glColor4f(0, 0, 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(position.x, position.y);
        
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(position.x, position.y + dimension.y);
        
        gl.glTexCoord2f(1, 0);        
        gl.glVertex2d(position.x + dimension.x, position.y + dimension.y);
        
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(position.x + dimension.x, position.y);
        gl.glEnd();
        gl.glFlush();
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        gl.glTranslatef((float)-position.x,(float)-position.y, 0);
    }
}