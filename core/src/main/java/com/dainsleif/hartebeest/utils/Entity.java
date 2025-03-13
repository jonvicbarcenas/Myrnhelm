package com.dainsleif.hartebeest.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.helpers.PlayerSpriteAtlas;

public class Entity extends PlayerSpriteAtlas {
    protected boolean movingDown;
    protected boolean movingUp;
    protected boolean movingLeft;
    protected boolean movingRight;

    protected Rectangle bounds;
    protected ShapeRenderer shapeRenderer;

    public Entity(String atlasPath, float x, float y) {
        super(atlasPath, x, y);
        shapeRenderer = new ShapeRenderer();
    }

    public void update(float deltaTime, KeyHandler keyHandler) {
        movingDown = keyHandler.isDownPressed();
        movingUp = keyHandler.isUpPressed();
        movingLeft = keyHandler.isLeftPressed();
        movingRight = keyHandler.isRightPressed();
        super.update(deltaTime, keyHandler, GameInfo.getPlayerSpeed());

        // Save the entity's position
        GameInfo.setPlayerX(getX());
        GameInfo.setPlayerY(getY());

        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch, movingDown, movingUp, movingLeft, movingRight);

        // Draw entity bounds
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1); // Red color
        shapeRenderer.rect(
            bounds.x + (bounds.width - (bounds.width - 8)) / 2,
            bounds.y + (bounds.height - (bounds.height - 8)) / 2 - 3,
            bounds.width - 8,
            bounds.height - 8
        );
        shapeRenderer.end();
        batch.begin();
    }

    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
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
