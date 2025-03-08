package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Player extends PlayerSprite {
    private boolean movingDown;
    private boolean movingUp;
    private boolean movingLeft;
    private boolean movingRight;

    private Rectangle playerBounds;
    private ShapeRenderer shapeRenderer;

    public Player(String atlasPath, float x, float y) {
        super(atlasPath, x, y);
        shapeRenderer = new ShapeRenderer();
    }

    public void update(float deltaTime, KeyHandler keyHandler) {
        movingDown = keyHandler.isDownPressed();
        movingUp = keyHandler.isUpPressed();
        movingLeft = keyHandler.isLeftPressed();
        movingRight = keyHandler.isRightPressed();
        super.update(deltaTime, keyHandler, GameInfo.getPlayerSpeed());

        // Save the player's position
        GameInfo.setPlayerX(getX());
        GameInfo.setPlayerY(getY());

        playerBounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch, movingDown, movingUp, movingLeft, movingRight);

        // Draw player bounds
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1); // Red color
        shapeRenderer.rect(
            playerBounds.x + (playerBounds.width - (playerBounds.width - 8)) / 2,
            playerBounds.y + (playerBounds.height - (playerBounds.height - 8)) / 2 - 3,
            playerBounds.width - 8,
            playerBounds.height - 8
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
