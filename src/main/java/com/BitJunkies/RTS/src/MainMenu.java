/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import static com.BitJunkies.RTS.src.Assets.background;
import static com.BitJunkies.RTS.src.Assets.backgroundTexture;
import com.BitJunkies.RTS.src.server.KryoUtil;
import com.BitJunkies.RTS.ui.Button;
import com.BitJunkies.RTS.ui.Label;
import com.BitJunkies.RTS.ui.TextField;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;

/**
 *
 * @author ulise
 */
public class MainMenu extends GameState{
    protected static Button hostGame,joinGame;
    protected static GameState gameLobby;
    protected static BufferedImage background;
    protected static Texture backgroundTexture;
    protected static TextField textInput;
    protected static Label instructIp, instructHost;
    String ip;
    protected static MainMenu instance;
    
    public static MainMenu getInstance(){
        if(instance == null){
            instance = new MainMenu();
        }
        return instance;
    }

    public MainMenu() {
        
        hostGame = new Button(125,450,432,126,"HostGame.jpg",this,GameLobby.getInstance());
        joinGame = new Button(650,450,432,126,"JoinGame.jpg",this,GameLobby.getInstance());
        background = ImageLoader.loadImage("/Images/MainMenu.jpg");
        backgroundTexture = AWTTextureIO.newTexture(Display.getProfile(), background, true);
        textInput = new TextField(650,400,320,40);
        instructIp = new Label(650,350,200,40,"Insert IP of host");
        instructHost = new Label(175,400,200,40, "Start hosting the game");
    }
    
    
    public void tick(){
        
    }
    
    public void render(GL2 gl){

        Display.drawImageStatic(gl, null, backgroundTexture, 0, 0, Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT, 1);
        hostGame.render(gl);
        joinGame.render(gl);
        textInput.render(gl);
        instructIp.render(gl);
        instructHost.render(gl);
    }

    @Override
    public void checkPress() {
        textInput.setEnabled(false);
        if(MouseInput.mouseStaticHitBox.intersects(hostGame.getHitBox())){
            KryoUtil.setHOST_IP("localhost");
            hostGame.onPressed();
        }
        else if(MouseInput.mouseStaticHitBox.intersects(joinGame.getHitBox())){
            ip = textInput.getTextInput();
            KryoUtil.setHOST_IP(ip);
            joinGame.onPressed();
        }
        else if(MouseInput.mouseStaticHitBox.intersects(textInput.getHitBox())){
            textInput.setEnabled(true);
        }

    }
    
    @Override
    public void changeTextField(KeyEvent ke){
        if(textInput.isEnabled()){            
            textInput.updateLabel(ke);
        }
    }
}
