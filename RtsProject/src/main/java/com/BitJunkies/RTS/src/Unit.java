/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GLAutoDrawable;
import mikera.vectorz.*;

/**
 *
 * @author brobz
 */
public class Unit extends Entity{
    public Unit(){
        super();
    }
    
    public Unit(Vector2 dimension, Vector2 position){
        super(dimension, position);
    }
    
    public void tick(){
        super.tick();
    }
    
    public void render(GLAutoDrawable drawable){
        super.render(drawable);
    }
}
