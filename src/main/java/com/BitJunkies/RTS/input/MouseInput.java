/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.input;

import com.BitJunkies.RTS.src.Display;
import com.BitJunkies.RTS.src.Game;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import java.awt.Rectangle;
import mikera.vectorz.Vector2;

/**
 * Class to implement mouse functionalities
 * @author brobz
 */
public class MouseInput implements MouseListener{
    
    public static Rectangle mouseHitBox;
    public static Rectangle mouseStaticHitBox;

    /**
     * Checks if a mouse button is being clicked (pressed and released
     * @param me mouse event 
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        Game.mouseClicked(me.getButton());
    }

    /**
     * unused method
     * @param me
     */
    @Override
    public void mouseEntered(MouseEvent me) {
    }

    /**
     * unused method
     * @param me
     */
    @Override
    public void mouseExited(MouseEvent me) {
    }

    /**
     * Checks if a mouse button is being pressed
     * @param me mouse event 
     */
    @Override
    public void mousePressed(MouseEvent me) {
        Game.mousePressed(me.getButton());
    }

    /**
     * Checks if a mouse button is being released
     * @param me mouse event
     */
    @Override
    public void mouseReleased(MouseEvent me) {
        Game.mouseReleased(me.getButton());
    }

    /**
     * Checks if the mouse cursor is being moved within the screen
     * @param me mouse event
     */
    @Override
    public void mouseMoved(MouseEvent me) {
        if(Game.getCamera() != null){
            if(me.getX() >= Display.WINDOW_WIDTH - Game.getCamera().moveRange){
                Game.getCamera().velocity.x = Game.getCamera().moveSpeed;
            }else if(me.getX() <= 0 + Game.getCamera().moveRange){
                Game.getCamera().velocity.x = -Game.getCamera().moveSpeed;
            }else{
                Game.getCamera().velocity.x = 0;
            }
            
            if(me.getY() >= Display.WINDOW_HEIGHT - Game.getCamera().moveRange){
                Game.getCamera().velocity.y = Game.getCamera().moveSpeed;
            }else if(me.getY() <= 0 + Game.getCamera().moveRange){
                Game.getCamera().velocity.y = -Game.getCamera().moveSpeed;
            }else{
                Game.getCamera().velocity.y = 0;
            }
            mouseHitBox = new Rectangle(me.getX() + (int)Game.getCamera().position.x, me.getY() + (int)Game.getCamera().position.y, 2, 2);
            mouseStaticHitBox = new Rectangle(me.getX(), me.getY(), 2, 2);
        }
    }

    /**
     * Checks if the mouse is being dragged (pressed and moved)
     * @param me mouse event
     */
    @Override
    public void mouseDragged(MouseEvent me) {
        if(Game.getCamera() != null){
            if(me.getX() >= Display.WINDOW_WIDTH){
                Game.getCamera().velocity.x = 1;
            }else if(me.getX() <= 0){
                Game.getCamera().velocity.x = -1;
            }else{
                Game.getCamera().velocity.x = 0;
            }
            
            if(me.getY() >= Display.WINDOW_HEIGHT){
                Game.getCamera().velocity.y = 1;
            }else if(me.getY() <= 0){
                Game.getCamera().velocity.y = -1;
            }else{
                Game.getCamera().velocity.y = 0;
            }
            
            mouseHitBox = new Rectangle(me.getX() + (int)Game.getCamera().position.x, me.getY() + (int)Game.getCamera().position.y, 2, 2);
            mouseStaticHitBox = new Rectangle(me.getX(), me.getY(), 2, 2);
            Game.mouseDragged();
        }
        
        
    }

    /**
     * unused method
     * @param me
     */
    @Override
    public void mouseWheelMoved(MouseEvent me) {
    }
    
}
