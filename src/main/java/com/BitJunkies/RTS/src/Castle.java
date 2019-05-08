/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import DatabaseQueries.CreateJugadorEnPartida;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Color;
import java.awt.Font;
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
    protected float creatingWorkerPercentage;
    protected Timer creatingWorkerTimer;
    protected int workerCreateQueue;

    public Castle(Vector2 dimension, Vector2 position, int id, Player owner) {
        super(dimension, position, id, owner);
        this.maxHealth = 150;
        this.texture = Assets.casttleTexture;
        this.creatingWorkerPercentage = (float) 0.0;
        this.creatingWorkerTimer = new Timer(Game.getFPS());
        this.creatingWorkerTimer.setUp(timeCreateWorker);
        this.spawnBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 24), (int) dimension.x, 8);
        this.workerCreateQueue = 0;
    }
    
    public void tick(GridMap map){
        super.tick(map);
        if(workerCreateQueue != 0){
            if(creatingWorkerTimer.doneWaiting()){
                spawnWorker();
                workerCreateQueue --;
                creatingWorkerPercentage = (float)0.0;
                creatingWorkerTimer.setUp(timeCreateWorker);
                
                CreateJugadorEnPartida.mapRecGas.put(owner.getID(), CreateJugadorEnPartida.getAcumRecGas(owner.getID()) + Worker.RUBY_COST);
            }
            else{
                creatingWorkerPercentage = creatingWorkerTimer.getPercentage();
            }
            spawnBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 24), (int) dimension.x, 8);
        }
    }
    
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
        if(workerCreateQueue != 0){
            drawSpawnBar(gl, cam);
            if(workerCreateQueue != 0){
                renderQueueWorkersText(cam, (float)(position.x - dimension.x/2), (float)(position.y + dimension.y/2 + 15));
            }
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
       return workerCreateQueue != 0;
   }
   
   public void spawnWorker(){
       Game.client.sendSpawnUnitCommand(Game.currPlayer.getID(), id, 0, 0, Game.loggedInUsername);
   }   

    public void addWorker(){
        this.workerCreateQueue ++;
    }
    
    
    public void renderQueueWorkersText(Camera cam, float x, float y){
        TextRenderer textRenderer = new TextRenderer(new Font("Verdana", Font.BOLD, 15));
        textRenderer.beginRendering(Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT);
        textRenderer.setColor(Color.ORANGE);
        textRenderer.setSmoothing(true);
        Vector2 pos = cam.projectPosition(Vector2.of(x, y));
        pos.y = Display.WINDOW_HEIGHT - pos.y;
        //Vector2 pos = (Vector2.of(x, y));
        textRenderer.draw("Creating:" + workerCreateQueue, (int)pos.x, (int)pos.y);
        textRenderer.endRendering();
   }
}