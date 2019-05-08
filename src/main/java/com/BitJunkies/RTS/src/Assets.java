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
    public static BufferedImage[] workersStanding = new  BufferedImage[4];
    public static Texture[] workersStandingTexture = new Texture[4];
    public static BufferedImage[] workersWalking = new  BufferedImage[4];
    public static Texture[] workersWalkingTexture = new Texture[4];
    public static BufferedImage[] workersMining = new  BufferedImage[4];
    public static Texture[] workersMiningTexture = new Texture[4];
    public static BufferedImage[] workersWalkingSelected = new  BufferedImage[4];
    public static Texture[] workersWalkingSelectedTexture = new Texture[4];
    public static BufferedImage[] workersMiningSelected = new  BufferedImage[4];
    public static Texture[] workersMiningSelectedTexture = new Texture[4];
    public static BufferedImage[] workersStandingSelected = new  BufferedImage[4];
    public static Texture[] workersStandingSelectedTexture = new Texture[4];
    
    public static BufferedImage[] warriorsStanding = new  BufferedImage[4];
    public static Texture[] warriorsStandingTexture = new Texture[4];
    public static BufferedImage[] warriorsWalking = new  BufferedImage[4];
    public static Texture[] warriorsWalkingTexture = new Texture[4];
    public static BufferedImage[] warriorsAttacking = new  BufferedImage[4];
    public static Texture[] warriorsAttackingTexture = new Texture[4];
    public static BufferedImage[] warriorsWalkingSelected = new  BufferedImage[4];
    public static Texture[] warriorsWalkingSelectedTexture = new Texture[4];
    public static BufferedImage[] warriorsAttackingSelected = new  BufferedImage[4];
    public static Texture[] warriorsAttackingSelectedTexture = new Texture[4];
    public static BufferedImage[] warriorsStandingSelected = new  BufferedImage[4];
    public static Texture[] warriorsStandingSelectedTexture = new Texture[4];

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
        workersStanding[0] = ImageLoader.loadImage("/Images/Worker.png");
        workersStandingTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), workersStanding[0], true);        
        
        workersStanding[1] = ImageLoader.loadImage("/Images/Green Miner.png");
        workersStandingTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), workersStanding[1], true);
        
        workersStanding[2] = ImageLoader.loadImage("/Images/Blue Miner.png");
        workersStandingTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), workersStanding[2], true);
        
        workersStanding[3] = ImageLoader.loadImage("/Images/Purple Miner.png");
        workersStandingTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), workersStanding[3], true);
        
        
        workersWalking[0] = ImageLoader.loadImage("/Images/workerWalking.png");
        workersWalkingTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), workersWalking[0], true);        
        
        workersWalking[1] = ImageLoader.loadImage("/Images/Green Miner Walking.png");
        workersWalkingTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), workersWalking[1], true);
        
        workersWalking[2] = ImageLoader.loadImage("/Images/Blue Miner Walking.png");
        workersWalkingTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), workersWalking[2], true);
        
        workersWalking[3] = ImageLoader.loadImage("/Images/Purple Miner Walking.png");
        workersWalkingTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), workersWalking[3], true);
        

        //textures for animations
        workersMining[0] = ImageLoader.loadImage("/Images/workerMining.png");
        workersMiningTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), workersMining[0], true);
        
        workersMining[1] = ImageLoader.loadImage("/Images/Green Miner Mining.png");
        workersMiningTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), workersMining[1], true);
        
        workersMining[2] = ImageLoader.loadImage("/Images/Blue Miner Mining.png");
        workersMiningTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), workersMining[2], true);
        
        workersMining[3] = ImageLoader.loadImage("/Images/Purple Miner Mining.png");
        workersMiningTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), workersMining[3], true);
        
        
        //textures for animations
        warriorsStanding[0] = ImageLoader.loadImage("/Images/Warrior.png");
        warriorsStandingTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), warriorsStanding[0], true);        
        
        warriorsStanding[1] = ImageLoader.loadImage("/Images/Green Warrior.png");
        warriorsStandingTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), warriorsStanding[1], true);
        
        warriorsStanding[2] = ImageLoader.loadImage("/Images/Blue Warrior.png");
        warriorsStandingTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), warriorsStanding[2], true);
        
        warriorsStanding[3] = ImageLoader.loadImage("/Images/Purple Warrior.png");
        warriorsStandingTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), warriorsStanding[3], true);
        
        
        warriorsWalking[0] = ImageLoader.loadImage("/Images/warriorWalking.png");
        warriorsWalkingTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), warriorsWalking[0], true);  

        warriorsWalking[1] = ImageLoader.loadImage("/Images/Green Warrior Walking.png");
        warriorsWalkingTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), warriorsWalking[1], true);

        warriorsWalking[2] = ImageLoader.loadImage("/Images/Blue Warrior Walking.png");
        warriorsWalkingTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), warriorsWalking[2], true);

        warriorsWalking[3] = ImageLoader.loadImage("/Images/Purple Warrior Walking.png");
        warriorsWalkingTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), warriorsWalking[3], true);


        //textures for animations
        warriorsAttacking[0] = ImageLoader.loadImage("/Images/warriorAttacking.png");
        warriorsAttackingTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), warriorsAttacking[0], true);

        warriorsAttacking[1] = ImageLoader.loadImage("/Images/Green Warrior Attacking.png");
        warriorsAttackingTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), warriorsAttacking[1], true);

        warriorsAttacking[2] = ImageLoader.loadImage("/Images/Blue Warrior Attacking.png");
        warriorsAttackingTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), warriorsAttacking[2], true);

        warriorsAttacking[3] = ImageLoader.loadImage("/Images/Purple Warrior Attacking.png");
        warriorsAttackingTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), warriorsAttacking[3], true);
        
        
        
        
        
        //textures for animations
        workersStandingSelected[0] = ImageLoader.loadImage("/Images/Selected Miner.png");
        workersStandingSelectedTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), workersStandingSelected[0], true);        
        
        workersStandingSelected[1] = ImageLoader.loadImage("/Images/Green Selected Miner.png");
        workersStandingSelectedTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), workersStandingSelected[1], true);
        
        workersStandingSelected[2] = ImageLoader.loadImage("/Images/Blue Selected Miner.png");
        workersStandingSelectedTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), workersStandingSelected[2], true);
        
        workersStandingSelected[3] = ImageLoader.loadImage("/Images/Purple Selected Miner.png");
        workersStandingSelectedTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), workersStandingSelected[3], true);
        
        //textures for animations
        workersWalkingSelected[0] = ImageLoader.loadImage("/Images/Selected Miner Walking.png");
        workersWalkingSelectedTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), workersWalkingSelected[0], true);        
        
        workersWalkingSelected[1] = ImageLoader.loadImage("/Images/Green Selected Miner Walking.png");
        workersWalkingSelectedTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), workersWalkingSelected[1], true);
        
        workersWalkingSelected[2] = ImageLoader.loadImage("/Images/Blue Selected Miner Walking.png");
        workersWalkingSelectedTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), workersWalkingSelected[2], true);
        
        workersWalkingSelected[3] = ImageLoader.loadImage("/Images/Purple Selected Miner Walking.png");
        workersWalkingSelectedTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), workersWalkingSelected[3], true);
        

        //textures for animations
        workersMiningSelected[0] = ImageLoader.loadImage("/Images/Selected Miner Mining.png");
        workersMiningSelectedTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), workersMiningSelected[0], true);
        
        workersMiningSelected[1] = ImageLoader.loadImage("/Images/Green Selected Miner Mining.png");
        workersMiningSelectedTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), workersMiningSelected[1], true);
        
        workersMiningSelected[2] = ImageLoader.loadImage("/Images/Blue Selected Miner Mining.png");
        workersMiningSelectedTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), workersMiningSelected[2], true);
        
        workersMiningSelected[3] = ImageLoader.loadImage("/Images/Purple Selected Miner Mining.png");
        workersMiningSelectedTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), workersMiningSelected[3], true);
        
        
        
        //textures for animations
        warriorsStandingSelected[0] = ImageLoader.loadImage("/Images/Selected Warrior.png");
        warriorsStandingSelectedTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), warriorsStandingSelected[0], true);        
        
        warriorsStandingSelected[1] = ImageLoader.loadImage("/Images/Green Selected Warrior.png");
        warriorsStandingSelectedTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), warriorsStandingSelected[1], true);
        
        warriorsStandingSelected[2] = ImageLoader.loadImage("/Images/Blue Selected Warrior.png");
        warriorsStandingSelectedTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), warriorsStandingSelected[2], true);
        
        warriorsStandingSelected[3] = ImageLoader.loadImage("/Images/Purple Selected Warrior.png");
        warriorsStandingSelectedTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), warriorsStandingSelected[3], true);
        
        warriorsWalkingSelected[0] = ImageLoader.loadImage("/Images/Selected Warrior Walking.png");
        warriorsWalkingSelectedTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), warriorsWalkingSelected[0], true);  

        warriorsWalkingSelected[1] = ImageLoader.loadImage("/Images/Green Selected Warrior Walking.png");
        warriorsWalkingSelectedTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), warriorsWalkingSelected[1], true);

        warriorsWalkingSelected[2] = ImageLoader.loadImage("/Images/Blue Selected Warrior Walking.png");
        warriorsWalkingSelectedTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), warriorsWalkingSelected[2], true);

        warriorsWalkingSelected[3] = ImageLoader.loadImage("/Images/Purple Selected Warrior Walking.png");
        warriorsWalkingSelectedTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), warriorsWalkingSelected[3], true);


        //textures for animations
        warriorsAttackingSelected[0] = ImageLoader.loadImage("/Images/Selected Warrior Attacking.png");
        warriorsAttackingSelectedTexture[0] = AWTTextureIO.newTexture(Display.getProfile(), warriorsAttackingSelected[0], true);

        warriorsAttackingSelected[1] = ImageLoader.loadImage("/Images/Green Selected Warrior Attacking.png");
        warriorsAttackingSelectedTexture[1] = AWTTextureIO.newTexture(Display.getProfile(), warriorsAttackingSelected[1], true);

        warriorsAttackingSelected[2] = ImageLoader.loadImage("/Images/Blue Selected Warrior Attacking.png");
        warriorsAttackingSelectedTexture[2] = AWTTextureIO.newTexture(Display.getProfile(), warriorsAttackingSelected[2], true);

        warriorsAttackingSelected[3] = ImageLoader.loadImage("/Images/Purple Selected Warrior Attacking.png");
        warriorsAttackingSelectedTexture[3] = AWTTextureIO.newTexture(Display.getProfile(), warriorsAttackingSelected[3], true);
        
        barrack = ImageLoader.loadImage("/Images/barrack.png");
        barrackTexture = AWTTextureIO.newTexture(Display.getProfile(), barrack, true);
        
        
        //Sounds
        //otherExplosionSound = new SoundClip("/Sounds/explosion2.wav");
        //otherExplosionSound.setLooping(false);
        //otherExplosionSound.prePlayLoad();
    }
}