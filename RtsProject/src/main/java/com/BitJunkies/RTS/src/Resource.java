/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import mikera.vectorz.Vector2;

/**
 *
 * @author rober
 */
public class Resource extends Entity{
    private int lifePercentage;
    private boolean usable;
    private ArrayList<Unit> units;
    public Resource(){    
    }
    
    public Resource(Vector2 dimension, Vector2 position){
        super(dimension, position);
        lifePercentage = 1000;
        usable = true;
        units = Game.getUnits();
        this.texture = Assets.rockTexture;
    }
    
    public void tick(){
        if(usable){
            super.tick();
            if(lifePercentage <= 0) usable = false;
            else{
                if(lifePercentage >= 700) texture = Assets.rockTexture;
                else if(lifePercentage >= 300) texture = Assets.rockTextureD1;
                else texture = Assets.rockTextureD2;
            }
        }
    }
    
    public void render(GL2 gl, Camera cam){
        if(usable)
            super.render(gl, cam);
    }
    
    public void singleAttack(int damage){
        lifePercentage -= damage;
        System.out.println("Life: ");
        System.out.println(lifePercentage);
    }
    
    public boolean isUsable(){
        return usable;
    }
}