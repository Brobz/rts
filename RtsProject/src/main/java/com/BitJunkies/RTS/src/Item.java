package com.BitJunkies.RTS.src;
import java.awt.*;

public abstract class Item {
    protected int x; // to store x position
    protected int y; // to store y position
    Rectangle area;

    //constructor
    public Item(int x, int y){
        this.x = x;
        this.y = y;
    }

    //update position items for every thick
    public abstract void tick();

    //to paint the item
    public abstract void render(Graphics g);

    public Rectangle getRectangle(){
        return area;
    }

    public void setupRectangle(int x, int y, int width, int height){
        this.area = new Rectangle(x, y, width, height);
    }

    public void updateRectangle(int x, int y){
        this.area.setLocation(x, y);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
