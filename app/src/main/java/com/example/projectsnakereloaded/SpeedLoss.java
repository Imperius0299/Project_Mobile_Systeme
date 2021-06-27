package com.example.projectsnakereloaded;

import processing.core.PImage;

public class SpeedLoss extends Item{

    private int speedLoss;

    public SpeedLoss(int x, int y, PImage image) {
        super(x, y, image);
        speedLoss = 3;
    }

    public int getSpeedLoss() {
        return speedLoss;
    }

    public void setSpeedLoss(int speedLoss) {
        this.speedLoss = speedLoss;
    }
}
