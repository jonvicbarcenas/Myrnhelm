package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends PlayerSprite {
    private boolean movingDown;
    private boolean movingUp;
    private boolean movingLeft;
    private boolean movingRight;

    public Player(String atlasPath, float x, float y) {
        super(atlasPath, x, y);
    }

    public void update(float deltaTime, KeyHandler keyHandler) {
        movingDown = keyHandler.isDownPressed();
        movingUp = keyHandler.isUpPressed();
        movingLeft = keyHandler.isLeftPressed();
        movingRight = keyHandler.isRightPressed();
        super.update(deltaTime, keyHandler, GameInfo.getPlayerSpeed());
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch, movingDown, movingUp, movingLeft, movingRight);
    }

    public void dispose() {
        super.dispose();
    }

    public float getX() {
        return super.getX();
    }

    public float getY() {
        return super.getY();
    }

    public float getWidth() {
        return super.getWidth();
    }

    public float getHeight() {
        return super.getHeight();
    }
}
