/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import com.BitJunkies.RTS.src.server.GameClient;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.FPSAnimator;
import java.util.ArrayList;
import mikera.vectorz.Vector2;

/**
 *
 * @author rober
 */
public class Game {
    private static boolean running = false;
    private static int FPS = 60;
    private static GLWindow window;
    private static Camera camera;
    private static ArrayList<Unit> units;
    private static ArrayList<Resource> resources;
    private static int playerID = 1;
    public static Unit selectedUnit;
    
    public static void main(String args[]){
        window = Display.init();
        start();
        //GameClient cl = new GameClient();
    }
    
    public static void start(){
        Thread thread = new Thread(){
            public void run(){
                
                running = true;
                double timeTick = 1000000000 / FPS;
                double delta = 0;
                long now;
                long lastTime = System.nanoTime();
                while(running){
                    now = System.nanoTime();
                    delta += (now - lastTime) / timeTick;
                    lastTime = now;
                    if(delta >= 1){
                        // Input
                        // TODO
                        // Update
                        tick();
                        // Render
                        window.display();
                    }
                }
                window.destroy();
            }
        };
        thread.setName("GameLoop");
        thread.start();
    }
    
    public static void tick(){
        for(int i = 0; i < units.size(); i++){
            units.get(i).tick();
        }
        
        for(int i = 0; i < 5; i++){
            resources.get(i).tick();
       }
        
       camera.position.x += 0.2;
    }
    
    public static void render(GLAutoDrawable drawable){
       GL2 gl = drawable.getGL().getGL2();
       gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
       
      
       for(int i = 0; i < units.size(); i++){
           units.get(i).render(gl, camera);   
       }
       
       for(int i = 0; i < 5; i++){
            resources.get(i).render(gl, camera);
       } 
    }
    
    public static void stop(){
        running = false;
    }
    
    public static void init(){
        //inicializar players y map y cosas
        Assets.init();
        camera = new Camera();
        units = new ArrayList<Unit>();
        for(int i = 0; i < 50; i++){
            units.add(new Worker(Vector2.of(30, 30), Vector2.of(i * 50, i * 30), playerID));
        }
        
        resources = new ArrayList<Resource>();
        for(int i = 0; i < 5; i++){
            resources.add(new Resource(Vector2.of(60, 60), Vector2.of(i * 100, 400)));
        }
    }
    
    public static ArrayList<Unit> getUnits(){
        return units;
    }
    
    public static void mouseClicked(int button) {
        if(button == MouseEvent.BUTTON1){
            if(selectedUnit != null)
                selectedUnit.deselect(playerID);
            for(int i = 0; i < units.size(); i++){
                if(units.get(i).getHitBox().intersects(MouseInput.mouseHitBox)){
                    units.get(i).select(playerID);
                    break;
                }
            }
        }else if(button == MouseEvent.BUTTON3){
            if(selectedUnit != null){
                selectedUnit.moveTo(Vector2.of(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y));
            }
        }
    }
    
    public static Camera getCamera(){
        return camera;
    }
}
