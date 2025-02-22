package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyHandler {
    public boolean isUpPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.W);
    }

    public boolean isDownPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.S);
    }

    public boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.A);
    }

    public boolean isRightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.D);
    }
}
