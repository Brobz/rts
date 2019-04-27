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
    public static final int WARRIOR_WIDTH = 40, WARRIOR_HEIGHT = 40;
    private boolean onAtackCommand;
    private Building buildingToAtack;
    private Timer hitingBuildingTimer;
    public Warrior(){
        super();
    }
    
    public Warrior(Vector2 dimension, Vector2 position, int id){
       super(dimension, position, id);
       this.speed = 4;
       this.maxHealth = 10;
       this.health = this.maxHealth;
       this.damage = 100;
       this.attackSpeed = 0.5;
       this.range = regularRange;
       this.texture = Assets.warriorTexture;
       this.onAtackCommand = false;
       this.hitingBuildingTimer = new Timer(Game.getFPS());
       hitingBuildingTimer.setUp(1);
       this.hitingBuildingTimer = new Timer(Game.getFPS());
       hitingBuildingTimer.setUp(1);
    }
    
    public void tick(GridMap map){
        super.tick(map);
        //the warrior should attack
        if(onAtackCommand){
            if(!buildingToAtack.isUsable()){
                stopAtacking();
            }
            else{
                if(!onMoveCommand){
                    if(hitingBuildingTimer.doneWaiting()){
                        buildingToAtack.singleAtack(10);
                        hitingBuildingTimer.setUp(1);
                    }
                }
            }
        }
    }
    
    //simple render method
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
    }
    
    //method to deretmine where to atack
    public void atackAt(Building buildingToAtack){
        onAtackCommand = true;
        this.buildingToAtack = buildingToAtack;
        range = 100;
        moveTo(buildingToAtack.position);
    }
    
    //method to stop minning
    public void stopAtacking(){
        onAtackCommand = false;
        this.buildingToAtack = null;
        stopMoving();
        range = regularRange;
    }
}
