package com.example.projectsnakereloaded;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Item {

    private int x;
    private int y;
    private PImage image;

    public Item(int x, int y, PImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    //Todo: PImage einfügen
    public void show(PApplet pApplet) {
        pApplet.image(image, x, y, 1, 1);
    }

    public PVector getPos() {
        return new PVector(x, y);
    }
}
