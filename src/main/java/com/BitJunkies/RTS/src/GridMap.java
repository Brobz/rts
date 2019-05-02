/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.jogamp.opengl.GL2;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import mikera.vectorz.Vector2;

/**
 *
 * @author brobz
 */
public class GridMap {
    public static int GRID_SQUARE_SIZE = 10;
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
                if(map.get(x).get(y).getEntityContained() == e) map.get(x).get(y).setEntityContained(null);
            }
        }
    }
    
    public void updateMap(Entity e){
        int startingX = (int) ((e.position.x - e.dimension.x/2) / GRID_SQUARE_SIZE);
        int startingY = (int) ((e.position.y - e.dimension.y/2) / GRID_SQUARE_SIZE);
        for(int x = startingX; x <= (e.position.x + e.dimension.x/2) / GRID_SQUARE_SIZE; x++){
            for(int y = startingY; y <= (e.position.y + e.dimension.y/2) / GRID_SQUARE_SIZE; y++){
                //if(map.get(x).get(y).getEntityContained() instanceof Unit && map.get(x).get(y).getEntityContained() != e)
                
                map.get(x).get(y).setEntityContained(e);
            }
        }
    }
    
    public boolean isEmptyArea(Rectangle area){
        int startingX = (int) ((area.x) / GRID_SQUARE_SIZE);
        int startingY = (int) ((area.y) / GRID_SQUARE_SIZE);
        for(int x = startingX; x <= (area.x + area.width) / GRID_SQUARE_SIZE; x++){
            for(int y = startingY; y <= (area.y + area.height) / GRID_SQUARE_SIZE; y++){
                if(map.get(x).get(y).getEntityContained() != null) return false;
            }
        }
        return true;
    }

    /*
    public boolean intersects(Entity e, int row, int col){
        Entity e1 = null, e2 = null, e3 = null, e4 = null;
        Vector2 RC = translate(row, col);
        
        int startingX = (int) ((RC.x - e.dimension.x/2) / GRID_SQUARE_SIZE);
        int startingY = (int) ((RC.y - e.dimension.y/2) / GRID_SQUARE_SIZE);
        
        int endingX = (int) ((RC.x + e.dimension.x/2) / GRID_SQUARE_SIZE);
        int endingY = (int) ((RC.y + e.dimension.y/2) / GRID_SQUARE_SIZE);
        
        if(startingX > 0 && startingY > 0) e1 = map.get(startingX-1).get(startingY-1).getEntityContained();
        if(startingX > 0 && startingY < GRID_HEIGHT) e2 = map.get(startingX-1).get(endingY+1).getEntityContained();
        if(startingX < GRID_WIDTH && startingY > 0) e3 = map.get(endingX+1).get(startingY-1).getEntityContained();
        if(startingX < GRID_WIDTH && startingY < GRID_HEIGHT) e4 = map.get(endingX+1).get(endingY+1).getEntityContained();
        return(e1 != null && e1 != e || e2 != null && e2 != e || e3 != null && e3 != e || e4 != null && e4 != e);
    }
    */
    
    public boolean intersects(Entity e, Vector2 RC){
        int startingX = (int) ((RC.x) / GRID_SQUARE_SIZE);
        int startingY = (int) ((RC.y) / GRID_SQUARE_SIZE);
        return (map.get(startingX).get(startingY).getEntityContained() != null && map.get(startingX).get(startingY).getEntityContained() != e);
    } 
    
    /* PATH FINDING STUFF */
    
     private class qNode{
        qNode prnt;
        int row;
        int col;
        int currLev;
        public qNode(){}
        double dist;
        public qNode(qNode prnt, int row, int col, int currLev, double dist){
            this.prnt = prnt;
            this.row = row;
            this.col = col;
            this.currLev = currLev;
            this.dist = dist;
        }
    }
    
    public Vector2 translate(int row, int col){
        return Vector2.of((float)row * GRID_SQUARE_SIZE + GRID_SQUARE_SIZE/2, (float)col * GRID_SQUARE_SIZE + GRID_SQUARE_SIZE/2);
    }
    
    int [] [] nexts = new int[] [] {{0, 1}, {1, 0}, {1, 1}, {-1, 0}, {-1, 1}, {0,-1}, {-1, -1}, {1, -1}};
    //int [] [] nexts = new int[] [] {{0, 1}, {0, -1}, {-1, 0}, {1,0}};
    private static final int RADIUS = 30;
    public Vector2 getBestRoute(Entity src, Entity dest, Vector2 destPos){
        boolean [][] visited = new boolean[map.size()][map.get(0).size()];
        
        int startingX = (int) ((src.position.x) / GRID_SQUARE_SIZE);
        int startingY = (int) ((src.position.y) / GRID_SQUARE_SIZE);
        visited[startingX][startingY] = true;
        qNode start = new qNode(null, startingX, startingY, 1, 0);
        
        PriorityQueue<qNode> q = new PriorityQueue(new Comparator<qNode>() {
            public int compare(qNode n1, qNode n2) {
                return (int) (n1.dist - n2.dist);
            }
        });
        //Queue<qNode> q = new LinkedList<>();

        q.add(start);
        qNode curr = null;
        
        qNode res = null;
        double bestDist = 100000;
        double posDist;
        
        while(!q.isEmpty()){
            curr = q.remove();
            if(dest != null && map.get(curr.row).get(curr.col).getEntityContained() == dest){
                res = curr;
                break;
            }
            if(map.get(curr.row).get(curr.col).getEntityContained() != null && map.get(curr.row).get(curr.col).getEntityContained() != src && curr.currLev > 1){
            //if(intersects(src, translate(curr.row, curr.col))){
                continue;
            }
            if(curr.currLev <= RADIUS){
                posDist = translate(curr.row, curr.col).distance(destPos);
                if(posDist < bestDist){
                    bestDist = posDist;
                    res = curr;
                }
                if(curr.currLev == RADIUS) continue;
            }
            for(int i = 0; i < nexts.length; i++){
                int nrow = curr.row + nexts[i][0];
                int ncol = curr.col + nexts[i][1];
                if(nrow >= map.size() || nrow < 0 || ncol >= map.get(0).size() || ncol < 0) continue;
                if(visited[nrow][ncol]) continue;
                visited[nrow][ncol] = true;
                q.add(new qNode(curr, nrow, ncol, curr.currLev + 1, translate(nrow, ncol).distance(destPos)));
            }
        }
        while(res != null && res.prnt != null && res.prnt.prnt != null && res.prnt.prnt != null && res.prnt.prnt.prnt != null && res.prnt.prnt.prnt.prnt != null){
            res = res.prnt;
        }
        if(res == null|| res.prnt == null) return null;
        return translate(res.row, res.col);
    }
}
