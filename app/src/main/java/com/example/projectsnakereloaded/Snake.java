package com.example.projectsnakereloaded;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Snake {

    private ArrayList<PVector> body;
    private PVector dir;
    private int len;

    public Snake(int width, int height) {
        body = new ArrayList();
        body.add(new PVector(width / 2 , height / 2));
        dir = new PVector(0,0);
        len = 0;
    }

    public void setDir(int x, int y) {
        dir.x = x;
        dir.y = y;
    }

    public void move(int speed) {
        PVector head = body.get(body.size()-1).copy();
        body.remove(0);
        body.trimToSize();
        head.x += dir.x;
        head.y += dir.y;
        body.add(head);
    }

    public void grow() {
        PVector head = body.get(body.size()-1).copy();
        len++;
        body.add(head);
    }

    public boolean eat(PVector posFood) {
        PVector head = body.get(body.size()-1).copy();

        if (head.x == posFood.x && head.y == posFood.y) {
            grow();
            return true;
        }
        return false;
    }

    public void show(PApplet pApplet, PImage image) {
        for (int i = 0; i< body.size(); i++) {
            pApplet.image(image, body.get(i).x, body.get(i).y, 1, 1);
        }
    }

    public PVector getHeadVect() {
        return body.get(body.size()-1);
    }

    public int getLen() {
        return len;
    }

    public ArrayList<PVector> getBody() {
        return body;
    }

    public PVector getDir() {
        return dir;
    }
}
