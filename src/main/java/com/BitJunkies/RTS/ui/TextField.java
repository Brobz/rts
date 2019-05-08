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
 *
 * @author ulise
 */
public class TextField extends Item{
    Label input;
    String textInput = "";
    String showText = "";
    boolean enabled = false;
    boolean hide;
    
    public TextField(int x, int y, int width, int height) {
        super(x, y, width, height);
        input = new Label(x,y,width,height,showText);
        hide = false;
    }
    
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
    
    public void render(GL2 g1) {
        Display.drawRectangleStatic(g1, null, position.x, position.y, dimension.x, dimension.y, 256, 256, 256, 1);
        input.render(g1);
    }
    
    public String getTextInput(){
        return textInput;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if(enabled)
            input.setText(showText+ '|');
        else
            input.setText(showText);
        
        this.enabled = enabled;
    }
    
    public void setHide(boolean hide){
        this.hide = hide;
    }
    
}
