/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.src.server.GameClient;
import com.jogamp.opengl.GL2;
import java.awt.Rectangle;
import java.util.ArrayList;
import mikera.vectorz.*;

/**
 *
 * @author brobz
 */
//Basic class only used for 'people' in the game, intermidiate between Entity and the characters
public class Unit extends Entity{
    protected double speed, maxHealth, health, damage, attackSpeed, range; //simple unit attributes
    protected Rectangle healthBar; //GUI health representation
    protected Player owner; //owner of unit
    protected Vector2 positionTarget; // vector containing the position of the current target
    protected boolean onMoveCommand; //flag to know if we have to move the unit towards a target
    protected int regularRange;
    protected boolean onAttackCommand;
    protected Building buildingToAttack;
    protected Unit unitToAttack;
    protected Timer attackingTimer;
    protected Vector2 pathNext;
    protected Entity toReachTarget;
    protected int unitAttackRange, buildingAttackRange;
    protected boolean selected;
    protected int attentionRange;
    
    // image changing stuff
    Timer runningTimer;
    protected int runningCnt = 0;
    protected int direction;
    protected boolean animated;

    public Unit(){
        super();
    }    

    public Unit(Vector2 dimension, Vector2 position, int id, Player owner){
       super(dimension, position, id);
       this.healthBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 15), (int) dimension.x, 8);
       this.onMoveCommand = false;
       this.regularRange = 10;
       this.onAttackCommand = false;
       this.owner = owner;
       this.attackingTimer = new Timer(Game.getFPS());
       this.attentionRange = 75;
       attackingTimer.setUp(0);
       this.runningTimer = new Timer(Game.getFPS());
       this.runningTimer.setUp(0.2);
       this.animated = false;
    }
    
    //tick method
    @Override
    public void tick(GridMap map){
        if(!isAlive() && !cleanedUp){
            map.deleteMap(this);
            this.hitBox = new Rectangle(0, 0, 0, 0);
            if(selected) deselect();
            cleanedUp = true;
        }
        if(cleanedUp) return;
        healthBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 15), (int) dimension.x, 8);
        
        maintainGuard(map);
        //if onMoveCommand is active then the unit will move towards the
        //position of the target
        if(onMoveCommand){
            double distTarget = Vector2.of(position.x, position.y).distance(positionTarget);
            if(distTarget < range) stopMoving();
            else{
                if(pathNext == null) pathNext = Game.map.getBestRoute(this, toReachTarget, positionTarget);
                if(pathNext == null){
                    return;
                }
                double distance = position.distance(pathNext);
                if(distance < range) pathNext = Game.map.getBestRoute(this, toReachTarget, positionTarget);

                if(pathNext == null){
                    return;
                }
                else{
                    Vector2 mult = Vector2.of(pathNext.x - position.x, pathNext.y - position.y);
                    double multMag = position.distance(pathNext);
                    //System.out.println("---------------------------------------     " + mult);
                    //System.out.println(multMag);
                    mult.x /= multMag;
                    mult.y /= multMag;
                    //System.out.println(mult);
                    velocity = Vector2.of(speed * mult.x, speed * mult.y);
                }
            }
        }
        if(onAttackCommand){
            if(buildingToAttack != null){
                if(!buildingToAttack.isUsable()){
                    stopAttacking();
                }
                else{
                    double dist = Vector2.of(position.x, position.y).distance(buildingToAttack.position);
                    if(dist < range){
                        if(attackingTimer.doneWaiting()){
                            buildingToAttack.singleAttack(damage);
                            attackingTimer.setUp(attackSpeed);
                        }
                    }else{
                        moveTo(buildingToAttack);
                    }
                }
            }else{
                if(!unitToAttack.isAlive()){
                    stopAttacking();
                }
                else{
                    double dist = Vector2.of(position.x, position.y).distance(unitToAttack.position);
                    if(dist < range){
                        if(attackingTimer.doneWaiting()){
                            unitToAttack.singleAttack(damage);
                            attackingTimer.setUp(attackSpeed);
                        }
                    }else{
                        moveTo(unitToAttack.position);
                    }
                }
            }
        }else{
            if(attackingTimer.doneWaiting()){}
        }
        super.tick(map);        
    }
    
    //method to stopMoving the unit
    public void stopMoving(){
        positionTarget = position;
        velocity = Vector2.of(0, 0); 
        pathNext = null;
        this.toReachTarget = null;
        onMoveCommand = false;
    }
    
    public void moveTo(int playerID, GameClient client, Vector2 target){
        client.sendMoveCommand(playerID, id, (int) target.x, (int) target.y);
    }
    
    //method to setup moving to a target
    public void moveTo(Vector2 target){
        this.toReachTarget = null;
        positionTarget = target;
        onMoveCommand = true;
    }
    
    //method to test moving to a target
    public void moveTo(Entity target){
        //pathNext = Game.map.getBestRoute(this, target);
        positionTarget = target.position;
        this.toReachTarget = target;
        onMoveCommand = true;
    }
    
    //method to select unit to move
    public void select(){
        selected = true;
        Game.selectedUnits.add(this);
    }
    
    //method to deselect the unit to move
    public void deselect(){
        selected = false;
        Game.selectedUnits.remove(this);
    }
    
    //simple rendering method
    public void render(GL2 gl, Camera cam){
        if(isAlive()){
            //if(selected) draw otlined sprite
            super.render(gl, cam);
            if(health != maxHealth) drawHealthBar(gl, cam);
        }
    }
    
    public void renderAnimation(GL2 gl, Camera cam, int contFrame, int direction) {
        if(isAlive()){
            //if(selected) draw otlined sprite
            super.renderAnimation(gl, cam, contFrame, direction);
            if(health != maxHealth) drawHealthBar(gl, cam);
        }    
    }
    
    
    
    public boolean isAlive(){
        return health > 0;
    }
    
    public void singleAttack(double damage){
       health -= damage;
   }
    
    
    //method to deretmine where to atack
    public void attackAt(int playerID, GameClient client, Player playerToAttack, Building target){
        client.sendAttackCommand(playerID, id, playerToAttack.getID(), -1, target.id);
    }
    
    public void attackAt(int playerID, GameClient client, Player playerToAttack, Unit target){
        client.sendAttackCommand(playerID, id, playerToAttack.getID(), target.id, -1);
    }
    
    //method to deretmine where to atack
    public void attackAt(Building buildingToAtack){
        onAttackCommand = true;
        this.buildingToAttack = buildingToAtack;
        this.unitToAttack = null;
        this.range = this.buildingAttackRange;
    }
    
    public void attackAt(Unit unitToAttack){
        onAttackCommand = true;
        this.unitToAttack = unitToAttack;
        this.buildingToAttack = null;
        this.range = this.unitAttackRange;
    }
    
    //method to stop attacking
    public void stopAttacking(){
        onAttackCommand = false;
        this.buildingToAttack = null;
        this.unitToAttack = null;
        System.out.println("stopAttacking");
        stopMoving();
        this.range = regularRange;
    }
    
    private void drawHealthBar(GL2 gl, Camera camera){
        gl.glColor4f(0.85f, 0, 0, 1f);
             gl.glBegin(GL2.GL_QUADS);
             gl.glVertex2d(healthBar.x - camera.position.x, healthBar.y - camera.position.y);
             gl.glVertex2d(healthBar.x - camera.position.x, healthBar.y + healthBar.height - camera.position.y);       
             gl.glVertex2d(healthBar.x + healthBar.width - camera.position.x, healthBar.y + healthBar.height - camera.position.y);
             gl.glVertex2d(healthBar.x + healthBar.width - camera.position.x, healthBar.y - camera.position.y);
        gl.glEnd();
        
        gl.glColor4f(0, 0.85f, 0, 1f);
             gl.glBegin(GL2.GL_QUADS);
             gl.glVertex2d(healthBar.x - camera.position.x, healthBar.y - camera.position.y);
             gl.glVertex2d(healthBar.x - camera.position.x, healthBar.y + healthBar.height - camera.position.y);       
             gl.glVertex2d(healthBar.x + (healthBar.width * health / maxHealth) - camera.position.x, healthBar.y + healthBar.height - camera.position.y);
             gl.glVertex2d(healthBar.x + (healthBar.width * health / maxHealth) - camera.position.x, healthBar.y - camera.position.y);
        gl.glEnd();
    }
    
    public boolean isBusy(){
        return onMoveCommand || onAttackCommand;
    }
    
    public void maintainGuard(GridMap map){
        if(!isBusy()){
            int startingX = (int) ((position.x - dimension.x/2 - attentionRange) / GridMap.GRID_SQUARE_SIZE);
            int startingY = (int) ((position.y - dimension.y/2 - attentionRange) / GridMap.GRID_SQUARE_SIZE);
            for(int x = startingX; x <= (position.x + dimension.x/2 + attentionRange) / GridMap.GRID_SQUARE_SIZE; x++){
                for(int y = startingY; y <= (position.y + dimension.y/2 + attentionRange) / GridMap.GRID_SQUARE_SIZE; y++){
                   Entity e = map.getMap().get(x).get(y).getEntityContained();
                   if(e instanceof Unit){
                       if(((Unit) e).owner != this.owner){
                           attackAt((Unit) e);
                           return;
                       }
                   }
                }
            }
        }
    }

    void updateInfo(ArrayList<Double> info) {
        // 0 -> Health
        // 1 -> xPos
        // 2 -> yPos
        // 3 -> xVel
        // 4 -> yVel
        // 5 -> onMoveCommand
        // 6 -> onAttackCommand
        if(cleanedUp) return;
        
        this.health = (int) Math.floor(info.get(0));
        this.position = Vector2.of(info.get(1), info.get(2));
        this.velocity = Vector2.of(info.get(3), info.get(4));
        
        if((int) Math.floor(info.get(5)) == 1)
            this.onMoveCommand = true;
        else
            this.onMoveCommand = false;
        
        if((int) Math.floor(info.get(6)) == 1)
            this.onAttackCommand = true;
        else
            this.onAttackCommand = false;
        
        if(!isAlive() && !cleanedUp){
            Game.map.deleteMap(this);
            this.hitBox = new Rectangle(0, 0, 0, 0);
            if(selected) deselect();
            cleanedUp = true;
        }
        
        healthBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 15), (int) dimension.x, 8);
        changeAnimationSide();
        changeAttackingDirection();
    }
    
    public void changeAnimationSide(){
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
    }
    
    public void changeAttackingDirection() {
        double diffX = position.x - positionTarget.x;
        double diffY = position.y - positionTarget.y;

        if (diffX>=0 && diffY>=0) {
            if (diffX > diffY)
                direction = 3; //set direction to right
            else
                direction = 1; //set direction to up
        }
        else if (diffX<0 && diffY>=0) {
            if (Math.abs(diffX) > diffY)
                direction = 2; //set direction to left
            else
                direction = 1; //set direction to up
        }
        else if (diffX<0 && diffY<0) {
            if (Math.abs(diffX) > Math.abs(diffY))
                direction = 2; //set direction to left
            else
                direction = 0; //set direction to down
        }
        else if (diffX>=0 && diffY<0) {
            if (diffX > Math.abs(diffY))
                direction = 3; //set direction to right
            else
                direction = 0; //set direction to down
        }
        
    }
}