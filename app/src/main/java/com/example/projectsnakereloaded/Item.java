package com.example.projectsnakereloaded;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Item {

    private int x;
    private int y;

    public Item(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Todo: PImage einfügen
    public void show(PApplet pApplet) {
        pApplet.noStroke();
        pApplet.fill(255, 255, 0);
        pApplet.rect(x, y, 1, 1);
    }

    public PVector getPos() {
        return new PVector(x, y);
    }
}
