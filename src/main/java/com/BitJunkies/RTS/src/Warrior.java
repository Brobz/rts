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
       this.attackRange = 40;
    }
    
    public void tick(GridMap map){
        super.tick(map);
    }
    
    //simple render method
    public void render(GL2 gl, Camera cam){
        super.render(gl, cam);
    }
}
