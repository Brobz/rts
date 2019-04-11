/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import mikera.vectorz.Vector2;

/**
 *
 * @author Gibran Gonzalez
 */
public class Building extends Entity {
    private int maxHealth, health;
    
    public Building() {
        super();
    }
    
    public Building(Vector2 dimension, Vector2 position) {
        super(dimension, position);
    }
    
    @Override
    public void tick() {
        super.tick();
    }
    
    @Override
    public void render(GL2 gl, Camera cam) {
        super.render(gl, cam);
    }
}
