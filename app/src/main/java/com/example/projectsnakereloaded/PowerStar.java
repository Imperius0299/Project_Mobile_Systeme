package com.example.projectsnakereloaded;

import processing.core.PApplet;
import processing.core.PImage;

public class PowerStar extends Item{

    /**
     * Creates a Power Star at a specific position. Parent Class for Specific Items.
     * @param x The horizontal coordinate.
     * @param y The vertical coordinate.
     * @param image The image that represents the Item.
     */
    public PowerStar(int x, int y, PImage image) {
        super(x, y, image);
    }

    /**
     * Method for Star empowering.
     * @return True for Star empowering.
     */
    public boolean getEmpowered() {
        return true;
    }
}
