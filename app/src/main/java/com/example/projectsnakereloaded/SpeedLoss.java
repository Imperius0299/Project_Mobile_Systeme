package com.example.projectsnakereloaded;

import processing.core.PImage;

public class SpeedLoss extends Item{

    private double speedLoss;

    public SpeedLoss(int x, int y, PImage image) {
        super(x, y, image);
        speedLoss = 3;
    }

    public double getSpeedLoss() {
        return speedLoss;
    }

    public void setSpeedLoss(double speedLoss) {
        this.speedLoss = speedLoss;
    }
}
