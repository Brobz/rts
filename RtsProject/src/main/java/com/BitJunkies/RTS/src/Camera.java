/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import java.awt.Rectangle;
import mikera.vectorz.Vector2;

/**
 *
 * @author brobz
 */
public class Camera {
    public Vector2 position, velocity;
    public double viewport, moveRange, moveSpeed;
    
    public Camera(){
        this.viewport = 1;
        this.position = Vector2.of(0, 0);
        this.velocity = Vector2.of(0, 0);
        this.moveRange = 12;
        this.moveSpeed = 2.5;
    }
    
    public void tick(){
        position.add(velocity);
    }
    
    public Vector2 projectPosition(Vector2 position){
        return Vector2.of(position.x - this.position.x, position.y - this.position.y);
    }
    
    public Vector2 projectDimension(Vector2 dimension){
        return Vector2.of(dimension.x / viewport, dimension.y / viewport);
    }
    
    public Rectangle normalizeRectangle(Rectangle box){
        if(box.width < 0){
            box.x = box.x + box.width;
            box.width *= -1;
        }
        
        if(box.height < 0){
            box.y = box.y + box.height;
            box.height *= -1;
        }
        
        return box;
    }
    
}
