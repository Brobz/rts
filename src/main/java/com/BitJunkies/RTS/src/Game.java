/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import com.BitJunkies.RTS.src.server.AttackObject;
import com.BitJunkies.RTS.src.server.BuildObject;
import com.BitJunkies.RTS.src.server.ConnectionObject;
import com.BitJunkies.RTS.src.server.DisconnectionObject;
import com.BitJunkies.RTS.src.server.GameClient;
import com.BitJunkies.RTS.src.server.GameServer;
import com.BitJunkies.RTS.src.server.MineObject;
import com.BitJunkies.RTS.src.server.MoveObject;
import com.BitJunkies.RTS.src.server.SpawnBuildingObject;
import com.BitJunkies.RTS.src.server.SpawnUnitObject;
import com.esotericsoftware.kryonet.Connection;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import mikera.vectorz.Vector2;

/**
 *
 * @author rober
 */
public class Game {
    //game mechanics
    private static boolean running = true;
    private static int FPS = 60;
    private static GLWindow window;
    private static Camera camera;
    
    //player stuff
    private static HashMap<Integer, Resource> resources;
    public static Player currPlayer;
    private static HashMap<Integer, Player> players;
    
    //Map stuff
    private static GridMap map;
    
    //Server stuff
    private static GameServer server;
    private static GameClient client;
    private static boolean hosting = false;
    
    //Unit selection
    private static Rectangle selectionBox;
    private static boolean isSelecting;
    public static boolean workersActive;
    public static ArrayList<Unit> selectedUnits;
    public static int selectedUnitsType; // -1 Empty, 0 Army, 1 Worker
    
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
        if(hosting) server.tick();
        camera.tick();
        
        for(Player p : players.values()){
            p.tickUnits(map);
            p.tickBuildings(map);
        }
        
        //resources tick
         for(Resource res : resources.values()){
             res.tick(map);
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
             gl.glColor4f(1, 1, 1, 1);
        }
        
        //resources tick
        for(Resource res : resources.values()){
             res.render(gl, camera);
        }
        
        //player renders
        for(Player p : players.values()){
            p.renderUnits(gl, camera);
            p.renderBuildings(gl, camera);
        }
        
        //if workers are active then tick the menu
        if(workersActive) menuWorker.render(gl, camera);
        
        map.render(gl, camera);
    }
    
    public static void stop(){
        running = false;
    }
    
    public static void init(){
        //initialize basic stuff in game
        Assets.init();
        
        players = new HashMap<Integer, Player>();
        
        //ading dummy resources
        resources = new HashMap<>();
        for(int i = 0; i < 5; i++){
            int id = Entity.getId();
            resources.put(id, new Resource(Vector2.of(100, 60), Vector2.of((i + 1) * 120, 400), id));
        }
        
        //start server stuff
        if(hosting) hostServer();
        client = new GameClient();
        
        selectedUnits = new ArrayList<Unit>();
        camera = new Camera();
        //public Menu(Vector2 dimension, Vector2 position, AtomicInteger casttleCount, AtomicInteger buildersCount, AtomicInteger warriorsCount)
        menuWorker = new MenuWorker(Vector2.of(700, 100), Vector2.of(50, Display.WINDOW_HEIGHT-150), new AtomicInteger(2));
        isSelecting = false;
        workersActive = false;
        
        //initializng map
        map = new GridMap(3000, 3000);
        
    }
    
    public static HashMap<Integer, Unit> getUnits(){
        return currPlayer.units;
    }
    
    public static void mouseClicked(int button) {
        //check if it is a right click
       if(button == MouseEvent.BUTTON3){
        //moving worker units to resource
        if(selectedUnits.isEmpty())
            return;
        if(selectedUnitsType == 1){
            //move to resource flag to know if we moved to a resource in this click
            boolean movedToResource = false;
            boolean movedToBuilding = false;
            boolean movedToAttack = false;
            for(Resource res : resources.values()){
                if(!res.isUsable()) continue;
                if(res.getHitBox().intersects(MouseInput.mouseHitBox)){
                    for(int j = 0; j < selectedUnits.size(); j++){
                        ((Worker)selectedUnits.get(j)).mineAt(currPlayer.getID(), client, res);
                    }
                    movedToResource = true;
                    break;
                }
            }
            //if no movement was made then check if we have to move them to buildings
            if(!movedToResource){
                for(Building build : currPlayer.buildings.values()){
                    if(build.isCreated()) continue;
                    if(build.getHitBox().intersects(MouseInput.mouseHitBox)){
                        for(int j = 0; j < selectedUnits.size(); j++){
                            ((Worker)selectedUnits.get(j)).buildAt(currPlayer.getID(), client, build);

                        }
                        movedToBuilding = true;
                        break;
                    }
                }
            }
            
            //check if we need to attack something
            if(!movedToResource && !movedToBuilding){
                for(Player p : players.values()){
                    if(p == currPlayer) continue;
                    for(Unit u : p.units.values()){
                        if(u.getHitBox().intersects(MouseInput.mouseHitBox)){
                            for(int j = 0; j < selectedUnits.size(); j++){
                                ((Worker)selectedUnits.get(j)).attackAt(currPlayer.getID(), client, p, u);
                            }
                            movedToAttack = true;
                            break;
                        }
                    }
                    if(movedToAttack) break;
                    for(Building b : p.buildings.values()){
                        if(b.getHitBox().intersects(MouseInput.mouseHitBox)){
                            for(int j = 0; j < selectedUnits.size(); j++){
                                ((Worker)selectedUnits.get(j)).attackAt(currPlayer.getID(), client, p, b);
                            }
                            movedToAttack = true;
                            break;
                        }
                    }
                    if(movedToAttack) break;
                }
            }
            
            //finally if no unit was moved then just move them to the clicked position
            if(!movedToResource && !movedToBuilding && !movedToAttack){
                for(int i = 0; i < selectedUnits.size(); i++){
                    ((Worker)selectedUnits.get(i)).stopMining();
                    ((Worker)selectedUnits.get(i)).stopBuilding();
                    ((Worker)selectedUnits.get(i)).stopAttacking();
                    ((Worker)selectedUnits.get(i)).moveTo(currPlayer.getID(), client, Vector2.of(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y));
                }
            }
        }
        else{
            boolean movedToAttack = false;
            for(Player p : players.values()){
                if(p == currPlayer) continue;
                for(Unit u : p.units.values()){
                    if(u.getHitBox().intersects(MouseInput.mouseHitBox)){
                        for(int j = 0; j < selectedUnits.size(); j++){
                            ((Warrior)selectedUnits.get(j)).attackAt(currPlayer.getID(), client, p, u);
                        }
                        movedToAttack = true;
                        break;
                    }
                }
                if(movedToAttack) break;
                for(Building b : p.buildings.values()){
                    if(b.getHitBox().intersects(MouseInput.mouseHitBox)){
                        for(int j = 0; j < selectedUnits.size(); j++){
                            ((Warrior)selectedUnits.get(j)).attackAt(currPlayer.getID(), client, p, b);
                        }
                        movedToAttack = true;
                        break;
                    }
                }
                if(movedToAttack) break;
            }
            if(!movedToAttack){
                for(int i = 0; i < selectedUnits.size(); i++){
                    ((Warrior)(selectedUnits.get(i))).stopAttacking();
                    ((Warrior)selectedUnits.get(i)).moveTo(currPlayer.getID(), client, Vector2.of(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y));
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
                for(Unit unit : currPlayer.units.values()){
                       if(camera.normalizeRectangle(selectionBox).intersects(unit.getHitBox())){
                           if(unit instanceof Warrior){
                               selectedUnitsType = 0;
                           }else if(unit instanceof Worker && selectedUnitsType == -1)  {
                               selectedUnitsType = 1;
                           }
                       }
                }
                //selection players
                for(Unit unit : currPlayer.units.values()){
                    if(camera.normalizeRectangle(selectionBox).intersects(unit.getHitBox())){
                           if(unit instanceof Warrior && selectedUnitsType == 0){
                               unit.select();
                           }else if(unit instanceof Worker && selectedUnitsType == 1)  {
                               unit.select();
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
                if(menuWorker.isCreatingCastle()){
                    menuWorker.stopCreatingCastle();
                    ArrayList<Integer> workerIDs = new ArrayList<Integer>();
                    for(int i = 0; i < selectedUnits.size(); i++){
                        if(selectedUnits.get(i) instanceof Worker){
                            workerIDs.add(selectedUnits.get(i).id);
                        }
                    }
                    client.sendSpawnBuildingCommand(currPlayer.getID(), 0, MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, workerIDs);
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
    
    public static void executeMoveCommand(MoveObject cmd){
        players.get(cmd.playerID).units.get(cmd.entityID).stopAttacking();
        if(players.get(cmd.playerID).units.get(cmd.entityID) instanceof Worker){
            ((Worker) (players.get(cmd.playerID).units.get(cmd.entityID))).stopBuilding();
            ((Worker) (players.get(cmd.playerID).units.get(cmd.entityID))).stopMining();
        }
        players.get(cmd.playerID).units.get(cmd.entityID).moveTo(Vector2.of(cmd.xPosition, cmd.yPosition));
    }
    
    public static void executeMineCommand(MineObject cmd){
        ((Worker)players.get(cmd.playerID).units.get(cmd.workerID)).mineAt(resources.get(cmd.resourceID));
    }
    
    public static void executeAttackCommand(AttackObject cmd){
        if(cmd.targetBuildingID == -1)
            players.get(cmd.playerID).units.get(cmd.unitID).attackAt(players.get(cmd.targetPlayerID).units.get(cmd.targetUnitID));
        else
            players.get(cmd.playerID).units.get(cmd.unitID).attackAt(players.get(cmd.targetPlayerID).buildings.get(cmd.targetBuildingID));

    }
    
    public static void executeBuildCommand(BuildObject cmd){
        ((Worker)players.get(cmd.playerID).units.get(cmd.workerID)).buildAt(players.get(cmd.playerID).buildings.get(cmd.targetID));
    }
    
    public static void executeSpawnUnitCommand(SpawnUnitObject cmd){
        // spawnear unidad en el building
    }
    
    public static void executeSpawnBuildingCommand(SpawnBuildingObject cmd){
        // int playerID, int buildingIndex, int xPos, int yPos
        if(cmd.buildingIndex == 0){
            int new_id = Entity.getId();
            Castle c = new Castle(Vector2.of(Castle.CASTLE_WIDTH, Castle.CASTLE_HEIGHT), Vector2.of(cmd.xPos, cmd.yPos), new_id);
            players.get(cmd.playerID).buildings.put(new_id, c);
            for(int i = 0; i < cmd.workerIDs.size(); i++){
                ((Worker) (players.get(cmd.playerID).units.get(cmd.workerIDs.get(i)))).buildAt(c);
            }
        }
    }
    
    
    
    public static void hostServer(){
        server = new GameServer();
    }
    
    public static void addNewPlayer(ConnectionObject conObj){
        int id = conObj.connectionID;
        if(conObj.self){
            currPlayer = new Player(id);
            players.put(id, currPlayer);
        }else{
            players.put(id, new Player(id));
        }
        
        //ading dummy workers
        for(int i = 0; i < 3; i++){
            int unit_id = Entity.getId();
            players.get(id).units.put(unit_id, new Worker(Vector2.of(Worker.WORKER_WIDTH, Worker.WORKER_HEIGHT), Vector2.of((i + 1) * 100, 100 * players.size()), unit_id, players.get(id)));
        }
        
        //ading dummy warriors
        for(int i = 0; i < 3; i++){
            int unit_id = Entity.getId();
            players.get(id).units.put(unit_id, new Warrior(Vector2.of(Warrior.WARRIOR_WIDTH, Warrior.WARRIOR_HEIGHT), Vector2.of((i + 1) * 100, 150 * players.size()), unit_id, players.get(id)));
        }
        
    }

    public static void removePlayer(DisconnectionObject disconObj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}