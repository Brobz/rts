/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import mikera.vectorz.Vector2;

/**
 *
 * @author brobz
 */
public class GridMap {
    private static int GRID_SQUARE_SIZE = 10;
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
        int startingX = (int) ((e.position.x - e.dimension.x/2) / GRID_SQUARE_SIZE);
        int startingY = (int) ((e.position.y - e.dimension.y/2) / GRID_SQUARE_SIZE);
        for(int x = startingX; x <= (e.position.x + e.dimension.x/2) / GRID_SQUARE_SIZE; x++){
            for(int y = startingY; y <= (e.position.y + e.dimension.y/2) / GRID_SQUARE_SIZE; y++){
                map.get(x).get(y).setEntityContained(null);
            }
        }
    }
    
    public void updateMap(Entity e){
        int startingX = (int) ((e.position.x - e.dimension.x/2) / GRID_SQUARE_SIZE);
        int startingY = (int) ((e.position.y - e.dimension.y/2) / GRID_SQUARE_SIZE);
        for(int x = startingX; x <= (e.position.x + e.dimension.x/2) / GRID_SQUARE_SIZE; x++){
            for(int y = startingY; y <= (e.position.y + e.dimension.y/2) / GRID_SQUARE_SIZE; y++){
                map.get(x).get(y).setEntityContained(e);
            }
        }
    }
    
    
    
    /* PATH FINDING STUFF */
    
     private class qNode{
        qNode prnt;
        int row;
        int col;
        public qNode(){}
        public qNode(qNode prnt, int row, int col){
            this.prnt = prnt;
            this.row = row;
            this.col = col;
        }
    }
    
    public Vector2 translate(int row, int col){
        return Vector2.of((float)row * GRID_SQUARE_SIZE + GRID_SQUARE_SIZE/2, (float)col * GRID_SQUARE_SIZE + GRID_SQUARE_SIZE/2);
    }
    
    int [] [] nexts = new int[] [] {{0, 1}, {1, 0}, {1, 1}, {-1, 0}, {-1, -1}, {-1, 1}, {1,-1}, {0, -1}};
    public ArrayList<Vector2> getBestRoute(Entity src, Entity dest){
        boolean [][] visited = new boolean[map.size()][map.get(0).size()];
        
        ArrayList<Vector2> path = new ArrayList<>();
        int startingX = (int) ((src.position.x - src.dimension.x/2) / GRID_SQUARE_SIZE);
        int startingY = (int) ((src.position.y - src.dimension.y/2) / GRID_SQUARE_SIZE);
        visited[startingX][startingY] = true;
        qNode start = new qNode(null, startingX, startingY);
        Queue<qNode> q = new LinkedList<qNode>();
        q.add(start);
        qNode curr = null;
        boolean find = false;
        while(!q.isEmpty()){
            //System.out.println("checking");
            curr = q.remove();
            if(map.get(curr.row).get(curr.col).getEntityContained() == dest){
                find = true;
                break;
            }
            else if(map.get(curr.row).get(curr.col).getEntityContained() != null && map.get(curr.row).get(curr.col).getEntityContained() != src){
                //System.out.println(map.get(curr.row).get(curr.col).id);
                //System.out.println("flagship xdxd");
                continue;
            }
            for(int i = 0; i < 8; i++){
                int nrow = curr.row + nexts[i][0];
                int ncol = curr.col + nexts[i][1];
                if(nrow >= map.size() || nrow <= 0 || ncol >= map.get(0).size() || ncol <= 0) continue;
                if(visited[nrow][ncol]) continue;
                visited[nrow][ncol] = true;
                q.add(new qNode(curr, nrow, ncol));
            }
        }
        //reconstruct path
        if(find){
            System.out.println("path was found xd");
            while(curr != null){
                path.add(translate(curr.row, curr.col));
                curr = curr.prnt;
            }
            System.out.println(path.size());
            Collections.reverse(path);
            return path;
        }
        else{
            System.out.println("path was not found");
        }
        return null;
    }
}