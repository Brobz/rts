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
    
    public static Texture barrackTexture;
    public static BufferedImage barrack;
    
    public static Texture circleTexture;
    public static BufferedImage circle;
    
    public static Texture circleSmallTexture;
    public static BufferedImage circleSmall;
    
    
    public static Texture menuSingleTexture;
    public static BufferedImage menuSingle;
    
    public static Texture mapTexture;
    public static BufferedImage map;
    
    public static Texture darkMapTexture;
    public static BufferedImage darkMap;
    
    //Animations
    public static Texture workerWalkingTexture;
    public static BufferedImage workerWalking;
    public static Texture workerMiningTexture;
    public static BufferedImage workerMining;
    
    public static Texture warriorWalkingTexture;
    public static BufferedImage warriorWalking;
    public static Texture warriorAttackingTexture;
    public static BufferedImage warriorAttacking;

    //Sound clips
    public static SoundClip explosionSound;
    public static SoundClip otherExplosionSound;

    // init creates obejects so that they are avbailable to our game
    public static void init(){
        //every image consists of a buffered image itself and a texture used for openGl
        background = ImageLoader.loadImage("/Images/background.jpg");
        backgroundTexture = AWTTextureIO.newTexture(Display.getProfile(), background, true);
        
        rock = ImageLoader.loadImage("/Images/Diamond2.png");
        rockTexture = AWTTextureIO.newTexture(Display.getProfile(), rock, true);

        rockD1 = ImageLoader.loadImage("/Images/Stone.png");
        rockTextureD1 = AWTTextureIO.newTexture(Display.getProfile(), rockD1, true);
        
        rockD2 = ImageLoader.loadImage("/Images/Stone.png");
        rockTextureD2 = AWTTextureIO.newTexture(Display.getProfile(), rockD2, true);
        
        casttle = ImageLoader.loadImage("/Images/castle.png");
        casttleTexture = AWTTextureIO.newTexture(Display.getProfile(), casttle, true);
        
        worker = ImageLoader.loadImage("/Images/Worker.png");
        workerTexture = AWTTextureIO.newTexture(Display.getProfile(), worker, true);
        
        menuSingle = ImageLoader.loadImage("/Images/menuImage.png");
        menuSingleTexture = AWTTextureIO.newTexture(Display.getProfile(), menuSingle, true);
        
        map = ImageLoader.loadImage("/Images/MapC.png");
        mapTexture = AWTTextureIO.newTexture(Display.getProfile(), map, true);
        
        darkMap = ImageLoader.loadImage("/Images/DarkMapC.png");
        darkMapTexture = AWTTextureIO.newTexture(Display.getProfile(), darkMap, true);
        
        warrior = ImageLoader.loadImage("/Images/Warrior.png");
        warriorTexture = AWTTextureIO.newTexture(Display.getProfile(), warrior, true);
        
        
        circle = ImageLoader.loadImage("/Images/perimeterCircle.png");
        circleTexture = AWTTextureIO.newTexture(Display.getProfile(), circle, true);
        
        circleSmall = ImageLoader.loadImage("/Images/perimeterCircleSmall.png");
        circleSmallTexture = AWTTextureIO.newTexture(Display.getProfile(), circleSmall, true);
        
        //textures for animations
        workerWalking = ImageLoader.loadImage("/Images/workerWalking.png");
        workerWalkingTexture = AWTTextureIO.newTexture(Display.getProfile(), workerWalking, true);
        workerMining = ImageLoader.loadImage("/Images/workerMining.png");
        workerMiningTexture = AWTTextureIO.newTexture(Display.getProfile(), workerMining, true);
        
        warriorWalking = ImageLoader.loadImage("/Images/warriorWalking.png");
        warriorWalkingTexture = AWTTextureIO.newTexture(Display.getProfile(), warriorWalking, true);
        warriorAttacking = ImageLoader.loadImage("/Images/warriorAttacking.png");
        warriorAttackingTexture = AWTTextureIO.newTexture(Display.getProfile(), warriorAttacking, true);
        barrack = ImageLoader.loadImage("/Images/barrack.png");
        barrackTexture = AWTTextureIO.newTexture(Display.getProfile(), barrack, true);
        
        //Sounds
        //otherExplosionSound = new SoundClip("/Sounds/explosion2.wav");
        //otherExplosionSound.setLooping(false);
        //otherExplosionSound.prePlayLoad();
    }
}