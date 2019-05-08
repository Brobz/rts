/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import com.BitJunkies.RTS.src.Entity;
import com.BitJunkies.RTS.src.Game;
import com.BitJunkies.RTS.src.MainMenu;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author rober
 */
public class GameClient {
     
    public Client client;
    public String name;
    public ArrayList<ConnectionObject> currServerConnectedPlayers;
    
    public GameClient(String name) {
        this.name = name;
        
        currServerConnectedPlayers = new ArrayList<>();
        
        Log.set(Log.LEVEL_ERROR);
 
        client = new Client();
        KryoUtil.registerClientClass(client);
 
        /* Kryonet > 2.12 uses Daemon threads ? */
        new Thread(client).start();
 
        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                connection.sendTCP(client.toString());
            }
 
            @Override
            public void received(Connection connection, Object object) {
                
                if(object instanceof ResetLobbyObject){
                    currServerConnectedPlayers.clear();
                    if(!Game.hosting) Game.resetMatch();
                }
                
                else if (object instanceof ConnectionObject) {
                    currServerConnectedPlayers.add((ConnectionObject) object);
                    Game.addNewPlayer(((ConnectionObject) object));
                }
                
                else if (object instanceof StartMatchObject) {
                    Game.startMatch((StartMatchObject) object);
                }
                
                else if (object instanceof SpawnUnitObject) {
                    Game.executeSpawnUnitCommand((SpawnUnitObject) object);
                }
                
                else if (object instanceof SpawnBuildingObject) {
                    Game.executeSpawnBuildingCommand((SpawnBuildingObject) object);
                }
                
                else if (object instanceof DisconnectionObject) {
                    Game.removePlayer((DisconnectionObject) object);

                    for(int i = 0; i < currServerConnectedPlayers.size(); i++){
                        if(currServerConnectedPlayers.get(i).toString().equals(((DisconnectionObject)object).toString())){
                            currServerConnectedPlayers.remove(i);
                            break;
                        }
                    }
                }
                
                if(Game.hosting){
                    if (object instanceof MoveObject) {
                        Game.executeMoveCommand((MoveObject) object);
                    }
                    else if (object instanceof MineObject) {
                        Game.executeMineCommand((MineObject) object);
                    }
                    else if (object instanceof AttackObject) {
                        Game.executeAttackCommand((AttackObject) object);
                    }

                    else if (object instanceof BuildObject) {
                        Game.executeBuildCommand((BuildObject) object);
                    }


                    else if (object instanceof StartMatchObject) {
                        Game.startMatch((StartMatchObject) object);
                    }
                }
                
                else{
                    if (object instanceof UnitInfoObject) {
                        Game.updateUnitInfo((UnitInfoObject) object);
                    }

                    else if (object instanceof BuildingInfoObject) {
                        Game.updateBuildingInfo((BuildingInfoObject) object);
                    }
                    
                    else if (object instanceof ResourceInfoObject) {
                        Game.updateResourcesInfo((ResourceInfoObject) object);
                    }
                    
                    else if (object instanceof PlayerInfoObject) {
                        Game.updatePlayerInfo((PlayerInfoObject) object);
                    }
                }
            }
 
            @Override
            public void disconnected(Connection connection) {
                Game.players.clear();
                Entity.curr_id = -1;
                Game.setCurrGameState(MainMenu.getInstance());
            }
        });
        
        try {
            client.setName(this.name);
            /* Make sure to connect using both tcp and udp port */
            client.connect(5000, KryoUtil.HOST_IP, KryoUtil.TCP_PORT, KryoUtil.UDP_PORT);
        } catch (IOException ex) {
        }
    }
    
    public void sendMoveCommand(int playerID, int entityID, int positionX, int positionY, String playerName){
        client.sendUDP(new MoveObject(playerID, entityID, positionX, positionY, playerName));
    }
    
    public void sendMineCommand(int playerID, int workerID, int resourceID, String playerName){
        client.sendUDP(new MineObject(playerID, workerID, resourceID, playerName));
    }

    public void sendAttackCommand(int playerID, int unitID, int targetPlayerID, int targetUnitID, int targetBuildingID, String playerName, String targetPlayerName) {
        client.sendUDP(new AttackObject(playerID, unitID, targetPlayerID, targetUnitID, targetBuildingID, playerName, targetPlayerName));
    }
    
    public void sendBuildCommand(int playerID, int workerID, int targetID, String playerName) {
        client.sendUDP(new BuildObject(playerID, workerID, targetID, playerName));
    }
    
    public void sendSpawnUnitCommand(int playerID, int buildingID, int unitIndex, int unitType, String playerName) {
        client.sendUDP(new SpawnUnitObject(playerID, buildingID, unitIndex, unitType, playerName));
    }
    
    public void sendSpawnBuildingCommand(int playerID, int buildingIndex, int xPos, int yPos, ArrayList<Integer> workerIDs, String playerName) {
        client.sendUDP(new SpawnBuildingObject(playerID, buildingIndex, xPos, yPos, workerIDs, playerName));
    }
    
    public void sendStartMatchCommand(int playerID) {
        client.sendUDP(new StartMatchObject(playerID));
    }
    
    public void sendUnitInfo(int playerID, ConcurrentHashMap<Integer, ArrayList<Double>> unitInfo, String playerName){
        client.sendUDP(new UnitInfoObject(playerID, unitInfo, playerName));
    }
    
    public void sendBuildingInfo(int playerID, ConcurrentHashMap<Integer, ArrayList<Double>> buildingInfo, String playerName){
        client.sendUDP(new BuildingInfoObject(playerID, buildingInfo, playerName));
    }
    
    public void sendResourcesInfo(ConcurrentHashMap<Integer, ArrayList<Double>> resInfo) {
        client.sendUDP(new ResourceInfoObject(resInfo));
    }

    public void sendPlayerInfo(int playerID, int rubys, boolean hasFallen, String playerName) {
        client.sendUDP(new PlayerInfoObject(playerID,  rubys, hasFallen, playerName));
    }
    
    public void sendSpendInfo(int playerID, int rubys, String playerName) {
        client.sendUDP(new SpendRubysObject(playerID, rubys, playerName));
    }
    
    public void sendDisconnectCommand(int playerID, String playerName){
        client.sendUDP(new DisconnectionObject(playerName));
    }
    
}