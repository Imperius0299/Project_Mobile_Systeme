package com.example.projectsnakereloaded;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;


import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.sound.SoundFile;


public class Sketch extends PApplet {

    private boolean gameover = false;
    private Snake snake;
    private PVector food;

    private ArrayList<Obstacle> obstaclaList;
    private ArrayList<Item> itemList;

    private int width;
    private int height;

    private float rez;
    private int w;
    private int h;

    private float framecountDivider;
    private int itemActiveFrameTime;
    private int itemActiveFrameCount;

    private static final String TAG = "Snake";

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
    PImage itemSpeedBoostImage;
    PImage itemSpeedLossImage;
    PImage itemPowerStarImage;
    PFont Font;

    //Sound
    MediaPlayer mp;

// https://stackoverflow.com/questions/18459122/play-sound-on-button-click-android

    public Sketch(int width, int height) {
        this.width = width;
        this.height = height;
    }
    @Override
    public void settings() {
        fullScreen();
        size(width, height);
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
        snakeImage = loadImage("snake_block_default.png");
        backgroundImage = loadImage("background_image_stone.png");
        gameoverImage = loadImage("gameover_background.png");
        itemSpeedBoostImage = loadImage("item_speedup.png");
        itemSpeedLossImage = loadImage("item_speeddown.png");
        itemPowerStarImage = loadImage("item_stern.png");


        System.out.println(dataPath(""));
        //foodImage = loadImage();
        //image(testImage, 0, 0, 320, 320, 0, 0 ,64 ,64);
        //createImage()

        rez = 60;
        w = floor(width / rez);
        h = floor(height / rez);
        frameRate(10);

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

        framecountDivider = 2;
        itemActiveFrameTime = 20;
        itemActiveFrameCount = 0;
        createFood();

        //Zum Prüfen des Grids
        System.out.println(displayHeight * pixelDensity + "llss" + displayWidth * pixelDensity);
        System.out.println(snake.getHeadVect().x + "," + snake.getHeadVect().y);
        System.out.println(w + "," + h);


        PFont font = createFont("SansSerif", 24 * displayDensity);
        textFont(font);
        textAlign(CENTER, CENTER);

        //Sound
        //mp = new MediaPlayer();

    }

    public void playSound(int rawId) {
        mp = MediaPlayer.create((MainActivity)getActivity(), R.raw.apfelsound_badum);
        mp.start();

    }

    public void createFood() {
        food = new PVector((int) random(w), (int) random(h));
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



        float a1Full = round(calcSquareTriangle(pA, pB, pM));
        float a2Full = round(calcSquareTriangle(pA, pC, pM));
        float a3Full = round(calcSquareTriangle(pB, pD, pM));
        float a4Full = round(calcSquareTriangle(pC, pD, pM));

        float a1Sum = calculatePartialTriangleTotalSquare(pA, pB, pM, pMouse);

        float a2Sum = calculatePartialTriangleTotalSquare(pA, pC, pM, pMouse);

        float a3Sum = calculatePartialTriangleTotalSquare(pB, pD, pM, pMouse);

        float a4Sum = calculatePartialTriangleTotalSquare(pC, pD, pM, pMouse);


        if (a1Full == a1Sum){
            if (snake.getBody().size() > 1 && snake.getDir().y == 1) {
                return;
            }
            snake.setDir(0, -1);
        } else if (a2Full == a2Sum) {
            if (snake.getBody().size() > 1 && snake.getDir().x == 1) {
                return;
            }
            snake.setDir(-1, 0);
        } else if (a3Full == a3Sum) {
            if (snake.getBody().size() > 1 && snake.getDir().x == -1) {
                return;
            }
            snake.setDir(1, 0);
        } else if (a4Full == a4Sum) {
            if (snake.getBody().size() > 1 && snake.getDir().y == -1) {
                return;
            }
            snake.setDir(0, 1);
        }

        Log.d(TAG, a1Full + ":" + a1Sum);
        Log.d(TAG, a2Full + ":" + a2Sum);
        Log.d(TAG, a3Full + ":" + a3Sum);
        Log.d(TAG, a4Full + ":" + a4Sum);

        if (gameover) {
            loop();
            gameover = false;
            setup();
        }
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
        if (key == CODED && (frameCount % 1 == 0)) {
            switch (keyCode) {
                    case UP:
                        if (snake.getBody().size() > 1 && snake.getDir().y == 1) {
                            break;
                        }
                        snake.setDir(0, -1);
                        break;
                    case DOWN:
                        if (snake.getBody().size() > 1 && snake.getDir().y == -1) {
                            break;
                        }
                        snake.setDir(0, 1);
                        break;
                    case LEFT:
                        if (snake.getBody().size() > 1 && snake.getDir().x == 1) {
                            break;
                        }
                        snake.setDir(-1, 0);
                        break;
                    case RIGHT:
                        if (snake.getBody().size() > 1 && snake.getDir().x == -1) {
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

        float scoreCoordinateX = width/2;
        float scoreCoordinateY = height/60;

        fill(0,0,0);
        text("Score: "+snake.getLen(), (float) (scoreCoordinateX+Math.cbrt(scoreCoordinateX/2)), scoreCoordinateY);
        text("Score: "+snake.getLen(), (float) (scoreCoordinateX-Math.cbrt(scoreCoordinateX/2)), scoreCoordinateY);
        text("Score: "+snake.getLen(), scoreCoordinateX, (float) (scoreCoordinateY+Math.cbrt(scoreCoordinateX/2)));
        text("Score: "+snake.getLen(), scoreCoordinateX, (float) (scoreCoordinateY-Math.cbrt(scoreCoordinateX/2)));

        fill(255,255,255);
        text("Score: "+snake.getLen(), scoreCoordinateX, scoreCoordinateY);

        scale(rez);

        //Todo: Testen wegen slow Down Speed Up
        //createFood();
        if (!gameover) {

            if (frameCount % framecountDivider == 0) {
                if (snake.eat(food)) {
                    createFood();
                    playSound(R.raw.apfelsound_badum);
                    //soundFile.play();
                }


                if (snake.collectItem(itemList)) {
                    //Todo: Implement Feature Sound
                    snakeImage = loadImage("snake_block_item.png");
                    itemActiveFrameCount = frameCount + itemActiveFrameTime;
                    framecountDivider += snake.getSpeedDifference();
                    snake.resetSpeedDifference();
                }
                snake.move((int) rez);
            }

            if (frameCount == itemActiveFrameCount){
                framecountDivider = 2;
                snake.resetItemPower();
                snakeImage = loadImage("snake_block_default.png");
            }


            snake.show(this, snakeImage);



            if (frameCount % 2 == 0) {
                if ((snake.getDir().x != 0 || snake.getDir().y != 0)){
                    if (frameCount % 30 == 0) {
                        int randomPosX = (int) random(w);
                        int randomPosY = (int) random(h);
                        obstaclaList.add(new Obstacle(randomPosX, randomPosY));
                    }
                    if (frameCount % 60 == 0) {
                        int randomItem = (int) random(0, 3);

                        int randomPosX = (int) random(w);
                        int randomPosY = (int) random(h);

                        switch (randomItem) {
                            case 0:
                                itemList.add(new SpeedBoost(randomPosX, randomPosY, itemSpeedBoostImage));
                                break;
                            case 1:
                                itemList.add(new SpeedLoss(randomPosX, randomPosY, itemSpeedLossImage));
                                break;
                            case 2:
                                itemList.add(new PowerStar(randomPosX, randomPosY, itemPowerStarImage));
                                break;
                        }
                    }
                    if (frameCount % 90 == 0) {
                        int randomPos = (int) random(0, obstaclaList.size());
                        obstaclaList.remove(randomPos);
                        obstaclaList.trimToSize();

                    }
                }
            }


                for (Obstacle obstacle : obstaclaList) {
                    obstacle.show(this, obstacleImage);

                }

                for (Item item : itemList) {
                    item.show(this);
                }

                image(foodImage,food.x, food.y, 1, 1);

                if (frameCount % framecountDivider == 0){
                    endGame();
                }




        }else {
            //TODO: Loop unterbrechen, damit nicht immer abgeschickt
            //looping = !looping;
            noLoop();
            finalScore = snake.getLen();
            callback.onEndedGameScore(finalScore);
            //callback.

            ((MainActivity)getActivity()).playDeathsound();

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

        }
    }

    public void endGame() {
        PVector snakeHead = snake.getHeadVect();
        ArrayList<PVector> snakeBody = snake.getBody();
        if (snakeHead.x < 0 || snakeHead.x > w - 1 || snakeHead.y < 0 || snakeHead.y > h - 1){
            if (snake.getEmpoweredState()) {
                snake.teleport(w, h);
                return;
            }
            gameover = true;
        }
        for (Obstacle obstacle : obstaclaList) {
            if (snakeHead.x == obstacle.getPos().x && snakeHead.y == obstacle.getPos().y) {
                if (snake.getEmpoweredState()) {
                    obstaclaList.remove(obstacle);
                    // Todo : nur einmal oder x frames?
                    snake.resetItemPower();
                    return;
                }
                gameover = true;
            }
        }
        for (int i=0; i< snake.getBody().size() -1; i++){
            if (snakeHead.x == snakeBody.get(i).x && snakeHead.y == snakeBody.get(i).y){
                gameover = true;
            }
        }
    }
}