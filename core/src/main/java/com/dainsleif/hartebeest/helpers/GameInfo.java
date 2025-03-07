package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.Gdx;
import com.dainsleif.hartebeest.database.PlayerPosition;

public class GameInfo {
    //width and height of the screen
    public static double playerX = PlayerPosition.getPlayerXFromDB();
    public static double playerY = PlayerPosition.getPlayerYFromDB();

    public static final int WIDTH = Gdx.graphics.getWidth();
    public static final int HEIGHT = Gdx.graphics.getHeight();

    //frames per second
    public static final float fps = Gdx.graphics.getFramesPerSecond();

    //player speed with setter and getter
    private static float playerSpeed = 75f;
    public static void setPlayerSpeed(float speed) {
        playerSpeed = speed;
    }
    public static float getPlayerSpeed() {
        return playerSpeed;
    }

    // player position setters and getters
    public static void setPlayerX(float x) {
        playerX = x;
        PlayerPosition.savePlayerPosition();
    }
    public static float getPlayerX() {
        return (float) playerX;
    }

    public static void setPlayerY(float y) {
        playerY = y;
        PlayerPosition.savePlayerPosition();
    }
    public static float getPlayerY() {
        return (float) playerY;
    }


    //music volume with setter and getter
    private static float musicVolume = 0.2f;
    public static void setMusicVolume(float volume) {
        musicVolume = volume;
    }
    public static float getMusicVolume() {
        return musicVolume;
    }

}
