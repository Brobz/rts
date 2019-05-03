/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import com.jogamp.opengl.GL2;
import java.awt.Rectangle;
import java.util.ArrayList;
import mikera.vectorz.Vector2;

/**
 *
 * @author roberto
 */
public class MiniMap{
    private static ArrayList<Entity> actives;
    private static final float[][] colors = new float[][]{{1, 0, 0},{0, 1, 0},{0, 0, 1},{1, 1, 0},{1, 1, 1}, {1, 1, 1}};
    private static final float usedMapWidth = MapLayout.width * MapLayout.scale;
    private static final float usedMapHeight = MapLayout.height * MapLayout.scale;
    private static boolean movingCamera;
    private Vector2 dimension, position;
    private int id;
    private Rectangle hitBox;
    
    public MiniMap(){
        super();
    }
    
    public MiniMap(Vector2 dimension, Vector2 position, int id){
        this.dimension = dimension;
        this.position = position;
        this.id = id;
        actives = new ArrayList<>();
        updateHitBox();
        this.movingCamera = false;
    }
    
    public static void addToMap(Entity e){
        actives.add(e);
    }
    
    public void tick(GridMap map){
        if(movingCamera){
            changeCameraPosition();
        }
    }
    
    public void drawInMap(Entity e, GL2 gl, Camera cam){
        Vector2 posDraw, dimDraw;
        posDraw = Vector2.of(e.position.x / usedMapWidth * dimension.x + position.x, e.position.y / usedMapHeight * dimension.y + position.y);
        dimDraw = Vector2.of(e.dimension.x / usedMapWidth * dimension.x, e.dimension.y / usedMapHeight * dimension.y);
        int idx = 0;
        if(e instanceof Resource) idx = 0;
        else if(e instanceof Worker) idx = 1;
        else if(e instanceof Castle) idx = 2;
        else if(e instanceof Barrack) idx = 3;
        else if(e instanceof Wall) idx = 4;
        Display.drawRectangleStatic(gl, cam, posDraw.x, posDraw.y, dimDraw.x, dimDraw.y, colors[idx][0], colors[idx][1], colors[idx][2], (float)1.0);
    }
    
    public void drawCamera(GL2 gl, Camera cam){
        Vector2 posDraw, dimDraw;
        posDraw = Vector2.of(cam.position.x / usedMapWidth * dimension.x + position.x, cam.position.y / usedMapHeight * dimension.y + position.y);
        dimDraw = Vector2.of(Display.WINDOW_WIDTH / usedMapWidth * dimension.x, Display.WINDOW_HEIGHT / usedMapHeight * dimension.y);
        Display.drawRectangleStatic(gl, cam, posDraw.x, posDraw.y, dimDraw.x, dimDraw.y, 0, 0, 0.85f, 0.4f);
    }
    
    public void render(GL2 gl, Camera cam){
        //Display.drawImageCentered(gl, cam, texture, position.x, position.y, dimension.x, dimension.y, (float)opacity);
        Display.drawRectangleStatic(gl, cam, position.x, position.y, dimension.x, dimension.y, 0,0,0, 0.7f);
        for(Entity act : actives) drawInMap(act, gl, cam);
        actives.clear();
        //draw camera
        drawCamera(gl, cam);
    }
    
    private void changeCameraPosition(){
        
        Vector2 mousePositionInMiniMap = Vector2.of(MouseInput.mouseStaticHitBox.x - position.x, MouseInput.mouseStaticHitBox.y - position.y);
        Vector2 dimCam = Vector2.of(Display.WINDOW_WIDTH / usedMapWidth * dimension.x, Display.WINDOW_HEIGHT / usedMapHeight * dimension.y);
        if(mousePositionInMiniMap.x - dimCam.x / 2 < 0) mousePositionInMiniMap.x = dimCam.x / 2;
        else if(mousePositionInMiniMap.x + dimCam.x / 2 > dimension.x) mousePositionInMiniMap.x = dimension.x - dimCam.x / 2;
        
        if(mousePositionInMiniMap.y - dimCam.y / 2 < 0) mousePositionInMiniMap.y = dimCam.y / 2;
        else if(mousePositionInMiniMap.y + dimCam.y / 2  > dimension.y) mousePositionInMiniMap.y = dimension.y - dimCam.y / 2;

        Vector2 cameraPositionTranslate = Vector2.of(mousePositionInMiniMap.x * usedMapWidth / dimension.x - Display.WINDOW_WIDTH / 2, mousePositionInMiniMap.y * usedMapHeight / dimension.y - Display.WINDOW_HEIGHT / 2);
        
        Game.camera.setPosition(cameraPositionTranslate);
    }
    
    public boolean checkPress(){    
        movingCamera = MouseInput.mouseStaticHitBox.intersects(hitBox);
        if(movingCamera) System.out.println("-------------------------> clicking in map");
        return movingCamera;
    }
    
    public void stopMovingCam(){
        movingCamera = false;
        Game.miniMapMovingCam = false;
    }
    
    //method to update the rectangle hitbox
    public void updateHitBox(){
        hitBox = new Rectangle((int)(position.x), (int)(position.y), (int)dimension.x, (int)dimension.y);
    }
}