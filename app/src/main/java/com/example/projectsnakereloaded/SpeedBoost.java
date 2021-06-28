package com.example.projectsnakereloaded;

import processing.core.PImage;

/**
 * Represents the Speed Boost Item in the Sketch.
 * @author Alexander Storbeck
 */
public class SpeedBoost extends Item{

    private double speedBoost;

    /**
     * Creates a Speed Boost Item at a specific position.
     * @param x The horizontal coordinate.
     * @param y The vertical coordinate.
     * @param image The image that represents the Item.
     */
    public SpeedBoost(int x, int y, PImage image) {
        super(x, y, image);
        speedBoost = -1;
    }
    /**
     * Get's the speed Boost provided by the item for further acceleration.
     * @return A int representing the Speed Loss value.
     */
    public double getSpeedBoost() {
        return speedBoost;
    }

    public void setSpeedBoost(double speedBoost) {
        this.speedBoost = speedBoost;
    }
}
