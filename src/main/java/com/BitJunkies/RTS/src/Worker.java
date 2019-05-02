/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.src.server.GameClient;
import com.jogamp.opengl.GL2;
import java.util.concurrent.ConcurrentHashMap;
import mikera.vectorz.Vector2;

/**
 *
 * @author brobz
 */

//Simple Worker class
public class Worker extends Unit{
    
    public static final int RUBY_COST = 30;
    
    private static final int MINING_TOP = 3;
    //Worker unique variables
    public static final int WORKER_WIDTH = 40, WORKER_HEIGHT = 40;
    private Timer hitingResourceTimer,buildingCasttleTimer;
    private boolean onMineCommand;
    private boolean onBuildCommand;
    private Resource targetMiningPatch;
    private Building targetBuilding;
    private int miningRange;
    private int creationImpact;
    
    
    //mining stuff
    private int currMining;
    private boolean onBringResourcesBackCommand;
    private Building nearestMiningBuilding;
    
    // image changing stuff
    Timer runningTimer;
    private int runningCnt = 0;
    private int direction;
    
    public Worker(){
        super();
    }
    
    public Worker(Vector2 dimension, Vector2 position, int id, Player owner){
       super(dimension, position, id, owner);
       this.speed = 4;
       this.maxHealth = 10;
       this.health = this.maxHealth;
       this.damage = 2;
       this.attackSpeed = 1;
       this.range = regularRange;
       this.hitingResourceTimer = new Timer(Game.getFPS());
       hitingResourceTimer.setUp(attackSpeed);
       this.buildingCasttleTimer = new Timer(Game.getFPS());
       buildingCasttleTimer.setUp(attackSpeed);
       this.miningRange = 60;
       this.buildingAttackRange = 60;
       this.unitAttackRange = 35;
       this.creationImpact = 5;
       this.texture = Assets.workerWalkingTexture;
       this.currMining = 0;
       this.onBringResourcesBackCommand = false;
       this.runningTimer = new Timer(Game.getFPS());
       this.runningTimer.setUp(0.2);
    }
    
    public void tick(GridMap map){
        super.tick(map);
        if(onMoveCommand || onMineCommand || onBuildCommand){
            if(runningTimer.doneWaiting()){
                // cambio
                runningCnt ++;
                runningCnt %= 4;
                this.runningTimer.setUp(0.3);
            }
        }
        
        if (onMoveCommand) {
            texture = Assets.workerWalkingTexture;
        }
        else if(onMineCommand || onBuildCommand) {
            texture = Assets.workerMiningTexture;
        }
        
        //change direction according to velocity
        if (velocity.x>=0 && velocity.y>=0) {
            if (velocity.x > velocity.y)
                direction = 3; //set direction to right
            else
                direction = 1; //set direction to up
        }
        else if (velocity.x<0 && velocity.y>=0) {
            if (Math.abs(velocity.x) > velocity.y)
                direction = 2; //set direction to left
            else
                direction = 1; //set direction to up
        }
        else if (velocity.x<0 && velocity.y<0) {
            if (Math.abs(velocity.x) > Math.abs(velocity.y))
                direction = 2; //set direction to left
            else
                direction = 0; //set direction to down
        }
        else if (velocity.x>=0 && velocity.y<0) {
            if (velocity.x > Math.abs(velocity.y))
                direction = 3; //set direction to right
            else
                direction = 0; //set direction to down
        }
        
        //If the worker is designated to mine then...
        if(onMineCommand){
            double dist = Vector2.of(position.x, position.y).distance(targetMiningPatch.position);
            changeAnimationSide(true);
            
            if(currMining == MINING_TOP){
                onMineCommand = false;
                onBringResourcesBackCommand = true;
                System.out.println("go bring resources back");
                findNearesMiningBuilding();
            }
            
            //checking if the mining resource is still usable
            else if(!targetMiningPatch.isUsable()){
                stopMining();
                if(currMining != 0){
                    onBringResourcesBackCommand = true;
                    System.out.println("go bring resources back");
                    findNearesMiningBuilding();
                }
            }
            //otherwise check its already mining
            else if(dist < range){
                if(hitingResourceTimer.doneWaiting()){
                    System.out.println("miner hit");
                    targetMiningPatch.singleAttack((int)damage);
                    hitingResourceTimer.setUp(attackSpeed);
                    currMining ++;
                }
            }else{
                moveTo(targetMiningPatch.position);
            }
        }
        //has to bring resources back
        else if(onBringResourcesBackCommand){
            if(nearestMiningBuilding == null) stopMining();
            else{
                //we are moving to the nearest building
                //if we've reached the building
                double dist = Vector2.of(position.x, position.y).distance(nearestMiningBuilding.position);
                if(dist < range){
                    owner.giveRubys(currMining);
                    currMining = 0;
                    onBringResourcesBackCommand = false;
                    if(targetMiningPatch != null){
                       onMineCommand = true;
                       range = miningRange;
                    }
                }else{
                    moveTo(nearestMiningBuilding.position);
                }
            }
        }
        //If the worker is designated to build...
        else if(onBuildCommand){
            changeAnimationSide(false);
            //check if the building is not built yet
            if(targetBuilding.isCreated()){
                stopBuilding();
                return;
            }
            //check if the worker is designated to build
            double dist = Vector2.of(position.x, position.y).distance(targetBuilding.position);
            if(dist < range){
                if(buildingCasttleTimer.doneWaiting()){
                    targetBuilding.singleCreation(creationImpact);
                    buildingCasttleTimer.setUp(attackSpeed);
                }
            }else{
                moveTo(targetBuilding.position);
            }
        }
    }
    
    //method to deretmine where to mine
    public void mineAt(int playerID, GameClient client, Resource resourcePatch){
        client.sendMineCommand(owner.getID(), id, resourcePatch.id);
    }
    
    public void mineAt(Resource resourcePatch){
        stopBuilding();
        stopAttacking();
        onMineCommand = true;
        targetMiningPatch = resourcePatch;
        range = miningRange;
        nearestMiningBuilding = null;
    }
    
    //simple render method
    public void render(GL2 gl, Camera cam){
        super.renderAnimation(gl, cam, runningCnt, direction);
    }
    
    //method to stop minning
    public void stopMining(){
        onMineCommand = false;
        onBringResourcesBackCommand = false;
        targetMiningPatch = null;
        System.out.println("stopMining");
        stopMoving();
        range = regularRange;
        texture = Assets.workerWalkingTexture;
    }
    
    public void buildAt(int playerID, GameClient client, Building target){
        client.sendBuildCommand(owner.getID(), id, target.id);
    }
    
    //method to tell worker where to go build
    public void buildAt(Building building){
        stopMining();
        stopAttacking();
        onBuildCommand = true;
        targetBuilding = building;
        range = miningRange;
    }
    
    
    //method to tell worker to stop building
    public void stopBuilding(){
        onBuildCommand = false;
        targetBuilding = null;
        System.out.println("stop building");
        stopMoving();
        range = regularRange;
        texture = Assets.workerWalkingTexture;
    }
    
    public void findNearesMiningBuilding(){
        //BFS
        //AQUI deberia ir una bfs
        ConcurrentHashMap<Integer, Building> currBuildings = owner.buildings;
        double distance = 10000000;
        for(Building build : currBuildings.values()){
            if(!build.created || !build.isAlive()) continue;
            if(build instanceof Castle){
                double currDist = position.distance(build.position);
                if(currDist < distance){
                    nearestMiningBuilding = build;
                    distance = currDist;
                }
            }
        }
    }
    
    @Override
    public void attackAt(Building buildingToAtack){
        stopMining();
        stopBuilding();
        super.attackAt(buildingToAtack);
    }
    
    @Override
    public void attackAt(Unit unitToAttack){
        stopMining();
        stopBuilding();
        super.attackAt(unitToAttack);
    }
    

    public boolean isBusy(){
        return onMoveCommand || onMineCommand || onBuildCommand || onAttackCommand;
    }
    private void changeAnimationSide(boolean mining){
        double diffX, diffY;
        if(mining){
            diffX = position.x - targetMiningPatch.position.x;
            diffY = position.y - targetMiningPatch.position.y;
        }else{
            diffX = position.x - targetBuilding.position.x;
            diffY = position.y - targetBuilding.position.y;
        }

        if (diffX>=0 && diffY>=0) {
            if (diffX > diffY)
                direction = 2; //set direction to right
            else
                direction = 1; //set direction to up
        }
        else if (diffX<0 && diffY>=0) {
            if (Math.abs(diffX) > diffY)
                direction = 3; //set direction to left
            else
                direction = 1; //set direction to up
        }
        else if (diffX<0 && diffY<0) {
            if (Math.abs(diffX) > Math.abs(diffY))
                direction = 3; //set direction to left
            else
                direction = 0; //set direction to down
        }
        else if (diffX>=0 && diffY<0) {
            if (diffX > Math.abs(diffY))
                direction = 2; //set direction to right
            else
                direction = 0; //set direction to down
        }
    }
}