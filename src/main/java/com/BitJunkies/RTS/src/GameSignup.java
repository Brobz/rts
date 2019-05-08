/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import static com.BitJunkies.RTS.src.GameLogin.backgroundTexture;
import com.BitJunkies.RTS.ui.Button;
import com.BitJunkies.RTS.ui.TextField;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;

/**
 *
 * @author ulise
 */
public class GameSignup extends GameState{
    TextField username,password,confirm;
    Button signup,back;
    protected static GameSignup instance;
    protected static BufferedImage background;
    protected static Texture backgroundTexture;
    
    public static GameSignup getInstance(){
        if(instance == null){
            instance = new GameSignup();
        }
        return instance;
    }
    
    public GameSignup(){
        back = new Button(50,50,80,80,"back.png",this,null);
        signup = new Button(487,539,260,64,"SignupGame.jpg",this,MainMenu.getInstance());
        username = new TextField(535,240,350,40);
        password = new TextField(535,333,350,40);
        confirm = new TextField(535,428,350,40);
        password.setHide(true);
        confirm.setHide(true);
        background = ImageLoader.loadImage("/Images/Signup.jpg");
        backgroundTexture = AWTTextureIO.newTexture(Display.getProfile(), background, true);
    }
    
    @Override
    public void tick() {
        
    }

    @Override
    public void render(GL2 gl) {
        Display.drawImageStatic(gl, null, backgroundTexture, 0, 0, Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT, 1);
        signup.render(gl);
        back.render(gl);
        username.render(gl);
        password.render(gl);
        confirm.render(gl);
        
    }

    @Override
    public void checkPress() {
        username.setEnabled(false);
        password.setEnabled(false);
        confirm.setEnabled(false);
        if(MouseInput.mouseStaticHitBox.intersects(back.getHitBox())){
            back.setNextState(GameLogin.getInstance());
            back.onPressed();
        }
        else if(MouseInput.mouseStaticHitBox.intersects(signup.getHitBox())){
            signup.onPressed();
        }
        else if(MouseInput.mouseStaticHitBox.intersects(username.getHitBox())){
            username.setEnabled(true);
        }
        else if(MouseInput.mouseStaticHitBox.intersects(password.getHitBox())){
            password.setEnabled(true);
        }
        else if(MouseInput.mouseStaticHitBox.intersects(confirm.getHitBox())){
            confirm.setEnabled(true);
        }
    }

    @Override
    public void changeTextField(KeyEvent ke) {
        if(username.isEnabled()){            
            username.updateLabel(ke);
        }
        if(password.isEnabled()){            
            password.updateLabel(ke);
        }
        if(confirm.isEnabled()){            
            confirm.updateLabel(ke);
        }

    }
    
}
