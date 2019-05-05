/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import java.awt.Rectangle;
import mikera.vectorz.Vector2;

/**
 *
 * @author brobz
 */
public class Castle extends Building{
    public static final int timeCreateWorker = 7;
    public static final int CASTLE_WIDTH = 85, CASTLE_HEIGHT = 85, RUBY_COST = 150;
    protected Rectangle spawnBar;
    protected boolean creatingWorker;
    protected float creatingWorkerPercentage;
    protected Timer creatingWorkerTimer;

    public Castle(Vector2 dimension, Vector2 position, int id, Player owner) {
        super(dimension, position, id, owner);
        this.maxHealth = 150;
        this.texture = Assets.casttleTexture;
        this.creatingWorker = false;
        this.creatingWorkerPercentage = (float) 0.0;
        this.creatingWorkerTimer = new Timer(Game.getFPS());
        this.creatingWorkerTimer.setUp(timeCreateWorker);
        this.spawnBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 24), (int) dimension.x, 8);
    }
    
    public void tick(GridMap map){
        super.tick(map);
        if(creatingWorker){
            if(creatingWorkerTimer.doneWaiting()){
                spawnWorker();
                creatingWorker = false;
                creatingWorkerPercentage = (float)0.0;
                creatingWorkerTimer.setUp(timeCreateWorker);
            }
            else{
                creatingWorkerPercentage = creatingWorkerTimer.getPercentage();
            }
            spawnBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 24), (int) dimension.x, 8);
        }
    }
    
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
        if(creatingWorker){
            drawSpawnBar(gl, cam);
        }
    }
    
    private void drawSpawnBar(GL2 gl, Camera camera){
        gl.glColor4f(0.85f, 0.85f, 0.85f, 1f);
             gl.glBegin(GL2.GL_QUADS);
             gl.glVertex2d(spawnBar.x - camera.position.x, spawnBar.y - camera.position.y);
             gl.glVertex2d(spawnBar.x - camera.position.x, spawnBar.y + spawnBar.height - camera.position.y);       
             gl.glVertex2d(spawnBar.x + spawnBar.width - camera.position.x, spawnBar.y + spawnBar.height - camera.position.y);
             gl.glVertex2d(spawnBar.x + spawnBar.width - camera.position.x, spawnBar.y - camera.position.y);
        gl.glEnd();
        
        gl.glColor4f(0, 0, 0.85f, 1f);
             gl.glBegin(GL2.GL_QUADS);
             gl.glVertex2d(spawnBar.x - camera.position.x, spawnBar.y - camera.position.y);
             gl.glVertex2d(spawnBar.x - camera.position.x, spawnBar.y + spawnBar.height - camera.position.y);       
             gl.glVertex2d(spawnBar.x + (spawnBar.width * creatingWorkerPercentage) - camera.position.x, spawnBar.y + spawnBar.height - camera.position.y);
             gl.glVertex2d(spawnBar.x + (spawnBar.width * creatingWorkerPercentage) - camera.position.x, spawnBar.y - camera.position.y);
        gl.glEnd();
    }
   
   public boolean isCreatingWorker(){
       return creatingWorker;
   }
   
   public void setCreatingWorker(boolean creatingWorker){
       this.creatingWorker = creatingWorker;
   }
   
   public void spawnWorker(){
       Game.client.sendSpawnUnitCommand(Game.currPlayer.getID(), id, 0, 0);
   }
}