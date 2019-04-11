/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import java.awt.Rectangle;
import java.util.concurrent.atomic.AtomicInteger;
import mikera.vectorz.Vector2;

/**
 *
 * @author rober
 */
public class Menu{
    public static final int CREATE_CASTTLE = 1;
    public static final int CREATE_BUILDER = 2;
    public static final int CREATE_WARRIOR = 3;
    public static final int CREATE_NOTHING = 0;
    protected Vector2 dimension, position, velocity;
    
    private AtomicInteger casttleCount;
    private AtomicInteger buildersCount;
    private AtomicInteger warriorsCount;
    private int spacingLeft, spacingTop, widthItem, heightItem;
    private Rectangle casttleHitBox, buildersHitBox, warriorsHitBox;
    
    public Menu(){
    }
    
    public Menu(Vector2 dimension, Vector2 position, AtomicInteger casttleCount, AtomicInteger buildersCount, AtomicInteger warriorsCount){
        this.dimension = dimension;
        this.position = position;
        this.velocity = Vector2.of(0, 0);
        this.casttleCount = casttleCount;
        this.buildersCount = buildersCount;
        this.warriorsCount = warriorsCount;
        this.spacingLeft = 20;
        this.spacingTop = 10;
        this.widthItem = 80;
        this.heightItem = 80;
    }
    
     public void tick(){
    }
    
    public void render(GL2 gl, Camera cam){
        
        //dibujamos el menu como tal
        gl.glEnable(GL2.GL_TEXTURE_2D);
        Vector2 pos = position;
        Vector2 dim = dimension;
        
        gl.glColor4f((float).5, 1, 1, 1);
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
        
        //dibujamos cada tipo de unit y despues cada building
        int currSpacing = 0;
        if(!buildersCount.equals(0)){
            gl.glEnable(GL2.GL_TEXTURE_2D);

            gl.glBindTexture(GL2.GL_TEXTURE_2D, Assets.backgroundTexture.getTextureObject());
            
            gl.glColor4f(1, 1, 1, 1);
            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0, 0);
            gl.glVertex2d(pos.x + spacingLeft + currSpacing, pos.y + spacingTop);

            gl.glTexCoord2f(0, 1);
            gl.glVertex2d(pos.x + spacingLeft + currSpacing, pos.y + spacingTop + heightItem);

            gl.glTexCoord2f(1, 1);        
            gl.glVertex2d(pos.x + spacingLeft + currSpacing + widthItem, pos.y + spacingTop + heightItem);

            gl.glTexCoord2f(1, 0);
            gl.glVertex2d(pos.x + spacingLeft + currSpacing + widthItem, pos.y + spacingTop);
            gl.glEnd();
            gl.glFlush();
            
            buildersHitBox = new Rectangle((int)pos.x + spacingLeft + currSpacing, (int)pos.y + spacingTop, (int)widthItem, (int)heightItem);
            
            gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
            currSpacing += spacingLeft + widthItem;
        }
        if(!warriorsCount.equals(0)){
            gl.glEnable(GL2.GL_TEXTURE_2D);

            gl.glBindTexture(GL2.GL_TEXTURE_2D, Assets.backgroundTexture.getTextureObject());
            
            gl.glColor4f(1, 1, 1, 1);
            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0, 0);
            gl.glVertex2d(pos.x + spacingLeft + currSpacing, pos.y + spacingTop);

            gl.glTexCoord2f(0, 1);
            gl.glVertex2d(pos.x + spacingLeft + currSpacing, pos.y + spacingTop + heightItem);

            gl.glTexCoord2f(1, 1);        
            gl.glVertex2d(pos.x + spacingLeft + currSpacing + widthItem, pos.y + spacingTop + heightItem);

            gl.glTexCoord2f(1, 0);
            gl.glVertex2d(pos.x + spacingLeft + currSpacing + widthItem, pos.y + spacingTop);
            gl.glEnd();
            gl.glFlush();

            warriorsHitBox = new Rectangle((int)pos.x + spacingLeft + currSpacing, (int)pos.y + spacingTop, (int)widthItem, (int)heightItem);
            
            gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
            currSpacing += spacingLeft + widthItem;
        }
        if(!casttleCount.equals(0)){
            gl.glEnable(GL2.GL_TEXTURE_2D);

            gl.glBindTexture(GL2.GL_TEXTURE_2D, Assets.casttleTexture.getTextureObject());
            
            gl.glColor4f(1, 1, 1, 1);
            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0, 0);
            gl.glVertex2d(pos.x + spacingLeft + currSpacing, pos.y + spacingTop);

            gl.glTexCoord2f(0, 1);
            gl.glVertex2d(pos.x + spacingLeft + currSpacing, pos.y + spacingTop + heightItem);

            gl.glTexCoord2f(1, 1);        
            gl.glVertex2d(pos.x + spacingLeft + currSpacing + widthItem, pos.y + spacingTop + heightItem);

            gl.glTexCoord2f(1, 0);
            gl.glVertex2d(pos.x + spacingLeft + currSpacing + widthItem, pos.y + spacingTop);
            gl.glEnd();
            gl.glFlush();

            casttleHitBox = new Rectangle((int)pos.x + spacingLeft + currSpacing, (int)pos.y + spacingTop, (int)widthItem, (int)heightItem);
            
            gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
            currSpacing += spacingLeft + widthItem;
        }
    }
    
    public int checkPress(Rectangle mouseHitBox){
        if(!buildersCount.equals(0)){
            if(buildersHitBox.intersects(mouseHitBox)) return CREATE_BUILDER;
        }
        if(!warriorsCount.equals(0)){
            if(warriorsHitBox.intersects(mouseHitBox)) return CREATE_WARRIOR;
        }
        if(!casttleCount.equals(0)){
            if(casttleHitBox.intersects(mouseHitBox)) return CREATE_CASTTLE;
        }
        return CREATE_NOTHING;
    }
}