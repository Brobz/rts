/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import mikera.vectorz.Vector2;

/**
 *
 * @author brobz
 */
public class GridMap {
    private static int GRID_SQUARE_SIZE = 20;
    private static int GRID_WIDTH, GRID_HEIGHT;
    private ArrayList<ArrayList<GridSquare>> map;
    
    public GridMap(int mapWidth, int mapHeight){
        GRID_WIDTH = mapWidth / GRID_SQUARE_SIZE;
        GRID_HEIGHT = mapHeight / GRID_SQUARE_SIZE;
        map = new ArrayList<ArrayList<GridSquare>>();
        for(int x = 0; x < GRID_WIDTH; x++){
            map.add(new ArrayList<GridSquare>());
            for(int y = 0; y < GRID_HEIGHT; y++){
                map.get(x).add(y, new GridSquare(Vector2.of(GRID_SQUARE_SIZE, GRID_SQUARE_SIZE), Vector2.of(x * GRID_SQUARE_SIZE, y * GRID_SQUARE_SIZE)));
            }
        }
    }
    
    public void tick(){
        /*/
        for(int i = 0; i < map.size(); i++){
            map.get(i).tick();
        }
        /*/
    }
    
    public ArrayList<ArrayList<GridSquare>> getMap(){
        return getMap();
    }
   
    
    public void render(GL2 g, Camera cam){
        for(int i = 0; i < map.size(); i++){
            for(int j = 0; j < map.size(); j++){
                map.get(i).get(j).render(g, cam);
            }
        }
    }
    
    public void deleteMap(Entity e){
        int startingX = (int) (e.position.x / GRID_SQUARE_SIZE);
        int startingY = (int) (e.position.y / GRID_SQUARE_SIZE);
        for(int x = startingX; x < (startingX + e.dimension.x) / GRID_SQUARE_SIZE; x++){
            for(int y = startingY; x < (startingY + e.dimension.y) / GRID_SQUARE_SIZE; y++){
                map.get(x).get(y).setEntityContained(null);
            }
        }
    }
    
    public void updateMap(Entity e){
        int startingX = (int) (e.position.x / GRID_SQUARE_SIZE);
        int startingY = (int) (e.position.y / GRID_SQUARE_SIZE);
        for(int x = startingX; x < (startingX + e.dimension.x) / GRID_SQUARE_SIZE; x++){
            for(int y = startingY; x < (startingY + e.dimension.y) / GRID_SQUARE_SIZE; y++){
                map.get(x).get(y).setEntityContained(e);
            }
        }
    }
    
}
