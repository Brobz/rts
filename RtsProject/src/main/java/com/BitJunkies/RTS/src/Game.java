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
    
    //Unit selection
    private static Rectangle selectionBox;
    private static boolean isSelecting;
    public static ArrayList<Unit> selectedUnits;
    public static int selectedUnitsType; // -1 Empty, 0 Army, 1 Worker
    public static boolean workersActive;
    public static ArrayList<Building> buildings;
    
    //creating and menu stuff
    private static MenuWorker menuWorker;
    private static boolean creating;
    
    public static void main(String args[]){
        window = Display.init();
        start();
    }
    
    //method that starts thread
    public static void start(){
        Thread thread = new Thread(){
            public void run(){
                
                running = true;
                double timeTick = 1000000000 / FPS;
                double delta = 0;
                long now;
                long lastTime = System.nanoTime();
                //basic game loop
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
    
    //general tick method
    public static void tick(){
        camera.tick();
        
        //unit tick
        for(int i = 0; i < units.size(); i++){
            units.get(i).tick();
        }
        
        //resources tick
        for(int i = 0; i < 5; i++){
            resources.get(i).tick();
        }
        
        //buildings tick
        for(int i = 0; i < buildings.size(); i++){
           buildings.get(i).tick();
        }
        //worker menu tick
        if(workersActive) menuWorker.tick();
    }
    
    public static void render(GLAutoDrawable drawable){
        //basic openGl methods
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        //check if selection is being done to draw selection square
        if(isSelecting){
             gl.glColor4f(0, 0.85f, 0, 0.3f);
             gl.glBegin(GL2.GL_QUADS);
             gl.glVertex2d(selectionBox.x - camera.position.x, selectionBox.y - camera.position.y);
             gl.glVertex2d(selectionBox.x - camera.position.x, selectionBox.y + selectionBox.height - camera.position.y);       
             gl.glVertex2d(selectionBox.x + selectionBox.width - camera.position.x, selectionBox.y + selectionBox.height - camera.position.y);
             gl.glVertex2d(selectionBox.x + selectionBox.width - camera.position.x, selectionBox.y - camera.position.y);
             gl.glEnd();
        }
        
        //resources tick
        for(int i = 0; i < 5; i++){
             resources.get(i).render(gl, camera);
        }
       
        //building tick
        for(int i = 0; i < buildings.size(); i++){
           buildings.get(i).render(gl, camera);   
        }
        
        //units tick
        for(int i = 0; i < units.size(); i++){
            units.get(i).render(gl, camera);   
        }
        
        //if workers are active then tick the menu
        if(workersActive) menuWorker.render(gl, camera);
    }
    
    public static void stop(){
        running = false;
    }
    
    public static void init(){
        //initialize basic stuff in game
        Assets.init();
        player = new Player(1);
        selectedUnits = new ArrayList<Unit>();
        camera = new Camera();
        units = new ArrayList<Unit>();
        buildings = new ArrayList<Building>();
        //public Menu(Vector2 dimension, Vector2 position, AtomicInteger casttleCount, AtomicInteger buildersCount, AtomicInteger warriorsCount)
        menuWorker = new MenuWorker(Vector2.of(700, 100), Vector2.of(50, Display.WINDOW_HEIGHT-150), new AtomicInteger(2), new AtomicInteger(2), new AtomicInteger(2));
        isSelecting = false;
        workersActive = false;
        //ading dummy units
        for(int i = 0; i < 12; i++){
            units.add(new Worker(Vector2.of(Worker.WORKER_WIDTH, Worker.WORKER_HEIGHT), Vector2.of((i + 1) * 100, 200), player));
        }
        
        //ading dummy resources
        resources = new ArrayList<Resource>();
        for(int i = 0; i < 5; i++){
            resources.add(new Resource(Vector2.of(100, 60), Vector2.of((i + 1) * 130, 400)));
        }
        
        //ading dummy warriors
        for(int i = 0; i < 12; i++){
            units.add(new Warrior(Vector2.of(Warrior.WARRIOR_WIDTH, Warrior.WARRIOR_HEIGHT), Vector2.of((i + 1) * 100, 300), player));
        }
    }
    
    public static ArrayList<Unit> getUnits(){
        return units;
    }
    
    public static void mouseClicked(int button) {
        //check if it is a left click
       if(button == MouseEvent.BUTTON3){
           //move to resource flag to know if we moved to a resource in this click
            boolean movedToResource = false;
            boolean movedToBuilding = false;
            
            //moving worker units to resource
            if(!selectedUnits.isEmpty()){
                for(int i = 0; i < resources.size(); i++){
                    if(!resources.get(i).isUsable()) continue;
                    if(resources.get(i).getHitBox().intersects(MouseInput.mouseHitBox)){
                        for(int j = 0; j < selectedUnits.size(); j++){
                            if(selectedUnits.get(j) instanceof Worker){
                                ((Worker)selectedUnits.get(j)).stopBuilding();
                                ((Worker)selectedUnits.get(j)).mineAt(resources.get(i));
                            }
                        }
                        movedToResource = true;
                        break;
                    }
                }
                //if no movement was made then check if we have to move them to buildings
                if(!movedToResource){
                    for(int i = 0; i < buildings.size(); i++){
                        if(buildings.get(i).isCreated()) continue;
                        if(buildings.get(i).getHitBox().intersects(MouseInput.mouseHitBox)){
                            for(int j = 0; j < selectedUnits.size(); j++){
                                if(selectedUnits.get(j) instanceof Worker){
                                    ((Worker)selectedUnits.get(j)).stopMining();
                                    ((Worker)selectedUnits.get(j)).buildAt(buildings.get(i));
                                }
                            }
                            movedToBuilding = true;
                            break;
                        }
                    }
                }
                //finally if no unit was moved then just move them to the clicked position
                if(!movedToResource && !movedToBuilding){
                    for(int i = 0; i < selectedUnits.size(); i++){
                        if(selectedUnits.get(i) instanceof Worker) {
                            ((Worker)selectedUnits.get(i)).stopMining();
                            ((Worker)selectedUnits.get(i)).stopBuilding();
                        }
                        selectedUnits.get(i).moveTo(Vector2.of(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y));
                    }
                }
            }
        }
    }
    
    public static void mousePressed(int button){
        //checking if mouse are pressed
        if(button == MouseEvent.BUTTON1){
            //check if workers are active
            if(workersActive) creating = menuWorker.checkPress(MouseInput.mouseStaticHitBox);
            //check if we are not creatign to draw a rectangle
            if(!creating){
                isSelecting = true;
                selectionBox = new Rectangle(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, 1, 1);
            }
        }
    }
    
    public static void mouseDragged(){
        //checking if we are selecting to expand selection box
        if(isSelecting){
           selectionBox = new Rectangle(selectionBox.x, selectionBox.y, MouseInput.mouseHitBox.x - selectionBox.x, MouseInput.mouseHitBox.y - selectionBox.y);
        }
    }
    
    public static void mouseReleased(int button){
        //if it was a right click
        if(button == MouseEvent.BUTTON1){
            //checking if a selection is being made
            if(isSelecting){
                //here we check the selection of units
                selectedUnits.clear();
                selectedUnitsType = -1; // Empty
                //checking if any unit was selected BEFORE mouse release
                for(int i = 0; i < units.size(); i++){
                       if(camera.normalizeRectangle(selectionBox).intersects(units.get(i).getHitBox())){
                           if(units.get(i) instanceof Warrior){
                               selectedUnitsType = 0;
                           }else if(units.get(i) instanceof Worker && selectedUnitsType == -1)  {
                               selectedUnitsType = 1;
                           }
                       }
                }
                //selection players
                for(int i = 0; i < units.size(); i++){
                    if(camera.normalizeRectangle(selectionBox).intersects(units.get(i).getHitBox())){
                           if(units.get(i) instanceof Warrior && selectedUnitsType == 0){
                               units.get(i).select(player.getPlayerID());
                           }else if(units.get(i) instanceof Worker && selectedUnitsType == 1)  {
                               units.get(i).select(player.getPlayerID());
                           }
                       }
                }
                //setting workers active if workers are selected
                workersActive = (selectedUnitsType == 1);
                isSelecting = false;
                selectionBox = new Rectangle(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, 1, 1);
            }
            //check if we are creating an object from a menu
            else if(creating){
                //if statements to check what exactly we where creating
                if(menuWorker.isCreatingBuilder()){
                    menuWorker.stopCreatingWorker(units, player);
                }
                if(menuWorker.isCreatingWarrior()){
                    menuWorker.stopCreatingWarrior(units, player);
                }
                if(menuWorker.isCreatingCasttle()){
                    menuWorker.stopCreatingCasttle(buildings, player);
                    //moving units to build
                    for(int i = 0; i < selectedUnits.size(); i++){
                        if(selectedUnits.get(i) instanceof Worker){
                            ((Worker)selectedUnits.get(i)).stopMining();
                            ((Worker)(selectedUnits.get(i))).buildAt(buildings.get(buildings.size()-1));
                        }
                    }
                }
                creating = false;
            }
        }   
    }
    
    public static Camera getCamera(){
        return camera;
    }
    
    public static int getFPS(){
        return FPS;
    }
}