/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import static com.BitJunkies.RTS.src.Game.camera;
import static com.jogamp.opengl.GL.GL_DST_ALPHA;
import static com.jogamp.opengl.GL.GL_ONE;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_DST_ALPHA;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_SRC_COLOR;
import static com.jogamp.opengl.GL.GL_ZERO;
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
    private static final float[][] colors = new float[][]{{1, 0, 0},{0.5f, 0.5f, 0.5f}, {0.7f, 0, 0},{0, 0.7f, 0},{0, 0, 0.7f},{0.0f, 0.7f, 0.317647f}, {1, 0, 0},{0, 1, 0},{0, 0, 1},{.2745f, 0, 0.517647f}};
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
        else if(e instanceof Wall) idx = 1;
        else if(e instanceof Building){
            idx = 1 + ((Building)(e)).owner.getID() ;
        }
        else if(e instanceof Unit){
            idx = 5 + ((Unit)(e)).owner.getID();
        }
        Display.drawRectangleStatic(gl, cam, posDraw.x, posDraw.y, dimDraw.x, dimDraw.y, colors[idx][0], colors[idx][1], colors[idx][2], (float)1.0);
    }
    
    public void drawCamera(GL2 gl, Camera cam){
        Vector2 posDraw, dimDraw;
        posDraw = Vector2.of(cam.position.x / usedMapWidth * dimension.x + position.x, cam.position.y / usedMapHeight * dimension.y + position.y);
        dimDraw = Vector2.of(Display.WINDOW_WIDTH / usedMapWidth * dimension.x, Display.WINDOW_HEIGHT / usedMapHeight * dimension.y);
        Display.drawRectangleStatic(gl, cam, posDraw.x, posDraw.y, dimDraw.x, dimDraw.y, 0, 0, 0.85f, 0.4f);
    }
    
    public void drawMaskInMap(Entity act, GL2 gl, Camera cam){
        Vector2 posDraw, dimDraw;
        float rad;
        if(act instanceof Building){
            if(((Building)act).owner.getID() == Game.currPlayer.getID()){
                posDraw = Vector2.of(act.position.x / usedMapWidth * dimension.x + position.x, act.position.y / usedMapHeight * dimension.y + position.y);
                dimDraw = Vector2.of(act.dimension.x / usedMapWidth * dimension.x, act.dimension.y / usedMapHeight * dimension.y);
                rad = (float)(((Building)act).currMaskRad / usedMapWidth * dimension.x);
                Display.drawImageStatic(gl, cam, Assets.circleSmallTexture, posDraw.x-rad/2, posDraw.y-rad/2, dimDraw.x+rad, dimDraw.y+rad, 1);
            }
        }
        else if (act instanceof Unit){
            if(((Unit)act).owner.getID() == Game.currPlayer.getID()){
                posDraw = Vector2.of(act.position.x / usedMapWidth * dimension.x + position.x, act.position.y / usedMapHeight * dimension.y + position.y);
                dimDraw = Vector2.of(act.dimension.x / usedMapWidth * dimension.x, act.dimension.y / usedMapHeight * dimension.y);
                rad = (float)(Entity.MAX_MASK_RADIUS / usedMapWidth * dimension.x);
                Display.drawImageStatic(gl, cam, Assets.circleSmallTexture, posDraw.x - rad / 2 , posDraw.y-rad / 2, dimDraw.x + rad, dimDraw.y + rad, 1);
            }
        }
    }
    
    public void render(GL2 gl, Camera cam){
        gl.glBlendFunc(GL_ONE, GL_ZERO);
        Display.drawRectangleStatic(gl, cam, position.x, position.y, dimension.x, dimension.y, 0.28627f,0.44705f,0.058823f, 1f);
        
        gl.glBlendFuncSeparate(GL_ZERO, GL_ONE, GL_SRC_COLOR, GL_ZERO);
        Display.drawRectangleStatic(gl, camera, position.x, position.y,dimension.x, dimension.y, 0.0f, 0.0f, 0.0f, 0f);
        
        gl.glBlendFuncSeparate(GL_ZERO, GL_ONE, GL_ONE, GL_ONE);
        //render masks
        for(Entity act : actives){
            drawMaskInMap(act, gl, cam);
        }
        gl.glBlendFuncSeparate(GL_DST_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_DST_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        Display.drawRectangleStatic(gl, cam, position.x, position.y, dimension.x, dimension.y, 0.68627f,0.84705f,0.458823f, 1f);
        //Display.drawImageCentered(gl, cam, texture, position.x, position.y, dimension.x, dimension.y, (float)opacity);
        for(Entity act : actives) drawInMap(act, gl, cam);
        actives.clear();
        gl.glBlendFunc(GL_ONE_MINUS_DST_ALPHA, GL_DST_ALPHA);
        Display.drawRectangleStatic(gl, cam, position.x, position.y, dimension.x, dimension.y, 0.28627f,0.44705f,0.058823f, 1f);
        //draw camera
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
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
        return movingCamera;
    }
    
    public Vector2 checkPositionPress(){
        if(!MouseInput.mouseStaticHitBox.intersects(hitBox)){
            return null;
        }
        Vector2 mousePositionInMiniMap = Vector2.of(MouseInput.mouseStaticHitBox.x - position.x, MouseInput.mouseStaticHitBox.y - position.y);
        Vector2 translation = Vector2.of(mousePositionInMiniMap.x / dimension.x * usedMapWidth, mousePositionInMiniMap.y / dimension.y * usedMapHeight); 
        return translation;
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