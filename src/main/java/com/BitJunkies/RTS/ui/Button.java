/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.ui;
import com.BitJunkies.RTS.src.Display;
import com.BitJunkies.RTS.src.Game;
import com.BitJunkies.RTS.src.GameState;
import com.BitJunkies.RTS.src.ImageLoader;
import com.BitJunkies.RTS.src.Timer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import mikera.vectorz.*;

/**
 * Button class to display
 * @author ulises
 */

public class Button extends Item{
    protected float opacity;
    protected Texture texture;
    protected GameState currState,nextState;
    protected Timer buttonTimer;
    
    protected BufferedImage curr;
    
    /**
     * Constructor for the button
     * @param x int for the x position of the button
     * @param y int for the y position of the button
     * @param width int width for the width of the button
     * @param height int height for the height of the button
     * @param fileNameWithExtension String for the filename for the image
     * @param currState GameState for knowing the current game state of the game itself
     * @param nextState GameState for knowing the next game state of the game itself
     */
    public Button(int x, int y, int width, int height,String fileNameWithExtension,GameState currState,GameState nextState){
        super(x,y,width,height);
        updateHitBox();
        opacity = 1;
        curr = ImageLoader.loadImage("/Images/"+ fileNameWithExtension);
        texture = AWTTextureIO.newTexture(Display.getProfile(), curr, true);
        this.currState = currState;
        this.nextState = nextState;
        buttonTimer = new Timer(Game.getFPS());
    }
    
    /**
     * Constructor for the button (Vector based)
     * @param dimension Vector2 for the dimension of the button
     * @param position Vector2 for the position of the button
     */
    public Button(Vector2 dimension, Vector2 position){
        super(dimension,position);
        this.opacity = (float) 1;
    }
    
    /**
     * Render method
     * @param gl GL2 for rendering
     */
    @Override
    public void render(GL2 gl){
        if(texture == null) return;
        Display.drawImageStatic(gl, null, texture, position.x, position.y, dimension.x, dimension.y, (float)opacity);
    }
    
    /**
     * Changes the game state to the next one from the current
     */
    public void onPressed(){
        Game.setCurrGameState(nextState);
    }
    
    /**
     * Gets hitbox of the Button, useful for intersections
     * @return Rectangle for checking intersections
     */
    public Rectangle getHitBox(){
        return hitBox;
    }
    
    
    /**
     * Sets opacity of button
     * @param opacity float for desired new opacity
     */
    public void setOpacity(float opacity){
        this.opacity = opacity;
    }

    /**
     * Sets the next game state to the one passed as a parameter
     * @param nextState GameState for using as the nextSate
     */
    public void setNextState(GameState nextState) {
        this.nextState = nextState;
    }
    
}