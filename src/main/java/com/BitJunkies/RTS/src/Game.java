/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import DatabaseQueries.CreateGame;
import DatabaseQueries.CreateJugador;
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
import com.jogamp.newt.event.KeyEvent;
import DatabaseQueries.CreateJugadorEnPartida;
import DatabaseQueries.InsertToDB;
import DatabaseQueries.SelectFromDB;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_DST_ALPHA;
import static com.jogamp.opengl.GL.GL_DST_COLOR;
import static com.jogamp.opengl.GL.GL_ONE;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_DST_ALPHA;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_SRC_COLOR;
import static com.jogamp.opengl.GL.GL_ZERO;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import mikera.vectorz.Vector2;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rober
 */
public class Game {
    //game mechanics
    private static boolean running = true;
    private static int FPS = 30;
    private static GLWindow window;
    public static Camera camera;
    
    //player stuff
    private static ConcurrentHashMap<Integer, Resource> resources;
    private static ConcurrentHashMap<Integer, Wall> walls;
    public static Player currPlayer;
    public static ConcurrentHashMap<String, Player> players;
    
    //Map stuff
    public static GridMap map;
    public static MiniMap miniMap;
    public static boolean miniMapMovingCam;
    
    //Server stuff
    public static GameServer server;
    public static GameClient client;

    public static String loggedInUsername = null;
    public static boolean hosting = false;
    public static boolean matchStarted = false;
    public static boolean matchIsOver = false;
    public static boolean queriesDone = false;
    
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
    
    //Game States (screens)
    private static GameState currState;
    
    public static int framesUntillNextSync;
    public static final int syncDelay = FPS / 12;
    
     // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.postgresql.Driver";  
    static final String DB_URL = "jdbc:postgresql://ec2-54-225-95-183.compute-1.amazonaws.com:5432/dah1sh2i3uomfn?sslmode=require&user=seaynizasqgwhc&password=015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020";
    public static Connection conn;
    

    // Attributes for match
    public static int partidaId  = 0;
    public static long inicioPartida;
    public static long finPartida;
    private static String winner;
    public static ArrayList<Float> resultsQueries;
    
    private static Connection getConnection() throws URISyntaxException, SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    /**
     * Main class, calls for the start of thread and connection to database
     * @param args
     * @throws URISyntaxException
     */
    public static void main(String args[]) throws URISyntaxException{
        try {
            conn = getConnection();
            System.out.println("Connected to Database");
        } catch (SQLException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        window = Display.init();
        
        start();
    }
    
    /**
     * Method that starts the thread
     */
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
                        try {
                            // Input
                            // TODO
                            // Update
                            tick();
                        } catch (SQLException ex) {
                            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
    
    /**
     * Method that ticks the game
     * @throws SQLException
     * @throws URISyntaxException
     */
    public static void tick() throws SQLException, URISyntaxException{
        camera.tick();
        
        
        if(hosting && server != null) server.tick();
        
        if(!matchStarted){
            currState.tick();
            return;
        }
        
        if(matchIsOver){
            resetMatch();
        }
        
        
        if(hosting && !matchIsOver){
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
                if (!p.hasLost()) {
                    p.tickBuildings(map);
                    p.tickUnits(map);
                }
                else {
                    if (!p.hasKilledUnits())
                        p.killUnits();
                }
            }
            
            int contFallenPlayers = 0;
            for(Player p : players.values()){
                if (p.hasLost()) 
                    contFallenPlayers++;
            }
            if (contFallenPlayers == players.size() - 1)
                matchIsOver = true;
            
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
                        }else{
                            i.add(null);
                            i.add(null);
                        }
                        i.add((double)Entity.curr_id);
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
                        i.add((double)Entity.curr_id);
                        bInfo.put(b.id, i);
                    }
                    
                    client.sendUnitInfo(p.getID(), uInfo, p.getUsername());
                    client.sendBuildingInfo(p.getID(), bInfo, p.getUsername());
                    client.sendPlayerInfo(p.getID(), p.getRubys(), p.hasLost(), p.getUsername());
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
        
        if(matchIsOver) {
            finPartida = System.currentTimeMillis();
            
            for (Player p : players.values()) {
                if (!p.hasLost()) {
                    winner = p.getUsername();
                    break;
                }
            }
            executeInsertQueries();
        }
    }
    
    /**
     * Method that renders the objects in the screen
     * @param drawable
     */
    public static void render(GLAutoDrawable drawable){
        //basic openGl methods
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        
        /*
            gl.glEnable(GL_BLEND);
            // Use a simple blendfunc for drawing the background
            gl.glBlendFunc(GL_ONE, GL_ZERO);
            // Draw entire background without masking
            Display.drawRectangleStatic(gl, camera, 30, 30, 500, 500, 1f, 0, 0, 1f);
            // Next, we want a blendfunc that doesn't change the color of any pixels,
            // but rather replaces the framebuffer alpha values with values based
            // on the whiteness of the mask. In other words, if a pixel is white in the mask,
            // then the corresponding framebuffer pixel's alpha will be set to 1.
            gl.glBlendFuncSeparate(GL_ZERO, GL_ONE, GL_SRC_COLOR, GL_ZERO);
            // Now "draw" the mask (again, this doesn't produce a visible result, it just
            // changes the alpha values in the framebuffer)
            Display.drawRectangleStatic(gl, camera, 30, 30, 500, 500, 0, 0, 0, 0f);
            Display.drawImageStatic(gl, camera, Assets.circleTexture, 30, 30, 250, 250, 1f);
            Display.drawImageStatic(gl, camera, Assets.circleTexture, 280, 30, 250, 250, 1f);
            // Finally, we want a blendfunc that makes the foreground visible only in
            // areas with high alpha.
            gl.glBlendFunc(GL_DST_ALPHA, GL_ONE_MINUS_DST_ALPHA);
            Display.drawRectangleStatic(gl, camera, 30, 30, 500, 500, 0, 0, 1, 1f);
        */
        
        if(!matchStarted){
            currState.render(gl);
            return;
        }
        
        gl.glEnable(GL_BLEND);
        // Use a simple blendfunc for drawing the background
        gl.glBlendFunc(GL_ONE, GL_ZERO);
        Display.drawImage(gl, camera, Assets.darkMapTexture, 0, 0, MapLayout.SCALED_WIDTH, MapLayout.SCALED_HEIGHT, 1f);
        // Next, we want a blendfunc that doesn't change the color of any pixels,
        // but rather replaces the framebuffer alpha values with values based
        // on the whiteness of the mask. In other words, if a pixel is white in the mask,
        // then the corresponding framebuffer pixel's alpha will be set to 1.
        gl.glBlendFuncSeparate(GL_ZERO, GL_ONE, GL_SRC_COLOR, GL_ZERO);
        //render masks
        Display.drawRectangle(gl, camera, 0, 0, MapLayout.SCALED_WIDTH, MapLayout.SCALED_HEIGHT, 0.0f, 0.0f, 0.0f, 0f);
        
        //test blending ?
        gl.glBlendFuncSeparate(GL_ZERO, GL_ONE, GL_ONE, GL_ONE);
        
        currPlayer.renderMasks(gl, camera);
        
        //render areas with high alpha.
        gl.glBlendFuncSeparate(GL_DST_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_DST_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //gl.glBlendFuncSeparate(GL_DST_ALPHA, GL_ONE_MINUS_DST_ALPHA, GL_DST_ALPHA, GL_ONE);      
        //Display.drawRectangle(gl, camera, 0, 0, MapLayout.SCALED_WIDTH, MapLayout.SCALED_HEIGHT, 0.2f, 0.2f, 0.2f, 1f);
        Display.drawImage(gl, camera, Assets.mapTexture, 0, 0, MapLayout.SCALED_WIDTH, MapLayout.SCALED_HEIGHT, 1f);
        
        //gl.glBlendFunc(GL_DST_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
       
        //resources render
        for(Resource res : resources.values()){
             res.render(gl, camera);
        }
        
        //resources render
        for(Wall wall : walls.values()){
             wall.render(gl, camera);
        }
        
        //player renders
        for(Player p : players.values()){
            p.renderUnits(gl, camera);
            p.renderBuildings(gl, camera);
        }
        
        //test render alv
        
        gl.glBlendFunc(GL_ONE_MINUS_DST_ALPHA, GL_DST_ALPHA);
        Display.drawImage(gl, camera, Assets.darkMapTexture, 0, 0, MapLayout.SCALED_WIDTH, MapLayout.SCALED_HEIGHT, 1f);
        
        //go back to normal render
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        //if workers are active then tick the menu
        if(workersActive) menuWorker.render(gl, camera);
        if(buildingActive){
            if(selectedBuilding instanceof Castle) menuCastle.render(gl, camera);
            else if(selectedBuilding instanceof Barrack) menuBarrack.render(gl, camera);
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
        
        TextRenderer textRenderer = new TextRenderer(new Font("Verdana", Font.BOLD, 25));
        textRenderer.beginRendering(Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT);
        textRenderer.setColor(Color.YELLOW);
        textRenderer.setSmoothing(true);
        if(currPlayer != null)
            textRenderer.draw("Rubys: " + currPlayer.getRubys(), 50, Display.WINDOW_HEIGHT - 50);
        textRenderer.endRendering();
        //map.render(gl, camera);
        miniMap.render(gl, camera);
    }
    
    /**
     * Method that stops the game
     */
    public static void stop(){
        running = false;
    }
    
    /**
     * Method that initializes all the objects used in the game
     */
    public static void init(){
        //initialize basic stuff in game
        Assets.init();
        Assets.backgroundMusic.play();
        
        players = new ConcurrentHashMap<String, Player>();
        
        loadMap();
        
        //start server stuff
        // if(hosting) hostServer();
        // client = new GameClient();
        
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
        miniMap = new MiniMap(Vector2.of(230, 230), Vector2.of(Display.WINDOW_WIDTH - 250, Display.WINDOW_HEIGHT-250), 0);
        
        //initialize game state
        currState = GameLogin.getInstance();
        miniMapMovingCam = false;
        
        
        resultsQueries = new ArrayList<>();
        
    }
    
    /**
     * Method that gets the units of the current player
     * @return ConcurrentHashMap
     */
    public static ConcurrentHashMap<Integer, Unit> getUnits(){
        return currPlayer.units;
    }
    
    /**
     * Method that gets the players that are in a match
     * @return ConcurrentHashMap
     */
    public static ConcurrentHashMap<String, Player> getPlayers() {
        return players;
    }
    
    /**
     * Method that listens when the mouse is being clicked (pressed and removed)
     * @param button
     */
    public static void mouseClicked(int button) {
        if(!matchStarted) return;
        //check if it is a right click
       if(button == MouseEvent.BUTTON3){
        //moving worker units to resource
        if(selectedUnits.isEmpty())
            return;
        if(selectedUnitsType == 1){
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
       
       CreateJugadorEnPartida.mapAcciones.put(currPlayer.getID(), CreateJugadorEnPartida.getAcumAcciones(currPlayer.getID()) + 1);
    }
    
    /**
     * Method that listens when the mouse is being pressed
     * @param button of mouse
     */
    public static void mousePressed(int button){
        if(!matchStarted){
            //check if button is pressed
            currState.checkPress();
            return;
        }
        //checking if mouse are pressed
        if(button == MouseEvent.BUTTON1){
            
            
            //check if workers are active
            miniMapMovingCam = miniMap.checkPress();
            if(!miniMapMovingCam && workersActive) creating = menuWorker.checkPress(MouseInput.mouseStaticHitBox);
            
            //check if a building is active
            if(buildingActive){
                if(selectedBuilding instanceof Castle){
                    if(menuCastle.checkPress(MouseInput.mouseStaticHitBox)){
                        ((Castle)selectedBuilding).addWorker();
                        client.sendSpendInfo(currPlayer.getID(), Worker.RUBY_COST, loggedInUsername);
                    }
                }
                else if(selectedBuilding instanceof Barrack){
                    if(menuBarrack.checkPress(MouseInput.mouseStaticHitBox)){ 
                        ((Barrack)selectedBuilding).addWarrior();
                        client.sendSpendInfo(currPlayer.getID(), Warrior.RUBY_COST, loggedInUsername);
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
    
    /**
     * Method that listens when the mouse is being dragged
     */
    public static void mouseDragged(){
        //checking if we are selecting to expand selection box
        if(isSelecting){
           selectionBox = new Rectangle(selectionBox.x, selectionBox.y, MouseInput.mouseHitBox.x - selectionBox.x, MouseInput.mouseHitBox.y - selectionBox.y);
        }
    }
    
    /**
     * Method that listens when the mouse button is being released
     * @param button of the mouse
     */
    public static void mouseReleased(int button){
        if(!matchStarted) return;
        //if it was a right click
        if(button == MouseEvent.BUTTON1){
            //checking if a selection is being made
            if(isSelecting){
                //here we check the selection of units
                for(int i = 0; i < selectedUnits.size(); i++){
                    selectedUnits.get(i).deselect();
                    i--;
                }
                //selectedUnits.clear();
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
                    client.sendSpendInfo(currPlayer.getID(), Castle.RUBY_COST, loggedInUsername);
                    client.sendSpawnBuildingCommand(currPlayer.getID(), 0, MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, workerIDs, loggedInUsername);
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
                    client.sendSpendInfo(currPlayer.getID(), Barrack.RUBY_COST, loggedInUsername);
                    client.sendSpawnBuildingCommand(currPlayer.getID(), 1, MouseInput.mouseHitBox.x, MouseInput.mouseHitBox.y, workerIDs, loggedInUsername);
                }
                creating = false;
            }
            else if(miniMapMovingCam) miniMap.stopMovingCam();
        }   
    }
            
    /**
     * method that listens when a key is being pressed
     * @param ke
     */
    public static void keyPressed(KeyEvent ke){
        if(!matchStarted){
            currState.changeTextField(ke);
            return;
        }
    }
    
    /**
     * Method that gets the camera of the game
     * @return Camera
     */
    public static Camera getCamera(){
        return camera;
    }
    
    /**
     * Method that gets the FPS of the game
     * @return int
     */
    public static int getFPS(){
        return FPS;
    }
    
    /**
     * Method that processes movements for the units
     * @param cmd contains id's of units and players
     */
    public static void executeMoveCommand(MoveObject cmd){
        players.get(cmd.playerName).units.get(cmd.entityID).stopAttacking();
        if(players.get(cmd.playerName).units.get(cmd.entityID) instanceof Worker){
            ((Worker) (players.get(cmd.playerName).units.get(cmd.entityID))).stopBuilding();
            ((Worker) (players.get(cmd.playerName).units.get(cmd.entityID))).stopMining();
        }
        players.get(cmd.playerName).units.get(cmd.entityID).moveTo(Vector2.of(cmd.xPosition, cmd.yPosition));
    }
    
    /**
     * Method that processes if the worker is going to mine
     * @param cmd contains id's of units and players
     */
    public static void executeMineCommand(MineObject cmd){
        ((Worker)players.get(cmd.playerName).units.get(cmd.workerID)).mineAt(resources.get(cmd.resourceID));
    }
    
    /**
     * Method that processes if the warrior is going to attack
     * @param cmd contains id's of units and players
     */
    public static void executeAttackCommand(AttackObject cmd){
        if(cmd.targetBuildingID == -1)
            players.get(cmd.playerName).units.get(cmd.unitID).attackAt(players.get(cmd.targetPlayerName).units.get(cmd.targetUnitID));
        else
            players.get(cmd.playerName).units.get(cmd.unitID).attackAt(players.get(cmd.targetPlayerName).buildings.get(cmd.targetBuildingID));

    }
    
    /**
     * Method that processes  if a building is being created
     * @param cmd contains id's of units and players
     */
    public static void executeBuildCommand(BuildObject cmd){
        ((Worker)players.get(cmd.playerName).units.get(cmd.workerID)).buildAt(players.get(cmd.playerName).buildings.get(cmd.targetID));
    }
    
    /**
     * Method that processes if a unit is spawning from a building
     * @param cmd contains id's of units and players
     */
    public static void executeSpawnUnitCommand(SpawnUnitObject cmd){
        // spawnear unidad en el building
        if(cmd.unitIndex == 0){
            int new_id = Entity.getId();
            //CreateUnit cU = new CreateUnit(new_id, );
            //warrior
            if(cmd.type == 1){
                Building buildingSpawning = players.get(cmd.playerName).buildings.get(cmd.unitId);
                Warrior war = new Warrior(Vector2.of(Warrior.WARRIOR_WIDTH, Warrior.WARRIOR_HEIGHT), buildingSpawning.getSpawningPosition(), new_id, players.get(cmd.playerName));
                players.get(cmd.playerName).units.put(new_id, war);
            
                //Añadir datos para nuevo warrior en la base de datos
                CreateJugadorEnPartida.mapUn.put(currPlayer.getID(), CreateJugadorEnPartida.getAcumUn(currPlayer.getID()) + 1);

            }
            
            
            //worker
            else{
                Building buidlingSpawning = players.get(cmd.playerName).buildings.get(cmd.unitId);
                Worker wor = new Worker(Vector2.of(Worker.WORKER_WIDTH, Worker.WORKER_HEIGHT), buidlingSpawning.getSpawningPosition(), new_id, players.get(cmd.playerName));
                players.get(cmd.playerName).units.put(new_id, wor);
                
                //Añadir datos para nuevo worker en la base de datos
                CreateJugadorEnPartida.mapUn.put(currPlayer.getID(), CreateJugadorEnPartida.getAcumUn(currPlayer.getID()) + 1);
            }
        }
    }
    
    /**
     * Method that processes if a building is being created by the workers
     * @param cmd contains id's of units and players
     */
    public static void executeSpawnBuildingCommand(SpawnBuildingObject cmd){
        // int playerID, int buildingIndex, int xPos, int yPos
        
        if(cmd.buildingIndex == 0){
            int new_id = Entity.getId();
            Castle c = new Castle(Vector2.of(Castle.CASTLE_WIDTH, Castle.CASTLE_HEIGHT), Vector2.of(cmd.xPos, cmd.yPos), new_id, players.get(cmd.playerName));
            players.get(cmd.playerName).buildings.put(new_id, c);
            for(int i = 0; i < cmd.workerIDs.size(); i++){
                ((Worker) (players.get(cmd.playerName).units.get(cmd.workerIDs.get(i)))).buildAt(c);
            }
            
            CreateJugadorEnPartida.mapEd.put(currPlayer.getID(), CreateJugadorEnPartida.getAcumEd(currPlayer.getID()) + 1);
        }
        else if(cmd.buildingIndex == 1){
            int new_id = Entity.getId();
            Barrack b = new Barrack(Vector2.of(Barrack.CASTLE_WIDTH, Barrack.CASTLE_HEIGHT), Vector2.of(cmd.xPos, cmd.yPos), new_id, players.get(cmd.playerName));
            players.get(cmd.playerName).buildings.put(new_id, b);
            for(int i = 0; i < cmd.workerIDs.size(); i++){
                ((Worker) (players.get(cmd.playerName).units.get(cmd.workerIDs.get(i)))).buildAt(b);
            }
            
            CreateJugadorEnPartida.mapEd.put(currPlayer.getID(), CreateJugadorEnPartida.getAcumEd(currPlayer.getID()) + 1);
        }
    }

    /**
     * Method that starts the match session
     * @param cmd contains id's of units and players
     */
    public static void startMatch(StartMatchObject cmd){
        matchStarted = true;
        inicioPartida =  System.currentTimeMillis();
    }
    
    /**
     * Method that initializes the server for the hosting
     */
    public static void hostServer(){
        hosting = true;
        server = new GameServer();
        client = new GameClient(loggedInUsername);
    }
    
    /**
     * Method that adds a new player to a match session
     * @param conObj
     */
    public static void addNewPlayer(ConnectionObject conObj){
        int id = players.size() + 1;
        if(conObj.self){
            currPlayer = new Player(id, conObj.connectionName);
            players.put(conObj.connectionName, currPlayer);
            camera.setPosition(Vector2.of(MapLayout.cameraStartPositions[id - 1][0] * MapLayout.scale, MapLayout.cameraStartPositions[id - 1][1] * MapLayout.scale));
        }else{
            players.put(conObj.connectionName, new Player(id, conObj.connectionName));
        }
        
        //ading starting workers
        for(int i = 0; i < MapLayout.workerSpawnPositions[players.size() - 1].length; i++){
            int unit_id = Entity.getId();
            Vector2 pos = Vector2.of(MapLayout.workerSpawnPositions[players.size() - 1][i][0] * MapLayout.scale, MapLayout.workerSpawnPositions[players.size() - 1][i][1] * MapLayout.scale);
            players.get(conObj.connectionName).units.put(unit_id, new Worker(Vector2.of(Worker.WORKER_WIDTH, Worker.WORKER_HEIGHT), pos, unit_id, players.get(conObj.connectionName)));
        }
       
        
        int building_id = Entity.getId();
        Vector2 pos = Vector2.of(MapLayout.buildingSpawnPositions[players.size() - 1][0] * MapLayout.scale, MapLayout.buildingSpawnPositions[players.size() - 1][1] * MapLayout.scale);
        players.get(conObj.connectionName).buildings.put(building_id, new Castle(Vector2.of(Castle.CASTLE_WIDTH, Castle.CASTLE_HEIGHT), pos, building_id, players.get(conObj.connectionName)));
        players.get(conObj.connectionName).buildings.get(building_id).setHealth(players.get(conObj.connectionName).buildings.get(building_id).getMaxHealth());
    }

    /**
     * Method that disconnects a player from a match session
     * @param disconObj
     */
    public static void removePlayer(DisconnectionObject disconObj) {
        for (Player p : players.values()) {
            
            if (p.getUsername().equals(disconObj.connectionName)){
                players.remove(p.getUsername());
                break;
            }
        }
        
        int i = 1;
        for (Player p : players.values()) {
            p.setID(i);
            i++;
        }
        
        if(disconObj.connectionName.equals(loggedInUsername)) {
            client.client.close();
            if(hosting){
                server.server.close();
                hosting = false;
            }
            players.clear();
            Entity.curr_id = -1;
        }
    }
    
    /**
     * Method that initializes the map
     */
    public static void loadMap(){
        resources = new ConcurrentHashMap<>();
        walls = new ConcurrentHashMap<>();
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

    /**
     * Method that synchronizes all the events of the units between the players
     * @param unitInfoObject contains information about current units
     */
    public static void updateUnitInfo(UnitInfoObject unitInfoObject) {
        if(!matchStarted) return;
        
        Player p = players.get(unitInfoObject.playerName);
        
        for(int id : unitInfoObject.unitInfo.keySet()){
           if(!p.units.containsKey(id)){
               Entity.curr_id = (int) Math.floor((unitInfoObject.unitInfo.get(id).get(11)));
               if(unitInfoObject.unitInfo.get(id).get(10) == null){
                   p.units.put(id, new Warrior(Vector2.of(Warrior.WARRIOR_WIDTH, Warrior.WARRIOR_HEIGHT), Vector2.of(unitInfoObject.unitInfo.get(id).get(1), unitInfoObject.unitInfo.get(id).get(2)), id, p));
               }
               else{
                   p.units.put(id, new Worker(Vector2.of(Worker.WORKER_WIDTH, Worker.WORKER_HEIGHT), Vector2.of(unitInfoObject.unitInfo.get(id).get(1), unitInfoObject.unitInfo.get(id).get(2)), id, p));
               }
           }
        }
        
        for(Unit u : p.units.values()){
            ArrayList<Double> info = unitInfoObject.unitInfo.get(u.id);
            if(info != null)
                u.updateInfo(info);
        }
    }

    /**
     * Method that synchronizes all the events of the buildings between the players
     * @param buildingInfoObject contains information about current buildings
     */
    public static void updateBuildingInfo(BuildingInfoObject buildingInfoObject) {
        if(!matchStarted) return;
        
        Player p = players.get(buildingInfoObject.playerName);
        
        for(int id : buildingInfoObject.buildingInfo.keySet()){
           if(!p.buildings.containsKey(id)){
               Entity.curr_id = (int) Math.floor((buildingInfoObject.buildingInfo.get(id).get(6)));
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

    /**
     * Method that synchronizes the events of the players
     * @param playerInfoObject contains information about current players
     */
    public static void updatePlayerInfo(PlayerInfoObject playerInfoObject) {
        if(!matchStarted) return;
        
        Player p = players.get(playerInfoObject.playerName);
        p.updateInfo(playerInfoObject);
    }
    
    /**
     * Method that removes rubies from the player when creating something
     * @param cmd contains how much rubies
     */
    public static void spendRubys(SpendRubysObject cmd){
        players.get(cmd.playerName).spendRubys(cmd.amount);
    }

    /**
     * Method that synchronizes the resources between the players
     * @param resourceInfoObject
     */
    public static void updateResourcesInfo(ResourceInfoObject resourceInfoObject) {
        if(!matchStarted) return;
        
        for(Resource r : resources.values()){
            ArrayList<Double> info = resourceInfoObject.resourceInfo.get(r.id);
            if(info != null)
                r.updateInfo(info);
        }
    }
    
    /**
     * Method to change the Game State, screen
     * @param nextState
     */
    public static void setCurrGameState(GameState nextState){
        currState = nextState;
    }
    
    /**
     * Method for the user to join the server
     */
    public static void joinServer(){
        client = new GameClient(loggedInUsername);
    }
    
    /**
     * Method that inserts rows in the database
     * @throws SQLException
     * @throws URISyntaxException
     */
    public static void executeInsertQueries() throws SQLException, URISyntaxException {
        InsertToDB.insertGame(new CreateGame.createGameQuery(inicioPartida, finPartida, winner));
        InsertToDB.insertJugadorEnPartida(CreateJugadorEnPartida.arrCreateJugadorEnPartida);
    }
    
    /**
     * Method that run the select queries for the database
     * @return
     */
    public static ArrayList<Float> executeSelectQueries() {
        Game.queriesDone = true;
        ArrayList<Float> results;
        results = new ArrayList<Float>();
        results.add(SelectFromDB.getActionsPerMin(loggedInUsername));
        results.add(SelectFromDB.getBuildingPerGame(loggedInUsername));
        results.add(SelectFromDB.getUnitsPerGame(loggedInUsername));
        results.add(SelectFromDB.getWinRate(loggedInUsername));
        results.add(SelectFromDB.getFloatingResources(loggedInUsername));
        
        return results;
    }

    /**
     * Method that starts over a Match after it's finished
     */
    public static void resetMatch() {
        matchStarted = false;
        matchIsOver = false;
        Entity.curr_id = -1;
        
        players = new ConcurrentHashMap<String, Player>();
        
        if(hosting){
            server.resetLobby();
        }
        
        loadMap();
        
        //start server stuff
        // if(hosting) hostServer();
        // client = new GameClient();
        
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
        miniMap = new MiniMap(Vector2.of(230, 230), Vector2.of(Display.WINDOW_WIDTH - 250, Display.WINDOW_HEIGHT-250), 0);
        
        miniMapMovingCam = false;
        currState = GameLobby.getInstance();
        
        resultsQueries = new ArrayList<>();
        
    }
}