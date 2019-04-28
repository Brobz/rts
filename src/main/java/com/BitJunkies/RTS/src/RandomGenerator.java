package com.BitJunkies.RTS.src;
import java.util.Random;

public class RandomGenerator {
    //random obejt to generate random number within bounds
    public static Random rand;

    //funtion which returns the generated random number within the bounds
    public static int generate(int start, int end){
        rand = new Random();
        return start + rand.nextInt(end-start);
    }
}