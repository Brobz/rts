/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.ui;

import com.jogamp.opengl.GL2;
import java.awt.Rectangle;
import mikera.vectorz.Vector2;

/**
 *
 * @author ulise
 */
public abstract class Item {
    protected Rectangle hitBox;
    protected Vector2 dimension, position;

    public Item(int x, int y, int width, int height){
        dimension = Vector2.of(width, height);
        position = Vector2.of(x, y);
        updateHitBox();
    }

    public Item(Vector2 dimension, Vector2 position) {
        this.hitBox = hitBox;
        this.dimension = dimension;
        this.position = position;
        updateHitBox();
    }
    
    //method to update the rectangle hitbox
    public void updateHitBox(){
        hitBox = new Rectangle((int)(position.x - dimension.x / 2), (int)(position.y - dimension.y / 2), (int)dimension.x, (int)dimension.y);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }
    
    public abstract void render(GL2 g1);
}
