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
 * Item abstract class for other UI components
 * @author ulise
 */
public abstract class Item {
    protected Rectangle hitBox;
    protected Vector2 dimension, position;

    /**
     * Constructor for Item (based on coordinates)
     * @param x int for position in x axis
     * @param y int for position in y axis
     * @param width int for width of item
     * @param height int for height of item
     */
    public Item(int x, int y, int width, int height){
        dimension = Vector2.of(width, height);
        position = Vector2.of(x, y);
        updateHitBox();
    }

    /**
     * Constructor for Item (based on vectors)
     * @param dimension Vector2 for the dimensions of the item
     * @param position Vector2 for the position of the item
     */
    public Item(Vector2 dimension, Vector2 position) {
        this.hitBox = hitBox;
        this.dimension = dimension;
        this.position = position;
        updateHitBox();
    }
    
    /**
     * Updates the hitBox of the item (used or intersections)
     */
    //method to update the rectangle hitbox
    public void updateHitBox(){
        hitBox = new Rectangle((int)(position.x ), (int)(position.y), (int)dimension.x, (int)dimension.y);
    }

    /**
     * Gets the hitbox of the item
     * @return 
     */
    public Rectangle getHitBox() {
        return hitBox;
    }
    
    /**
     * renders the item
     * @param g1 GL2 for rendering
     */
    public abstract void render(GL2 g1);
}
