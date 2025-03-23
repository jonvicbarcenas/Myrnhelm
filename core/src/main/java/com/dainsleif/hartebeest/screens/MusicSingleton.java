package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;
import com.dainsleif.hartebeest.helpers.GameInfo;

public class MusicSingleton {
    private static MusicSingleton instance;
    private Music backgroundMusic;

    private MusicSingleton() {
        // Load and play background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/dawn_winery_MenuBGM.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(GameInfo.getMusicVolume());
    }

    public static MusicSingleton getInstance() {
        if (instance == null) {
            instance = new MusicSingleton();
        }
        return instance;
    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }
}
