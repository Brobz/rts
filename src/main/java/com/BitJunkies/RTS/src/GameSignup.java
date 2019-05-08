/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import static com.BitJunkies.RTS.src.GameLogin.backgroundTexture;
import com.BitJunkies.RTS.ui.Button;
import com.BitJunkies.RTS.ui.Label;
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
    Label missmatch1,missmatch2,empty1,empty2,empty3,empty4;
    protected static GameSignup instance;
    protected static BufferedImage background;
    protected static Texture backgroundTexture;
    protected static boolean passMismatch = false,emptyUsername = false, emptyPassword;
    
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
        missmatch1 = new Label(990, 320, 100, 40, "Passwords");
        missmatch2 = new Label(990, 360, 100, 40, "don't match!");
        empty1 = new Label(990, 230, 100, 40, "Cant be");
        empty2 = new Label(990, 270, 100, 40, "blank!");
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
        if(passMismatch){
            missmatch1.render(gl);
            missmatch2.render(gl);
        }
    }

    @Override
    public void checkPress() {
        username.setEnabled(false);
        password.setEnabled(false);
        confirm.setEnabled(false);
        passMismatch = false;
        if(MouseInput.mouseStaticHitBox.intersects(back.getHitBox())){
            back.setNextState(GameLogin.getInstance());
            back.onPressed();
        }
        // Signup button pressed
        else if(MouseInput.mouseStaticHitBox.intersects(signup.getHitBox())){
            //If the password and confirmations match
            if(password.getTextInput() == null ? confirm.getTextInput() == null : password.getTextInput().equals(confirm.getTextInput())){
                // TODO Validate if username isn't already created
                
                // Call change of game state (Only if everything is OK to go)
                signup.onPressed();
            }
            else{
                passMismatch = true;
            }
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
