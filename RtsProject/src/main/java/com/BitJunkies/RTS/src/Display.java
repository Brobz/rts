package com.BitJunkies.RTS.src;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;

public class Display implements GLEventListener {
    
   public static final int WINDOW_WIDTH = 800;
   public static final int WINDOW_HEIGHT = 600;
   private static GLProfile profile;
    
   @Override
   public void display(GLAutoDrawable drawable) {
       Game.render(drawable);
   }
	
   @Override
   public void dispose(GLAutoDrawable drawable) {
   }
	
   @Override
   public void init(GLAutoDrawable drawable) {
       GL2 gl = drawable.getGL().getGL2();
       
       gl.glClearColor(0.5f, 0.5f, 0, 1);
   }
	
   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
       GL2 gl = drawable.getGL().getGL2();
       
       gl.glMatrixMode(GL2.GL_PROJECTION);
       gl.glLoadIdentity();
       
       gl.glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, -1, 1);
       gl.glMatrixMode(GL2.GL_MODELVIEW);
   }
   
   public static GLWindow initDisplay(){
       // Getting the capabilities object of GL2 profile
      GLProfile.initSingleton();
      profile = GLProfile.get(GLProfile.GL2);
      GLCapabilities caps = new GLCapabilities(profile);
      
      GLWindow window = GLWindow.create(caps);
      
      window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
      window.setTitle("RTS");
      window.setResizable(false);
      //window.setFullscreen(true);
      window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
      window.addGLEventListener(new Display());
      
      window.addWindowListener(new WindowAdapter() { 
          @Override 
          public void windowDestroyNotify(WindowEvent windowEvent){
            Game.stop();
          }
      });

      window.setVisible(true);
      return window;
   }
   
   public static GLProfile getProfile(){
       return profile;
   }
}