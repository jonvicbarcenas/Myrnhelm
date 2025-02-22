package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.Gdx;

public class GameInfo {
    //width and height of the screen
    public static final int WIDTH = Gdx.graphics.getWidth();
    public static final int HEIGHT = Gdx.graphics.getHeight();

    //frames per second
    public static final float fps = Gdx.graphics.getFramesPerSecond();

    //music volume with setter and getter
    public static float musicVolume = 0.2f;
    public static void setMusicVolume(float volume) {
        musicVolume = volume;
    }
    public static float getMusicVolume() {
        return musicVolume;
    }



}
