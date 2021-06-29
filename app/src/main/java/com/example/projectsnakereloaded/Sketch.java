package com.example.projectsnakereloaded;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;


import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;


public class Sketch extends PApplet {

    private boolean gameover = false;
    private Snake snake;
    private PVector food;

    private ArrayList<Obstacle> obstacleList;
    private ArrayList<Item> itemList;


    private float rez;
    private int w;
    private int h;

    private float framecountDivider;
    private int itemActiveFrameTime;
    private int itemActiveFrameCount;

    private static final String TAG = "Snake";

    private PVector pA;
    private PVector pB;
    private PVector pC;
    private PVector pD;
    private PVector pM;

    private int finalScore;
    private boolean wallHit;

    interface Callback {
        void onEndedGameScore(int score, int itemsPickedUp, int fieldsMoved, boolean wallHit);
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
    PImage itemTeleportImage;

    PFont Font;

    //Sound
    MediaPlayer mp;

    SharedPreferences prefs;

// https://stackoverflow.com/questions/18459122/play-sound-on-button-click-android


    @Override
    public void settings() {
        fullScreen();
        size(width, height);
    }



    public void setCallback(Callback callback) {
        this.callback = callback;
    }



    @Override
    public void setup() {

        prefs = PreferenceManager.getDefaultSharedPreferences((MainActivity)getActivity());
        String difficulty = prefs.getString("difficulty_key", "easy");
        String obst = prefs.getString("list_food_type", "apple");
        String bgImage = prefs.getString("background_key", "wall");

        if(obst.equals("peach")){
            foodImage = loadImage("snake_peach.png");
        }
        else if(obst.equals("lemon")){
            foodImage = loadImage("snake_lemon.png");
        }
        else {
            foodImage = loadImage("snake_apple.png");
        }

        if (bgImage.equals("beach")) {
            backgroundImage = loadImage("background_image.png");
        }
        else {
            backgroundImage = loadImage("background_image_stone.png");
        }


        if(difficulty.equals("hard")){
            rez = 100;
        }
        else if (difficulty.equals("medium")) {
            rez = 80;
        }
        else {
            rez = 60;
        }



        // Quelle für png https://github.com/rembound/Snake-Game-HTML5
        //String url = "https://raw.githubusercontent.com/rembound/Snake-Game-HTML5/master/snake-graphics.png";
        obstacleImage = loadImage("snake_brick.png");
        snakeImage = loadImage("snake_block_default.png");
        gameoverImage = loadImage("gameover_background.png");
        itemSpeedBoostImage = loadImage("item_speedup.png");
        itemSpeedLossImage = loadImage("item_speeddown.png");
        itemPowerStarImage = loadImage("item_stern.png");
        itemTeleportImage = loadImage("item_teleporter.png");

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
        obstacleList = new ArrayList<>();
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


    }

    public void playSound(int rawId) {
        mp = MediaPlayer.create((MainActivity)getActivity(), rawId);
        mp.start();
    }

    public void createFood() {
        food = getPosAvailable();
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

    public PVector getPosAvailable() {
        PVector randomVector = new PVector((int) random(w), (int) random(h));

        for (Obstacle obstacle : obstacleList) {
            if (obstacle.getPos().x == randomVector.x && obstacle.getPos().y == randomVector.y) {
                randomVector = getPosAvailable();
                break;
            }
        }
        for (Item item : itemList) {
            if (item.getPos().x == randomVector.x && item.getPos().y == randomVector.y) {
                randomVector = getPosAvailable();
                break;
            }
        }
        if (food != null) {
            if (food.x == randomVector.x && food.y == randomVector.y) {
                randomVector = getPosAvailable();
            }
        }

        return randomVector;
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

        //Todo : delete frameRate
        fill(255,255,255);
        text("Score: "+snake.getLen() + ": " + frameRate, scoreCoordinateX, scoreCoordinateY);


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
                    if (snake.getSpeedDifference() != 0) {
                        snake.resetSpeedDifference();
                    }
                }
                snake.move();
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
                        PVector posAvailable = getPosAvailable();
                        int posX = (int) posAvailable.x;
                        int posY = (int) posAvailable.y;
                        obstacleList.add(new Obstacle(posX, posY, obstacleImage));
                    }
                    if (frameCount % 60 == 0) {
                        int randomItem = (int) random(0, 4);

                        PVector posAvailable = getPosAvailable();

                        int posX = (int) posAvailable.x;
                        int posY = (int) posAvailable.y;



                        switch (randomItem) {
                            case 0:
                                itemList.add(new SpeedBoost(posX, posY, itemSpeedBoostImage));
                                break;
                            case 1:
                                itemList.add(new SpeedLoss(posX, posY, itemSpeedLossImage));
                                break;
                            case 2:
                                itemList.add(new PowerStar(posX, posY, itemPowerStarImage));
                                break;
                            case 3:
                                itemList.add(new Teleport(posX, posY, itemTeleportImage));
                        }
                    }
                    if (frameCount % 90 == 0) {
                        int randomPos = (int) random(0, obstacleList.size());
                        obstacleList.remove(randomPos);
                        obstacleList.trimToSize();

                    }
                }
            }


                for (Obstacle obstacle : obstacleList) {
                    obstacle.show(this);

                }

                for (Item item : itemList) {
                    item.show(this);
                }

                image(foodImage,food.x, food.y, 1, 1);

                if (frameCount % framecountDivider == 0){
                    endGame();
                }



        }else {
            noLoop();
            finalScore = snake.getLen();
            callback.onEndedGameScore(finalScore, snake.getItemsCollected(), snake.getFieldsMoved(), wallHit);


            playSound(R.raw.tot_dum_dum_dum);
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
            if (snake.getTeleportedEmpoweredState()) {
                snake.teleport();
                return;
            }
            gameover = true;
        }
        for (Obstacle obstacle : obstacleList) {
            if (snakeHead.x == obstacle.getPos().x && snakeHead.y == obstacle.getPos().y) {
                if (snake.getEmpoweredState()) {
                    obstacleList.remove(obstacle);
                    obstacleList.trimToSize();
                    // Todo : nur einmal oder x frames?
                    //snake.resetItemPower();
                    wallHit = true;
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