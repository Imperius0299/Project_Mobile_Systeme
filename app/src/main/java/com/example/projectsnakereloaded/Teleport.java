package com.example.projectsnakereloaded;

import processing.core.PImage;

/**
 * Represents the Teleport Item in the Sketch.
 */
public class Teleport extends Item{

    /**
     * Creates a Teleport Item at a specific position.
     * @param x The horizontal coordinate.
     * @param y The vertical coordinate.
     * @param image The image that represents the Item.
     */
    public Teleport(int x, int y, PImage image) {
        super(x, y, image);
    }

    /**
     * Method for teleport empowering.
     * @return True for teleport empowering.
     */
    public boolean getTeleportEmpowered() {
        return true;
    }
}
