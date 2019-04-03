/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.src.server.HostServer;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import java.awt.Frame;
import javax.swing.JFrame;

/**
 *
 * @author rober
 */
public class Game {
    public static void main(String args[]){
        //getting the capabilities object of GL2 profile
      final GLProfile profile = GLProfile.get(GLProfile.GL2);
      GLCapabilities capabilities = new GLCapabilities(profile);
        
      // The canvas
      final GLCanvas glcanvas = new GLCanvas(capabilities);
      Display d = new Display();
      glcanvas.addGLEventListener(d);        
      glcanvas.setSize(400, 400);
        
      //creating frame
      final JFrame frame = new JFrame (" Basic Frame");
        
      //adding canvas to frame
      frame.add(glcanvas);
      frame.setSize( 640, 480 );
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      
      HostServer srv = new HostServer();
    }
    
}
