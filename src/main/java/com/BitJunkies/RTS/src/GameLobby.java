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
import java.util.ArrayList;

/**
 *
 * @author ulise
 */
public class GameLobby extends GameState{

    protected static Button startGame,leaveGame;
    protected static BufferedImage background;
    protected static Texture backgroundTexture;
    protected static Label hostIP;
    protected static ArrayList<Label> connectedPlayers;
    private static GameLobby instance;
    
    public static GameLobby getInstance(){
        if(instance == null){
            instance = new GameLobby();
        }
        return instance;
    }

    public GameLobby() {
        startGame = new Button(900,475,200,66,"StartGame.jpg",this,null);
        leaveGame = new Button(900,555,200,66,"LeaveGame.jpg",this,null);
        background = ImageLoader.loadImage("/Images/GameLobby.jpg");
        backgroundTexture = AWTTextureIO.newTexture(Display.getProfile(), background, true);
        connectedPlayers = new ArrayList<>();
    }
    
    
    public void tick(){
        if(Game.hosting){
            if(Game.server  != null) hostIP = new Label(900,100,200,66, Game.server.getIP());
            if(Game.server.connectedPlayers.size() != connectedPlayers.size()){
                connectedPlayers.clear();
                for(int i = 0; i < Game.server.connectedPlayers.size(); i++){
                    connectedPlayers.add(new Label(200, 100 * i + 100, 300, 100, Game.server.connectedPlayers.get(i).toString()));
                }
            }
        }else{
            if(Game.client  != null && Game.client.currServerConnectedPlayers.size() > 0) hostIP = new Label(900,100,200,66, Game.client.currServerConnectedPlayers.get(0).connectionIP);
            if(Game.client.currServerConnectedPlayers.size() != connectedPlayers.size()){
                connectedPlayers.clear();
                for(int i = 0; i < Game.client.currServerConnectedPlayers.size(); i++){
                    connectedPlayers.add(new Label(200, 100 * i + 100, 300, 100, Game.client.currServerConnectedPlayers.get(i).connectionName));
                }
            }
        }
    }
    
    public void render(GL2 gl){
        
        Display.drawImageStatic(gl, null, backgroundTexture, 0, 0, Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT, 1);
        if(Game.hosting) startGame.render(gl);
        if(hostIP != null) hostIP.render(gl);
        leaveGame.render(gl);
        for(int i = 0; i < connectedPlayers.size(); i++){
            connectedPlayers.get(i).render(gl);
        }
    }

    @Override
    public void checkPress() {
        System.out.println("Checkin");
        if(Game.hosting && MouseInput.mouseStaticHitBox.intersects(startGame.getHitBox())){
            Game.client.sendStartMatchCommand(Game.client.client.getID());
        }
        else if(MouseInput.mouseStaticHitBox.intersects(leaveGame.getHitBox())){
            leaveGame.setNextState(MainMenu.getInstance());
            leaveGame.onPressed();
        }

    }

    @Override
    public void changeTextField(KeyEvent ke) {
    }
}
