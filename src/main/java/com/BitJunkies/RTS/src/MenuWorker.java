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
    private boolean creatingCastle, creatingBarrack;
    private AtomicInteger castleCount;
    private double spacingLeft, spacingTop, widthItem, heightItem;
    private Rectangle castleHitBox, barrackHitbox;
    
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
        this.creatingBarrack = false;
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
            float opac = 0.15f;
            if(Game.currPlayer.hasRubys(Castle.RUBY_COST)) opac = 1f;
            Display.drawImageStatic(gl, cam, Assets.casttleTexture, pos.x + spacingLeft + currSpacing, pos.y + spacingTop, widthItem, heightItem, (float)opac);
            castleHitBox = new Rectangle((int)(pos.x + spacingLeft + currSpacing), (int)(pos.y + spacingTop), (int)widthItem, (int)heightItem);
            currSpacing += spacingLeft + widthItem;
            
            float opacB = 0.15f;
            if(Game.currPlayer.hasRubys(Barrack.RUBY_COST)) opacB = 1f;
            Display.drawImageStatic(gl, cam, Assets.barrackTexture, pos.x + spacingLeft + currSpacing, pos.y + spacingTop, widthItem, heightItem, (float)opacB);
            barrackHitbox = new Rectangle((int)(pos.x + spacingLeft + currSpacing), (int)(pos.y + spacingTop), (int)widthItem, (int)heightItem);
            currSpacing += spacingLeft + widthItem;
        }
        //rendering creation action of an object if it was selected
        if(creatingCastle) createCastle(gl, cam);
        if(creatingBarrack) createBarrack(gl, cam);
    }
    
    //method that checks if an item in the menu was pressed
    public boolean checkPress(Rectangle mouseHitBox){
        //each of this checks individually for every item if it was pressed and
        //if so then it activates a creating mode for that specific item
        if(!castleCount.equals(0)){
            if(castleHitBox.intersects(mouseHitBox) && Game.currPlayer.hasRubys(Castle.RUBY_COST)){
                System.out.println("casttlePress");
                creatingCastle = true;
                return true;
            }
            if(barrackHitbox.intersects(mouseHitBox) && Game.currPlayer.hasRubys(Barrack.RUBY_COST)){
                System.out.println("barrackPress");
                creatingBarrack = true;
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
    
    public boolean canPlaceCastle(GridMap map){
        return map.isEmptyArea(new Rectangle(MouseInput.mouseHitBox.x - Castle.CASTLE_WIDTH / 2, MouseInput.mouseHitBox.y - Castle.CASTLE_HEIGHT / 2, Castle.CASTLE_WIDTH, Castle.CASTLE_HEIGHT));
    }
    
    //method to stop the creatin mode of casttle
    public void stopCreatingCastle(){
        creatingCastle = false;
    }
    
    public void createBarrack(GL2 gl, Camera cam){
        Display.drawImageCentered(gl, cam, Assets.barrackTexture, MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, Barrack.CASTLE_WIDTH, Barrack.CASTLE_HEIGHT, (float).4);
    }
    
    public boolean isCreatingBarrack() {
        return creatingBarrack;
    }
    
    public boolean canPlaceBarrack(GridMap map){
        return map.isEmptyArea(new Rectangle(MouseInput.mouseHitBox.x - Castle.CASTLE_WIDTH / 2, MouseInput.mouseHitBox.y - Barrack.CASTLE_HEIGHT / 2, Barrack.CASTLE_WIDTH, Barrack.CASTLE_HEIGHT));
    }
    
    //method to stop the creatin mode of casttle
    public void stopCreatingBarrack(){
        creatingBarrack = false;
    }
}
