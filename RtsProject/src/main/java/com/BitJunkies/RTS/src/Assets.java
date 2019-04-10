package com.BitJunkies.RTS.src;
import java.awt.image.BufferedImage;

public class Assets {
    //class that contains game resources

    //Images
    public static BufferedImage background;
    public static BufferedImage player;
    public static BufferedImage playerSecond;
    public static BufferedImage malito;
    public static BufferedImage livesText;
    public static BufferedImage gameOver;

    //Sound clips
    public static SoundClip explosionSound;
    public static SoundClip otherExplosionSound;

    // init creates obejects so that they are avbailable to our game
    public static void init(){
        background = ImageLoader.loadImage("/Images/background.jpg");
        player = ImageLoader.loadImage("/Images/spaceship.png");
        malito = ImageLoader.loadImage("/Images/ovni.png");
        livesText = ImageLoader.loadImage("/Images/LivesText.png");
        gameOver = ImageLoader.loadImage("/Images/gameover.png");

        //Sounds
        explosionSound = new SoundClip("/Sounds/explosion2.wav");
        explosionSound.setLooping(false);
        explosionSound.prePlayLoad();

        //Sounds
        otherExplosionSound = new SoundClip("/Sounds/explosion2.wav");
        otherExplosionSound.setLooping(false);
        otherExplosionSound.prePlayLoad();
    }
}