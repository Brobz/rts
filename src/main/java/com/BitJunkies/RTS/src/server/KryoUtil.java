/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

/**
 *
 * @author rober
 */
public class KryoUtil {
 
    public static final int TCP_PORT = 55223;
    public static final int UDP_PORT = 55224;
    
    public static final String HOST_IP = "localhost";
 
    public static void registerServerClasses(Server server) {
        register(server.getKryo());
    }
 
    public static void registerClientClass(Client client) {
        register(client.getKryo());        
    }
 
    private static void register(Kryo kryo) {
                kryo.register(ConnectionObject.class);
                kryo.register(DisconnectionObject.class);
                kryo.register(MoveObject.class);
                kryo.register(MineObject.class);
                kryo.register(BuildObject.class);
                kryo.register(AttackObject.class);
                kryo.register(double.class);	
		kryo.register(int.class);
		kryo.register(String.class);
    }

}