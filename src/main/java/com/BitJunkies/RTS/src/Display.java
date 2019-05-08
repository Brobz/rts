package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.KeyboardInput;
import com.BitJunkies.RTS.input.MouseInput;
import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.event.KeyListener;
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

/**
 * Display class that controls the display in the window of the game
 * @author ulise
 */
public class Display implements GLEventListener {
    
   //open GL variables
   public static final int WINDOW_WIDTH = 1200;
   public static final int WINDOW_HEIGHT = 700;
   private static GLProfile profile;
   
   /**
    * Method to draw objects in the screen
    * @param drawable 
    */
   @Override
   public void display(GLAutoDrawable drawable) {
       Game.render(drawable);
   }
	
   /**
    * Remove the objects from 
    * @param drawable 
    */
   @Override
   public void dispose(GLAutoDrawable drawable) {
   }

   /**
    * Initialize the screen for being able to displaying
    * @param drawable GLAutoDrawable object for drawing in opengl
    */
   @Override
   public void init(GLAutoDrawable drawable) {
       //setting up openGl
       GL2 gl = drawable.getGL().getGL2();
       
       gl.glClearColor(0f, 0.0f, 0.0f, 1f);
       
       gl.glEnable(GL2.GL_TEXTURE_2D);
       gl.glEnable(GL_BLEND);
       gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
       
       Game.init();
   }
	
   /**
    * method to do an openGl reshape
    * @param drawable
    * @param x position
    * @param y position
    * @param width
    * @param height 
    */
   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
       GL2 gl = drawable.getGL().getGL2();
       
       gl.glMatrixMode(GL2.GL_PROJECTION);
       gl.glLoadIdentity();
       
       gl.glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, 0, 1);
       gl.glMatrixMode(GL2.GL_MODELVIEW);
   }

   /**
    * method to initialize the openGl window
    * @return 
    */
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
      window.addKeyListener(new KeyboardInput());

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

   /**
    * Getter for the profile
    * @return 
    */
   public static GLProfile getProfile(){
       return profile;
   }
   /**
    * method to draw a basic image
    * @param gl GL2 for opengl
    * @param cam Camera of the game
    * @param texture Texture to draw
    * @param x int
    * @param y int
    * @param width int
    * @param height int 
    * @param transp float tranparency
    */
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
   
   /**
    * method to draw an image centered at it's position
    * @param gl
    * @param cam
    * @param texture
    * @param x
    * @param y
    * @param width
    * @param height
    * @param transp 
    */
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
   
   /**
    * method to draw an image centerd at it's position
    * @param gl
    * @param cam
    * @param texture
    * @param x
    * @param y
    * @param width
    * @param height
    * @param transp
    * @param contFrame
    * @param direction 
    */
   public static void drawAnimation(GL2 gl, Camera cam, Texture texture, double x, double y, double width, double height, float transp, int contFrame, int direction){
        Vector2 pos = cam.projectPosition(Vector2.of(x, y));
        Vector2 dim = cam.projectDimension(Vector2.of(width, height));

        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());

        gl.glColor4f(1, 1, 1, transp);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(contFrame * 0.25f, direction * 0.25f);//0, 0
        gl.glVertex2d(pos.x - dim.x / 2, pos.y - dim.y / 2);

        gl.glTexCoord2f(contFrame * 0.25f, direction * 0.25f + 0.25f); //0, 1
        gl.glVertex2d(pos.x - dim.x / 2, pos.y + dim.y / 2);

        gl.glTexCoord2f(contFrame * 0.25f + 0.25f, direction * 0.25f + 0.25f); // 1, 1
        gl.glVertex2d(pos.x + dim.x / 2, pos.y + dim.y / 2);

        gl.glTexCoord2f(contFrame * 0.25f + 0.25f, direction * 0.25f); // 1, 0
        gl.glVertex2d(pos.x + dim.x / 2, pos.y - dim.y / 2);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
   }
   
   /**
    * Method to draw an Animation static in the camera
    * @param gl
    * @param cam
    * @param texture
    * @param x
    * @param y
    * @param width
    * @param height
    * @param transp
    * @param contFrame
    * @param direction 
    */
   public static void drawAnimationStatic(GL2 gl, Camera cam, Texture texture, double x, double y, double width, double height, float transp, int contFrame, int direction){
        Vector2 pos = Vector2.of(x, y);
        Vector2 dim = Vector2.of(width, height);

        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());

        gl.glColor4f(1, 1, 1, transp);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(contFrame * 0.25f, direction * 0.25f);//0, 0
        gl.glVertex2d(pos.x, pos.y);

        gl.glTexCoord2f(contFrame * 0.25f, direction * 0.25f + 0.25f); //0, 1
        gl.glVertex2d(pos.x, pos.y + dim.y);

        gl.glTexCoord2f(contFrame * 0.25f + 0.25f, direction * 0.25f + 0.25f); // 1, 1
        gl.glVertex2d(pos.x + dim.x, pos.y + dim.y);

        gl.glTexCoord2f(contFrame * 0.25f + 0.25f, direction * 0.25f); // 1, 0
        gl.glVertex2d(pos.x + dim.x, pos.y);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
   }
   
   //method to draw a frame form animation not affected by the camera
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
   
   /**
    * Method to draw a rectangle not affected by the camera
    * @param gl
    * @param cam
    * @param x
    * @param y
    * @param width
    * @param height
    * @param red
    * @param green
    * @param blue
    * @param transp 
    */
   public static void drawRectangleStatic(GL2 gl, Camera cam, double x, double y, double width, double height, float red, float green, float blue, float transp) {
        Vector2 pos = Vector2.of(x,y);
        Vector2 dim = Vector2.of(width, height);
        
        gl.glColor4f(red, green, blue, transp);
        gl.glBegin(GL2.GL_QUADS);
        //gl.glTexCoord2f(0, 0);
        gl.glVertex2d(pos.x, pos.y);
        
        //gl.glTexCoord2f(0, 1);
        gl.glVertex2d(pos.x, pos.y + dim.y);
        
        //gl.glTexCoord2f(1, 1);        
        gl.glVertex2d(pos.x + dim.x, pos.y + dim.y);
        
        //gl.glTexCoord2f(1, 0);
        gl.glVertex2d(pos.x + dim.x, pos.y);
        gl.glEnd();
        gl.glFlush();    
   }
   
   /**
    * Method that draws a rectangle 
    * @param gl GL2 for opengl
    * @param cam Camera for game
    * @param x int
    * @param y int
    * @param width int
    * @param height int 
    * @param red Color
    * @param green Color
    * @param blue Color
    * @param transp  Float
    */
   public static void drawRectangleEmtpyStatic(GL2 gl, Camera cam, double x, double y, double width, double height, float red, float green, float blue, float transp){
        gl.glColor4f(red, green, blue, transp);
        gl.glBegin(GL2.GL_QUADS);
        Vector2 pos = Vector2.of(x,y);
        Vector2 dim = Vector2.of(width, height);
        gl.glVertex2d(pos.x, pos.y);
        gl.glVertex2d(pos.x, pos.y + dim.y);       
        gl.glVertex2d(pos.x + dim.x, pos.y + dim.y);
        gl.glVertex2d(pos.x + dim.x, pos.y);
        gl.glEnd();
        gl.glFlush(); 
        gl.glColor4f(1, 1, 1, 1);
   }
    /**
     * method to draw a basic image
     * @param gl
     * @param cam
     * @param x
     * @param y
     * @param width
     * @param height
     * @param red
     * @param green
     * @param blue
     * @param transp 
     */
    public static void drawRectangle(GL2 gl, Camera cam, double x, double y, double width, double height, float red, float green, float blue, float transp){
         Vector2 pos = cam.projectPosition(Vector2.of(x, y));
         Vector2 dim = cam.projectDimension(Vector2.of(width, height));

         gl.glColor4f(red, green, blue, transp);
         gl.glBegin(GL2.GL_QUADS);
         gl.glVertex2d(pos.x, pos.y);

         gl.glVertex2d(pos.x, pos.y + height);
     
         gl.glVertex2d(pos.x + width, pos.y + height);

         gl.glVertex2d(pos.x + width, pos.y);
         gl.glEnd();
         gl.glFlush();
    }
}