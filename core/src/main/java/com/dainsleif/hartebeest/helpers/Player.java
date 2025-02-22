package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private Texture texture;
    private Sprite sprite;
    private float speed = 100f;

    public Player(String texturePath, float x, float y) {
        texture = new Texture(texturePath);
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(32, 32);
    }

    public void update(float deltaTime, KeyHandler keyHandler) {
        if (keyHandler.isUpPressed()) {
            sprite.translateY(speed * deltaTime);
        }
        if (keyHandler.isDownPressed()) {
            sprite.translateY(-speed * deltaTime);
        }
        if (keyHandler.isLeftPressed()) {
            sprite.translateX(-speed * deltaTime);
        }
        if (keyHandler.isRightPressed()) {
            sprite.translateX(speed * deltaTime);
        }
    }


    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void dispose() {
        texture.dispose();
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

}
