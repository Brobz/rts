/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.src.server.GameClient;
import com.jogamp.opengl.GL2;
import java.awt.Rectangle;
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
    protected boolean onAtackCommand;
    protected Building buildingToAttack;
    protected Unit unitToAttack;
    protected Timer attackingTimer;
    protected Vector2 pathNext;
    protected Entity toReachTarget;
    
    public Unit(){
        super();
    }    

    public Unit(Vector2 dimension, Vector2 position, int id){
       super(dimension, position, id);
       this.healthBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 15), (int) dimension.x, 8);
       this.onMoveCommand = false;
       this.regularRange = 10;
       this.onAtackCommand = false;
       this.attackingTimer = new Timer(Game.getFPS());
       attackingTimer.setUp(0);
    }
    
    //tick method
    @Override
    public void tick(GridMap map){
        if(!isAlive()) return;
        healthBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 15), (int) dimension.x, 8);
        //if onMoveCommand is active then the unit will move towards the
        //position of the target
        if(onMoveCommand){
            //System.out.println("should move");
            if(pathNext == null) pathNext = Game.map.getBestRoute(this, toReachTarget, positionTarget);
            
            double distTarget = Vector2.of(position.x, position.y).distance(positionTarget);
            if(distTarget < range || pathNext == null){
                stopMoving();
                return;
            }
            
            double distance = Vector2.of(position.x, position.y).distance(pathNext);
            if(distance < 6) pathNext = Game.map.getBestRoute(this, toReachTarget, positionTarget);
            
            if(pathNext == null){
                stopMoving();
                return;
            }
            
            Vector2 mult = Vector2.of(pathNext.x - position.x, pathNext.y - position.y);
            double multMag = pathNext.distance(position);
            mult.x /= multMag;
            mult.y /= multMag;
            velocity = Vector2.of(speed * mult.x, speed * mult.y);
        }
        if(onAtackCommand){
            if(buildingToAttack != null){
                if(!buildingToAttack.isUsable()){
                    stopAttacking();
                }
                else{
                    double dist = Vector2.of(position.x, position.y).distance(buildingToAttack.position);
                    if(dist < range){
                        if(attackingTimer.doneWaiting()){
                            buildingToAttack.singleAttack(damage);
                            attackingTimer.setUp(1);
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
                            attackingTimer.setUp(1);
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
        Game.selectedUnits.add(this);
    }
    
    //method to deselect the unit to move
    public void deselect(){
        Game.selectedUnits.remove(this);
    }
    
    //simple rendering method
    public void render(GL2 gl, Camera cam){
        if(isAlive()){
            super.render(gl, cam);
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
        onAtackCommand = true;
        this.buildingToAttack = buildingToAtack;
        this.unitToAttack = null;
        this.range = 40;
    }
    
    public void attackAt(Unit unitToAttack){
        onAtackCommand = true;
        this.unitToAttack = unitToAttack;
        this.buildingToAttack = null;
        this.range = 40;
    }
    
    //method to stop attacking
    public void stopAttacking(){
        onAtackCommand = false;
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
}