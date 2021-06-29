package com.example.projectsnakereloaded;

import processing.core.PImage;

/**
 * Represents the speed loss item in the sketch.
 * @author Alexander Storbeck
 */
public class SpeedLoss extends Item{

    private double speedLoss;

    /**
     * Creates a speed loss item at a specific position.
     * @param x - The horizontal coordinate.
     * @param y - The vertical coordinate.
     * @param image - The image that represents the Item.
     */
    public SpeedLoss(int x, int y, PImage image) {
        super(x, y, image);
        speedLoss = 3;
    }

    /**
     * Gets the speed loss provided by the item for further slowing down.
     * @return An int representing the speed loss value.
     */
    public double getSpeedLoss() {
        return speedLoss;
    }

}
