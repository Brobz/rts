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
 * Menu of the barrack for creating workers
 * @author rober
 */
public class MenuBarrack extends Menu{
    //Basic variables holding menu settings
    private boolean creatingWarrior;
    private AtomicInteger castleCount;
    private double spacingLeft, spacingTop, widthItem, heightItem;
    private Rectangle warriorHitBox;
    private int animationIdx;
    private Timer animationTimer;
    
    /**
     * Constructor of the class
     */
    public MenuBarrack(){
        super();
    }
    
    /**
     * Constructor of the class
     * @param dimension vector2 of width and height
     * @param position vector2 of x and y
     * @param castleCount AtomicInteger
     */
    public MenuBarrack(Vector2 dimension, Vector2 position, AtomicInteger castleCount){
        super(dimension, position);
        this.castleCount = castleCount;
        this.spacingLeft = 20;
        this.spacingTop = 10;
        this.widthItem = 80;
        this.heightItem = 80;
        this.creatingWarrior = false;
        this.animationIdx = 0;
        this.animationTimer = new Timer(Game.getFPS());
        this.animationTimer.setUp(0.4);
    }
    
    /**
     * ticking of the game
     */
    public void tick(){
        super.tick();
    }
    
    /**
     * Paint the elements of the object in the display
     * @param gl
     * @param cam 
     */
    public void render(GL2 gl, Camera cam){
       // Display.drawRectangleStatic(gl, cam, position.x, position.y, dimension.x, dimension.y, 0, 0, 0, (float)1);
        Display.drawImageStatic(gl, cam, Assets.menuSingleTexture, position.x,position.y, 300, dimension.y, 1f);
        
        //super.render(gl, cam);
        Vector2 pos = position;
        //We draw every item from the menu in case it exists
        int currSpacing = 0;
        if(!castleCount.equals(0)){
            float opac = 0.15f;
            if(Game.currPlayer.hasRubys(Warrior.RUBY_COST)) opac = 1f;
            //Display.drawImageStatic(gl, cam, Assets.warriorTexture, pos.x + spacingLeft + currSpacing, pos.y + spacingTop, widthItem, heightItem, (float)opac);
            Display.drawAnimationStatic(gl, cam, Assets.warriorsWalkingTexture[Game.currPlayer.getID() - 1], pos.x + spacingLeft + currSpacing, pos.y + spacingTop, widthItem, heightItem, (float)opac, animationIdx, 1);
            warriorHitBox = new Rectangle((int)(pos.x + spacingLeft + currSpacing), (int)(pos.y + spacingTop), (int)widthItem, (int)heightItem);
            currSpacing += spacingLeft + widthItem;
            if(animationTimer.doneWaiting()){
                animationIdx ++;
                animationIdx %= 4;
                animationTimer.setUp(0.4);
            }
        }
    }
    
    /**
     * method that checks if an item in the menu was pressed
     * @param mouseHitBox
     * @return 
     */
    public boolean checkPress(Rectangle mouseHitBox){
        //each of this checks individually for every item if it was pressed and
        //if so then it activates a creating mode for that specific item
        if(!castleCount.equals(0)){
            if(warriorHitBox.intersects(mouseHitBox) && Game.currPlayer.hasRubys(Warrior.RUBY_COST)){
                creatingWarrior = true;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if the barrack is creating a warrior
     * @return boolean
     */
    public boolean isCreatingWarrior() {
        return creatingWarrior;
    }
    /**
     * method to stop the creating mode of castle
     */
    public void stopCreatingWarrior(){
        creatingWarrior = false;
    }
}
