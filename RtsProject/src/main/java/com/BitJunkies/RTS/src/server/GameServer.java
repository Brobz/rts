/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

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
    private ArrayList<MoveObject> movesIssued;
    private ArrayList<MineObject> minesIssued;
    
    public GameServer() {
        movesIssued = new ArrayList<MoveObject>();
        minesIssued = new ArrayList<MineObject>();

        Log.set(Log.LEVEL_DEBUG);
 
        server = new Server();
        KryoUtil.registerServerClasses(server);
 
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("connected: " + connection.toString());
            }
 
            @Override
            public void disconnected(Connection connection) {
                System.out.println("disconnected: " + connection.toString());
            }
 
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof MoveObject) {
                    movesIssued.add((MoveObject) object);
                }
                
                if (object instanceof MineObject) {
                    minesIssued.add((MineObject) object);
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
                    server.getConnections()[j].sendUDP(movesIssued.get(i));
                }
                movesIssued.remove(i);
                i--;
            }
            for(int i = 0; i < minesIssued.size(); i++){
                for(int j = 0; j < server.getConnections().length; j++){
                    server.getConnections()[j].sendUDP(minesIssued.get(i));
                }
                minesIssued.remove(i);
                i--;
            }
            tickTime = System.currentTimeMillis();
        }
        
    }
}