/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import com.jogamp.opengl.GL2;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import mikera.vectorz.Vector2;

/**
 *
 * @author rober
 */
public class Menu{

    private Vector2 dimension, position, velocity;
    private boolean creatingCasttle, creatingWarrior, creatingBuilder;
    
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
        this.creatingCasttle = this.creatingWarrior = this.creatingBuilder = false;
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

            gl.glBindTexture(GL2.GL_TEXTURE_2D, Assets.workerTexture.getTextureObject());
            
            gl.glColor4f(1, 1, 1, (float)1);
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

            gl.glBindTexture(GL2.GL_TEXTURE_2D, Assets.warriorTexture.getTextureObject());
            
            gl.glColor4f(1, 1, 1, (float)1);
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
            
            gl.glColor4f(1, 1, 1, (float)1);
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
        //rendering creation if neccesary
        if(creatingCasttle) createCasttle(gl, cam);
        if(creatingBuilder) createBuilder(gl, cam);
        if(creatingWarrior) createWarrior(gl, cam);
    }
    
    public boolean checkPress(Rectangle mouseHitBox){
        if(!buildersCount.equals(0)){
            if(buildersHitBox.intersects(mouseHitBox)){
                System.out.println("buildersPress");
                creatingBuilder = true;
                return true;
            }
        }
        if(!warriorsCount.equals(0)){
            if(warriorsHitBox.intersects(mouseHitBox)){
                System.out.println("warriorPress");
                creatingWarrior = true;
                return true;
            }
        }
        if(!casttleCount.equals(0)){
            if(casttleHitBox.intersects(mouseHitBox)){
                System.out.println("casttlePress");
                creatingCasttle = true;
                return true;
            }
        }
        return false;
    }
    
    public void createCasttle(GL2 gl, Camera cam){
        gl.glEnable(GL2.GL_TEXTURE_2D);
        Vector2 pos = cam.projectPosition(Vector2.of(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y));
        Vector2 dim = cam.projectDimension(Vector2.of(Building.BUILDING_WIDTH, Building.BUILDING_HEIGHT));

        gl.glBindTexture(GL2.GL_TEXTURE_2D, Assets.casttleTexture.getTextureObject());

        gl.glColor4f(1, 1, 1, (float).4);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(pos.x - dim.x / 2, pos.y - dim.y / 2);

        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(pos.x - dim.x / 2, pos.y + dim.y / 2);

        gl.glTexCoord2f(1, 1);        
        gl.glVertex2d(pos.x + dim.x / 2, pos.y + dim.y / 2);

        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(pos.x + dim.x / 2, pos.y - dim.y / 2);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
    }
    
    public void createBuilder(GL2 gl, Camera cam){
        gl.glEnable(GL2.GL_TEXTURE_2D);
        Vector2 pos = cam.projectPosition(Vector2.of(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y));
        Vector2 dim = cam.projectDimension(Vector2.of(Worker.WORKER_WIDTH, Worker.WORKER_HEIGHT));

        gl.glBindTexture(GL2.GL_TEXTURE_2D, Assets.workerTexture.getTextureObject());

        gl.glColor4f(1, 1, 1, (float).4);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(pos.x - dim.x / 2, pos.y - dim.y / 2);

        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(pos.x - dim.x / 2, pos.y + dim.y / 2);

        gl.glTexCoord2f(1, 1);        
        gl.glVertex2d(pos.x + dim.x / 2, pos.y + dim.y / 2);

        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(pos.x + dim.x / 2, pos.y - dim.y / 2);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
    }
        
    public void createWarrior(GL2 gl, Camera cam){
        gl.glEnable(GL2.GL_TEXTURE_2D);
        Vector2 pos = cam.projectPosition(Vector2.of(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y));
        Vector2 dim = cam.projectDimension(Vector2.of(Warrior.WARRIOR_WIDTH, Warrior.WARRIOR_HEIGHT));
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, Assets.warriorTexture.getTextureObject());

        gl.glColor4f(1, 1, 1, (float).4);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(pos.x - dim.x / 2, pos.y - dim.y / 2);

        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(pos.x - dim.x / 2, pos.y + dim.y / 2);

        gl.glTexCoord2f(1, 1);        
        gl.glVertex2d(pos.x + dim.x / 2, pos.y + dim.y / 2);

        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(pos.x + dim.x / 2, pos.y - dim.y / 2);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
    }
    
    public boolean isCreatingCasttle() {
        return creatingCasttle;
    }

    public boolean isCreatingWarrior() {
        return creatingWarrior;
    }

    public boolean isCreatingBuilder() {
        return creatingBuilder;
    }
    
    public void stopCreatingCasttle(ArrayList<Building> buildings, Player player){
        System.out.println("stop creating casttle");
        buildings.add(new Building(Vector2.of(Building.BUILDING_WIDTH, Building.BUILDING_HEIGHT), Vector2.of(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y), player));
        creatingCasttle = false;
    }
    
    public void stopCreatingWorker(ArrayList<Unit> units, Player player){
        System.out.println("stop creating worker");
        units.add(new Worker(Vector2.of(Worker.WORKER_WIDTH, Worker.WORKER_HEIGHT), Vector2.of(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y), player));
        creatingBuilder = false;
    }
    
    public void stopCreatingWarrior(ArrayList<Unit> units, Player player){
        System.out.println("stop creating warrior");
        units.add(new Warrior(Vector2.of(Warrior.WARRIOR_WIDTH, Warrior.WARRIOR_HEIGHT), Vector2.of(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y), player));
        creatingWarrior = false;
    }
}