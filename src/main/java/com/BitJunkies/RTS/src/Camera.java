/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import java.awt.Rectangle;
import mikera.vectorz.Vector2;

/**
 * camera class that allows us to travel throughout the map
 * @author brobz
 */

public class Camera {
    //camera unique variables
    public Vector2 position, velocity;
    public double viewport, moveRange, moveSpeed;
    public Rectangle hitBox;
    
    /**
     * Constructor of the class
     */
    public Camera(){
        this.viewport = 1;
        this.position = Vector2.of(0, 0);
        this.velocity = Vector2.of(0, 0);
        this.moveRange = 12;
        this.moveSpeed = 5;
        updateHitBox();
    }
    
    /**
     * ticking of the class
     */
    public void tick(){
        //changing the position fo the camera
        position.add(velocity);
        if(position.x < -MapLayout.scale)
            position.x = -MapLayout.scale;
        if(position.x + Display.WINDOW_WIDTH > MapLayout.SCALED_WIDTH)
            position.x = MapLayout.SCALED_WIDTH - Display.WINDOW_WIDTH;
        if(position.y < -MapLayout.scale)
            position.y = -MapLayout.scale;
        if(position.y + Display.WINDOW_HEIGHT> MapLayout.SCALED_HEIGHT)
            position.y = MapLayout.SCALED_HEIGHT - Display.WINDOW_HEIGHT;
        updateHitBox();
    }
    /**
     * method to project position of objects according to the camera 
     * @param position
     * @return 
     */
    public Vector2 projectPosition(Vector2 position){
        return Vector2.of(position.x - this.position.x, position.y - this.position.y);
    }
    /**
     * method to project dimension of object according to the camera
     * @param dimension
     * @return 
     */
    public Vector2 projectDimension(Vector2 dimension){
        return Vector2.of(dimension.x / viewport, dimension.y / viewport);
    }
    /**
     * method to normalize rectangles
     * @param box
     * @return 
     */
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
    
    /**
     * Setter of the position of the camera
     * @param nPos vector2 with x and y
     */
    public void setPosition(Vector2 nPos){
        this.position = nPos;
    }
    
    /**
     * Method to update the hit box to click the camera
     */
    public void updateHitBox(){
        hitBox = new Rectangle((int)(position.x), (int)(position.y), (int)Display.WINDOW_WIDTH, (int)Display.WINDOW_HEIGHT);
    }
    
}
