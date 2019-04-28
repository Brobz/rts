package com.BitJunkies.RTS.src;
 public class Timer{
    private int  framesToWait;
    private int actualFrame;
    private boolean active;
    private int fps;

    public Timer(int fps){
        this.active = false;
        this.fps = fps;
    }

    //setup method to start the timer
    public void setUp(double seconds){
        //calculating the frames to wait
        framesToWait = (int)Math.floor(seconds * fps);
        actualFrame = 0;
        active = true;
    }

    //done waiting method to ask the timer if the time is up
    public boolean doneWaiting(){
        //updating the frames
        actualFrame ++;
        //checking if we reached the desired frame
        if(actualFrame >= framesToWait){
            active = false;
            return true;
        }
        return false;
    }
    public boolean isActive(){
        return active;
    }
}