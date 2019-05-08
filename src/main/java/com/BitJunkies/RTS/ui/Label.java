/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.ui;

import com.BitJunkies.RTS.src.Display;
import com.jogamp.opengl.GL2;
import java.awt.Font;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Color;

/**
 * Label class to create labels in the menus
 * @author ulise
 */
public class Label extends Item{
    protected String text;
    protected Font awtFont;
    protected Color color;
    
    /**
     * Constructor for Label
     * @param x int for position in the x axis
     * @param y int for position in the y axis
     * @param width int for the width of the label
     * @param height int for the height of the label
     * @param text String for the text of the label
     */
    public Label(int x, int y, int width, int height,String text) {
        super(x, y, width, height);
        this.text = text;
        awtFont = new Font("Monotype Corsiva",Font.BOLD,30);
        color = new Color(145,77,39);
    }
    
    /**
     * Renders the label
     * @param g1 GL2 for rendering
     */
    @Override
    public void render(GL2 g1) {
        TextRenderer textRender = new TextRenderer(awtFont);
        textRender.beginRendering(Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT);
        textRender.setColor(color);
        textRender.setSmoothing(true);
  
        textRender.draw(text,(int)position.x,Display.WINDOW_HEIGHT- (int) position.y - 30);
        textRender.endRendering();
    }

    /**
     * Sets the text of the label to whatever the parameter is
     * @param text String
     */
    public void setText(String text) {
        this.text = text;
    }
    
}
