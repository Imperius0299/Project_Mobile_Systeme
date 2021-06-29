package com.example.projectsnakereloaded;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Represents the Obstacle in the Sketch.
 * @author Alexander Storbeck
 */
public class Obstacle {

    private int x;
    private int y;
    PImage image;

    /**
     * Creates an obstacle at a specific position.
     * @param x - The horizontal coordinate.
     * @param y - The vertical coordinate.
     * @param image - The image that represents the obstacle.
     */
    public Obstacle(int x, int y, PImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    /**
     * Shows the obstacle tile with the proposed image.
     * @param pApplet - The sketch where to show the image.
     */
    public void show(PApplet pApplet) {
            pApplet.image(image, this.x, this.y, 1, 1);
    }

    /**
     * Gets the position of the obstacle.
     * @return A PVector holding the position coordinates.
     */
    public PVector getPos() {
        return new PVector(x, y);
    }
}
