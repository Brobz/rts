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
 *
 * @author brobz
 */
public class MouseInput implements MouseListener{
    
    public static Rectangle mouseHitBox;

    @Override
    public void mouseClicked(MouseEvent me) {
        Game.mouseClicked(me.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        Game.mousePressed(me.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        Game.mouseReleased(me.getButton());
    }

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
        }
    }

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
            Game.mouseDragged();
        }
        
        
    }

    @Override
    public void mouseWheelMoved(MouseEvent me) {
    }
    
}
