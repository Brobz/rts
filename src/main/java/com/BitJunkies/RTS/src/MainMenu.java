/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import static com.BitJunkies.RTS.src.Assets.background;
import static com.BitJunkies.RTS.src.Assets.backgroundTexture;
import com.BitJunkies.RTS.ui.Button;
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

    public MainMenu() {
        gameLobby = new GameLobby();
        hostGame = new Button(200,400,432,126,"HostGame.jpg",this,gameLobby);
        joinGame = new Button(700,400,432,126,"JoinGame.jpg",this,gameLobby);
        background = ImageLoader.loadImage("/Images/MainMenu.jpg");
        backgroundTexture = AWTTextureIO.newTexture(Display.getProfile(), background, true);
        textInput = new TextField(400,300,400,40);
    }
    
    
    public void tick(){
        
    }
    
    public void render(GL2 gl){

        Display.drawImageStatic(gl, null, backgroundTexture, 0, 0, Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT, 1);
        hostGame.render(gl);
        joinGame.render(gl);
        textInput.render(gl);
    }

    @Override
    public void checkPress() {
        if(MouseInput.mouseStaticHitBox.intersects(hostGame.getHitBox())){
            hostGame.onPressed();
        }
        else if(MouseInput.mouseStaticHitBox.intersects(joinGame.getHitBox())){
            joinGame.onPressed();
        }

    }
    
    @Override
    public void changeTextField(KeyEvent ke){
        textInput.updateLabel(ke);
    }
}
