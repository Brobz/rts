/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.input;

import com.BitJunkies.RTS.src.Game;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;



/**
 * Class to implement keyboard functionalities
 * @author ulise
 */
public class KeyboardInput implements KeyListener{

    /**
     * Check if a key is pressed
     * @param ke event of the keyboard
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        Game.keyPressed(ke);
    }

    /**
     * Check if a key is released
     * @param ke event of the keyboard
     */
    @Override
    public void keyReleased(KeyEvent ke) {
    }
    
}
