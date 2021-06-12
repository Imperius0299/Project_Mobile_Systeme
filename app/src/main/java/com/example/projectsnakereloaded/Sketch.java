package com.example.projectsnakereloaded;

import processing.core.PApplet;

public class Sketch extends PApplet {

    @Override
    public void settings() {
        super.settings();
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void draw() {
        if (mousePressed) {
            ellipse(mouseX, mouseY, 50, 50);
        }
    }
}
