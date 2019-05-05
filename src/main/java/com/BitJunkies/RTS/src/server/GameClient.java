/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import com.BitJunkies.RTS.src.Game;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author rober
 */
public class GameClient {
     
    Client client;
 
    public GameClient() {
        Log.set(Log.LEVEL_DEBUG);
 
        client = new Client();
        KryoUtil.registerClientClass(client);
 
        /* Kryonet > 2.12 uses Daemon threads ? */
        new Thread(client).start();
 
        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
            }
 
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof ConnectionObject) {
                    Game.addNewPlayer(((ConnectionObject) object));
                }
                
                if (object instanceof SpawnUnitObject) {
                    Game.executeSpawnUnitCommand((SpawnUnitObject) object);
                }
                
                if (object instanceof SpawnBuildingObject) {
                    Game.executeSpawnBuildingCommand((SpawnBuildingObject) object);
                }
                
                if (object instanceof MoveObject) {
                    Game.executeMoveCommand((MoveObject) object);
                }
                if (object instanceof MineObject) {
                    Game.executeMineCommand((MineObject) object);
                }
                if (object instanceof AttackObject) {
                    Game.executeAttackCommand((AttackObject) object);
                }
                
                if (object instanceof BuildObject) {
                    Game.executeBuildCommand((BuildObject) object);
                }
                
                if (object instanceof DisconnectionObject) {
                    Game.removePlayer((DisconnectionObject) object);
                }
                
                if (object instanceof StartMatchObject) {
                    Game.startMatch((StartMatchObject) object);
                }
            }
 
            @Override
            public void disconnected(Connection connection) {
            }
        });
        
        try {
            /* Make sure to connect using both tcp and udp port */
            client.connect(5000, KryoUtil.HOST_IP, KryoUtil.TCP_PORT, KryoUtil.UDP_PORT);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public void sendMoveCommand(int playerID, int entityID, int positionX, int positionY){
        client.sendUDP(new MoveObject(playerID, entityID, positionX, positionY));
    }
    
    public void sendMineCommand(int playerID, int workerID, int resourceID){
        client.sendUDP(new MineObject(playerID, workerID, resourceID));
    }

    public void sendAttackCommand(int playerID, int unitID, int targetPlayerID, int targetUnitID, int targetBuildingID) {
        client.sendUDP(new AttackObject(playerID, unitID, targetPlayerID, targetUnitID, targetBuildingID));
    }
    
    public void sendBuildCommand(int playerID, int workerID, int targetID) {
        client.sendUDP(new BuildObject(playerID, workerID, targetID));
    }
    
    public void sendSpawnUnitCommand(int playerID, int buildingID, int unitIndex, int unitType) {
        client.sendUDP(new SpawnUnitObject(playerID, buildingID, unitIndex, unitType));
    }
    
    public void sendSpawnBuildingCommand(int playerID, int buildingIndex, int xPos, int yPos, ArrayList<Integer> workerIDs) {
        client.sendUDP(new SpawnBuildingObject(playerID, buildingIndex, xPos, yPos, workerIDs));
    }
    
    public void sendStartMatchCommand(int playerID) {
        client.sendUDP(new StartMatchObject(playerID));
    }
    
}