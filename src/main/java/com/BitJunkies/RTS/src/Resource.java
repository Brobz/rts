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
//Resource class that contains the rubys from the game basically
public class Resource extends Entity{
    //Basic resource variables such as health and if it is usable
    public int lifePercentage;
    private boolean usable;
    public Resource(){    
    }
    
    public Resource(Vector2 dimension, Vector2 position, int id){
        super(dimension, position, id);
        lifePercentage = 500;
        usable = true;
        this.texture = Assets.rockTexture;
    }
    
    public void tick(GridMap map){
        //we only tick a resource if it's usable
        if(usable){
            super.tick(map);
            if(lifePercentage <= 0) usable = false;
            else{
                setOpacity((float)(lifePercentage / 500.0));
            }
        }
    }
    
    public void render(GL2 gl, Camera cam){
        //w eonly render a resource if ti is usable
        if(usable)
            super.render(gl, cam);
    }
    
    //method to reduce health of resource when a worker 'mines' it
    public void singleAttack(int damage){
        lifePercentage -= damage;
    }
    
    public boolean isUsable(){
        return usable;
    }

    void updateInfo(ArrayList<Double> info) {
        this.lifePercentage = ((int)(Math.floor(info.get(0))));
    }
}