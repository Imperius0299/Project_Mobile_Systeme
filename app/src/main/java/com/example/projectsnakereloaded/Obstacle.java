package com.example.projectsnakereloaded;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Obstacle {

    private int x;
    private int y;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void show(PApplet pApplet, PImage image) {
            pApplet.image(image, this.x, this.y, 1, 1);
    }

    public PVector getPos() {
        return new PVector(x, y);
    }
}
