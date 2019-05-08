/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import java.util.ArrayList;

/**
 * Basic object for screen
 * @author brobz
 */
public abstract class GameState {
    /**
     * Ticking for the game
     */
    public abstract void tick();
     /**
     * Abstract method to draw the ui of the screen
     * @param gl GL2
     */
    public abstract void render(GL2 gl);

    /**
     * Method to check if a button of the screen or a text field is pressed
     */
    public abstract void checkPress();
    
    /**
     * Used to change text of text field(not used)
     * @param ke 
     */
    public abstract void changeTextField(KeyEvent ke);
}
