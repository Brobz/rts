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
 * @author roberto
 */
public class MiniMap extends Entity{
    private static ArrayList<Entity> actives;
    private static final float[][] colors = new float[][]{{1, 0, 0},{0, 1, 0},{0, 0, 1},{1, 1, 0},{1, 1, 1}, {1, 1, 1}};
    private static final float usedMapWidth = MapLayout.width * MapLayout.scale;
    private static final float usedMapHeight = MapLayout.height * MapLayout.scale;
    
    public MiniMap(){
        super();
    }
    
    public MiniMap(Vector2 dimension, Vector2 position, int id){
        super(dimension, position, id);
        this.dimension = dimension;
        this.position = position;
        this.velocity = Vector2.of(0, 0);
        this.opacity = (float) .7;
        this.id = id;
        this.cleanedUp = false;
        actives = new ArrayList<>();
        updateHitBox();
    }
    
    public void tick(GridMap map){
        //changing the place of the Entity in the screen
        map.deleteMap(this);
        position.add(velocity);
        updateHitBox();
        map.updateMap(this);
    }
    
    public static void addToMap(Entity e){
        actives.add(e);
    }
    
    public void drawInMap(Entity e, GL2 gl, Camera cam){
        Vector2 posDraw, dimDraw;
        posDraw = Vector2.of(e.position.x / usedMapWidth * dimension.x + position.x, e.position.y / usedMapHeight * dimension.y + position.y);
        dimDraw = Vector2.of(e.dimension.x / usedMapWidth * dimension.x, e.dimension.y / usedMapHeight * dimension.y);
        int idx = 0;
        if(e instanceof Warrior) idx = 0;
        else if(e instanceof Worker) idx = 1;
        else if(e instanceof Castle) idx = 2;
        else if(e instanceof Barrack) idx = 3;
        else if(e instanceof Wall) idx = 4;
        Display.drawRectangleStatic(gl, cam, posDraw.x, posDraw.y, dimDraw.x, dimDraw.y, colors[idx][0], colors[idx][1], colors[idx][2], (float)1.0);
    }
    
    public void render(GL2 gl, Camera cam){
        //Display.drawImageCentered(gl, cam, texture, position.x, position.y, dimension.x, dimension.y, (float)opacity);
        Display.drawRectangleStatic(gl, cam, position.x, position.y, dimension.x, dimension.y, 0,0,0, opacity);
        for(Entity act : actives) drawInMap(act, gl, cam);
        actives.clear();
    }
}