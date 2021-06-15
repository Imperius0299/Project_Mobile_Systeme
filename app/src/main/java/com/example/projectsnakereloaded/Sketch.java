package com.example.projectsnakereloaded;

import android.util.DisplayMetrics;
import android.view.MotionEvent;

import androidx.core.view.GestureDetectorCompat;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class Sketch extends PApplet {

    ArrayList<Integer> x = new ArrayList<Integer>(), y = new ArrayList<Integer>();
    int w = 30, h = 45, blocks = 35, direction = 2, foodx = 15, foody = 15, speed = 8, fc1 = 255, fc2 = 255, fc3 = 255;
    int[] x_direction = {0, 0, 1, -1}, y_direction = {1, -1, 0, 0}; //direction for x and y
    boolean gameover = false;
    Snake snake;
    PVector food;
    float rez;
    int size;


    @Override
    public void settings() {

        fullScreen();
    }

    @Override
    public void setup() {
        //super.setup();
        //x.add(0); //snake start position
        //y.add(15);
        rez = 20;
        size = 35;
        frameRate(5);
        snake = new Snake(floor(width/rez) , floor(height/rez), this);
        createFood();
        System.out.println(height * displayDensity + "llss" + width * displayDensity);


        PFont font = createFont("SansSerif", 24 * displayDensity);
        textFont(font);
        textAlign(CENTER, CENTER);
    }

    public void createFood() {
        food = new PVector((int) random(width/rez), (int) random(height/rez));
    }

    public void endGame() {
        if (snake.getHeadVect().x < 0 || snake.getHeadVect().x + size > width ||snake.getHeadVect().y < 0 || snake.getHeadVect().y + size > height){
            gameover = true;
        }
    }

    @Override
    public void draw() {
        background(197, 167, 225);
        scale(rez);
        //rect(width/2,height/2,20 * displayDensity,20 * displayDensity);
        //text("Hello", width/2, height/2);
        fill(197, 167, 225);
        rect(0, 0, 1077, 250);
        rect(0, 250, 250, 1200);
        rect(830, 250, 250, 1200);
        rect(0, 1570, 1077, 250);


        //createFood();
        if (!gameover) {
            snake.move((int) rez);
            snake.show();



            if (snake.eat(food)) {
                createFood();
            }

            fill(255, 0, 0);
            rect(food.x, food.y, 1, 1);

            endGame();
        }else {
            fill(200, 200, 0);
            textSize(24 * displayDensity);
            textAlign(CENTER, CENTER);
            text("GAME OVER! \n Your Score is: " + snake.getLen() + ". \n Click to restart!", width / 2, height / 3);
            if (mousePressed) {
                gameover = false;
            }
        }


        if (mousePressed) {
            if (mouseY <= 250) {
                if(snake.getLen() == 1) {
                    //System.out.println("test");
                    snake.setDir(0, -1);
                }
                else{
                    //System.out.println("test");
                    snake.setDir(0, -1);
                }
            }
            else if (mouseY > 250 && mouseY <= 1620 && mouseX <= 250) {
                if(snake.getLen() == 1) {
                    direction = 3; //LEFT
                }
                else{
                    snake.setDir(-1, 0);
                }
            }
            else if (mouseY > 250 && mouseY <= 1200 && mouseX >= 830) {
                if(snake.getLen() == 1) {
                    direction = 2; //LEFT
                }
                else{
                    snake.setDir(1, 0);
                }
            }
            else if (mouseY >= 1200) {
                if(snake.getLen() == 1) {
                    direction = 0; //LEFT
                }
                else{
                    snake.setDir(0, 1);
                }
            }
        }

      /*  fill(0, 255, 0); //snake color green
        for (int i = 0; i < x.size(); i++)
            rect(x.get(i) * blocks, y.get(i) * blocks, blocks, blocks); //snake
        if (!gameover) {
            fill(fc1, fc2, fc3); //food color red
            ellipse(foodx * blocks + 10, foody * blocks + 10, blocks, blocks); //food
            textAlign(LEFT); //score
            textSize(45);
            fill(255);
            text("Score: " + x.size(), 10, 10, width - 20, 50);
            if (frameCount % speed == 0) {
                x.add(0, x.get(0) + x_direction[direction]); //make snake longer
                y.add(0, y.get(0) + y_direction[direction]);
                if (x.get(0) < 0 || y.get(0) < 0 || x.get(0) >= w || y.get(0) >= h) gameover = true;
                for (int i = 1; i < x.size(); i++)
                    if (x.get(0) == x.get(i) && y.get(0) == y.get(i)) gameover = true;
                if (x.get(0) == foodx && y.get(0) == foody) { //new food if we touch
                    if (x.size() % 5 == 0 && speed >= 2)
                        speed -= 1;  // every 5 points speed increase
                    foodx = (int) random(0, w); //new food
                    foody = (int) random(0, h);
                    fc1 = (int) random(255);
                    fc2 = (int) random(255);
                    fc3 = (int) random(255); //new food color
                } else {
                    x.remove(x.size() - 1);
                    y.remove(y.size() - 1);
                }
            }
        } else {
            fill(200, 200, 0);
            textSize(displayWidth / 15);
            textAlign(CENTER);
            text("GAME OVER! \n Your Score is: " + x.size() + ". \n Click to restart!", width / 2, height / 3);
            if (mousePressed) {
                x.clear();
                y.clear();
                x.add(0);
                y.add(15);
                direction = 2;
                speed = 8;
                gameover = false;
            }
        }


        if (mousePressed) {
            if (mouseY <= 250) {
                if(x.size() == 1) {
                    direction = 1;
                }
                else if (direction != 0) {
                    direction = 1; //UP
                }
            }
            else if (mouseY > 250 && mouseY <= 1620 && mouseX <= 250) {
                if(x.size() == 1) {
                    direction = 3; //LEFT
                }
                else if (direction != 2) {
                    direction = 3; //LEFT
                }
            }
            else if (mouseY > 250 && mouseY <= 1620 && mouseX >= 830) {
                if(x.size() == 1) {
                    direction = 2; //LEFT
                }
                else if (direction != 3) {
                    direction = 2; //LEFT
                }
            }
            else if (mouseY >= 1620) {
                if(x.size() == 1) {
                    direction = 0; //LEFT
                }
                else if (direction != 1) {
                    direction = 0; //LEFT
                }
            }
        } */
    }
}