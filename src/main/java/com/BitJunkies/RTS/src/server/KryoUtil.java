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
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author rober
 */
public class KryoUtil {
 
    public static final int TCP_PORT = 55223;
    public static final int UDP_PORT = 55224;
    
    public static final String HOST_IP = "10.23.21.96";
 
    public static void registerServerClasses(Server server) {
        register(server.getKryo());
    }
 
    public static void registerClientClass(Client client) {
        register(client.getKryo());        
    }
 
    private static void register(Kryo kryo) {
                kryo.register(ConnectionObject.class);
                kryo.register(DisconnectionObject.class);
                kryo.register(StartMatchObject.class);
                kryo.register(SpawnUnitObject.class);
                kryo.register(SpawnBuildingObject.class);
                kryo.register(MoveObject.class);
                kryo.register(MineObject.class);
                kryo.register(BuildObject.class);
                kryo.register(AttackObject.class);
                kryo.register(UnitInfoObject.class);
                kryo.register(BuildingInfoObject.class);
                kryo.register(PlayerInfoObject.class);
                kryo.register(SpendRubysObject.class);
                kryo.register(double.class);	
		kryo.register(int.class);
		kryo.register(String.class);
                kryo.register(ArrayList.class);
                kryo.register(ConcurrentHashMap.class);
    }

}