/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.ui;

import com.BitJunkies.RTS.src.Display;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;

/**
 * TextField class to generically create textfields in the menus
 * @author ulise
 */
public class TextField extends Item{
    Label input;
    String textInput = "";
    String showText = "";
    boolean enabled = false;
    boolean hide;
    
    /**
     * Constructor for Textfield
     * @param x int for the position in the screen in the x axis
     * @param y int for the position in the screen in the y axis
     * @param width int for the width of the text field
     * @param height int for the height of the textfield
     */
    public TextField(int x, int y, int width, int height) {
        super(x, y, width, height);
        input = new Label(x,y,width,height,showText);
        hide = false;
    }
    
    /**
     * Updates the letter held on the buffer while inserting data into the text field
     * @param ke KeyEvent for getting the value of the key pressed
     */
    public void updateLabel(KeyEvent ke){
        if(ke.getKeyCode()==KeyEvent.VK_BACK_SPACE){
            if(textInput.length() > 0){
                textInput = textInput.substring(0, textInput.length()-1);
            }
        }
        else if((ke.getKeyCode()>=46 && ke.getKeyCode() <= 57) || (ke.getKeyCode()>=65 && ke.getKeyCode() <= 90)){
            if(textInput.length() < 20){
                textInput += ke.getKeyChar();
            }
            //System.out.println(textInput);
        }
        

        if(textInput.length() >= 20){
            showText = textInput.substring(textInput.length()-20, textInput.length());
        }
        else{
            showText = textInput;
        }
        if(hide){
            showText = showText.replaceAll(".", "*");
            //System.out.println(showText);
        }
            
        input.setText(showText + '|');
    }
    
    /**
     * renders the text field
     * @param g1 GL2 for rendering
     */
    public void render(GL2 g1) {
        Display.drawRectangleStatic(g1, null, position.x, position.y, dimension.x, dimension.y, 256, 256, 256, 1);
        input.render(g1);
    }
    
    /**
     * Getter for textInput attribute
     * @return String
     */
    public String getTextInput(){
        return textInput;
    }

    /**
     * Checks if the current text field is enabled (selected)
     * @return boolean
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Sets field enabled or disabled according to the entered boolean
     * @param enabled boolean
     */
    public void setEnabled(boolean enabled) {
        if(enabled)
            input.setText(showText+ '|');
        else
            input.setText(showText);
        
        this.enabled = enabled;
    }
    
    /**
     * Hides current text in text field so that it shows the * character insted
     * @param hide 
     */
    public void setHide(boolean hide){
        this.hide = hide;
    }
    
}
