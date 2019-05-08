/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ResourceInfoClass to use information of the resources in the network context
 * @author brobz
 */
public class ResourceInfoObject {
    public ConcurrentHashMap<Integer, ArrayList<Double>> resourceInfo;
    /**
     * Empty Constructor
     */
    public ResourceInfoObject() {
    } 
 
    /**
     * Constructor for resource Info
     * @param resourceInfo ArrayList for the resource information itself
     */
    public ResourceInfoObject(ConcurrentHashMap<Integer, ArrayList<Double>> resourceInfo) {
        this.resourceInfo = resourceInfo;
    }
}
