package com.BitJunkies.RTS.src;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;
public class Assets {
    //class that contains game resources

    //Images and textures
    public static Texture backgroundTexture;
    public static BufferedImage background;
    
    public static Texture rockTexture;
    public static BufferedImage rock;
    
    public static Texture rockTextureD1;
    public static BufferedImage rockD1;
    
    public static Texture rockTextureD2;
    public static BufferedImage rockD2;
    
    public static Texture casttleTexture;
    public static BufferedImage casttle;
    
    public static Texture workerTexture;
    public static BufferedImage worker;
    
    public static Texture warriorTexture;
    public static BufferedImage warrior;
    
    //Animations
    public static Texture workerWalkingTexture;
    public static BufferedImage workerWalking;
    public static Texture workerMiningTexture;
    public static BufferedImage workerMining;

    //Sound clips
    public static SoundClip explosionSound;
    public static SoundClip otherExplosionSound;

    // init creates obejects so that they are avbailable to our game
    public static void init(){
        //every image consists of a buffered image itself and a texture used for openGl
        background = ImageLoader.loadImage("/Images/background.jpg");
        backgroundTexture = AWTTextureIO.newTexture(Display.getProfile(), background, true);
        
        rock = ImageLoader.loadImage("/Images/Diamond.png");
        rockTexture = AWTTextureIO.newTexture(Display.getProfile(), rock, true);

        rockD1 = ImageLoader.loadImage("/Images/RockDamage1.png");
        rockTextureD1 = AWTTextureIO.newTexture(Display.getProfile(), rockD1, true);
        
        rockD2 = ImageLoader.loadImage("/Images/RockDamage2.png");
        rockTextureD2 = AWTTextureIO.newTexture(Display.getProfile(), rockD2, true);
        
        casttle = ImageLoader.loadImage("/Images/castle.png");
        casttleTexture = AWTTextureIO.newTexture(Display.getProfile(), casttle, true);
        
        worker = ImageLoader.loadImage("/Images/Worker.png");
        workerTexture = AWTTextureIO.newTexture(Display.getProfile(), worker, true);
        
        warrior = ImageLoader.loadImage("/Images/Warrior.png");
        warriorTexture = AWTTextureIO.newTexture(Display.getProfile(), warrior, true);
        
        //textures for animations
        workerWalking = ImageLoader.loadImage("/Images/workerWalking.png");
        workerWalkingTexture = AWTTextureIO.newTexture(Display.getProfile(), workerWalking, true);
        
        workerMining = ImageLoader.loadImage("/Images/workerMining.png");
        workerMiningTexture = AWTTextureIO.newTexture(Display.getProfile(), workerMining, true);
        
        //Sounds
        //otherExplosionSound = new SoundClip("/Sounds/explosion2.wav");
        //otherExplosionSound.setLooping(false);
        //otherExplosionSound.prePlayLoad();
    }
}