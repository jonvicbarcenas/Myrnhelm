package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class KeyHandler extends InputAdapter {
    private boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    private OrthographicCamera camera;

    public KeyHandler(){
        // Empty constructor
    }

    public KeyHandler(OrthographicCamera camera){
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                upPressed = true;
                break;
            case Input.Keys.S:
                downPressed = true;
                break;
            case Input.Keys.A:
                leftPressed = true;
                break;
            case Input.Keys.D:
                rightPressed = true;
                break;
            case Input.Keys.ENTER:
                enterPressed = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                upPressed = false;
                break;
            case Input.Keys.S:
                downPressed = false;
                break;
            case Input.Keys.A:
                leftPressed = false;
                break;
            case Input.Keys.D:
                rightPressed = false;
                break;
            case Input.Keys.ENTER:
                enterPressed = false;
                break;
        }
        return true;
    }

    public boolean isEnterPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ENTER);
    }



    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }


    public void zoomIn() {
        camera.zoom -= 0.01f;
        if (camera.zoom < 0.5f) {
            camera.zoom = 0.5f;
        }
        camera.update();
    }

    public void zoomOut() {
        camera.zoom += 0.01f;
        if (camera.zoom > 2.0f) {
            camera.zoom = 2.0f;
        }
        camera.update();
    }
}
