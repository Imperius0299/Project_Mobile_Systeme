package com.example.projectsnakereloaded;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Represents the parent item for the specific items.
 * @author Alexander Storbeck
 */
public class Item {

    private int x;
    private int y;
    private PImage image;

    /**
     * Creates an item at a specific position. Parent class for specific items.
     * @param x - The horizontal coordinate.
     * @param y - The vertical coordinate.
     * @param image - The image that represents the item.
     */
    public Item(int x, int y, PImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }


    /**
     * Shows the item tile with the proposed image.
     * @param pApplet - The sketch where to show the image.
     */
    public void show(PApplet pApplet) {
        pApplet.image(image, x, y, 1, 1);
    }

    /**
     * Gets the position of the item.
     * @return A PVector holding the position coordinates.
     */
    public PVector getPos() {
        return new PVector(x, y);
    }

}
