/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import mikera.vectorz.*;

/**
 *
 * @author brobz
 */
//Basic class only used for 'people' in the game, intermidiate between Entity and the characters
public class Unit extends Entity{
    protected double speed, maxHealth, health, damage, attackSpeed, range; //simple unit attributes
    protected Player owner; //owner of unit
    protected Vector2 positionTarget; // vector containing the position of the current target
    protected boolean onMoveCommand; //flag to know if we have to move the unit towards a target
    protected int regularRange;
    
    public Unit(){
        super();
    }    

    public Unit(Vector2 dimension, Vector2 position){
       super(dimension, position);
       this.onMoveCommand = false;
       this.regularRange = 10;
    }
    
    //tick method
    @Override
    public void tick(){
        //if onMoveCommand is active then the unit will move towards the
        //position of the target
        if(onMoveCommand){
            Vector2 mult = Vector2.of(1, 1);
            if(position.x > positionTarget.x) mult.x *= -1;
            if(position.y > positionTarget.y) mult.y *= -1;
            double dist = Vector2.of(position.x, position.y).distance(positionTarget);
            velocity = Vector2.of(speed * mult.x, speed * mult.y);
            if(dist < range) stopMoving();
        }
        super.tick();
    }
    
    //method to stopMoving the unit
    public void stopMoving(){
        onMoveCommand = false;
        positionTarget = position;
        velocity = Vector2.of(0, 0);  
    }
    
    //method to setup moving to a target
    public void moveTo(Vector2 target){
        onMoveCommand = true;
        positionTarget = target;
    }
    
    //method to select unit to move
    public void select(){
        Game.selectedUnits.add(this);
    }
    
    //method to deselect the unit to move
    public void deselect(){
        Game.selectedUnits.remove(this);
    }
    
    //simple rendering method
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
    }
}
