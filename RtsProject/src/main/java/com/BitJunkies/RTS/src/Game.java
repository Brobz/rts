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
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import mikera.vectorz.Vector2;

/**
 *
 * @author rober
 */
public class Game {
    //game mechanics
    private static boolean running = false;
    private static int FPS = 60;
    private static GLWindow window;
    private static Camera camera;
    
    //player stuff
    private static ArrayList<Unit> units;
    private static ArrayList<Resource> resources;
    private static Player player;
    private static Menu menu;
    
    //Unit selection
    private static Rectangle selectionBox;
    private static boolean isSelecting;
    public static ArrayList<Unit> selectedUnits;
    
    //Building creation
    private static boolean creatingBuilding;
    
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
        menu.tick();
        camera.tick();
        
        for(int i = 0; i < units.size(); i++){
            units.get(i).tick();
        }
        
        for(int i = 0; i < 5; i++){
            resources.get(i).tick();
       }
        
    }
    
    public static void render(GLAutoDrawable drawable){
       GL2 gl = drawable.getGL().getGL2();
       gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
       menu.render(gl, camera);
       
       if(isSelecting){
            gl.glColor4f(0, 0.85f, 0, 0.3f);
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2d(selectionBox.x - camera.position.x, selectionBox.y - camera.position.y);
            gl.glVertex2d(selectionBox.x - camera.position.x, selectionBox.y + selectionBox.height - camera.position.y);       
            gl.glVertex2d(selectionBox.x + selectionBox.width - camera.position.x, selectionBox.y + selectionBox.height - camera.position.y);
            gl.glVertex2d(selectionBox.x + selectionBox.width - camera.position.x, selectionBox.y - camera.position.y);
            gl.glEnd();
       }
       
      
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
        player = new Player(1);
        selectedUnits = new ArrayList<Unit>();
        camera = new Camera();
        units = new ArrayList<Unit>();
        //public Menu(Vector2 dimension, Vector2 position, AtomicInteger casttleCount, AtomicInteger buildersCount, AtomicInteger warriorsCount)
        menu = new Menu(Vector2.of(700, 100), Vector2.of(50, Display.WINDOW_HEIGHT-150), new AtomicInteger(2), new AtomicInteger(2), new AtomicInteger(2));
        isSelecting = false;
        creatingBuilding = false;
        for(int i = 0; i < 12; i++){
            units.add(new Worker(Vector2.of(30, 30), Vector2.of((i + 1) * 50, 200), player));
        }
        
        resources = new ArrayList<Resource>();
        for(int i = 0; i < 5; i++){
            resources.add(new Resource(Vector2.of(60, 60), Vector2.of((i + 1) * 100, 400)));
        }
    }
    
    public static ArrayList<Unit> getUnits(){
        return units;
    }
    
    public static void mouseClicked(int button) {
       if(button == MouseEvent.BUTTON3){
           System.out.println("xdxd");
            boolean movedToResource = false;
            if(selectedUnits.size() != 0){
                if(selectedUnits.get(0) instanceof Worker){
                    for(int i = 0; i < resources.size(); i++){
                        if(!resources.get(i).isUsable()) continue;
                        if(resources.get(i).getHitBox().intersects(MouseInput.mouseHitBox)){
                           for(int j = 0; j < selectedUnits.size(); j++){
                            ((Worker)selectedUnits.get(j)).mineAt(resources.get(i));
                           }
                           movedToResource = true;
                           break;
                        }
                    }
                }
                if(!movedToResource){
                    if(selectedUnits.get(0) instanceof Worker){
                        for(int i = 0; i < selectedUnits.size(); i++){
                            ((Worker)selectedUnits.get(i)).stopMining();
                        }
                    }
                    
                    for(int i = 0; i < selectedUnits.size(); i++){
                        selectedUnits.get(i).moveTo(Vector2.of(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y));
                    }
                }
            }
        }
    }
    
    public static void mousePressed(int button){
        
        if(button == MouseEvent.BUTTON1){
            isSelecting = true;
            selectionBox = new Rectangle(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, 1, 1);
        }
    }
    
    public static void mouseDragged(){
        if(isSelecting){
           selectionBox = new Rectangle(selectionBox.x, selectionBox.y, MouseInput.mouseHitBox.x - selectionBox.x, MouseInput.mouseHitBox.y - selectionBox.y);
        }
        else if(creatingBuilding){
            //aqui va lo de crear un building
        }
    }
    
    public static void mouseReleased(int button){     
        if(button == MouseEvent.BUTTON1){
            selectedUnits.clear();
            for(int i = 0; i < units.size(); i++){
                   if(camera.normalizeRectangle(selectionBox).intersects(units.get(i).getHitBox())){
                       units.get(i).select(player.getPlayerID());
                   }
               }
            isSelecting = false;
            selectionBox = new Rectangle(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, 1, 1);
        }   
    }
    
    public static Camera getCamera(){
        return camera;
    }
    
    public static int getFPS(){
        return FPS;
    }
}
