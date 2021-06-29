package com.example.projectsnakereloaded;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/** Represents the snake in the sketch.
 * @author Alexander Storbeck
 * @author Luca Jetter
 */
public class Snake {

    private ArrayList<PVector> body;
    private PVector dir;

    private int w;
    private int h;

    private int len;
    private int fieldsMoved;
    private int itemsCollected;
    private double speedDifference;

    private boolean isEmpowered;
    private boolean isTeleportEmpowered;
    private boolean isSpeedAffected;


    /** Creates a snake at a specific position. This position is the middle of the sketch.
     *
     * @param width - The width of the sketch.
     * @param height - The height of the sketch.
     */
    public Snake(int width, int height) {
        body = new ArrayList();
        this.w = width;
        this.h = height;
        body.add(new PVector(width / 2 , height / 2));
        dir = new PVector(0,0);
        len = 0;
        isEmpowered = false;
        isSpeedAffected = false;
    }

    /** Sets the Direction of the Snake.
     *
     * @param x - The horizontal direction in the coordinate system.
     * @param y - The vertical direction in the coordinate system.
     */
    public void setDir(int x, int y) {
        dir.x = x;
        dir.y = y;
    }

    /**
     * Moves the different body parts of the snake, depending on the direction.
     */
    public void move() {
        PVector head = body.get(body.size()-1).copy();
        body.remove(0);
        body.trimToSize();
        head.x += dir.x;
        head.y += dir.y;
        body.add(head);
        if (dir.x != 0 || dir.y != 0) {
            fieldsMoved++;
        }
    }

    /**
     * Adds a new body part to the snake.
     */
    public void grow() {
        PVector head = body.get(body.size()-1).copy();
        len++;
        body.add(head);
    }

    /**
     * Detection if the snakes head is on the same position as an food tile.
     * @param posFood - the position coordinates of the food tile.
     * @return The boolean if the snake collide with a food element or not.
     */
    public boolean eat(PVector posFood) {
        PVector head = body.get(body.size()-1).copy();

        if (head.x == posFood.x && head.y == posFood.y) {
            grow();
            return true;
        }
        return false;
    }

    /**
     * Detection if the snakes head is on the same position as an item tile.
     * @param itemList - The actual list of items
     * @return A boolean if the snakes collide with an item
     */
    public boolean collectItem(ArrayList<Item> itemList) {
        PVector head = body.get(body.size() - 1).copy();

        for (Item item : itemList) {
            if (head.x == item.getPos().x && head.y == item.getPos().y) {
                setItemPower(item);
                itemList.remove(item);
                itemsCollected++;
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the power of the collected item.
     * @param item - The item that the snake collected.
     */
    public void setItemPower(Item item) {
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
        if (item.getClass() == Teleport.class) {
            isTeleportEmpowered = ((Teleport) item).getTeleportEmpowered();
        }
    }

    /**
     * Teleports the snake to the opposite position, when hitting the edges.
     */
    public void teleport() {
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

    /**
     * Resets the item power state of the snake.
     */
    public void resetItemPower() {
        if (isSpeedAffected) {
            isSpeedAffected = false;
        }
        if (isEmpowered) {
            isEmpowered = false;
        }
        if (isTeleportEmpowered) {
            isTeleportEmpowered = false;
        }
    }

    /**
     * Resets the speed difference of the snake.
     */
    public void resetSpeedDifference() {
        speedDifference = 0;
    }

    /**
     * Shows the snake's body tiles with the proposed image.
     * @param pApplet The sketch where to show the image.
     * @param image The image for the snake.
     */
    public void show(PApplet pApplet, PImage image) {
        for (int i = 0; i< body.size(); i++) {
            pApplet.image(image, body.get(i).x, body.get(i).y, 1, 1);
        }
    }

    /**
     * Gets the head position of the snake.
     * @return A PVector with the coordinates of the head.
     */
    public PVector getHeadVect() {
        return body.get(body.size()-1);
    }

    /**
     * Gets the length of the snake.
     * @return An int representing the length.
     */
    public int getLen() {
        return len;
    }

    /**
     * Gets the body of the snake.
     * @return An ArrayList with the body elements of the snake.
     */
    public ArrayList<PVector> getBody() {
        return body;
    }

    /**
     * Gets the actual direction of the snake.
     * @return A PVector holding the direction coordinates.
     */
    public PVector getDir() {
        return dir;
    }

    /**
     * Gets the empowered state of the snake.
     * @return A boolean if the snake is empowered.
     */
    public boolean getEmpoweredState() {
        return isEmpowered;
    }

    /**
     * Gets the teleported empowered state.
     * @return A boolean if the snake is teleported empowered.
     */
    public boolean getTeleportedEmpoweredState() {
        return isTeleportEmpowered;
    }

    /**
     * Gets the speed difference of the snake.
     * @return A int representing the actual speed difference.
     */
    public double getSpeedDifference() {
        return speedDifference;
    }

    public int getFieldsMoved() {
        return fieldsMoved;
    }

    public int getItemsCollected() {
        return itemsCollected;
    }

}
