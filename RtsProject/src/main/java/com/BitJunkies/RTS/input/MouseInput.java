/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.input;

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
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        mouseHitBox = new Rectangle(me.getX(), me.getY(), 2, 2);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
    }

    @Override
    public void mouseWheelMoved(MouseEvent me) {
    }
    
}
