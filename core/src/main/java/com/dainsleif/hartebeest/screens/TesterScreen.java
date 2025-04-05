package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;



public class TesterScreen implements Screen {
    DeathStage deathStage;

    @Override
    public void show() {

        deathStage = new DeathStage();

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        deathStage.update(v);
        deathStage.draw(); // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        deathStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        deathStage.dispose();
    }
}
