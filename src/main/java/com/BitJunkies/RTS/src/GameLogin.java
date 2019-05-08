/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;


import DatabaseQueries.SelectFromDB;
import com.BitJunkies.RTS.input.MouseInput;
import static com.BitJunkies.RTS.src.MainMenu.textInput;
import com.BitJunkies.RTS.src.server.KryoUtil;
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
public class GameLogin extends GameState{
    protected Button signup,login;
    protected TextField username,password;
    protected Label failMessage;
    protected boolean failLogin = false;
    protected static BufferedImage background;
    protected static Texture backgroundTexture;
    private static GameLogin instance;
    
    public static GameLogin getInstance(){
        if(instance == null){
            instance = new GameLogin();
        }
        return instance;
    }
    
    public GameLogin(){
        login = new Button(442,405,330,80,"LoginGame.jpg",this,MainMenu.getInstance());
        signup = new Button(442,560,330,80,"SignupGame.jpg",this,GameSignup.getInstance());
        username = new TextField(520,195,300,50);
        password = new TextField(520,310,300,50);
        failMessage = new Label(520,260,200,50,"Wrong username or password, please try again");
        password.setHide(true);
        background = ImageLoader.loadImage("/Images/Login.jpg");
        backgroundTexture = AWTTextureIO.newTexture(Display.getProfile(), background, true);
    }
    
    
    @Override
    public void checkPress() {
        username.setEnabled(false);
        password.setEnabled(false);
        failLogin = false;
        if(MouseInput.mouseStaticHitBox.intersects(login.getHitBox())){
            
            if(!SelectFromDB.validatePassword(username.getTextInput(), password.getTextInput())){
                failLogin = true;
                return;
            }
            
            Game.loggedInUsername = username.getTextInput();
            login.onPressed();
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
    }

    @Override
    public void changeTextField(KeyEvent ke) {
        if(username.isEnabled()){            
            username.updateLabel(ke);
        }
        if(password.isEnabled()){            
            password.updateLabel(ke);
        }
    }

    @Override
    public void tick() {
        
    }

    @Override
    public void render(GL2 gl) {
        Display.drawImageStatic(gl, null, backgroundTexture, 0, 0, Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT, 1);
        signup.render(gl);
        login.render(gl);
        username.render(gl);
        password.render(gl);
        if(failLogin){
            failMessage.render(gl);
        }
    }
    
}
