/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import com.BitJunkies.RTS.src.Game;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GameServer {
    // server tickrate in milis
    private static double tickrate = 30;
    private double tickTime = -1;
    
    // server command lists
    private Server server;
    private ArrayList<Connection> connectedPlayers;
    private ArrayList<MoveObject> movesIssued;
    private ArrayList<MineObject> minesIssued;
    private ArrayList<AttackObject> attacksIssued;
    
    public GameServer() {
        connectedPlayers = new ArrayList<Connection>();
        movesIssued = new ArrayList<MoveObject>();
        minesIssued = new ArrayList<MineObject>();
        attacksIssued = new ArrayList<AttackObject>();

        Log.set(Log.LEVEL_DEBUG);
 
        server = new Server();
        KryoUtil.registerServerClasses(server);
 
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("connected: " + connection.toString());
                for(int i = 0; i < connectedPlayers.size(); i++){
                    connectedPlayers.get(i).sendTCP(new ConnectionObject(connection.getID(), false));
                    connection.sendTCP(new ConnectionObject(connectedPlayers.get(i).getID(), false));
                }
                connection.sendTCP(new ConnectionObject(connection.getID(), true));
                connectedPlayers.add(connection);
            }
 
            @Override
            public void disconnected(Connection connection) {
                System.out.println("disconnected: " + connection.toString());
                connectedPlayers.remove(connection);
            }
 
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof MoveObject) {
                    movesIssued.add((MoveObject) object);
                }
                
                if (object instanceof MineObject) {
                    minesIssued.add((MineObject) object);
                }
                
                if (object instanceof AttackObject) {
                    attacksIssued.add((AttackObject) object);
                }
            }
        });
 
        try {
            server.bind(KryoUtil.TCP_PORT, KryoUtil.UDP_PORT);
        } catch (IOException ex) {
            System.out.println(ex);
        }
 
        server.start();
    }
    
    public void tick(){
        double delta = System.currentTimeMillis() - tickTime;
        if(tickTime == -1 || delta >= tickrate){
            
            for(int i = 0; i < movesIssued.size(); i++){
                for(int j = 0; j < server.getConnections().length; j++){
                    server.getConnections()[j].sendTCP(movesIssued.get(i));
                }
                movesIssued.remove(i);
                i--;
            }
            
            for(int i = 0; i < minesIssued.size(); i++){
                for(int j = 0; j < server.getConnections().length; j++){
                    server.getConnections()[j].sendTCP(minesIssued.get(i));
                }
                minesIssued.remove(i);
                i--;
            }
            
            for(int i = 0; i < attacksIssued.size(); i++){
                for(int j = 0; j < server.getConnections().length; j++){
                    server.getConnections()[j].sendTCP(attacksIssued.get(i));
                }
                attacksIssued.remove(i);
                i--;
            }
           
            tickTime = System.currentTimeMillis();
        }
        
    }
}