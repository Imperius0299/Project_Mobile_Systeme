package com.example.projectsnakereloaded;

public class Mushroom extends Item{

    private int speedBoost;

    public Mushroom(int x, int y) {
        super(x, y);
        speedBoost = 3;
    }

    public void setSpeedBoost(int speedBoost) {
        this.speedBoost = speedBoost;
    }

    public int getSpeedBoost() {
        return speedBoost;
    }
}
