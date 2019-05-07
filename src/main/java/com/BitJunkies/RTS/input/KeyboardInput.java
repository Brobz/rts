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
 *
 * @author ulise
 */
public class KeyboardInput implements KeyListener{


    @Override
    public void keyPressed(KeyEvent ke) {
        Game.keyPressed(ke);
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }
    
}
