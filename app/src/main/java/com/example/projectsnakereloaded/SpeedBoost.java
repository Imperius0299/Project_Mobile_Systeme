package com.example.projectsnakereloaded;

import processing.core.PImage;

public class SpeedBoost extends Item{

    private int speedBoost;

    public SpeedBoost(int x, int y, PImage image) {
        super(x, y, image);
        speedBoost = 3;
    }

    public void setSpeedBoost(int speedBoost) {
        this.speedBoost = speedBoost;
    }

    public int getSpeedBoost() {
        return speedBoost;
    }
}
