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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author rober
 */
public class KryoUtil {
 
    public static final int TCP_PORT = 55223;
    public static final int UDP_PORT = 55224;
    
    public static String HOST_IP = "localhost";
 
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
                kryo.register(ResourceInfoObject.class);
                kryo.register(PlayerInfoObject.class);
                kryo.register(SpendRubysObject.class);
                kryo.register(double.class);	
		kryo.register(int.class);
		kryo.register(String.class);
                kryo.register(ArrayList.class);
                kryo.register(ConcurrentHashMap.class);
    }

    public static String getHOST_IP() {
        return HOST_IP;
    }

    public static void setHOST_IP(String ip){
        HOST_IP = ip;
    }
    
    public static String getPublicIP() throws UnknownHostException, SocketException{
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            // drop inactive
            if (!networkInterface.isUp())
                continue;

            // smth we can explore
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while(addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                String ip = addr.getHostAddress();
                if(!ip.equals("127.0.0.1") && !ip.contains(":") && !addr.getHostName().contains("Loopback")) return ip;
            }
        }
        return null;
    }
}