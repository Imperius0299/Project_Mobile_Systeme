package com.example.projectsnakereloaded;

import processing.core.PImage;

/**
 * Represents the teleport item in the sketch.
 * @author Alexander Storbeck
 * @author Luca Jetter
 */
public class Teleport extends Item{

    /**
     * Creates a teleport item at a specific position.
     * @param x The horizontal coordinate.
     * @param y The vertical coordinate.
     * @param image The image that represents the item.
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
