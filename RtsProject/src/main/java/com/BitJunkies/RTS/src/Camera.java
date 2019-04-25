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

//camera class that allows us to travel throughout the map
public class Camera {
    //camera unique variables
    public Vector2 position, velocity;
    public double viewport, moveRange, moveSpeed;
    
    public Camera(){
        this.viewport = 1;
        this.position = Vector2.of(0, 0);
        this.velocity = Vector2.of(0, 0);
        this.moveRange = 12;
        this.moveSpeed = 5;
    }
    
    public void tick(){
        //changing the position fo the camera
        //position.add(velocity);
    }
    
    //method to project position of objects according to the camera
    public Vector2 projectPosition(Vector2 position){
        return Vector2.of(position.x - this.position.x, position.y - this.position.y);
    }
    
    //method to project dimencion of object according to the camera
    public Vector2 projectDimension(Vector2 dimension){
        return Vector2.of(dimension.x / viewport, dimension.y / viewport);
    }
    
    //method to nomralaize reactangles
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
