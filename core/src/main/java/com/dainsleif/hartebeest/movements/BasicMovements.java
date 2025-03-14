package com.dainsleif.hartebeest.movements;

import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.badlogic.gdx.physics.box2d.Body;

public class BasicMovements {
    private float stateTime;
    private String currentDirection = "down";
    private boolean isMoving = false;

    public void update(float deltaTime, KeyHandler keyHandler, Body body) {
        stateTime += deltaTime;
        float dx = 0;
        float dy = 0;
        float speed = GameInfo.getPlayerSpeed();

        if (keyHandler.isUpPressed()) {
            dy += speed;
            currentDirection = "up";
        }
        if (keyHandler.isDownPressed()) {
            dy -= speed;
            currentDirection = "down";
        }
        if (keyHandler.isLeftPressed()) {
            dx -= speed;
            currentDirection = "left";
        }
        if (keyHandler.isRightPressed()) {
            dx += speed;
            currentDirection = "right";
        }

        // Normalize the velocity if moving diagonally
        if (dx != 0 && dy != 0) {
            float magnitude = (float) Math.sqrt(dx * dx + dy * dy);
            dx = (dx / magnitude) * speed * (float) Math.sqrt(2) + 5;
            dy = (dy / magnitude) * speed * (float) Math.sqrt(2) + 5;
        }

        // Check if player is moving
        isMoving = (dx != 0 || dy != 0);

        // Apply velocity to the body
        body.setLinearVelocity(dx, dy);
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public String getCurrentDirection() {
        return currentDirection;
    }

    public boolean isMoving() {
        return isMoving;
    }
}
