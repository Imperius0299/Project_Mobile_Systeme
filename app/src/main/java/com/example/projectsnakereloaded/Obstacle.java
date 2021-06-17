package com.example.projectsnakereloaded;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Obstacle {

    private int x;
    private int y;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void show(PApplet pApplet) {

            pApplet.fill(139,69,19);
            pApplet.noStroke();
            pApplet.rect(this.x, this.y, 1, 1);

    }

    public PVector getPos() {
        return new PVector(x, y);
    }
}
