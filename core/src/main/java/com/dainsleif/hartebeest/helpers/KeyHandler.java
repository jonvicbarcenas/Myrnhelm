package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

public class KeyHandler extends InputAdapter {
    private boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    private OrthographicCamera camera;

    private float targetZoom;
    private float zoomSpeed = 4.0f;
    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 2.0f;
    private static final float ZOOM_STEP = 0.2f;

    public KeyHandler(){
        // Empty constructor
    }

    public KeyHandler(OrthographicCamera camera){
        this.camera = camera;
        this.targetZoom = camera.zoom;
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
        targetZoom -= ZOOM_STEP;
        if (targetZoom < MIN_ZOOM) {
            targetZoom = MIN_ZOOM;
        }
    }

    public void zoomOut() {
        targetZoom += ZOOM_STEP;
        if (targetZoom > MAX_ZOOM) {
            targetZoom = MAX_ZOOM;
        }
    }

    public void update(float deltaTime) {
        if (Math.abs(camera.zoom - targetZoom) > 0.01f) {
            camera.zoom = MathUtils.lerp(camera.zoom, targetZoom, deltaTime * zoomSpeed);
            camera.update();
        }
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (amountY > 0) {
            zoomOut();
        } else if (amountY < 0) {
            zoomIn();
        }
        return true;
    }
}
