/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import static com.BitJunkies.RTS.src.GameSignup.background;
import static com.BitJunkies.RTS.src.GameSignup.backgroundTexture;
import com.BitJunkies.RTS.ui.Button;
import com.BitJunkies.RTS.ui.Label;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;

/**
 *
 * @author ulise
 */
public class GameStats extends GameState{
    Button back;
    Label promAccPMin,promEdiPPart,promUniPPart, winRate, floatingRes;
    protected static BufferedImage background;
    protected static Texture backgroundTexture;
    private static GameStats instance;
    private static final int X = 940;
    
    /**
     * Singleton method for object
     * @return GameStats
     */
    public static GameStats getInstance(){
        if(instance == null){
            instance = new GameStats();
        }
        return instance;
    }

    /**
     * Constructor with the attributes of the ui
     */
    public GameStats(){
        
        /*
        0 - actions
        1 - buildings/game
        2 - units/game
        3 - winRate
        4 - resource-floating
        */
        back = new Button(50,50,80,80,"back.png",this,null);
        promAccPMin = new Label(X,250,100,50,"0.0");
        promEdiPPart = new Label(X,335,100,50,"0.0");
        promUniPPart = new Label(X,405,100,50,"0.0");
        winRate = new Label(X,490,100,50,"0.0");
        floatingRes = new Label(X,575,100,50,"0.0");
        background = ImageLoader.loadImage("/Images/Stats.jpg");
        backgroundTexture = AWTTextureIO.newTexture(Display.getProfile(), background, true);
    }
    
    /**
     * ticking for the game
     */
    @Override
    public void tick() {
        if (Game.queriesDone) {
            this.setValuesToQueries();
            Game.queriesDone = false;
        }
        
    }

    /**
     * Abstract method to draw the ui of the screen
     * @param gl GL2
     */
    @Override
    public void render(GL2 gl) {
        Display.drawImageStatic(gl, null, backgroundTexture, 0, 0, Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT, 1);
        back.render(gl);
        promAccPMin.render(gl);
        promEdiPPart.render(gl);
        promUniPPart.render(gl);
        winRate.render(gl);
        floatingRes.render(gl);
    }

    /**
     * Used to change text of text field(not used)
     * @param ke 
     */
    @Override
    public void checkPress() {
        if(MouseInput.mouseStaticHitBox.intersects(back.getHitBox())){
            back.setNextState(MainMenu.getInstance());
            back.onPressed();
        }
    }

    /**
     * Used to change text of text field(not used)
     * @param ke 
     */
    @Override
    public void changeTextField(KeyEvent ke) {
    }
    
    public void setValuesToQueries() {
        promAccPMin.setText(Float.toString(Game.resultsQueries.get(0)));
        promEdiPPart.setText(Float.toString(Game.resultsQueries.get(1)));
        promUniPPart.setText(Float.toString(Game.resultsQueries.get(2)));
        winRate.setText(Float.toString(Game.resultsQueries.get(3)));
        floatingRes.setText(Float.toString(Game.resultsQueries.get(4)));
    }
    
}
