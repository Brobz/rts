/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import mikera.vectorz.Vector2;

/**
 * Basic unit for the grid map
 * @author brobz
 */
public class GridSquare extends Entity{
    private Entity entityContained;
    
    /**
     * Constructor of a class
     * @param dimension vector2 with width and height
     * @param position vector2 with x and y
     */
    public GridSquare(Vector2 dimension, Vector2 position){
        super(dimension, position, 0);
        opacity = 0.3f;
        texture = null;
    }

    /**
     * Getter for the entity contained in the grid square
     * @return Entity
     */
    public Entity getEntityContained(){
        return entityContained;
    }
    
    /**
     * Setter for entity that is contained in the grid
     * @param e Entity
     */
    public void setEntityContained(Entity e){
        entityContained = e;
    }
}
