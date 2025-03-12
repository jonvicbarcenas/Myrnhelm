package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.dainsleif.hartebeest.movements.PlayerBasicMovements;

public class PlayerSpriteAtlas extends PlayerBasicMovements {
    private static final float FRAME_TIME = 1 / 5f;
    private TextureAtlas atlas;
    private Animation<TextureRegion> walkDownAnimation;
    private Animation<TextureRegion> walkUpAnimation;
    private Animation<TextureRegion> walkLeftAnimation;
    private Animation<TextureRegion> walkRightAnimation;
    private float stateTime;
    private Sprite sprite;

    public PlayerSpriteAtlas(String atlasPath, float x, float y) {
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
            System.out.println(e.getMessage());
        }
    }

    public void update(float deltaTime, KeyHandler keyHandler, float speed) {
        super.update(deltaTime, keyHandler, sprite);
        stateTime += deltaTime;
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
            System.out.println(e.getMessage());
        }
    }

    public void dispose() {
        atlas.dispose();
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
