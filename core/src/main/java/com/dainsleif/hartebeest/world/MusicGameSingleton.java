package com.dainsleif.hartebeest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.dainsleif.hartebeest.helpers.GameInfo;

/**
 * Singleton class to manage game background music across different screens
 */
public class MusicGameSingleton {
    private static MusicGameSingleton instance;
    private Music gameBackgroundMusic;

    private MusicGameSingleton() {
        // Load game background music
        gameBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/16bitRpgBGMUSIC.mp3"));
        gameBackgroundMusic.setLooping(true);
        gameBackgroundMusic.setVolume(GameInfo.getMusicVolume());
    }

    public static MusicGameSingleton getInstance() {
        if (instance == null) {
            instance = new MusicGameSingleton();
        }
        return instance;
    }

    public Music getBackgroundMusic() {
        return gameBackgroundMusic;
    }

    public void play() {
        if (gameBackgroundMusic != null && !gameBackgroundMusic.isPlaying()) {
            gameBackgroundMusic.play();
        }
    }

    public void stop() {
        if (gameBackgroundMusic != null && gameBackgroundMusic.isPlaying()) {
            gameBackgroundMusic.stop();
        }
    }

    public void updateVolume() {
        if (gameBackgroundMusic != null) {
            gameBackgroundMusic.setVolume(GameInfo.getMusicVolume());
        }
    }

    public void dispose() {
        if (gameBackgroundMusic != null) {
            gameBackgroundMusic.dispose();
        }
        instance = null;
    }
}
