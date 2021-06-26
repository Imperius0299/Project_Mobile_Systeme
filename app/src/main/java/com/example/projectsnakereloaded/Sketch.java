package com.example.projectsnakereloaded;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.telecom.Call;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import java.io.IOException;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
//import processing.sound.Sound;
//import processing.sound.SoundFile;

public class Sketch extends PApplet {

    private boolean gameover = false;
    private Snake snake;
    private PVector food;

    private ArrayList<Obstacle> obstaclaList;
    private ArrayList<Item> itemList;

    private float rez;
    private int w;
    private int h;

    /*
    private int randomPosX;
    private int randomPosY;
    */

    private PVector pA;
    private PVector pB;
    private PVector pC;
    private PVector pD;
    private PVector pM;

    private int finalScore;

    interface Callback {
        void onEndedGameScore(int score);
    }

    private Callback callback = null;

    PImage snakeImage;
    PImage obstacleImage;
    PImage foodImage;
    PImage backgroundImage;
    PImage gameoverImage;
    PFont Font;

// https://stackoverflow.com/questions/18459122/play-sound-on-button-click-android

    @Override
    public void settings() {
        fullScreen();
    }



    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    //Sound sound;
    //SoundFile soundFile;

    @Override
    public void setup() {

        //soundFile = new SoundFile(this, "apfelsound_badum.mp3");



        // Quelle für png https://github.com/rembound/Snake-Game-HTML5
        //String url = "https://raw.githubusercontent.com/rembound/Snake-Game-HTML5/master/snake-graphics.png";
        obstacleImage = loadImage("snake_brick.png");
        foodImage = loadImage("snake_apple.png");
        snakeImage = loadImage("snake_block.png");
        backgroundImage = loadImage("background_image.png");
        gameoverImage = loadImage("gameover_background.png");

        System.out.println(dataPath(""));
        //foodImage = loadImage();
        //image(testImage, 0, 0, 320, 320, 0, 0 ,64 ,64);
        //createImage()

        rez = 60;
        w = floor(width / rez);
        h = floor(height / rez);
        frameRate(5);

        Font = createFont("The Impostor.ttf", rez/30);


        //Eckpunkt- und Mittelpunktkoordinaten des Displays
        pA = new PVector(0,0);
        pB = new PVector(w, 0);
        pC = new PVector(0, h);
        pD = new PVector(w, h);
        pM = new PVector(w/2, h/2);

        snake = new Snake(w , h);
        obstaclaList = new ArrayList<>();
        itemList = new ArrayList<>();
        createFood();

        //Zum Prüfen des Grids
        System.out.println(height * displayDensity + "llss" + width * displayDensity);
        System.out.println(snake.getHeadVect().x + "," + snake.getHeadVect().y);
        System.out.println(w + "," + h);


        PFont font = createFont("SansSerif", 24 * displayDensity);
        textFont(font);
        textAlign(CENTER, CENTER);
    }

    public void createFood() {
        food = new PVector((int) random(w), (int) random(h));
    }

    public void endGame() {
        PVector snakeHead = snake.getHeadVect();
        ArrayList<PVector> snakeBody = snake.getBody();
        if (snakeHead.x < 0 || snakeHead.x > w - 1 || snakeHead.y < 0 || snakeHead.y > h - 1){
            gameover = true;
        }
        for (Obstacle obstacle : obstaclaList) {
            if (snakeHead.x == obstacle.getPos().x && snakeHead.y == obstacle.getPos().y) {
                gameover = true;
            }
        }
        /*for (PVector bodyPart : snake.getBody()) {
            if (bodyPart.x == snakeHead.x && bodyPart.y == snakeHead.y && !bodyPart.equals(snakeHead)) {
                gameover = true;
            }
        }*/
        for (int i=0; i< snake.getBody().size() -1; i++){
            if (snakeHead.x == snakeBody.get(i).x && snakeHead.y == snakeBody.get(i).y){
                gameover = true;
            }
        }
    }

    private float calcCircumference(float a, float b, float c) {
        return (a + b + c) / 2;
    }

    private float[] getEdges(PVector v1, PVector v2, PVector v3) {
        float a = v1.dist(v2);
        float b = v2.dist(v3);
        float c = v1.dist(v3);

        float[] edges = {a, b, c};
        return edges;
    }

    // Berechne die Fläche eines beliebigen Dreiecks
    private float heronsFormula(float a, float b, float c) {
        return sqrt((a + b + c) * (-a + b + c) * (a - b + c) * (a + b - c)) / 4;
    }

    private float calcSquareTriangle(PVector p0, PVector p1, PVector p2) {
        float[] edges = getEdges(p0, p1, p2);
        float a = edges[0];
        float b = edges[1];
        float c = edges[2];

        return heronsFormula(a, b, c);
    }

    private float sumSquareTri(float a, float b, float c) {
        return round(a + b + c);
    }

    private float calculatePartialTriangleTotalSquare(PVector p1, PVector p2, PVector p3, PVector pMouse) {

        float squareA = calcSquareTriangle(p1, p2, pMouse);
        float squareB = calcSquareTriangle(p1, p3, pMouse);
        float squareC = calcSquareTriangle(p2, p3, pMouse);

        return round(squareA + squareB + squareC);
    }

    @Override
    public void mousePressed() {
        PVector snakeHead = snake.getHeadVect();
        PVector dir = snake.getDir();

        float mouseXRez =  (mouseX / rez);
        float mouseYRez =  (mouseY / rez);

        PVector pMouse = new PVector(mouseXRez, mouseYRez);


        //Triangle 1 full Top
        float aFull = round(calcSquareTriangle(pA, pB, pM));
        /*
        float a11 = calcSquareTriangle(pA, pM, pMouse);
        float a12 = calcSquareTriangle(pM, pB, pMouse);
        float a13 = calcSquareTriangle(pA, pB, pMouse);

        float a1MouseSum = sumSquareTri(a11, a12, a13);
         */
        float a1Sum = calculatePartialTriangleTotalSquare(pA, pB, pM, pMouse);

        float a2Sum = calculatePartialTriangleTotalSquare(pA, pC, pM, pMouse);

        float a3Sum = calculatePartialTriangleTotalSquare(pB, pD, pM, pMouse);

        float a4Sum = calculatePartialTriangleTotalSquare(pC, pD, pM, pMouse);


        //float a31 = calcSquareTriangle()

        //Triangle 2 full Left
        //float a2Full = round(calcSquareTriangle(pA, pC, pM));

        /*
        float a21 = calcSquareTriangle(pA, pM, pMouse);
        float a22 = calcSquareTriangle(pM, pC, pMouse);
        float a23 = calcSquareTriangle(pA, pC, pMouse);

        float a2MouseSum = sumSquareTri(a21, a22, a23);
        */
        // Right

        //float a3Full = round(calcSquareTriangle(pB, pD, pM));

        /*
        float a31 = calcSquareTriangle(pB, pM, pMouse);
        float a32 = calcSquareTriangle(pM, pD, pMouse);
        float a33 = calcSquareTriangle(pB, pD, pMouse);

        float a3MouseSum = sumSquareTri(a31, a32, a33);
        */
        // Bottom

        //float a4Full = round(calcSquareTriangle(pC, pD, pM));
        /*
        float a41 = calcSquareTriangle(pC, pM, pMouse);
        float a42 = calcSquareTriangle(pM, pD, pMouse);
        float a43 = calcSquareTriangle(pC, pD, pMouse);

        float a4MouseSum = sumSquareTri(a41, a42, a43);
        */
        if (aFull == a1Sum){
            snake.setDir(0, -1);
        } else if (aFull == a2Sum) {
            snake.setDir(-1, 0);
        } else if (aFull == a3Sum) {
            snake.setDir(1, 0);
        } else if (aFull == a4Sum) {
            snake.setDir(0, 1);
        }

        System.out.println(aFull + ":" + a1Sum);
        System.out.println(aFull + ":" + a2Sum);




    }

    @Override
    public void touchStarted() {
        super.touchStarted();
    }

    @Override
    public void touchMoved() {
        super.touchMoved();
    }

    @Override
    public void touchEnded() {
        super.touchEnded();
    }

    @Override
    public void keyPressed() {
        if (key == CODED) {
            switch (keyCode) {
                case UP:
                    if (snake.getBody().size() > 1 && snake.getDir().y == 1){
                        break;
                    }
                    snake.setDir(0, -1);
                    break;
                case DOWN:
                    if (snake.getBody().size() > 1 && snake.getDir().y == -1){
                        break;
                    }
                    snake.setDir(0, 1);
                    break;
                case LEFT:
                    if (snake.getBody().size() > 1 && snake.getDir().x == 1){
                        break;
                    }
                    snake.setDir(-1, 0);
                    break;
                case RIGHT:
                    if (snake.getBody().size() > 1 && snake.getDir().x == -1){
                        break;
                    }
                    snake.setDir(1, 0);
                    break;
            }
        }
    }

    @Override
    public Activity getActivity() {
        return super.getActivity();
    }

    @Override
    public void draw() {
        background(197, 167, 225);


        backgroundImage.resize(width, height);
        image(backgroundImage, 0, 0);
        textAlign(CENTER, CENTER);
        textFont(Font);
        float tSizeHelper;
        tSizeHelper = (min(displayHeight, displayWidth)/25);
        textSize(tSizeHelper);

        float scoreCoordinateX = (float) (width/7.5);
        float scoreCoordinateY = height/60;

        fill(0,0,0);
        text("Score: "+snake.getLen(), (float) (scoreCoordinateX+Math.cbrt(scoreCoordinateX/2)), scoreCoordinateY);
        text("Score: "+snake.getLen(), (float) (scoreCoordinateX-Math.cbrt(scoreCoordinateX/2)), scoreCoordinateY);
        text("Score: "+snake.getLen(), scoreCoordinateX, (float) (scoreCoordinateY+Math.cbrt(scoreCoordinateX/2)));
        text("Score: "+snake.getLen(), scoreCoordinateX, (float) (scoreCoordinateY-Math.cbrt(scoreCoordinateX/2)));

        fill(255,255,255);
        text("Score: "+snake.getLen(), scoreCoordinateX, scoreCoordinateY);

        scale(rez);
        //rect(width/2,height/2,20 * displayDensity,20 * displayDensity);
        //text("Hello", width/2, height/2);
       /* fill(197, 167, 225);
        rect(0, 0, 1077, 250);
        rect(0, 250, 250, 1200);
        rect(830, 250, 250, 1200);
        rect(0, 1570, 1077, 250); */
        PVector pV1 = new PVector(0,0);
        PVector pV2 = new PVector(w, h);
        PVector pV3 = new PVector(0, h);
        PVector pV4 = new PVector(w, 0);

        pV1.dist(pV2);
        //System.out.println(pV1.dist(pV2));

        /*
        fill(255);
        line(0,0, w, h);
        line(w, 0, 0, h);

         */







        //createFood();
        if (!gameover) {

            if (snake.eat(food)) {
                createFood();
                //soundFile.play();
            }

            if (snake.collectItem(itemList)) {
                //Todo: Implement Feature Sound
            }
            snake.move((int) rez);
            snake.show(this, snakeImage);


            if ((snake.getDir().x != 0 || snake.getDir().y != 0)){
                if (frameCount % 30 == 0) {
                    int randomPosX = (int) random(w);
                    int randomPosY = (int) random(h);
                    obstaclaList.add(new Obstacle(randomPosX, randomPosY));
                }
                if (frameCount % 60 == 0) {
                    int randomPosX = (int) random(w);
                    int randomPosy = (int) random(h);
                    itemList.add(new Mushroom(randomPosX, randomPosy));
                }
            }

            for (Obstacle obstacle : obstaclaList) {
                obstacle.show(this, obstacleImage);

            }

            for (Item item : itemList) {
                item.show(this);
            }

            image(foodImage,food.x, food.y, 1, 1);

            endGame();
        }else {
            //TODO: Loop unterbrechen, damit nicht immer abgeschickt
            //looping = !looping;
            finalScore = snake.getLen();
            callback.onEndedGameScore(finalScore);
            //((GameActivity)getActivity()).testMethod();

            scale(1/rez);
            gameoverImage.resize(width, height);
            image(gameoverImage, 0, 0);
            String gameovermessage = "Your score: "+finalScore+".";
            String restartgame = "Tap to restart the game";
            float minSizeW = textWidth(gameovermessage) ;
            float minSizeH = (textDescent()+textAscent()) ;
            float minSize = min(minSizeW, minSizeH);
            textSize((float) (minSize*0.7));
            fill(0,0,0);
            text(gameovermessage, (float) ((width/2)*1.005), (float) (((height/2)-2*minSize)*1.005));
            fill(255,255,255);
            text(gameovermessage,width/2, (float) ((height/2)-2*minSize));
            textSize((float) (minSize*0.5));
            fill(0,0,0);
            text(restartgame, (float) ((width/2)*1.004), (float) ((height/2)*1.004));
            fill(255,255,255);
            text(restartgame,width/2, (float) ((height/2)));



            if (mousePressed) {
                gameover = false;
                //looping = !looping;
                setup();
            }
        }


        if (mousePressed) {
            if (mouseY <= 250 || keyCode == UP) {
                if(snake.getLen() == 1) {
                    //System.out.println("test");
                    //snake.setDir(0, -1);
                }
                else{
                    //System.out.println("test");
                    snake.setDir(0, -1);
                }
            }
            else if ((mouseY > 250 && mouseY <= 1620 && mouseX <= 250) || keyCode == LEFT) {
                if(snake.getLen() == 1) {
                    //direction = 3; //LEFT
                }
                else{
                    snake.setDir(-1, 0);
                }
            }
            else if ((mouseY > 250 && mouseY <= 1200 && mouseX >= 830) || keyCode == RIGHT) {
                if(snake.getLen() == 1) {
                    //direction = 2; //Right
                }
                else{
                    snake.setDir(1, 0);
                }
            }
            else if ((mouseY >= 1200) || keyCode == DOWN) {
                if(snake.getLen() == 1) {
                    //direction = 0; //Down
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