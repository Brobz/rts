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
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import mikera.vectorz.Vector2;

/**
 *
 * @author rober
 */
//Menu used when worker/s are selected
public class MenuWorker extends Menu{
    //Basic variables holding menu settings
    private boolean creatingCastle;
    private AtomicInteger castleCount;
    private double spacingLeft, spacingTop, widthItem, heightItem;
    private Rectangle castleHitBox;
    
    public MenuWorker(){
        super();
    }
    
    public MenuWorker(Vector2 dimension, Vector2 position, AtomicInteger castleCount){
        super(dimension, position);
        this.castleCount = castleCount;
        this.spacingLeft = 20;
        this.spacingTop = 10;
        this.widthItem = 80;
        this.heightItem = 80;
        this.creatingCastle = false;
    }
    
    public void tick(){
        super.tick();
    }
    
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
        Vector2 pos = position;
        //We draw every item from the menu in case it exists
        int currSpacing = 0;
        if(!castleCount.equals(0)){
            Display.drawImageStatic(gl, cam, Assets.casttleTexture, pos.x + spacingLeft + currSpacing, pos.y + spacingTop, widthItem, heightItem, (float)1);
            castleHitBox = new Rectangle((int)(pos.x + spacingLeft + currSpacing), (int)(pos.y + spacingTop), (int)widthItem, (int)heightItem);
            currSpacing += spacingLeft + widthItem;
        }
        //rendering creation action of an object if it was selected
        if(creatingCastle) createCastle(gl, cam);
    }
    
    //method that checks if an item in the menu was pressed
    public boolean checkPress(Rectangle mouseHitBox){
        //each of this checks individually for every item if it was pressed and
        //if so then it activates a creating mode for that specific item
        if(!castleCount.equals(0)){
            if(castleHitBox.intersects(mouseHitBox)){
                System.out.println("casttlePress");
                creatingCastle = true;
                return true;
            }
        }
        return false;
    }
    
    public void createCastle(GL2 gl, Camera cam){
        Display.drawImageCentered(gl, cam, Assets.casttleTexture, MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, Castle.CASTLE_WIDTH, Castle.CASTLE_HEIGHT, (float).4);
    }
    
    public boolean isCreatingCastle() {
        return creatingCastle;
    }
    
    //method to stop the creatin mode of casttle
    public void stopCreatingCastle(){
        creatingCastle = false;
    }
}