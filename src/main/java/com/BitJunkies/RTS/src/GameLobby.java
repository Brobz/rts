/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import com.BitJunkies.RTS.ui.Button;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;

/**
 *
 * @author ulise
 */
public class GameLobby extends GameState{

    protected static Button startGame,leaveGame;
    //protected static GameState mainMenu;
    protected static BufferedImage background;
    protected static Texture backgroundTexture;

    public GameLobby() {
        startGame = new Button(900,475,200,66,"StartGame.jpg",this,null);
        leaveGame = new Button(900,555,200,66,"LeaveGame.jpg",this,null);
        background = ImageLoader.loadImage("/Images/GameLobby.jpg");
        backgroundTexture = AWTTextureIO.newTexture(Display.getProfile(), background, true);
    }
    
    
    public void tick(){
        
    }
    
    public void render(GL2 gl){

        Display.drawImageStatic(gl, null, backgroundTexture, 0, 0, Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT, 1);
        startGame.render(gl);
        leaveGame.render(gl);
    }

    @Override
    public void checkPress() {
        System.out.println("Checkin");
        if(MouseInput.mouseStaticHitBox.intersects(startGame.getHitBox())){
            Game.startMatch(null);
        }
        else if(MouseInput.mouseStaticHitBox.intersects(leaveGame.getHitBox())){
            leaveGame.onPressed();
        }

    }

    @Override
    public void changeTextField(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
