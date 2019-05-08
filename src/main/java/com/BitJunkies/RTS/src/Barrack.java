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
 * @author rober
 */


public class Barrack extends Building{
    public static final int timeCreateWarrior = 10;
    public static final int CASTLE_WIDTH = 75, CASTLE_HEIGHT = 75, RUBY_COST = 100;
    protected Rectangle spawnBar;
    protected float creatingWarriorPercentage;
    protected Timer creatingWarriorTimer;
    protected int warriorCreateQueue;

    public Barrack(Vector2 dimension, Vector2 position, int id, Player owner) {
        super(dimension, position, id, owner);
        this.maxHealth = 85;
        this.texture = Assets.barrackTexture;
        this.creatingWarriorPercentage = (float) 0.0;
        this.creatingWarriorTimer = new Timer(Game.getFPS());
        this.creatingWarriorTimer.setUp(timeCreateWarrior);
        this.spawnBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 24), (int) dimension.x, 8);
        this.warriorCreateQueue = 0;
    }
    
    public void tick(GridMap map){
        super.tick(map);
        if(warriorCreateQueue != 0){
            if(creatingWarriorTimer.doneWaiting()){
                spawnWarrior();
                warriorCreateQueue --;
                creatingWarriorPercentage = (float)0.0;
                creatingWarriorTimer.setUp(timeCreateWarrior);
                
                CreateJugadorEnPartida.mapRecGas.put(owner.getID(), CreateJugadorEnPartida.getAcumRecGas(owner.getID()) + Warrior.RUBY_COST);
            }
            else{
                creatingWarriorPercentage = creatingWarriorTimer.getPercentage();
            }
            spawnBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 24), (int) dimension.x, 8);
        }
    }
    
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
        if(warriorCreateQueue != 0){
            drawSpawnBar(gl, cam);
            if(warriorCreateQueue != 0){
                renderQueueWarriorsText(cam, (float)(position.x - dimension.x/2), (float)(position.y + dimension.y/2 + 15));
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
             gl.glVertex2d(spawnBar.x + (spawnBar.width * creatingWarriorPercentage) - camera.position.x, spawnBar.y + spawnBar.height - camera.position.y);
             gl.glVertex2d(spawnBar.x + (spawnBar.width * creatingWarriorPercentage) - camera.position.x, spawnBar.y - camera.position.y);
        gl.glEnd();
    }
   
   public boolean isCreatingWarrior(){
       return warriorCreateQueue != 0;
   }
  
   
   public void spawnWarrior(){
       Game.client.sendSpawnUnitCommand(Game.currPlayer.getID(), id, 0, 1);
   }   

   public void addWarrior(){
       this.warriorCreateQueue ++;
   }
   
       
    public void renderQueueWarriorsText(Camera cam, float x, float y){
        TextRenderer textRenderer = new TextRenderer(new Font("Verdana", Font.BOLD, 15));
        textRenderer.beginRendering(Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT);
        textRenderer.setColor(Color.ORANGE);
        textRenderer.setSmoothing(true);
        Vector2 pos = cam.projectPosition(Vector2.of(x, y));
        pos.y = Display.WINDOW_HEIGHT - pos.y;
        //Vector2 pos = (Vector2.of(x, y));
        textRenderer.draw("Creating:" + warriorCreateQueue, (int)pos.x, (int)pos.y);
        textRenderer.endRendering();
   }
}