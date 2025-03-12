package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dainsleif.hartebeest.movements.PlayerBasicMovements;

public class PlayerSpriteJson extends PlayerBasicMovements {
    private TextureRegion[] walkUpFrames;
    private TextureRegion[] walkDownFrames;
    private TextureRegion[] walkLeftFrames;
    private TextureRegion[] walkRightFrames;
    private TextureRegion currentFrame;
    private Vector2 position;
    private float stateTime;

    public PlayerSpriteJson(String texturePath, String jsonPath, Vector2 startPosition) {
        SpriteSheetLoaderJson loader = new SpriteSheetLoaderJson(texturePath, jsonPath);
        walkUpFrames = loader.getFrames();
        walkDownFrames = loader.getFrames();
        walkLeftFrames = loader.getFrames();
        walkRightFrames = loader.getFrames();
        position = startPosition;
        stateTime = 0f;
    }

    public void update(float deltaTime, KeyHandler keyHandler) {
        super.update(deltaTime, keyHandler, position);
        stateTime += deltaTime;

        if (keyHandler.isUpPressed()) {
            currentFrame = walkUpFrames[(int)(stateTime % walkUpFrames.length)];
        } else if (keyHandler.isDownPressed()) {
            currentFrame = walkDownFrames[(int)(stateTime % walkDownFrames.length)];
        } else if (keyHandler.isLeftPressed()) {
            currentFrame = walkLeftFrames[(int)(stateTime % walkLeftFrames.length)];
        } else if (keyHandler.isRightPressed()) {
            currentFrame = walkRightFrames[(int)(stateTime % walkRightFrames.length)];
        } else {
            currentFrame = walkDownFrames[0]; // Default frame when idle
        }
    }

    public void draw(Batch batch) {
        batch.draw(currentFrame, position.x, position.y);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return currentFrame.getRegionWidth();
    }

    public float getHeight() {
        return currentFrame.getRegionHeight();
    }
}
