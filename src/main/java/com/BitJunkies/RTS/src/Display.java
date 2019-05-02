package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.texture.Texture;
import mikera.vectorz.Vector2;


//Display classs that controls the display in the window
public class Display implements GLEventListener {
    
   //open GL variables
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
       //setting up openGl
       GL2 gl = drawable.getGL().getGL2();
       
       gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
       
       gl.glEnable(GL2.GL_TEXTURE_2D);
       gl.glEnable(GL_BLEND);
       gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
       
       Game.init();
   }
	
    //method to do an openGl reshape
   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
       GL2 gl = drawable.getGL().getGL2();
       
       gl.glMatrixMode(GL2.GL_PROJECTION);
       gl.glLoadIdentity();
       
       gl.glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, 0, 1);
       gl.glMatrixMode(GL2.GL_MODELVIEW);
   }

   // method to init the openGl window
   public static GLWindow init(){
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
      window.addMouseListener(new MouseInput());
      
      //setting up close button
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
   
   //method to draw a basic image
   public static void drawImage(GL2 gl, Camera cam, Texture texture, double x, double y, double width, double height, float transp){
        Vector2 pos = cam.projectPosition(Vector2.of(x, y));
        Vector2 dim = cam.projectDimension(Vector2.of(width, height));

        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());

        gl.glColor4f(1, 1, 1, transp);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(pos.x, pos.y);

        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(pos.x, pos.y + height);

        gl.glTexCoord2f(1, 1);        
        gl.glVertex2d(pos.x + width, pos.y + height);

        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(pos.x + width, pos.y);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
   }
   
   //method to draw an image centerd at it's position
   public static void drawImageCentered(GL2 gl, Camera cam, Texture texture, double x, double y, double width, double height, float transp){
        Vector2 pos = cam.projectPosition(Vector2.of(x, y));
        Vector2 dim = cam.projectDimension(Vector2.of(width, height));

        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());

        gl.glColor4f(1, 1, 1, transp);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(pos.x - dim.x / 2, pos.y - dim.y / 2);

        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(pos.x - dim.x / 2, pos.y + dim.y / 2);

        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(pos.x + dim.x / 2, pos.y + dim.y / 2);

        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(pos.x + dim.x / 2, pos.y - dim.y / 2);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
   }
   //method to draw an image not affected by the camera
   public static void drawImageStatic(GL2 gl, Camera cam, Texture texture, double x, double y, double width, double height, float transp){
        Vector2 pos = Vector2.of(x,y);
        Vector2 dim = Vector2.of(width, height);
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
        
        gl.glColor4f(1, 1, 1, transp);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(pos.x, pos.y);
        
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(pos.x, pos.y + dim.y);
        
        gl.glTexCoord2f(1, 1);        
        gl.glVertex2d(pos.x + dim.x, pos.y + dim.y);
        
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(pos.x + dim.x, pos.y);
        gl.glEnd();
        gl.glFlush();
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
   }
   

   //method to draw a rectangle not affected by the camera
   public static void drawRectangleStatic(GL2 gl, Camera cam, double x, double y, double width, double height, int red, int green, int blue, float transp) {
        Vector2 pos = Vector2.of(x,y);
        Vector2 dim = Vector2.of(width, height);
        //System.out.println(pos.x);
        
        gl.glColor4f(red, green, blue, transp);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(pos.x, pos.y);
        
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(pos.x, pos.y + dim.y);
        
        gl.glTexCoord2f(1, 1);        
        gl.glVertex2d(pos.x + dim.x, pos.y + dim.y);
        
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(pos.x + dim.x, pos.y);
        gl.glEnd();
        gl.glFlush();
        
   }
}