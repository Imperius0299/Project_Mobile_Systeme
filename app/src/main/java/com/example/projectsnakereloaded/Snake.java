package com.example.projectsnakereloaded;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Snake {

    private ArrayList<PVector> body;
    private PVector dir;
    private int len;
    private boolean isEmpowered;
    private double speedDifference;
    private boolean isSpeedAffected;


    public Snake(int width, int height) {
        body = new ArrayList();
        body.add(new PVector(width / 2 , height / 2));
        dir = new PVector(0,0);
        len = 0;
        isEmpowered = false;
        isSpeedAffected = false;
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

    public boolean collectItem(ArrayList<Item> itemList) {
        PVector head = body.get(body.size() - 1).copy();

        for (Item item : itemList) {
            if (head.x == item.getPos().x && head.y == item.getPos().y) {
                getItemPower(item);
                itemList.remove(item);
                return true;
            }
        }
        return false;
    }
    public void getItemPower(Item item) {
        if (item.getClass() == SpeedBoost.class && !isSpeedAffected) {
            speedDifference = ((SpeedBoost) item).getSpeedBoost();
            isSpeedAffected = true;
        }
        if (item.getClass() == SpeedLoss.class && !isSpeedAffected) {
            speedDifference = ((SpeedLoss) item).getSpeedLoss();
            isSpeedAffected = true;
        }
        if (item.getClass() == PowerStar.class) {
            isEmpowered = ((PowerStar) item).getEmpowered();
        }
    }
    //Todo: teleport Symbol
    public void teleport(int w, int h) {
        PVector head = body.get(body.size() - 1);

        if (head.x < 0){
            head.x = w;
            return;
        }
        if (head.x > w - 1) {
            head.x = 0;
            return;
        }
        if (head.y < 0) {
            head.y = h;
            return;
        }
        if (head.y > h - 1) {
            head.y = 0;
            return;
        }
    }

    public void resetItemPower() {
        if (isSpeedAffected) {
            isSpeedAffected = false;
        }
        if (isEmpowered) {
            isEmpowered = false;
        }
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

    public boolean getEmpoweredState() {
        return isEmpowered;
    }

    public double getSpeedDifference() {
        return speedDifference;
    }

    public void resetSpeedDifference() {
        speedDifference = 0;
    }
}
