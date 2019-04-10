/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import mikera.vectorz.*;

/**
 *
 * @author brobz
 */
public abstract class Entity {
    private Vector2 dimension, position, velocity;
    
    public Entity(){
        dimension = Vector2.of(0, 0);
        position = Vector2.of(0, 0);
        velocity = Vector2.of(0, 0);
    }
    
    public Entity(Vector2 dimension, Vector2 position){
        this.dimension = dimension;
        this.position = position;
        this.velocity = Vector2.of(0, 0);;
    }
    
    public abstract void tick();
    
    public abstract void render();
}
