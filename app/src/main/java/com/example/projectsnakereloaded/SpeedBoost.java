package com.example.projectsnakereloaded;

import processing.core.PImage;

/**
 * Represents the speed boost item in the sketch.
 * @author Alexander Storbeck
 */
public class SpeedBoost extends Item{

    private double speedBoost;

    /**
     * Creates a speed boost item at a specific position.
     * @param x - The horizontal coordinate.
     * @param y - The vertical coordinate.
     * @param image The image that represents the item.
     */
    public SpeedBoost(int x, int y, PImage image) {
        super(x, y, image);
        speedBoost = -1;
    }
    /**
     * Gets the speed boost provided by the item for further acceleration.
     * @return An int representing the speed loss value.
     */
    public double getSpeedBoost() {
        return speedBoost;
    }

}
