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
public class MenuWorker extends Menu{
    private boolean creatingCasttle, creatingWarrior, creatingBuilder;
    private AtomicInteger casttleCount;
    private AtomicInteger buildersCount;
    private AtomicInteger warriorsCount;
    private double spacingLeft, spacingTop, widthItem, heightItem;
    private Rectangle casttleHitBox, buildersHitBox, warriorsHitBox;
    
    public MenuWorker(){
        super();
    }
    
    public MenuWorker(Vector2 dimension, Vector2 position, AtomicInteger casttleCount, AtomicInteger buildersCount, AtomicInteger warriorsCount){
        super(dimension, position);
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
        super.tick();
    }
    
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
        Vector2 pos = position;
        //dibujamos cada tipo de unit y despues cada building
        int currSpacing = 0;
        if(!buildersCount.equals(0)){
            Display.drawImageStatic(gl, cam, Assets.workerTexture, pos.x + spacingLeft + currSpacing, pos.y + spacingTop, widthItem, heightItem, (float)1);
            buildersHitBox = new Rectangle((int)(pos.x + spacingLeft + currSpacing), (int)(pos.y + spacingTop), (int)widthItem, (int)heightItem);
            currSpacing += spacingLeft + widthItem;
        }
        if(!warriorsCount.equals(0)){
            Display.drawImageStatic(gl, cam, Assets.warriorTexture, pos.x + spacingLeft + currSpacing, pos.y + spacingTop, widthItem, heightItem, (float)1);
            warriorsHitBox = new Rectangle((int)(pos.x + spacingLeft + currSpacing), (int)(pos.y + spacingTop), (int)widthItem, (int)heightItem);
            currSpacing += spacingLeft + widthItem;
        }
        if(!casttleCount.equals(0)){
            Display.drawImageStatic(gl, cam, Assets.casttleTexture, pos.x + spacingLeft + currSpacing, pos.y + spacingTop, widthItem, heightItem, (float)1);
            casttleHitBox = new Rectangle((int)(pos.x + spacingLeft + currSpacing), (int)(pos.y + spacingTop), (int)widthItem, (int)heightItem);
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
        Display.drawImageCentered(gl, cam, Assets.casttleTexture, MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, Building.BUILDING_WIDTH, Building.BUILDING_HEIGHT, (float).4);
    }
    
    public void createBuilder(GL2 gl, Camera cam){
        Display.drawImageCentered(gl, cam, Assets.workerTexture, MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, Worker.WORKER_WIDTH, Worker.WORKER_HEIGHT, (float).4);
    }
        
    public void createWarrior(GL2 gl, Camera cam){
        Display.drawImageCentered(gl, cam, Assets.warriorTexture, MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, Warrior.WARRIOR_WIDTH, Warrior.WARRIOR_HEIGHT, (float).4);
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