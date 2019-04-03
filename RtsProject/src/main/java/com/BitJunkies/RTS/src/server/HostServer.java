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

public class HostServer {
    public HostServer() {
        Log.set(Log.LEVEL_DEBUG);
 
        Server server = new Server();
        KryoUtil.registerServerClasses(server);
 
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("connected");
            }
 
            @Override
            public void disconnected(Connection connection) {
                System.out.println("disconnected");
            }
 
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof TestObjectRequest) {
					TestObject test = new TestObject("Hello Client!");
                    connection.sendUDP(new TestObjectResponse(test));
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
}