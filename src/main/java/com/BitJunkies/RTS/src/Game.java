/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import com.BitJunkies.RTS.src.server.AttackObject;
import com.BitJunkies.RTS.src.server.BuildObject;
import com.BitJunkies.RTS.src.server.BuildingInfoObject;
import com.BitJunkies.RTS.src.server.ConnectionObject;
import com.BitJunkies.RTS.src.server.DisconnectionObject;
import com.BitJunkies.RTS.src.server.GameClient;
import com.BitJunkies.RTS.src.server.GameServer;
import com.BitJunkies.RTS.src.server.MineObject;
import com.BitJunkies.RTS.src.server.MoveObject;
import com.BitJunkies.RTS.src.server.PlayerInfoObject;
import com.BitJunkies.RTS.src.server.ResourceInfoObject;
import com.BitJunkies.RTS.src.server.SpawnBuildingObject;
import com.BitJunkies.RTS.src.server.SpawnUnitObject;
import com.BitJunkies.RTS.src.server.SpendRubysObject;
import com.BitJunkies.RTS.src.server.StartMatchObject;
import com.BitJunkies.RTS.src.server.UnitInfoObject;
import DatabaseQueries.CreateUnit;
import DatabaseQueries.CreateBuilding;
import DatabaseQueries.CreateJugadorEnPartida;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import mikera.vectorz.Vector2;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author rober
 */
public class Game {
    //game mechanics
    private static boolean running = true;
    private static int FPS = 60;
    private static GLWindow window;
    public static Camera camera;
    
    //player stuff
    private static HashMap<Integer, Resource> resources;
    private static HashMap<Integer, Wall> walls;
    public static Player currPlayer;
    private static HashMap<Integer, Player> players;
    
    //Map stuff
    public static GridMap map;
    public static MiniMap miniMap;
    public static boolean miniMapMovingCam;
    
    //Server stuff
    public static GameServer server;
    public static GameClient client;
    public static boolean hosting = true;
    public static boolean matchStarted = true;
    
    //Unit selection
    private static Rectangle selectionBox;
    private static boolean isSelecting;
    public static ArrayList<Unit> selectedUnits;
    public static int selectedUnitsType; // -1 Empty, 0 Army, 1 Worker
    
    //creating and menu stuff
    private static MenuWorker menuWorker;
    private static MenuCastle menuCastle;
    private static MenuBarrack menuBarrack;
    private static boolean creating;
    public static boolean workersActive;
    public static boolean buildingActive;
    public static Building selectedBuilding;
    
    //Game States
    private static GameState currState;
    
    public static int framesUntillNextSync;
    public static final int syncDelay = FPS / 3;
    
     // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.postgresql.Driver";  
    static final String DB_URL = "jdbc:postgresql://ec2-54-225-95-183.compute-1.amazonaws.com:5432/dah1sh2i3uomfn?user=seaynizasqgwhc&password=015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020&sslmode=require";

    //  Database credentials
    static final String USER = "seaynizasqgwhc";
    static final String PASS = "015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020";
    
    public static int partidaId  = 0;
    
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
                        framesUntillNextSync ++;
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
        
        
        if(hosting) server.tick();
        
        if(!matchStarted){
            currState.tick();
            return;
        }
        
        
        if(hosting){
            //resources tick
            ConcurrentHashMap<Integer, ArrayList<Double>> rInfo = new ConcurrentHashMap<>();
            for(Resource res : resources.values()){
                 res.tick(map);
                 ArrayList<Double> i = new ArrayList<Double>();
                 i.add((double)res.lifePercentage);
                 rInfo.put(res.id, i);
            }
            client.sendResourcesInfo(rInfo);

            for(Player p : players.values()){
                p.tickBuildings(map);
                p.tickUnits(map);
            }
            
            if(framesUntillNextSync >= syncDelay){
                for(Player p : players.values()){
                    ConcurrentHashMap<Integer, ArrayList<Double>> uInfo = new ConcurrentHashMap<>();
                    ConcurrentHashMap<Integer, ArrayList<Double>> bInfo = new ConcurrentHashMap<>();
                    
                    for(Unit u : p.units.values()){
                        ArrayList<Double> i = new ArrayList<>();
                        i.add(u.health);
                        i.add(u.position.x);
                        i.add(u.position.y);
                        i.add(u.velocity.x);
                        i.add(u.velocity.y);
                        if(u.positionTarget != null){
                            i.add(u.positionTarget.x);
                            i.add(u.positionTarget.y);
                        }else{
                            i.add(null);
                            i.add(null);
                        }
                        i.add((u.onMoveCommand) ? 1.0 : 0.0);
                        i.add((u.onAttackCommand) ? 1.0 : 0.0);
                        if(u instanceof Worker){
                            i.add((((Worker)(u)).onMineCommand) ? 1.0 : 0.0);
                            i.add((((Worker)(u)).onBuildCommand) ? 1.0 : 0.0);
                        }
                        uInfo.put(u.id, i);
                    }
                    
                    
                    for(Building b : p.buildings.values()){
                        ArrayList<Double> i = new ArrayList<>();
                        i.add((double) b.health);
                        i.add((b.created) ? 1.0 : 0.0);
                        i.add((b.usable) ? 1.0 : 0.0);
                        i.add((b instanceof Castle) ? 0.0 : 1.0);
                        i.add(b.position.x);
                        i.add(b.position.y);
                        bInfo.put(b.id, i);
                    }
                    
                    client.sendUnitInfo(p.getID(), uInfo);
                    client.sendBuildingInfo(p.getID(), bInfo);
                    client.sendPlayerInfo(p.getID(), p.getRubys());
                }
                
                framesUntillNextSync = 0;
            }
        }
        
        for(Player p : players.values()){
            p.tickBuildings(map);
        }
        
        for(Wall wall : walls.values()){
            wall.tick(map);
        }
        
        //worker menu tick
        if(workersActive) menuWorker.tick();
        if(buildingActive){
            if(selectedBuilding instanceof Castle) menuCastle.tick();
            else if(selectedBuilding instanceof Barrack) menuBarrack.tick();
        }
        
        //minimap tick
        miniMap.tick(map);
    }
    
    public static void render(GLAutoDrawable drawable){
        //basic openGl methods
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        if(!matchStarted){
            currState.render(gl);
            return;
        }
        //check if selection is being done to draw selection square
        if(isSelecting){
             gl.glColor4f(0, 0.85f, 0, 0.3f);
             gl.glBegin(GL2.GL_QUADS);
             gl.glVertex2d(selectionBox.x - camera.position.x, selectionBox.y - camera.position.y);
             gl.glVertex2d(selectionBox.x - camera.position.x, selectionBox.y + selectionBox.height - camera.position.y);       
             gl.glVertex2d(selectionBox.x + selectionBox.width - camera.position.x, selectionBox.y + selectionBox.height - camera.position.y);
             gl.glVertex2d(selectionBox.x + selectionBox.width - camera.position.x, selectionBox.y - camera.position.y);
             gl.glEnd();
             gl.glFlush();
             gl.glColor4f(1, 1, 1, 1);
        }
        
        //resources tick
        for(Resource res : resources.values()){
             res.render(gl, camera);
        }
        
        //resources tick
        for(Wall wall : walls.values()){
             wall.render(gl, camera);
        }
        
        //player renders
        for(Player p : players.values()){
            p.renderUnits(gl, camera);
            p.renderBuildings(gl, camera);
        }
        
        //if workers are active then tick the menu
        if(workersActive) menuWorker.render(gl, camera);
        if(buildingActive){
            if(selectedBuilding instanceof Castle) menuCastle.render(gl, camera);
            else if(selectedBuilding instanceof Barrack) menuBarrack.render(gl, camera);
        }
        
        map.render(gl, camera);
        miniMap.render(gl, camera);
        
        TextRenderer textRenderer = new TextRenderer(new Font("Verdana", Font.BOLD, 25));
        textRenderer.beginRendering(Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT);
        textRenderer.setColor(Color.YELLOW);
        textRenderer.setSmoothing(true);
        if(currPlayer != null)
            textRenderer.draw("Rubys: " + currPlayer.getRubys(), 50, Display.WINDOW_HEIGHT - 50);
        textRenderer.endRendering();
        
        
    }
    
    public static void stop(){
        running = false;
    }
    
    public static void init(){
        //initialize basic stuff in game
        Assets.init();
        
        players = new HashMap<Integer, Player>();
        
        loadMap();
        
        //start server stuff
        if(hosting) hostServer();
        client = new GameClient();
        
        selectedUnits = new ArrayList<Unit>();
        camera = new Camera();
        //public Menu(Vector2 dimension, Vector2 position, AtomicInteger casttleCount, AtomicInteger buildersCount, AtomicInteger warriorsCount)
        menuWorker = new MenuWorker(Vector2.of(700, 100), Vector2.of(50, Display.WINDOW_HEIGHT-150), new AtomicInteger(2));
        menuCastle = new MenuCastle(Vector2.of(700, 100), Vector2.of(50, Display.WINDOW_HEIGHT-150), new AtomicInteger(2));
        menuBarrack = new MenuBarrack(Vector2.of(700, 100), Vector2.of(50, Display.WINDOW_HEIGHT-150), new AtomicInteger(2));
        isSelecting = false;
        workersActive = false;
        buildingActive = false;
        selectedBuilding = null;
        
        //initializng map
        map = new GridMap(3000, 3000);
        miniMap = new MiniMap(Vector2.of(230, 230), Vector2.of(Display.WINDOW_WIDTH - 250, 20), 0);
        
        //initialize game state
        currState = new MainMenu();
        miniMapMovingCam = false;
        
        //Date date = (Date) Calendar.getInstance().getTime();  
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");  
        //partidaId = dateFormat.format(date);
         
    }
    
    public static ConcurrentHashMap<Integer, Unit> getUnits(){
        return currPlayer.units;
    }
    
    public static HashMap<Integer, Player> getPlayers() {
        return players;
    }
    
    public static void mouseClicked(int button) {
        if(!matchStarted) return;
        //check if it is a right click
       if(button == MouseEvent.BUTTON3){
        //moving worker units to resource
        if(selectedUnits.isEmpty())
            return;
        if(selectedUnitsType == 1){
            System.out.println("selected units 1 --------------------------------------------");
            Vector2 miniMapPositionLeftClick = miniMap.checkPositionPress();
            if(miniMapPositionLeftClick != null){
                Entity clickedEntityInMiniMap = map.getIntersectedEntity(miniMapPositionLeftClick);
                if(clickedEntityInMiniMap == null){
                    for(int i = 0; i < selectedUnits.size(); i++){
                        ((Worker)selectedUnits.get(i)).stopMining();
                        ((Worker)selectedUnits.get(i)).stopBuilding();
                        ((Worker)selectedUnits.get(i)).stopAttacking();
                        ((Worker)selectedUnits.get(i)).moveTo(currPlayer.getID(), client, Vector2.of(miniMapPositionLeftClick.x, miniMapPositionLeftClick.y));
                    }
                }
                else if(clickedEntityInMiniMap instanceof Resource){
                    for(int j = 0; j < selectedUnits.size(); j++){
                        ((Worker)selectedUnits.get(j)).mineAt(currPlayer.getID(), client, (Resource)clickedEntityInMiniMap);
                    }               
                }
                else if(clickedEntityInMiniMap instanceof Building){
                    if(((Building)clickedEntityInMiniMap).owner == currPlayer){
                        for(int j = 0; j < selectedUnits.size(); j++){
                            ((Worker)selectedUnits.get(j)).buildAt(currPlayer.getID(), client, (Building)clickedEntityInMiniMap);
                        }
                    }
                    else{
                        for(int j = 0; j < selectedUnits.size(); j++){
                            ((Worker)selectedUnits.get(j)).attackAt(currPlayer.getID(), client, ((Building)clickedEntityInMiniMap).owner, (Building)clickedEntityInMiniMap);
                        }                        
                    }
                }
                else if (clickedEntityInMiniMap instanceof Unit){
                    for(int j = 0; j < selectedUnits.size(); j++){
                        ((Worker)selectedUnits.get(j)).attackAt(currPlayer.getID(), client,((Unit)clickedEntityInMiniMap).owner , (Unit)clickedEntityInMiniMap);
                    }             
                }
                return;
            }
            
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
            System.out.println("warrior selection -----------------------------");
            
            Vector2 miniMapPositionLeftClick = miniMap.checkPositionPress();
            if(miniMapPositionLeftClick != null){
                Entity clickedEntityInMiniMap = map.getIntersectedEntity(miniMapPositionLeftClick);
                if(clickedEntityInMiniMap == null){
                    for(int i = 0; i < selectedUnits.size(); i++){
                        ((Warrior)selectedUnits.get(i)).stopAttacking();
                        ((Warrior)selectedUnits.get(i)).moveTo(currPlayer.getID(), client, Vector2.of(miniMapPositionLeftClick.x, miniMapPositionLeftClick.y));
                    }
                }
                else if(clickedEntityInMiniMap instanceof Building){
                    if(((Building)clickedEntityInMiniMap).owner == currPlayer){
                        return;
                    }
                    else{
                        for(int j = 0; j < selectedUnits.size(); j++){
                            ((Warrior)selectedUnits.get(j)).attackAt(currPlayer.getID(), client, ((Building)clickedEntityInMiniMap).owner, (Building)clickedEntityInMiniMap);
                        }                        
                    }
                }
                else if (clickedEntityInMiniMap instanceof Unit){
                    for(int j = 0; j < selectedUnits.size(); j++){
                        ((Warrior)selectedUnits.get(j)).attackAt(currPlayer.getID(), client,((Unit)clickedEntityInMiniMap).owner , (Unit)clickedEntityInMiniMap);
                    }             
                }
                return;
            }
            
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
       
       //UpdatePlayerActions.actionsPerPlayer.put(currPlayer.getID(), UpdatePlayerActions.getAcumActions(currPlayer.getID()) + 1);
        System.out.println("PLAYER ID: " + currPlayer.getID());
       //System.out.println(CreateJugadorEnPartida.getAcumAcciones(currPlayer.getID()) + 1);
       CreateJugadorEnPartida.mapAcciones.put(currPlayer.getID(), CreateJugadorEnPartida.getAcumAcciones(currPlayer.getID()) + 1);
    }
    
    public static void mousePressed(int button){
        if(!matchStarted) return;
        //checking if mouse are pressed
        if(button == MouseEvent.BUTTON1){
            //check if workers are active
            miniMapMovingCam = miniMap.checkPress();
            if(!miniMapMovingCam && workersActive) creating = menuWorker.checkPress(MouseInput.mouseStaticHitBox);
            
            //check if a building is active
            if(buildingActive){
                if(selectedBuilding instanceof Castle){
                    if(menuCastle.checkPress(MouseInput.mouseStaticHitBox)){
                        ((Castle)selectedBuilding).setCreatingWorker(true);
                        client.sendSpendInfo(currPlayer.getID(), Worker.RUBY_COST);
                        
                        //CreateJugadorEnPartida.mapRecGas.put(currPlayer.getID(), CreateJugadorEnPartida.getAcumRecGas(currPlayer.getID()) + Worker.RUBY_COST);
                    }
                }
                else if(selectedBuilding instanceof Barrack){
                    if(menuBarrack.checkPress(MouseInput.mouseStaticHitBox)){ 
                        ((Barrack)selectedBuilding).setCreatingWarrior(true);
                        client.sendSpendInfo(currPlayer.getID(), Warrior.RUBY_COST);
                        
                        //CreateJugadorEnPartida.mapRecGas.put(currPlayer.getID(), CreateJugadorEnPartida.getAcumRecGas(currPlayer.getID()) + Warrior.RUBY_COST);
                    }
                }
            }
            //check if we are not creating nor moving the cam to draw a rectangle
            if(!creating && !miniMapMovingCam){
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
        if(!matchStarted) return;
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
                if(selectedUnitsType == -1){
                    for(Building build : currPlayer.buildings.values()){
                        if(camera.normalizeRectangle(selectionBox).intersects(build.getHitBox())){
                            if(build instanceof Castle){
                                selectedUnitsType = 2;
                            }
                            else{
                                selectedUnitsType = 3;
                            }
                            selectedBuilding = build;
                            break;
                       }
                    }
                }
                //selection players
                if(selectedUnitsType == 0 || selectedUnitsType == 1){
                    for(Unit unit : currPlayer.units.values()){
                        if(camera.normalizeRectangle(selectionBox).intersects(unit.getHitBox())){
                               if(unit instanceof Warrior && selectedUnitsType == 0){
                                   unit.select();
                               }else if(unit instanceof Worker && selectedUnitsType == 1)  {
                                   unit.select();
                               }
                           }
                    }   
                }
                //setting workers active if workers are selected
                workersActive = (selectedUnitsType == 1);
                buildingActive = (selectedUnitsType == 2 || selectedUnitsType == 3);
                isSelecting = false;
                selectionBox = new Rectangle(MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, 1, 1);
            }
            //check if we are creating an object from a menu
            else if(creating){
                //if statements to check what exactly we where creating
                if(menuWorker.isCreatingCastle()){
                    menuWorker.stopCreatingCastle();
                    if(!menuWorker.canPlaceCastle(map)){ 
                        return;
                    }
                    ArrayList<Integer> workerIDs = new ArrayList<Integer>();
                    for(int i = 0; i < selectedUnits.size(); i++){
                        if(selectedUnits.get(i) instanceof Worker){
                            workerIDs.add(selectedUnits.get(i).id);
                        }
                    }
                    client.sendSpendInfo(currPlayer.getID(), Castle.RUBY_COST);
                    client.sendSpawnBuildingCommand(currPlayer.getID(), 0, MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, workerIDs);
                }
                else if(menuWorker.isCreatingBarrack()){
                    menuWorker.stopCreatingBarrack();
                    if(!menuWorker.canPlaceBarrack(map)){ 
                        return;
                    }
                    ArrayList<Integer> workerIDs = new ArrayList<Integer>();
                    for(int i = 0; i < selectedUnits.size(); i++){
                        if(selectedUnits.get(i) instanceof Worker){
                            workerIDs.add(selectedUnits.get(i).id);
                        }
                    }
                    client.sendSpendInfo(currPlayer.getID(), Barrack.RUBY_COST);
                    client.sendSpawnBuildingCommand(currPlayer.getID(), 1, MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, workerIDs);
                }
                creating = false;
            }
            else if(miniMapMovingCam) miniMap.stopMovingCam();
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
        if(cmd.unitIndex == 0){
            int new_id = Entity.getId();
            //CreateUnit cU = new CreateUnit(new_id, );
            //warrior
            if(cmd.type == 1){
                Building buildingSpawning = players.get(cmd.playerID).buildings.get(cmd.unitId);
                Warrior war = new Warrior(Vector2.of(Warrior.WARRIOR_WIDTH, Warrior.WARRIOR_HEIGHT), buildingSpawning.getSpawningPosition(), new_id, players.get(cmd.playerID), buildingSpawning.getDbId());
                players.get(cmd.playerID).units.put(new_id, war);
            
                //Añadir datos para nuevo warrior en la base de datos
                //CreateUnit.createUnitQuery cWr = new CreateUnit.createUnitQuery(new_id, Building.dbId, "Warrior");
                //CreateUnit.arrCreateUnit.add(cWr);
            }
            
            
            //worker
            else{
                Building buidlingSpawning = players.get(cmd.playerID).buildings.get(cmd.unitId);
                Worker wor = new Worker(Vector2.of(Worker.WORKER_WIDTH, Worker.WORKER_HEIGHT), buidlingSpawning.getSpawningPosition(), new_id, players.get(cmd.playerID), buidlingSpawning.getDbId());
                players.get(cmd.playerID).units.put(new_id, wor);
                
                //Añadir datos para nuevo worker en la base de datos
                //CreateUnit.createUnitQuery cWk = new CreateUnit.createUnitQuery(new_id, Building.dbId, "Worker");
                //CreateUnit.arrCreateUnit.add(cWk);
            }
        }
    }
    
    public static void executeSpawnBuildingCommand(SpawnBuildingObject cmd){
        // int playerID, int buildingIndex, int xPos, int yPos
        
        if(cmd.buildingIndex == 0){
            int new_id = Entity.getId();
            Castle c = new Castle(Vector2.of(Castle.CASTLE_WIDTH, Castle.CASTLE_HEIGHT), Vector2.of(cmd.xPos, cmd.yPos), new_id, players.get(cmd.playerID));
            c.setDbId();
            players.get(cmd.playerID).buildings.put(new_id, c);
            for(int i = 0; i < cmd.workerIDs.size(); i++){
                ((Worker) (players.get(cmd.playerID).units.get(cmd.workerIDs.get(i)))).buildAt(c);
            }
            
            //Meter datos para nuevo registro de castle en la base de datos
            //int cCDbId = c.getDbId();
            //CreateBuilding.createBuildingQuery cC = new CreateBuilding.createBuildingQuery(cCDbId, partidaId, cmd.playerID, "Castle");
            //CreateBuilding.arrCreateBuilding.add(cC);
        }
        else if(cmd.buildingIndex == 1){
            int new_id = Entity.getId();
            Barrack b = new Barrack(Vector2.of(Barrack.CASTLE_WIDTH, Barrack.CASTLE_HEIGHT), Vector2.of(cmd.xPos, cmd.yPos), new_id, players.get(cmd.playerID));
            b.setDbId();
            players.get(cmd.playerID).buildings.put(new_id, b);
            for(int i = 0; i < cmd.workerIDs.size(); i++){
                ((Worker) (players.get(cmd.playerID).units.get(cmd.workerIDs.get(i)))).buildAt(b);
            }
            
            //Meter datos para nuevo registro de barrack en la base de datos
            //int cBDbId = b.getDbId();
            //CreateBuilding.createBuildingQuery cB = new CreateBuilding.createBuildingQuery(cBDbId, partidaId, cmd.playerID, "Barrack");
            //CreateBuilding.arrCreateBuilding.add(cB);
        }
    }

    public static void startMatch(StartMatchObject cmd){
        matchStarted = true;
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
        
        //ading starting workers
        for(int i = 0; i < MapLayout.workerSpawnPositions[players.size() - 1].length; i++){
            int unit_id = Entity.getId();
            Vector2 pos = Vector2.of(MapLayout.workerSpawnPositions[players.size() - 1][i][0] * MapLayout.scale, MapLayout.workerSpawnPositions[players.size() - 1][i][1] * MapLayout.scale);
            players.get(id).units.put(unit_id, new Worker(Vector2.of(Worker.WORKER_WIDTH, Worker.WORKER_HEIGHT), pos, unit_id, players.get(id), 0));
        }
       
        
    }

    public static void removePlayer(DisconnectionObject disconObj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void loadMap(){
        resources = new HashMap<>();
        walls = new HashMap<>();
        for(int x = 0; x < MapLayout.mapLayout.length; x++){
            for(int y = 0; y < MapLayout.mapLayout[x].length; y++){
                if(MapLayout.mapLayout[x][y] == 1){
                    int new_id = Entity.getId();
                    walls.put(new_id, new Wall(Vector2.of(MapLayout.scale, MapLayout.scale), Vector2.of(x * MapLayout.scale, y * MapLayout.scale), new_id));
                }
                if(MapLayout.mapLayout[x][y] == 2){
                    int new_id = Entity.getId();
                    resources.put(new_id, new Resource(Vector2.of(MapLayout.scale, MapLayout.scale), Vector2.of(x * MapLayout.scale, y * MapLayout.scale), new_id));
                }
            }
        }
    }

    public static void updateUnitInfo(UnitInfoObject unitInfoObject) {
        Player p = players.get(unitInfoObject.playerID);
        for(Unit u : p.units.values()){
            ArrayList<Double> info = unitInfoObject.unitInfo.get(u.id);
            if(info != null)
                u.updateInfo(info);
        }
    }

    public static void updateBuildingInfo(BuildingInfoObject buildingInfoObject) {
        Player p = players.get(buildingInfoObject.playerID);
        
        for(int id : buildingInfoObject.buildingInfo.keySet()){
           if(!p.buildings.containsKey(id)){
               if(buildingInfoObject.buildingInfo.get(id).get(3) == 0){
                   p.buildings.put(id, new Castle(Vector2.of(Castle.CASTLE_WIDTH, Castle.CASTLE_HEIGHT), Vector2.of(buildingInfoObject.buildingInfo.get(id).get(4), buildingInfoObject.buildingInfo.get(id).get(5)), id, p));
               }
               if(buildingInfoObject.buildingInfo.get(id).get(3) == 1){
                   p.buildings.put(id, new Barrack(Vector2.of(Castle.CASTLE_WIDTH, Castle.CASTLE_HEIGHT), Vector2.of(buildingInfoObject.buildingInfo.get(id).get(4), buildingInfoObject.buildingInfo.get(id).get(5)), id, p));
               }
           }
        }
        
        for(Building b : p.buildings.values()){
            ArrayList<Double> info = buildingInfoObject.buildingInfo.get(b.id);
            if(info != null)
                b.updateInfo(info);
        }
    }

    public static void updatePlayerInfo(PlayerInfoObject playerInfoObject) {
        Player p = players.get(playerInfoObject.playerID);
        p.updateInfo(playerInfoObject);
    }
    
    public static void spendRubys(SpendRubysObject cmd){
        players.get(cmd.playerID).spendRubys(cmd.amount);
    }

    public static void updateResourcesInfo(ResourceInfoObject resourceInfoObject) {
        for(Resource r : resources.values()){
            ArrayList<Double> info = resourceInfoObject.resourceInfo.get(r.id);
            if(info != null)
                r.updateInfo(info);
        }
    }
}