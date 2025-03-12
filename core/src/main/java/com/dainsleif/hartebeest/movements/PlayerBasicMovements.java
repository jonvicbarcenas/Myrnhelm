package com.dainsleif.hartebeest.movements;

import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class PlayerBasicMovements {
    private float stateTime;

    //movement update with sprite
    public void update(float deltaTime, KeyHandler keyHandler, Sprite sprite) {
        stateTime += deltaTime;
        float dx = 0;
        float dy = 0;
        float speed = GameInfo.getPlayerSpeed();

        if (keyHandler.isUpPressed()) {
            dy += speed * deltaTime;
        }
        if (keyHandler.isDownPressed()) {
            dy -= speed * deltaTime;
        }
        if (keyHandler.isLeftPressed()) {
            dx -= speed * deltaTime;
        }
        if (keyHandler.isRightPressed()) {
            dx += speed * deltaTime;
        }

        // Normalize the movement vector to prevent faster diagonal movement
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length != 0) {
            dx /= length;
            dy /= length;
        }

        sprite.translate(dx, dy);
    }

    public float getStateTime() {
        return stateTime;
    }

    //movement update with Json
    public void update(float deltaTime, KeyHandler keyHandler, Vector2 position) {
        stateTime += deltaTime;
        float dx = 0;
        float dy = 0;
        float speed = GameInfo.getPlayerSpeed();

        if (keyHandler.isUpPressed()) {
            dy += speed * deltaTime;
        }
        if (keyHandler.isDownPressed()) {
            dy -= speed * deltaTime;
        }
        if (keyHandler.isLeftPressed()) {
            dx -= speed * deltaTime;
        }
        if (keyHandler.isRightPressed()) {
            dx += speed * deltaTime;
        }

        // Normalize the movement vector to prevent faster diagonal movement
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length != 0) {
            dx /= length;
            dy /= length;
        }

        position.add(dx, dy);
    }
}
