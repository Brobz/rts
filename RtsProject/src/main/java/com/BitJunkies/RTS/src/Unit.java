/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import mikera.vectorz.*;

/**
 *
 * @author brobz
 */
public class Unit extends Entity{
    protected double speed, maxHealth, health, damage, attackSpeed, range;
    private int owner;
    private Vector2 positionTarget;
    public boolean onMoveCommand;
    
    public Unit(){
        super();
    }
    
    public Unit(Vector2 dimension, Vector2 position, int owner){
       super(dimension, position);
       this.owner = owner;
    }
    
    @Override
    public void tick(){
        if(onMoveCommand){
            Vector2 mult = Vector2.of(1, 1);
            if(position.x > positionTarget.x) mult.x *= -1;
            if(position.y > positionTarget.y) mult.y *= -1;
            velocity = Vector2.of(speed * mult.x, speed * mult.y);
            if(Vector2.of(position.x + dimension.x / 2, position.y + dimension.y / 2).distance(positionTarget) < 3){
                onMoveCommand = false;
                positionTarget = position;
            }
        }else{
          velocity = Vector2.of(0, 0);  
        }
        super.tick();
        
    }
    
    public void moveTo(Vector2 target){
        onMoveCommand = true;
        positionTarget = target;
    }
    
    public void select(int playerID){
        if(owner == playerID)
            Game.selectedUnit = this;
    }
    
    public void deselect(int playerID){
        if(owner == playerID)
            Game.selectedUnit = null;
    }
    
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
    }
}
