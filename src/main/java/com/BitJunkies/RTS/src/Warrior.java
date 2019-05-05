/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import mikera.vectorz.Vector2;

/**
 *
 * @author rober
 */
//Simple warrior class
public class Warrior extends Unit{
    public static final int RUBY_COST = 50;
    public static final int WARRIOR_WIDTH = 40, WARRIOR_HEIGHT = 40;
    
    // image changing stuff
    Timer runningTimer;
    private int runningCnt = 0;
    private int direction;
    
    public Warrior(){
        super();
    }
    
    public Warrior(Vector2 dimension, Vector2 position, int id, Player owner){
       super(dimension, position, id, owner);
       this.speed = 2.5;
       this.maxHealth = 15;
       this.health = this.maxHealth;
       this.damage = 5;
       this.attackSpeed = 1;
       this.range = regularRange;
       this.texture = Assets.warriorTexture;
       this.buildingAttackRange = 55;
       this.unitAttackRange = 25;
       this.runningTimer = new Timer(Game.getFPS());
       this.runningTimer.setUp(0.2);
       this.unitAttackRange = 35;
    }
    
    public void tick(GridMap map){
        super.tick(map);
        
        if(onMoveCommand || onAttackCommand){
            if(runningTimer.doneWaiting()){
                // cambio
                runningCnt ++;
                runningCnt %= 4;
                this.runningTimer.setUp(0.2);
            }
        }
        
        if (onMoveCommand) {
            texture = Assets.warriorWalkingTexture;
        }
        else if(onAttackCommand) {
            texture = Assets.warriorAttackingTexture;
        }
        
        //change direction according to velocity
        if (velocity.x>=0 && velocity.y>=0) {
            if (velocity.x > velocity.y)
                direction = 3; //set direction to right
            else
                direction = 0; //set direction to up
        }
        else if (velocity.x<0 && velocity.y>=0) {
            if (Math.abs(velocity.x) > velocity.y)
                direction = 2; //set direction to left
            else
                direction = 0; //set direction to up
        }
        else if (velocity.x<0 && velocity.y<0) {
            if (Math.abs(velocity.x) > Math.abs(velocity.y))
                direction = 2; //set direction to left
            else
                direction = 1; //set direction to down
        }
        else if (velocity.x>=0 && velocity.y<0) {
            if (velocity.x > Math.abs(velocity.y))
                direction = 3; //set direction to right
            else
                direction = 1; //set direction to down
        }
        
        //If the worker is designated to mine then...
        if(onAttackCommand){
            double dist = Vector2.of(position.x, position.y).distance(positionTarget);
            double diffX = position.x - positionTarget.x;
            double diffY = position.y - positionTarget.y;
            
            if (diffX>=0 && diffY>=0) {
                if (diffX > diffY)
                    direction = 3; //set direction to right
                else
                    direction = 1; //set direction to up
            }
            else if (diffX<0 && diffY>=0) {
                if (Math.abs(diffX) > diffY)
                    direction = 2; //set direction to left
                else
                    direction = 1; //set direction to up
            }
            else if (diffX<0 && diffY<0) {
                if (Math.abs(diffX) > Math.abs(diffY))
                    direction = 2; //set direction to left
                else
                    direction = 0; //set direction to down
            }
            else if (diffX>=0 && diffY<0) {
                if (diffX > Math.abs(diffY))
                    direction = 3; //set direction to right
                else
                    direction = 0; //set direction to down
            }
        }
    }
    
    //simple render method
    
    public void render(GL2 gl, Camera cam){
        super.renderAnimation(gl, cam, runningCnt, direction);
    }
}
