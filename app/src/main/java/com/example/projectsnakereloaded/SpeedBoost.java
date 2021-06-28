package com.example.projectsnakereloaded;

import processing.core.PImage;

public class SpeedBoost extends Item{

    private double speedBoost;

    public SpeedBoost(int x, int y, PImage image) {
        super(x, y, image);
        speedBoost = -1;
    }

    public double getSpeedBoost() {
        return speedBoost;
    }

    public void setSpeedBoost(double speedBoost) {
        this.speedBoost = speedBoost;
    }
}
