/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import com.BitJunkies.RTS.ui.Button;
import com.BitJunkies.RTS.ui.Label;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Screen for the lobby before the match
 * @author ulise
 */
public class GameLobby extends GameState{

    protected static Button startGame,leaveGame;
    protected static BufferedImage background;
    protected static Texture backgroundTexture;
    protected static Label hostIP;
    protected static ArrayList<Label> connectedPlayers;
    private static GameLobby instance;
    
    /**
     * Singleton for object
     * @return GameLobby instance
     */
    public static GameLobby getInstance(){
        if(instance == null){
            instance = new GameLobby();
        }
        return instance;
    }

    /**
     * Constructor that initializes all the UI elements
     */
    public GameLobby() {
        startGame = new Button(900,475,200,66,"StartGame.jpg",this,null);
        leaveGame = new Button(900,555,200,66,"LeaveGame.jpg",this,null);
        background = ImageLoader.loadImage("/Images/GameLobby.jpg");
        backgroundTexture = AWTTextureIO.newTexture(Display.getProfile(), background, true);
        connectedPlayers = new ArrayList<>();
    }
    
    /**
     * ticking for the game
     */
    public void tick(){
        if(Game.hosting){
            if(Game.server  != null) hostIP = new Label(800,65,200,66, "Host IP: " + Game.server.getIP());
            if(Game.server.connectedPlayers.size() != connectedPlayers.size()){
                connectedPlayers.clear();
                for(int i = 0; i < Game.server.connectedPlayers.size(); i++){
                    connectedPlayers.add(new Label(265, 80 * i + 130, 300, 100, Game.server.connectedPlayers.get(i).toString()));
                }
            }
        }else{
            if(Game.client  != null && Game.client.currServerConnectedPlayers.size() > 0) hostIP = new Label(800,65,200,66, "Host IP: " + Game.client.currServerConnectedPlayers.get(0).connectionIP);
            if(Game.client.currServerConnectedPlayers.size() != connectedPlayers.size()){
                connectedPlayers.clear();
                for(int i = 0; i < Game.client.currServerConnectedPlayers.size(); i++){
                    connectedPlayers.add(new Label(265, 80 * i + 130, 300, 100, Game.client.currServerConnectedPlayers.get(i).connectionName));
                }
            }
        }
    }
    
    /**
     * Method to draw the ui of the screen
     * @param gl GL2
     */
    public void render(GL2 gl){
        
        Display.drawImageStatic(gl, null, backgroundTexture, 0, 0, Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT, 1);
        if(Game.hosting) startGame.render(gl);
        if(hostIP != null) hostIP.render(gl);
        leaveGame.render(gl);
        for(int i = 0; i < connectedPlayers.size(); i++){
            connectedPlayers.get(i).render(gl);
        }
    }

    /**
     * Method to check if a button of the screen or a text field is pressed
     */
    @Override
    public void checkPress() {
        // startGame button pressed
        if(Game.hosting && MouseInput.mouseStaticHitBox.intersects(startGame.getHitBox())){
            Game.client.sendStartMatchCommand(Game.client.client.getID());
        }
        // leaveGame button pressed
        else if(MouseInput.mouseStaticHitBox.intersects(leaveGame.getHitBox())){
            if(Game.hosting){
                Game.client.client.close();
                try {
                    Game.client.client.dispose();
                } catch (IOException ex) {
                    Logger.getLogger(GameLobby.class.getName()).log(Level.SEVERE, null, ex);
                }
                Game.server.server.close();
                try {
                    Game.server.server.dispose();
                } catch (IOException ex) {
                    Logger.getLogger(GameLobby.class.getName()).log(Level.SEVERE, null, ex);
                }
                Game.hosting = false;
            }else{
                Game.client.client.close();
                try {
                    Game.client.client.dispose();
                } catch (IOException ex) {
                    Logger.getLogger(GameLobby.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            leaveGame.setNextState(MainMenu.getInstance());
            leaveGame.onPressed();
        }

    }

    /**
     * Used to change text of text field(not used)
     * @param ke 
     */
    @Override
    public void changeTextField(KeyEvent ke) {
    }
}
