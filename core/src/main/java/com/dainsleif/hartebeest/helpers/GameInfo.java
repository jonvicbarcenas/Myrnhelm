package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.Gdx;

public class GameInfo {
    //width and height of the screen
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

    //music volume with setter and getter
    private static float musicVolume = 0.2f;
    public static void setMusicVolume(float volume) {
        musicVolume = volume;
    }
    public static float getMusicVolume() {
        return musicVolume;
    }

}
