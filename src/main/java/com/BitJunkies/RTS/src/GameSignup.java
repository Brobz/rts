/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import DatabaseQueries.InsertToDB;
import DatabaseQueries.SelectFromDB;
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
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Screen to create an account to access the game
 * @author ulise
 */
public class GameSignup extends GameState{
    TextField username,password,confirm;
    Button signup,back;
    Label missmatch1,missmatch2,empty1,empty2,empty3,empty4,repeatedUser1,repeatedUser2;
    protected static GameSignup instance;
    protected static BufferedImage background;
    protected static Texture backgroundTexture;
    protected static boolean passMismatch = false,emptyUsername = false, emptyPassword = false, userExists = false;
    
    /**
     * Singleton method for object
     * @return GameSignup instance
     */
    public static GameSignup getInstance(){
        if(instance == null){
            instance = new GameSignup();
        }
        return instance;
    }
    
    /**
     * Constructor that initializes all the UI elements
     */
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
        empty3 = new Label(990, 320, 100, 40, "Cant be");
        empty4 = new Label(990, 360, 100, 40, "blank!");
        repeatedUser1 = new Label(990, 230, 100, 40, "User already");
        repeatedUser2 = new Label(990, 270, 100, 40, "exists!");
        password.setHide(true);
        confirm.setHide(true);
        background = ImageLoader.loadImage("/Images/Signup.jpg");
        backgroundTexture = AWTTextureIO.newTexture(Display.getProfile(), background, true);
    }
    
    /**
     * ticking for the game
     */
    @Override
    public void tick() {
        
    }
    
    /**
     * Method to draw the ui of the screen
     * @param gl GL2
     */
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
        else if(emptyUsername){
            empty1.render(gl);
            empty2.render(gl);
        }
        else if(emptyPassword){
            empty3.render(gl);
            empty4.render(gl);
        }
        else if(userExists){
            repeatedUser1.render(gl);
            repeatedUser2.render(gl);
        }
    }

    /**
     * Method to check if a button of the screen or a text field is pressed
     */
    @Override
    public void checkPress() {
        username.setEnabled(false);
        password.setEnabled(false);
        confirm.setEnabled(false);
        passMismatch = false;
        emptyPassword = false;
        emptyUsername = false;
        userExists = false;
        if(MouseInput.mouseStaticHitBox.intersects(back.getHitBox())){
            back.setNextState(GameLogin.getInstance());
            back.onPressed();
        }
        // Signup button pressed
        else if(MouseInput.mouseStaticHitBox.intersects(signup.getHitBox())){
            //If the username is null
            if(username.getTextInput().isEmpty()){
                emptyUsername = true;
                return;
            }
            //If the password and confirmations match
            if(password.getTextInput().isEmpty()){
                emptyPassword = true;
                return;
            }
            if(password.getTextInput().equals(confirm.getTextInput())){
                if(!SelectFromDB.existsUsername(username.getTextInput())){
                    try {
                        InsertToDB.insertPlayer(username.getTextInput(), password.getTextInput());
                    } catch (SQLException ex) {
                        Logger.getLogger(GameSignup.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(GameSignup.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Game.loggedInUsername = username.getTextInput();
                    signup.onPressed();
                    Game.resultsQueries = Game.executeSelectQueries();
                    
                }
                userExists = true;
            }
            else{
                passMismatch = true;
                return;
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
    /**
     * Used to change text of text field(not used)
     * @param ke 
     */
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
