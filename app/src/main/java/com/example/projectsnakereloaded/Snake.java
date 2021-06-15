package com.example.projectsnakereloaded;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Snake extends PApplet {

    private ArrayList<PVector> body;
    private int xDir;
    private int yDir;
    private int len;
    private PApplet pApplet;

    public Snake(int width, int height, PApplet pApplet) {
        body = new ArrayList();
        body.add(new PVector(floor(width/2) , floor(height/2)));
        xDir = 0;
        yDir = 0;
        len = 0;
        this.pApplet = pApplet;
    }

    public void setDir(int x, int y) {
        xDir = x;
        yDir = y;
    }

    public void move(int speed) {
        PVector head = body.get(body.size()-1).copy();
        body.remove(0);
        head.x += xDir;
        head.y += yDir;
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

    public void show() {
        for (int i = 0; i< body.size(); i++) {
            pApplet.fill(0, 255, 0);
           // noStroke();
            pApplet.rect(body.get(i).x, body.get(i).y, 1, 1);
        }
    }

    public PVector getHeadVect() {
        return body.get(body.size()-1);
    }

    public int getLen() {
        return len;
    }
}
