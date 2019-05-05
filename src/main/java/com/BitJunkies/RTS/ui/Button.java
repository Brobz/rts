/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.ui;
import com.BitJunkies.RTS.src.Display;
import com.BitJunkies.RTS.src.ImageLoader;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import mikera.vectorz.*;

/**
 *
 * @author ulises
 */

public class Button extends Item{
    protected float opacity;
    protected Texture texture;
    
    protected BufferedImage curr;
    
    public Button(int x, int y, int width, int height,String fileNameWithExtension){
        super(x,y,width,height);
        updateHitBox();
        opacity = 1;
        curr = ImageLoader.loadImage("/Images/"+ fileNameWithExtension);
        texture = AWTTextureIO.newTexture(Display.getProfile(), curr, true);
    }
    
    public Button(Vector2 dimension, Vector2 position){
        super(dimension,position);
        this.opacity = (float) 1;
    }
    
    @Override
    public void render(GL2 gl){
        if(texture == null) return;
        Display.drawImageStatic(gl, null, texture, position.x, position.y, dimension.x, dimension.y, (float)opacity);
    }
    
    
    
    public Rectangle getHitBox(){
        return hitBox;
    }
    
    public void setOpacity(float opacity){
        this.opacity = opacity;
    }
        
}