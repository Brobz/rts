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
 * @author brobz
 */
public class Wall extends Entity{
    public Wall(){    
    }
    
    public Wall(Vector2 dimension, Vector2 position, int id){
        super(dimension, position, id);
        this.texture = Assets.rockTextureD1;
    }
    
    public void tick(GridMap map){
        super.tick(map);
    }
    
    public void render(GL2 gl, Camera cam){
        //w eonly render a resource if ti is usable
        super.render(gl, cam);
    }
}
