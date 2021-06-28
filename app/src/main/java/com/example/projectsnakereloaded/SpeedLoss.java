package com.example.projectsnakereloaded;

import processing.core.PImage;

/**
 * Represents the Speed Loss Item in the Sketch.
 * @author Alexander Storbeck
 */
public class SpeedLoss extends Item{

    private double speedLoss;

    /**
     * Creates a Speed Loss Item at a specific position.
     * @param x The horizontal coordinate.
     * @param y The vertical coordinate.
     * @param image The image that represents the Item.
     */
    public SpeedLoss(int x, int y, PImage image) {
        super(x, y, image);
        speedLoss = 3;
    }

    /**
     * Get's the speed Loss provided by the item for further slowing down.
     * @return A int representing the Speed Loss value.
     */
    public double getSpeedLoss() {
        return speedLoss;
    }

    public void setSpeedLoss(double speedLoss) {
        this.speedLoss = speedLoss;
    }
}
