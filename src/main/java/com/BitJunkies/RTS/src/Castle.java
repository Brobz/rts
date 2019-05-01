/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src;

import mikera.vectorz.Vector2;

/**
 *
 * @author brobz
 */
public class Castle extends Building{
    public static final int CASTLE_WIDTH = 100, CASTLE_HEIGHT = 100;

    public Castle(Vector2 dimension, Vector2 position, int id) {
        super(dimension, position, id);
        this.maxHealth = 100;
        this.cost = 10;
        this.texture = Assets.casttleTexture;
    }
}
