package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

public class PlayerSprite {
    private static final float FRAME_TIME = 1 / 5f;
    private TextureAtlas atlas;
    private Animation<TextureRegion> walkDownAnimation;
    private Animation<TextureRegion> walkUpAnimation;
    private Animation<TextureRegion> walkLeftAnimation;
    private Animation<TextureRegion> walkRightAnimation;
    private float stateTime;
    private Sprite sprite;

    public PlayerSprite(String atlasPath, float x, float y) {
        try {
            atlas = new TextureAtlas(atlasPath);
            Array<TextureAtlas.AtlasRegion> framesDown = atlas.findRegions("dwn");
            walkDownAnimation = new Animation<>(FRAME_TIME, framesDown, Animation.PlayMode.LOOP);
            Array<TextureAtlas.AtlasRegion> framesUp = atlas.findRegions("up");
            walkUpAnimation = new Animation<>(FRAME_TIME, framesUp, Animation.PlayMode.LOOP);
            Array<TextureAtlas.AtlasRegion> framesLeft = atlas.findRegions("left");
            walkLeftAnimation = new Animation<>(FRAME_TIME, framesLeft, Animation.PlayMode.LOOP);
            Array<TextureAtlas.AtlasRegion> framesRight = atlas.findRegions("rght");
            walkRightAnimation = new Animation<>(FRAME_TIME, framesRight, Animation.PlayMode.LOOP);
            stateTime = 0f;
            sprite = new Sprite(framesDown.first());
            sprite.setPosition(x, y);
            sprite.setSize(16, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(float deltaTime, KeyHandler keyHandler, float speed) {
        stateTime += deltaTime;
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

    public void draw(SpriteBatch batch, boolean movingDown, boolean movingUp, boolean movingLeft, boolean movingRight) {
        try {
            if (movingDown) {
                TextureRegion currentFrame = walkDownAnimation.getKeyFrame(stateTime);
                batch.draw(currentFrame, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
            } else if (movingUp) {
                TextureRegion currentFrame = walkUpAnimation.getKeyFrame(stateTime);
                batch.draw(currentFrame, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
            }else if (movingLeft) {
                TextureRegion currentFrame = walkLeftAnimation.getKeyFrame(stateTime);
                batch.draw(currentFrame, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
            } else if (movingRight) {
                TextureRegion currentFrame = walkRightAnimation.getKeyFrame(stateTime);
                batch.draw(currentFrame, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
            }else {
                sprite.draw(batch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dispose() {
        try {
            atlas.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
