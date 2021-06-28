package com.example.projectsnakereloaded;

import processing.core.PImage;

public class Teleport extends Item{

    public Teleport(int x, int y, PImage image) {
        super(x, y, image);
    }

    public boolean getTeleportEmpowered() {
        return true;
    }
}
