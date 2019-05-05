/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author brobz
 */
public class ResourceInfoObject {
    public ConcurrentHashMap<Integer, ArrayList<Double>> resourceInfo;
 
    public ResourceInfoObject() {
    } 
 
    public ResourceInfoObject(ConcurrentHashMap<Integer, ArrayList<Double>> resourceInfo) {
        this.resourceInfo = resourceInfo;
    }
}
