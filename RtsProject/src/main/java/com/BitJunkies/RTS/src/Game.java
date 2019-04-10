/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.src.server.GameClient;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.FPSAnimator;

/**
 *
 * @author rober
 */
public class Game {
    private static boolean running = false;
    private static int FPS = 60;
    private static GLWindow window;
    private static float x = 100;
    
    public static void main(String args[]){
        init();
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
        x += 1f;
    }
    
    public static void render(GLAutoDrawable drawable){
        //renders de players van aqui
    }
    
    public static void stop(){
        running = false;
    }
    
    public static void init(){
        //inicializacion de display
        window = Display.initDisplay();
        //inicializacion de Assets
        //Assets.init();
        //inicializar players y map y cosas
        
    }
    
}
