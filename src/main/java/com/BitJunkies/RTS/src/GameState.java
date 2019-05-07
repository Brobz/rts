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
 *
 * @author brobz
 */
public abstract class GameState {
    public static ArrayList<GameState> gameStates = new ArrayList<>();
            
    public void tick(){
    }
    
    public void render(GL2 gl){
        System.out.println("GameState ");
    }
    
    public abstract void checkPress();
    
    public abstract void changeTextField(KeyEvent ke);
}
