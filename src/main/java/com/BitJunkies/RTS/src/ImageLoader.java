package com.BitJunkies.RTS.src;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Class to load images
 * @author ulise
 */
public class ImageLoader {
    public static BufferedImage loadImage(String path){
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(ImageLoader.class.getResource(path));
        }
        catch(IOException ioe) {
            System.out.println("Error loading image " + path + ioe.toString());
            System.exit(1);
        }
        return bi;
    }
}