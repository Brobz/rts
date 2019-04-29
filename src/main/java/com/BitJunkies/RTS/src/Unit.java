/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import com.BitJunkies.RTS.input.MouseInput;
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
    
    public Unit(){
        super();
    }    

    public Unit(Vector2 dimension, Vector2 position, int id, Player owner){
       super(dimension, position, id);
       this.healthBar = new Rectangle((int) (position.x - dimension.x / 2), (int) (position.y - dimension.y / 2 - 15), (int) dimension.x, 8);
       this.onMoveCommand = false;
       this.regularRange = 10;
       this.onAtackCommand = false;
       this.owner = owner;
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
            Vector2 mult = Vector2.of(1, 1);
            if(position.x > positionTarget.x) mult.x *= -1;
            if(position.y > positionTarget.y) mult.y *= -1;
            double dist = Vector2.of(position.x, position.y).distance(positionTarget);
            velocity = Vector2.of(speed * mult.x, speed * mult.y);
            if(dist < range) stopMoving();
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
                            attackingTimer.setUp(attackSpeed);
                        }
                    }else{
                        moveTo(buildingToAttack.position);
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
        onMoveCommand = false;
        positionTarget = position;
        velocity = Vector2.of(0, 0);  
    }
    
    public void moveTo(int playerID, GameClient client, Vector2 target){
        client.sendMoveCommand(playerID, id, (int) target.x, (int) target.y);
    }
    
    //method to setup moving to a target
    public void moveTo(Vector2 target){
        onMoveCommand = true;
        positionTarget = target;
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
    }
    
    public void attackAt(Unit unitToAttack){
        onAtackCommand = true;
        this.unitToAttack = unitToAttack;
        this.buildingToAttack = null;
    }
    
    //method to stop attacking
    public void stopAttacking(){
        onAtackCommand = false;
        this.buildingToAttack = null;
        this.unitToAttack = null;
        stopMoving();
        range = regularRange;
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