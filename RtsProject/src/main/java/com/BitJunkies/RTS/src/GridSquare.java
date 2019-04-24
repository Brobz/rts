/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import mikera.vectorz.Vector2;

/**
 *
 * @author brobz
 */
public class GridSquare extends Entity{
    private Entity entityContained;
    
    public GridSquare(Vector2 dimension, Vector2 position){
        super(dimension, position);
        opacity = 0.3f;
        texture = null;
    }
    
    public Entity getEntityContained(){
        return entityContained;
    }
    
    public void setEntityContained(Entity e){
        entityContained = e;
        if(entityContained != null)
            texture = Assets.rockTexture;
        else
            texture = null;
    }
}
