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
public class MenuBarrack extends Menu{
    //Basic variables holding menu settings
    private boolean creatingWarrior;
    private AtomicInteger castleCount;
    private double spacingLeft, spacingTop, widthItem, heightItem;
    private Rectangle warriorHitBox;
    
    public MenuBarrack(){
        super();
    }
    
    public MenuBarrack(Vector2 dimension, Vector2 position, AtomicInteger castleCount){
        super(dimension, position);
        this.castleCount = castleCount;
        this.spacingLeft = 20;
        this.spacingTop = 10;
        this.widthItem = 80;
        this.heightItem = 80;
        this.creatingWarrior = false;
    }
    
    public void tick(){
        super.tick();
    }
    
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
        System.out.println("--------------------------------->   barrack menu render");
        Vector2 pos = position;
        //We draw every item from the menu in case it exists
        int currSpacing = 0;
        if(!castleCount.equals(0)){
            float opac = 0.15f;
            if(Game.currPlayer.hasRubys(Warrior.RUBY_COST)) opac = 1f;
            Display.drawImageStatic(gl, cam, Assets.warriorTexture, pos.x + spacingLeft + currSpacing, pos.y + spacingTop, widthItem, heightItem, (float)opac);
            warriorHitBox = new Rectangle((int)(pos.x + spacingLeft + currSpacing), (int)(pos.y + spacingTop), (int)widthItem, (int)heightItem);
            currSpacing += spacingLeft + widthItem;
        }
    }
    
    //method that checks if an item in the menu was pressed
    public boolean checkPress(Rectangle mouseHitBox){
        //each of this checks individually for every item if it was pressed and
        //if so then it activates a creating mode for that specific item
        if(!castleCount.equals(0)){
            if(warriorHitBox.intersects(mouseHitBox) && Game.currPlayer.hasRubys(Warrior.RUBY_COST)){
                System.out.println("warriorPress");
                Game.currPlayer.spendRubys(Warrior.RUBY_COST);
                creatingWarrior = true;
                return true;
            }
        }
        return false;
    }
    
    public boolean isCreatingWarrior() {
        return creatingWarrior;
    }
    
    //method to stop the creatin mode of casttle
    public void stopCreatingWarrior(){
        creatingWarrior = false;
    }
}
