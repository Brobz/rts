/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import static com.BitJunkies.RTS.src.Game.currPlayer;
import static com.BitJunkies.RTS.src.Game.map;
import static com.BitJunkies.RTS.src.Game.workersActive;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

/**
 *
 * @author brobz
 */
public class GameState {
    public static ArrayList<GameState> gameStates = new ArrayList<>();
            
    public static void tick(){
    }
    
    public static void render(GLAutoDrawable drawable){
        //basic openGl methods
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
    }
    
}
