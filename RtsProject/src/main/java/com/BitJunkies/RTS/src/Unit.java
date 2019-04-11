/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import static java.lang.Math.min;
import mikera.vectorz.*;

/**
 *
 * @author brobz
 */
public class Unit extends Entity{
    protected double speed, maxHealth, health, damage, attackSpeed, range;
    private Player owner;
    private Vector2 positionTarget;
    public boolean onMoveCommand;
    
    public Unit(){
        super();
    }    

    public Unit(Vector2 dimension, Vector2 position, Player owner){
       super(dimension, position);
       this.owner = owner;
    }
    
    @Override
    public void tick(){
        if(onMoveCommand){
            Vector2 mult = Vector2.of(1, 1);
            if(position.x > positionTarget.x) mult.x *= -1;
            if(position.y > positionTarget.y) mult.y *= -1;
            double dist = Vector2.of(position.x + dimension.x / 2, position.y + dimension.y / 2).distance(positionTarget);
            velocity = Vector2.of(speed * mult.x, speed * mult.y);
            if(dist < 5){
                stopMoving();
            }
        }
        super.tick();
    }
    
    public void stopMoving(){
        onMoveCommand = false;
        positionTarget = position;
        velocity = Vector2.of(0, 0);  
    }
    
    public void moveTo(Vector2 target){
        onMoveCommand = true;
        positionTarget = target;
    }
    
    public void select(int playerID){
        if(owner.getPlayerID() == playerID)
            Game.selectedUnits.add(this);
    }
    
    public void deselect(int playerID){
        if(owner.getPlayerID() == playerID)
            Game.selectedUnits.remove(this);
    }
    
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
    }
}
