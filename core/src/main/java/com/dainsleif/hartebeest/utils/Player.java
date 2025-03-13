package com.dainsleif.hartebeest.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dainsleif.hartebeest.helpers.KeyHandler;

public class Player extends Entity {

    public Player(String atlasPath, float x, float y) {
        super(atlasPath, x, y);
    }

    @Override
    public void update(float deltaTime, KeyHandler keyHandler) {
        super.update(deltaTime, keyHandler);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
