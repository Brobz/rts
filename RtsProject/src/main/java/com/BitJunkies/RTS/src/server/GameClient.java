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
                if (object instanceof MoveObject) {
                    Game.executeMoveCommand((MoveObject) object);
                }
                if (object instanceof MineObject) {
                    Game.executeMineCommand((MineObject) object);
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
    
    public void sendMoveCommand(int entityID, int positionX, int positionY){
        client.sendUDP(new MoveObject(entityID, positionX, positionY));
    }
    
    public void sendMineCommand(int workerID, int resourceID){
        client.sendUDP(new MineObject(workerID, resourceID));
    }
    
}